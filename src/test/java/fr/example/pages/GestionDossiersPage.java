package fr.example.pages;

import com.microsoft.playwright.Page;

public class GestionDossiersPage extends BasePage {

    public static final String FOLDERS_RETURN_BTN = "folders-return-btn";
    public static final String FOLDERS_FILTER_RESET_FILTER = "folders-filter-reset-filter";
    public static final String USERS_LIST_TABLE = "users-list-table";
    public static final String FOLDERS_LIST_SORT_NUMBER = "folders-list-sort-number";
    public static final String FOLDERS_LIST_SORT_NAME = "folders-list-sort-name";
    public static final String FOLDERS_LIST_SORT_EMAIL = "folders-list-sort-email";
    public static final String FOLDERS_LIST_SORT_CREATION_DATE = "folders-list-sort-creation-date";
    public static final String FOLDERS_LIST_SORT_SYNCHRO_DATE = "folders-list-sort-synchro-date";
    public static final String FOLDERS_LIST_SORT_STATUS = "folders-list-sort-status";
    public static final String USERS_LIST_ROW = "users-list-row";
    public static final String FOLDERS_LIST_NUMBER = "folders-list-number";
    public static final String FOLDERS_LIST_NAME = "folders-list-name";
    public static final String FOLDERS_LIST_EMAIL = "folders-list-email";
    public static final String FOLDERS_LIST_CREATION_DATE = "folders-list-creation-date";
    public static final String FOLDERS_LIST_SYNCHRO_DATE = "folders-list-synchro-date";
    public static final String FOLDERS_LIST_STATUS = "folders-list-status";
    public static final String FOLDER_LIST_RIGHTS_BTN = "folder-list-rights-btn";
    public static final String FOLDER_LIST_AUTO_VALIDATE_BTN = "folder-list-auto-validate-btn";
    public static final String FOLDER_LIST_DETAILS_BTN = "folder-list-details-btn";
    public static final String FOLDER_LIST_EMAIL_BTN = "folder-list-email-btn";
    public static final String FOLDER_LIST_INFOS_BTN = "folder-list-infos-btn";


    public GestionDossiersPage(Page page) {
        super(page);
    }

    public static void chercherDossier(String codeDossier) {
        PageObjectManager.getGestionDossiersPage().chercherDossierSurCettePage(codeDossier);
    }

    private void chercherDossierSurCettePage(String codeDossier) {
        executerAction("GestionDossiersPage.chercherDossier : recherche du dossier.",
                () -> remplir(page.locator("input[type='text'][placeholder='Numéro']"), codeDossier));
        attendreMillisecondes(5000);
        attendreMillisecondes(5000); // Attendre un court instant pour s'assurer que la recherche est bien effectuée
    }

    public static int compteurDossiersListe() {
        GestionDossiersPage gestionDossiersPage = PageObjectManager.getGestionDossiersPage();
        return gestionDossiersPage.executer(
                "GestionDossiersPage.compteurDossiersListe : comptage des dossiers affichés.",
                () -> gestionDossiersPage.page.getByTestId(USERS_LIST_ROW).count());
    }

    
}
