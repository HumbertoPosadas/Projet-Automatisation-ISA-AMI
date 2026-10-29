package fr.example.tools;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public final class TestExecutionLog implements AutoCloseable {

    private static final ThreadLocal<TestExecutionLog> CURRENT = new ThreadLocal<>();
    private static final DateTimeFormatter DIRECTORY_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");
    private static final DateTimeFormatter LOG_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final Path logFile;
    private final String testName;
    private final BufferedWriter writer;
    private String result = "NON TERMINE";
    private boolean closed;

    private TestExecutionLog(Path logFile, String testName) throws IOException {
        this.logFile = logFile;
        this.testName = testName;
        this.writer = Files.newBufferedWriter(
                logFile, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
    }

    public static TestExecutionLog start(String testClass, String testMethod) throws IOException {
        Path projectRoot = findProjectRoot();
        Path root = projectRoot.resolve("logs").resolve("test-executions");
        Files.createDirectories(root);

        String safeMethodName = testMethod.replaceAll("[^a-zA-Z0-9._-]", "_");
        String prefix = LocalDateTime.now().format(DIRECTORY_TIMESTAMP) + "_" + safeMethodName + "_";
        Path testDirectory = Files.createTempDirectory(root, prefix);
        String testName = testClass + "#" + testMethod;
        TestExecutionLog log = new TestExecutionLog(testDirectory.resolve("execution.txt"), testName);
        CURRENT.set(log);
        log.info("Début du test.");
        log.info("Log directory: " + testDirectory.toAbsolutePath());
        System.out.println("Journal du test : " + log.logFile.toAbsolutePath());
        return log;
    }

    public static void info(String message) {
        current().ifPresentOrElse(
                log -> log.write("INFO", message),
                () -> System.getLogger(TestExecutionLog.class.getName()).log(System.Logger.Level.INFO, message));
    }

    public static void action(String message) {
        current().ifPresentOrElse(
                log -> log.write("ACTION", message),
                () -> System.getLogger(TestExecutionLog.class.getName()).log(System.Logger.Level.INFO, message));
    }

    public static void result(String message) {
        current().ifPresentOrElse(
                log -> log.write("RESULT", message),
                () -> System.getLogger(TestExecutionLog.class.getName()).log(System.Logger.Level.INFO, message));
    }

    public static void failure(String message, Throwable error) {
        current().ifPresentOrElse(
                log -> log.writeFailure(message, error),
                () -> System.getLogger(TestExecutionLog.class.getName()).log(
                        System.Logger.Level.ERROR, message, error));
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        }
        write("INFO", "TEST RESULT: " + result);
        try {
            writer.close();
        } finally {
            closed = true;
            CURRENT.remove();
        }
    }

    private void writeFailure(String message, Throwable error) {
        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));
        write("ERROR", message + System.lineSeparator() + stackTrace);
    }

    private void write(String level, String message) {
        if (closed) {
            throw new IllegalStateException("Le journal du test est déjà fermé.");
        }
        try {
            for (String line : message.split("\\R", -1)) {
                writer.write("%s %s [%s] %s%n".formatted(
                        LocalDateTime.now().format(LOG_TIMESTAMP), testName, level, line));
            }
            writer.flush();
        } catch (IOException e) {
            throw new UncheckedIOException("Impossible d'écrire le journal du test dans " + logFile, e);
        }
    }

    private static Optional<TestExecutionLog> current() {
        return Optional.ofNullable(CURRENT.get());
    }

    private static Path findProjectRoot() {
        Path current = Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();
        Path workingDirectory = current;
        while (current != null) {
            if (Files.isRegularFile(current.resolve("pom.xml"))) {
                return current;
            }
            current = current.getParent();
        }
        return workingDirectory;
    }
}
