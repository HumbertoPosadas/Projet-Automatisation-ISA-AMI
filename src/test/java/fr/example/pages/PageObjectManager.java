package fr.example.pages;

import com.microsoft.playwright.Page;
import fr.example.tools.PlaywrightManager;

public final class PageObjectManager {
    private static Page page;
    private static BasePage basePage;
    private static ConnexionPage connexionPage;
    private static AccueilPage accueilPage;
    private static GestionDossiersPage gestionDossiersPage;
    private static AccountingPage accountingPage;

    private PageObjectManager() {
    }

    public static synchronized ConnexionPage getConnexionPage() {
        Page currentPage = PlaywrightManager.getPage();
        if (connexionPage == null || page != currentPage) {
            page = currentPage;
            basePage = new BasePage(currentPage);
            connexionPage = new ConnexionPage(currentPage);
            accueilPage = new AccueilPage(currentPage);
            gestionDossiersPage = new GestionDossiersPage(currentPage);
            accountingPage = new AccountingPage(currentPage);
        }
        return connexionPage;
    }

    public static synchronized BasePage getBasePage() {
        Page currentPage = PlaywrightManager.getPage();
        if (basePage == null || page != currentPage) {
            page = currentPage;
            basePage = new BasePage(currentPage);
            connexionPage = new ConnexionPage(currentPage);
            accueilPage = new AccueilPage(currentPage);
            gestionDossiersPage = new GestionDossiersPage(currentPage);
            accountingPage = new AccountingPage(currentPage);
        }
        return basePage;
    }

    public static synchronized AccueilPage getAccueilPage() {
        Page currentPage = PlaywrightManager.getPage();
        if (accueilPage == null || page != currentPage) {
            page = currentPage;
            basePage = new BasePage(currentPage);
            connexionPage = new ConnexionPage(currentPage);
            accueilPage = new AccueilPage(currentPage);
            gestionDossiersPage = new GestionDossiersPage(currentPage);
            accountingPage = new AccountingPage(currentPage);
        }
        return accueilPage;
    }

    public static synchronized GestionDossiersPage getGestionDossiersPage() {
        Page currentPage = PlaywrightManager.getPage();
        if (gestionDossiersPage == null || page != currentPage) {
            page = currentPage;
            basePage = new BasePage(currentPage);
            connexionPage = new ConnexionPage(currentPage);
            accueilPage = new AccueilPage(currentPage);
            gestionDossiersPage = new GestionDossiersPage(currentPage);
            accountingPage = new AccountingPage(currentPage);
        }
        return gestionDossiersPage;
    }

    public static synchronized AccountingPage getAccountingPage() {
        Page currentPage = PlaywrightManager.getPage();
        if (accountingPage == null || page != currentPage) {
            page = currentPage;
            basePage = new BasePage(currentPage);
            connexionPage = new ConnexionPage(currentPage);
            accueilPage = new AccueilPage(currentPage);
            gestionDossiersPage = new GestionDossiersPage(currentPage);
            accountingPage = new AccountingPage(currentPage);
        }
        return accountingPage;
    }
}
