package fr.example.tools;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;

public class TestLoggingExtension
        implements BeforeEachCallback, AfterTestExecutionCallback, AfterEachCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(TestLoggingExtension.class);
    private static final String LOG_KEY = "testExecutionLog";

    @Override
    public void beforeEach(ExtensionContext context) throws IOException {
        TestExecutionLog log = TestExecutionLog.start(
                context.getRequiredTestClass().getName(),
                context.getRequiredTestMethod().getName());
        context.getStore(NAMESPACE).put(LOG_KEY, log);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        TestExecutionLog log = context.getStore(NAMESPACE).get(LOG_KEY, TestExecutionLog.class);
        if (log == null) {
            return;
        }

        context.getExecutionException().ifPresentOrElse(
                error -> {
                    log.setResult("ECHEC");
                    TestExecutionLog.failure("Échec de la méthode de test.", error);
                },
                () -> {
                    log.setResult("REUSSI");
                    TestExecutionLog.result("La méthode de test s'est terminée sans exception.");
                });
    }

    @Override
    public void afterEach(ExtensionContext context) throws IOException {
        TestExecutionLog log = context.getStore(NAMESPACE).remove(LOG_KEY, TestExecutionLog.class);
        if (log != null) {
            if (context.getExecutionException().isPresent()) {
                log.setResult("ECHEC");
            }
            log.close();
        }
    }
}
