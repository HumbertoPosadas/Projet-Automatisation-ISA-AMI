package fr.example.pages;

import com.microsoft.playwright.Page;

public class AccueilPage extends BasePage {
    public static final String HEADER_COMPONENT = "header-component";
    public static final String HEADER_IMG = "header-img";
    public static final String HOME_HEADER_FILTER_SITE_SELECTOR_BTN = "home-header-filter-site-selector-btn";
    public static final String HOME_HEADER_FILTER_GROUP_SELECTOR_BTN = "home-header-filter-group-selector-btn";
    public static final String HOME_HEADER_FILTER_SECTOR_SELECTOR_BTN = "home-header-filter-sector-selector-btn";
    public static final String HEADER_IMPORT_MOBILE_BUTTON = "header-import-mobile-button";
    public static final String HEADER_LOGOUT_MOBILE_BUTTON = "header-logout-mobile-button";
    public static final String HEADER_HELP_BTN = "header-help-btn";
    public static final String HEADER_NOTIFICATION_CENTER_BTN = "header-notification-center-btn";
    public static final String HEADER_AVATAR_BTN = "header-avatar-btn";
    public static final String HOME_DASHBOARD_FOLDER_CARD = "home-dashboard-folder-card";
    public static final String HOME_DASHBOARD_FOLDER_VALUE = "home-dashboard-folder-value";
    public static final String HOME_DASHBOARD_CUTTING_CARD = "home-dashboard-cutting-card";
    public static final String HOME_DASHBOARD_CUTTING_VALUE = "home-dashboard-cutting-value";
    public static final String HOME_DASHBOARD_ACCOUNTING_CARD = "home-dashboard-accounting-card";
    public static final String HOME_DASHBOARD_ACCOUNTING_VALUE = "home-dashboard-accounting-value";
    public static final String HOME_DASHBOARD_ERRORS_CARD = "home-dashboard-errors-card";
    public static final String HOME_DASHBOARD_ERRORS_VALUE = "home-dashboard-errors-value";
    public static final String HOME_DASHBOARD_ERRORS_LINK = "home-dashboard-errors-link";
    public static final String HOME_EXERCISE_LIST_FILTER_PERIODICITY_SELECTOR_BTN = "home-exercise-list-filter-periodicity-selector-btn";
    public static final String HOME_EXERCISE_LIST_FILTER_CRITICITY_SELECTOR_BTN = "home-exercise-list-filter-criticity-selector-btn";
    public static final String HOME_EXERCISE_LIST_FILTER_DATE_START_INPUT = "home-exercise-list-filter-date-start-input";
    public static final String HOME_EXERCISE_LIST_FILTER_DATE_END_INPUT = "home-exercise-list-filter-date-end-input";
    public static final String HOME_EXERCISE_LIST_FILTER_SHOW_INACTIVES_CHECKBOX = "home-exercise-list-filter-show-inactives-checkbox";
    public static final String HOME_EXERCISE_LIST_FILTER_GLOBAL_INPUT = "home-exercise-list-filter-global-input";
    public static final String HOME_EXERCISE_LIST_TABLE = "home-exercise-list-table";
    public static final String HOME_EXERCISE_LIST_SORT_PERIODICITY = "home-exercise-list-sort-periodicity";
    public static final String HOME_EXERCISE_LIST_SORT_DEPOSIT_DATE = "home-exercise-list-sort-deposit-date";
    public static final String HOME_EXERCISE_LIST_SORT_CRITICITY = "home-exercise-list-sort-criticity";
    public static final String HOME_EXERCISE_LIST_SORT_FOLDER_CODE = "home-exercise-list-sort-folder-code";
    public static final String HOME_EXERCISE_LIST_SORT_COMPANY_NAME = "home-exercise-list-sort-company-name";
    public static final String HOME_EXERCISE_LIST_SORT_DATE_START = "home-exercise-list-sort-date-start";
    public static final String HOME_EXERCISE_LIST_SORT_COUNT_IMPORTING = "home-exercise-list-sort-count-importing";
    public static final String HOME_EXERCISE_LIST_SORT_COUNT_CUTTING = "home-exercise-list-sort-count-cutting";
    public static final String HOME_EXERCISE_LIST_SORT_COUNT_TRAY = "home-exercise-list-sort-count-tray";
    public static final String HOME_EXERCISE_LIST_SORT_COUNT_ACCOUNTING = "home-exercise-list-sort-count-accounting";
    public static final String HOME_EXERCISE_LIST_ROW = "home-exercise-list-row";
    public static final String HOME_EXERCISE_LIST_ROW_LOCKS = "home-exercise-list-row-locks";
    public static final String HOME_EXERCISE_LIST_ROW_PERIODICITY = "home-exercise-list-row-periodicity";
    public static final String HOME_EXERCISE_LIST_ROW_DEPOSIT_DATE = "home-exercise-list-row-deposit-date";
    public static final String HOME_EXERCISE_LIST_ROW_CRITICITY = "home-exercise-list-row-criticity";
    public static final String HOME_EXERCISE_LIST_ROW_FOLDER_CODE = "home-exercise-list-row-folder-code";
    public static final String HOME_EXERCISE_LIST_ROW_COMPANY_NAME = "home-exercise-list-row-company-name";
    public static final String HOME_EXERCISE_LIST_ROW_DATE_START = "home-exercise-list-row-date-start";
    public static final String HOME_EXERCISE_LIST_ROW_COUNT_IMPORTING = "home-exercise-list-row-count-importing";
    public static final String HOME_EXERCISE_LIST_ROW_COUNT_CUTTING = "home-exercise-list-row-count-cutting";
    public static final String HOME_EXERCISE_LIST_ROW_COUNT_TRAY = "home-exercise-list-row-count-tray";
    public static final String HOME_EXERCISE_LIST_ROW_COUNT_ACCOUNTING = "home-exercise-list-row-count-accounting";
    public static final String HOME_EXERCISE_LIST_ROW_ERROR = "home-exercise-list-row-error";
    public static final String HOME_EXERCISE_LIST_PAGINATION_SELECTOR = "home-exercise-list-pagination-selector";
    public static final String HOME_COOKIE_BTN = "home-cookie-btn";


    public AccueilPage(Page page) {
        super(page);
    }

    public static void chercherDossier(String codeDossier) {
        PageObjectManager.getAccueilPage().chercherDossierSurCettePage(codeDossier);
    }

    private void chercherDossierSurCettePage(String codeDossier) {
        executerAction("AccueilPage.chercherDossier : recherche du dossier.",
                () -> {
                    remplir(page.getByTestId(HOME_EXERCISE_LIST_FILTER_GLOBAL_INPUT), codeDossier);
                    cliquer(page.getByTestId(HOME_EXERCISE_LIST_FILTER_SHOW_INACTIVES_CHECKBOX));
                });
        attendreMillisecondes(2000);
    }

    public static int compteurDossiersListe() {
        AccueilPage accueilPage = PageObjectManager.getAccueilPage();
        return accueilPage.executer(
                "AccueilPage.compteurDossiersListe : comptage des dossiers affichés.",
                () -> accueilPage.page.getByTestId(HOME_EXERCISE_LIST_ROW).count());
    }

}
