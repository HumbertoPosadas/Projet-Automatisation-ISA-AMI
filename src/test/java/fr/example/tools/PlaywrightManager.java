package fr.example.tools;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public final class PlaywrightManager {
    private static final double DEFAULT_TIMEOUT_MS = 60_000;
    private static volatile Session session;

    private PlaywrightManager() {
    }

    static synchronized Session start() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    public static Page getPage() {
        return currentSession().page;
    }

    static Browser getBrowser() {
        return currentSession().browser;
    }

    static BrowserContext getBrowserContext() {
        return currentSession().context;
    }

    static Playwright getPlaywright() {
        return currentSession().playwright;
    }

    private static Session currentSession() {
        Session current = session;
        if (current == null) {
            throw new IllegalStateException(
                    "La session Playwright n'est pas initialisée. Ajoutez @ExtendWith(PlaywrightExtension.class).");
        }
        return current;
    }

    static final class Session implements org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource {
        private final Playwright playwright;
        private final Browser browser;
        private final BrowserContext context;
        private final Page page;

        private Session() {
            TestExecutionLog.action("Démarrage de la session Playwright.");
            playwright = Playwright.create();
            Browser createdBrowser = null;
            BrowserContext createdContext = null;
            try {
                boolean headless = Boolean.parseBoolean(System.getProperty("playwright.headless", "true"));
                createdBrowser = playwright.chromium().launch(
                        new BrowserType.LaunchOptions().setHeadless(headless));
                createdContext = createdBrowser.newContext();
                createdContext.setDefaultTimeout(DEFAULT_TIMEOUT_MS);
                createdContext.setDefaultNavigationTimeout(DEFAULT_TIMEOUT_MS);
                browser = createdBrowser;
                context = createdContext;
                page = context.newPage();
                TestExecutionLog.result("Session Playwright démarrée (headless : " + headless + ").");
            } catch (RuntimeException e) {
                TestExecutionLog.failure("Le démarrage de la session Playwright a échoué.", e);
                closeAfterInitializationFailure(createdContext, createdBrowser, playwright, e);
                throw e;
            }
        }

        @Override
        public void close() {
            TestExecutionLog.action("Fermeture de la session Playwright.");
            try {
                context.close();
            } finally {
                try {
                    browser.close();
                } finally {
                    playwright.close();
                    synchronized (PlaywrightManager.class) {
                        if (session == this) {
                            session = null;
                        }
                    }
                }
            }
        }

        private static void closeAfterInitializationFailure(
                BrowserContext context, Browser browser, Playwright playwright, RuntimeException failure) {
            if (context != null) {
                try {
                    context.close();
                } catch (RuntimeException closeFailure) {
                    failure.addSuppressed(closeFailure);
                }
            }
            if (browser != null) {
                try {
                    browser.close();
                } catch (RuntimeException closeFailure) {
                    failure.addSuppressed(closeFailure);
                }
            }
            try {
                playwright.close();
            } catch (RuntimeException closeFailure) {
                failure.addSuppressed(closeFailure);
            }
        }
    }
}
