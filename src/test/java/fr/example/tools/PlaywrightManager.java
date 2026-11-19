package fr.example.tools;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

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
        private final Path videoDir;
        private boolean testFailed;

        private Session() {
            TestExecutionLog.action("Démarrage de la session Playwright.");
            playwright = Playwright.create();
            Browser createdBrowser = null;
            BrowserContext createdContext = null;
            videoDir = Path.of("target", "test-results", "playwright-videos", UUID.randomUUID().toString());
            try {
                Files.createDirectories(videoDir);
                boolean headless = Boolean.parseBoolean(System.getProperty("playwright.headless", "false"));
                createdBrowser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                        .setHeadless(headless)
                        .setArgs(List.of("--start-maximized", "--force-device-scale-factor=0.8")));
                createdContext = createdBrowser.newContext(
                    new Browser.NewContextOptions()
                        .setViewportSize(null)
                        .setRecordVideoDir(videoDir)
                        .setRecordVideoSize(1280, 720));
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
            } catch (IOException e) {
                RuntimeException error = new UncheckedIOException("Impossible de préparer le dossier vidéo Playwright.", e);
                TestExecutionLog.failure("Le démarrage de la session Playwright a échoué.", error);
                closeAfterInitializationFailure(createdContext, createdBrowser, playwright, error);
                throw error;
            }
        }

        void markTestFailed(boolean failed) {
            testFailed |= failed;
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
                    if (!testFailed) {
                        deleteVideoDirectory(videoDir);
                    } else {
                        TestExecutionLog.result("Vidéo Playwright conservée après échec : " + videoDir);
                    }
                    synchronized (PlaywrightManager.class) {
                        if (session == this) {
                            session = null;
                        }
                    }
                }
            }
        }

        private static void deleteVideoDirectory(Path directory) {
            try (Stream<Path> paths = Files.walk(directory)) {
                for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                    Files.deleteIfExists(path);
                }
            } catch (IOException e) {
                throw new UncheckedIOException("Impossible de supprimer les vidéos Playwright d'un test réussi.", e);
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
