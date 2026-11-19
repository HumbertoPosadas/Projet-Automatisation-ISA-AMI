package fr.example.tools;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

public final class PlaywrightExtension
    implements ParameterResolver, AfterTestExecutionCallback, AfterEachCallback {
    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(PlaywrightExtension.class);
    private static final String SESSION = "session";

    @Override
    public void afterTestExecution(ExtensionContext context) {
        updateTestOutcome(context);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        updateTestOutcome(context);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return parameterType == Playwright.class
                || parameterType == Browser.class
                || parameterType == BrowserContext.class
                || parameterType == Page.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        extensionContext.getStore(NAMESPACE).getOrComputeIfAbsent(
                SESSION, ignored -> PlaywrightManager.start(), PlaywrightManager.Session.class);

        Class<?> parameterType = parameterContext.getParameter().getType();
        TestExecutionLog.info("Injection du paramètre Playwright " + parameterType.getSimpleName() + ".");
        if (parameterType == Playwright.class) {
            return PlaywrightManager.getPlaywright();
        }
        if (parameterType == Browser.class) {
            return PlaywrightManager.getBrowser();
        }
        if (parameterType == BrowserContext.class) {
            return PlaywrightManager.getBrowserContext();
        }
        if (parameterType == Page.class) {
            return PlaywrightManager.getPage();
        }

        throw new IllegalArgumentException("Type de paramètre Playwright non pris en charge : " + parameterType);
    }

    private static void updateTestOutcome(ExtensionContext context) {
        PlaywrightManager.Session session = context.getStore(NAMESPACE).get(SESSION,
                PlaywrightManager.Session.class);
        if (session != null) {
            session.markTestFailed(context.getExecutionException().isPresent());
        }
    }
}
