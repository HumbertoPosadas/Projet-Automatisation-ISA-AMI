package fr.example.tools;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;

public final class TestIdExtractor {
    private static final Path LOG_FILE = Path.of("target", "test-results", "testids.log");

    private TestIdExtractor() {
    }

    public static void recupererTousLesTestIds(Page page) throws IOException {
        Locator elements = page.locator("[data-testid]");
        int count = elements.count();

        StringBuilder log = new StringBuilder()
                .append("\n--- ")
                .append(Instant.now())
                .append(" | ")
                .append(page.url())
                .append(" | ")
                .append(count)
                .append(" éléments ---\n");

        TestExecutionLog.info("Extraction des data-testid : " + page.url() + " (" + count + " éléments).");
        for (int i = 0; i < count; i++) {
            String testId = elements.nth(i).getAttribute("data-testid");
            log.append("- ").append(testId).append('\n');
            TestExecutionLog.info("data-testid : " + testId);
        }

        Files.createDirectories(LOG_FILE.getParent());
        Files.writeString(
                LOG_FILE,
                log,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
}