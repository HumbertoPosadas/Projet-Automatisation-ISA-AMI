package fr.example.regression;

import com.microsoft.playwright.Page;
import fr.example.pages.PageObjectManager;
import fr.example.tools.PlaywrightExtension;
import fr.example.tools.TestIdExtractor;
import fr.example.tools.TestLoggingExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({PlaywrightExtension.class, TestLoggingExtension.class})
class DesktopWebRegressionTest {

    @Test
    void extractTestIdsFromPreproduction(Page page) throws Exception {
        String baseUrl = "https://preprod.ami-compta.fr/";
        PageObjectManager.getBasePage().naviguerVers(baseUrl);
        TestIdExtractor.recupererTousLesTestIds(page);
    }
}
