package fr.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import fr.example.tools.TestExecutionLog;

public class ConnexionPage extends BasePage {
    public static final String LOGIN_USERNAME = "login-username";
    public static final String LOGIN_PASSWORD = "login-password";
    public static final String LOGIN_SUBMIT = "login-submit";
    public static final String COOKIE_REJECT_ALL = "cookie-reject-all";
    public static final String COOKIE_PARAMETER = "cookie-parameter";
    public static final String COOKIE_ACCEPT_ALL = "cookie-accept-all";

    public ConnexionPage(Page page) {
        super(page);
    }

    public static void refuserCookiesSiAffiche() {
        ConnexionPage connexionPage = PageObjectManager.getConnexionPage();
        connexionPage.executerAction("ConnexionPage.refuserCookiesSiAffiche : traitement de la bannière cookies.",
                () -> {
                    Locator boutonRefuser = connexionPage.page.getByTestId(COOKIE_REJECT_ALL);
                    if (boutonRefuser.isVisible()) {
                        connexionPage.cliquer(boutonRefuser);
                    } else {
                        TestExecutionLog.result("Aucune bannière cookies visible.");
                    }
                });
    }

    public static void remplirChampsConnexion(String username, String password) {
        PageObjectManager.getConnexionPage().remplirChampsConnexionPourCettePage(username, password);
    }

    private void remplirChampsConnexionPourCettePage(String username, String password) {
        executerAction("ConnexionPage.remplirChampsConnexion : saisie des identifiants.",
                () -> {
                    remplir(page.getByTestId(LOGIN_USERNAME), username);
                    remplir(champMotDePassePourCettePage(), password);
                });
    }

    public static void soumettreConnexion() {
        PageObjectManager.getConnexionPage().soumettreConnexionPourCettePage();
    }

    private void soumettreConnexionPourCettePage() {
        executerAction("ConnexionPage.soumettreConnexion : soumission du formulaire.",
                () -> cliquer(page.getByTestId(LOGIN_SUBMIT)));
    }

    public static Locator obtenirChampUtilisateur() {
        ConnexionPage connexionPage = PageObjectManager.getConnexionPage();
        return connexionPage.executer(
                "ConnexionPage.obtenirChampUtilisateur : récupération du locator.",
                connexionPage::champUtilisateurPourCettePage);
    }

    private Locator champUtilisateurPourCettePage() {
        return page.getByTestId(LOGIN_USERNAME);
    }

    public static Locator obtenirChampMotDePasse() {
        ConnexionPage connexionPage = PageObjectManager.getConnexionPage();
        return connexionPage.executer(
                "ConnexionPage.obtenirChampMotDePasse : récupération du locator.",
                connexionPage::champMotDePassePourCettePage);
    }

    private Locator champMotDePassePourCettePage() {
        return page.getByTestId(LOGIN_PASSWORD).locator("input");
    }

    public static void connexionUtilisateur(String username, String password) {
        ConnexionPage connexionPage = PageObjectManager.getConnexionPage();
        connexionPage.executerAction("ConnexionPage.connexionUtilisateur : flux de connexion utilisateur.",
                () -> {
                    connexionPage.remplirChampsConnexionPourCettePage(username, password);
                    connexionPage.soumettreConnexionPourCettePage();
                });
    }
}
