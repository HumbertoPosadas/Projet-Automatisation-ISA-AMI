package fr.example.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;

public class AccountingPage extends BasePage {
    public static final String IMAGE_CONTAINER_FULLSCREEN_BTN = "image-container-fullscreen-btn";
    public static final String IMAGE_CONTAINER_DOWNLOAD_BTN = "image-container-download-btn";
    public static final String IMAGE_CONTAINER_ROTATE_RIGHT_BTN = "image-container-rotate-right-btn";
    public static final String IMAGE_CONTAINER_ROTATE_LEFT_BTN = "image-container-rotate-left-btn";
    public static final String IMAGE_CONTAINER_ZOOM_IN_BTN = "image-container-zoom-in-btn";
    public static final String IMAGE_CONTAINER_ZOOM_SLIDER = "image-container-zoom-slider";
    public static final String IMAGE_CONTAINER_ZOOM_OUT_BTN = "image-container-zoom-out-btn";
    public static final String HEADER_COMPONENT = "header-component";
    public static final String HEADER_IMG = "header-img";
    public static final String ACCOUNTING_EXERCISE_SELECTOR = "accounting-exercise-selector";
    public static final String FOLDER_SELECTOR_BTN = "folder-selector-btn";
    public static final String ACCOUNTING_CUTTING_COUNTER_BTN = "accounting-cutting-counter-btn";
    public static final String ACCOUNTING_CUTTING_COUNTER_VALUE = "accounting-cutting-counter-value";
    public static final String ACCOUNTING_ERROR_COUNTER_BTN = "accounting-error-counter-btn";
    public static final String ACCOUNTING_ERROR_COUNTER_VALUE = "accounting-error-counter-value";
    public static final String ACCOUNTING_IMPORTING_COUNTER_BTN = "accounting-importing-counter-btn";
    public static final String ACCOUNTING_IMPORTING_COUNTER_VALUE = "accounting-importing-counter-value";
    public static final String HEADER_HOME_BTN = "header-home-btn";
    public static final String HEADER_HELP_BTN = "header-help-btn";
    public static final String HEADER_NOTIFICATION_CENTER_BTN = "header-notification-center-btn";
    public static final String HEADER_AVATAR_BTN = "header-avatar-btn";
    public static final String MENU_BUTTON = "menu-button";
    public static final String INVOICE_LIST_SELECT_ALL_ROWS_CHECKBOX = "invoice-list-select-all-rows-checkbox";
    public static final String INVOICE_LIST_FILTER_IN_PROGRESS = "invoice-list-filter-in-progress";
    public static final String INVOICE_LIST_FILTER_DATE = "invoice-list-filter-date";
    public static final String INVOICE_LIST_STATUS_SELECTOR_BTN = "invoice-list-status-selector-btn";
    public static final String INVOICE_LIST_RESET_FILTER_BTN = "invoice-list-reset-filter-btn";
    public static final String INVOICE_LIST_GLOBAL_FILTER = "invoice-list-global-filter";
    public static final String TABLE_INVOICE_LIST = "table-invoice-list";
    public static final String INVOICE_LIST_SORT_JOURNAL = "invoice-list-sort-journal";
    public static final String INVOICE_LIST_SORT_DATE = "invoice-list-sort-date";
    public static final String INVOICE_LIST_SORT_STATUS = "invoice-list-sort-status";
    public static final String INVOICE_LIST_SORT_NUMBER = "invoice-list-sort-number";
    public static final String INVOICE_LIST_SORT_LABEL = "invoice-list-sort-label";
    public static final String INVOICE_LIST_SORT_TTC = "invoice-list-sort-ttc";
    public static final String INVOICE_LIST_ROW = "invoice-list-row";
    public static final String INVOICE_ROW_CHECKBOX = "invoice-row-checkbox";
    public static final String INVOICE_LIST_JOURNAL = "invoice-list-journal";
    public static final String INVOICE_LIST_DATE = "invoice-list-date";
    public static final String INVOICE_LIST_STATUS = "invoice-list-status";
    public static final String INVOICE_LIST_NUMBER = "invoice-list-number";
    public static final String INVOICE_LIST_LABEL = "invoice-list-label";
    public static final String INVOICE_LIST_TTC = "invoice-list-ttc";
    public static final String INVOICE_LIST_HISTORY_BTN = "invoice-list-history-btn";
    public static final String INVOICE_EDIT_TYPE_SELECTOR = "invoice-edit-type-selector";
    public static final String INVOICE_EDIT_DATE_SELECTOR = "invoice-edit-date-selector";
    public static final String INVOICE_EDIT_NUMBER_INPUT = "invoice-edit-number-input";
    public static final String INVOICE_EDIT_COUNTERPART_SELECTOR = "invoice-edit-counterpart-selector";
    public static final String INVOICE_EDIT_LABEL_INPUT = "invoice-edit-label-input";
    public static final String INVOICE_EDIT_TTC_INPUT = "invoice-edit-ttc-input";
    public static final String INVOICE_EDIT_DUE_DATE_SELECTOR = "invoice-edit-due-date-selector";
    public static final String INVOICE_EDIT_PAYMENT_SCHEDULE_BTN = "invoice-edit-payment-schedule-btn";
    public static final String INVOICE_EDIT_ERROR_MESSAGE = "invoice-edit-error-message";
    public static final String INVOICE_EDIT_WARNING_MESSAGE = "invoice-edit-warning-message";
    public static final String INVOICE_DETAIL_ROW = "invoice-detail-row";
    public static final String INVOICE_DETAIL_DELETE = "invoice-detail-delete";
    public static final String INVOICE_DETAIL_PCI_FOLDER_SELECTOR = "invoice-detail-pci-folder-selector";
    public static final String INVOICE_DETAIL_LABEL_INPUT = "invoice-detail-label-input";
    public static final String INVOICE_DETAIL_DEBIT_INPUT = "invoice-detail-debit-input";
    public static final String INVOICE_DETAIL_CREDIT_INPUT = "invoice-detail-credit-input";
    public static final String INVOICE_DETAIL_TVA_CODE_SELECTOR = "invoice-detail-tva-code-selector";
    public static final String INVOICE_DETAIL_TVA_RATE_SELECTOR = "invoice-detail-tva-rate-selector";
    public static final String INVOICE_EDIT_TOTAL_HT = "invoice-edit-total-ht";
    public static final String INVOICE_EDIT_TOTAL_TVA = "invoice-edit-total-tva";
    public static final String INVOICE_EDIT_GAP_TTC = "invoice-edit-gap-ttc";
    public static final String INVOICE_EDIT_GAP_TVA = "invoice-edit-gap-tva";
    public static final String TABLE_INVOICE_EDIT = "table-invoice-edit";
    public static final String INVOICE_EDIT_JOURNAL_SELECTOR = "invoice-edit-journal-selector";
    public static final String INVOICE_EDIT_PAYMENT_TYPE_SELECTOR = "invoice-edit-payment-type-selector";
    public static final String TABLE_INVOICE_DETAILS = "table-invoice-details";
    public static final String ADD_INVOICE_DETAIL = "add-invoice-detail";
    public static final String INVOICE_EDIT_COMMENT_INPUT = "invoice-edit-comment-input";
    public static final String ACCOUNTING_ARTICLES_BTN = "accounting-articles-btn";
    public static final String ACCOUNTING_MANAGE_BTN = "accounting-manage-btn";
    public static final String ACCOUNTING_CANCEL_BTN = "accounting-cancel-btn";
    public static final String ACCOUNTING_SAVE_BTN = "accounting-save-btn";
    public static final String ACCOUNTING_VALIDATE_BTN = "accounting-validate-btn";
    public static final String MENU_PANEL = "menu-panel";
    public static final String MENU_ITEM_HOME = "menu-item-home";
    public static final String MENU_ITEM_IMPORT = "menu-item-import";
    public static final String MENU_ITEM_CUTTING = "menu-item-cutting";
    public static final String MENU_ITEM_ACCOUNTING = "menu-item-accounting";
    public static final String MENU_ITEM_SEND = "menu-item-send";
    public static final String MENU_ITEM_HISTORY = "menu-item-history";
    public static final String MENU_ITEM_TRASH = "menu-item-trash";
    public static final String MENU_ITEM_SORTING = "menu-item-sorting";
    public static final String MENU_ITEM_DASHBOARD = "menu-item-dashboard";
    public static final String MENU_ITEM_STATISTICS = "menu-item-statistics";
    public static final String MENU_ITEM_TRACEABILITY = "menu-item-traceability";
    public static final String MENU_ITEM_LOGOUT = "menu-item-logout";
    public static final String ACCOUNTING_VALIDATE_MULTIPLE_MODAL = "accounting-validate-multiple-modal";
    public static final String ACCOUNTING_VALIDATE_REFERENCE_MODAL = "accounting-validate-reference-modal";
    public static final String ACCOUNTING_VALIDATE_SIMILAR_MODAL = "accounting-validate-similar-modal";
    public static final String ACCOUNTING_MANAGE_FOLDER_MODAL = "accounting-manage-folder-modal";
    public static final String FOLDER_SELECTOR_ROW = "folder-selector-row";
    public static final String PCI_FOLDER_SELECTOR_INPUT = "pci-folder-selector-input";
    public static final String PCI_FOLDER_SELECTOR_ROW = "pci-folder-selector-row";
    public static final String folderSearchbox = "input.p-select-filter[role='searchbox']";


    public AccountingPage(Page page) {
        super(page);
    }

    //Methode pour chercher un dossier 
    public static void chercherDossier(String codeDossier) {
        AccountingPage accountingPage = PageObjectManager.getAccountingPage();
        accountingPage.executerAction("AccountingPage.chercherDossier : recherche du dossier.",
                () -> accountingPage.page.getByTestId(FOLDER_SELECTOR_BTN).click());

        accountingPage.executerAction("AccountingPage.chercherDossier : remplir le champ de recherche du dossier.",
                () -> accountingPage.page.locator(folderSearchbox).fill(codeDossier));

        accountingPage.executerAction("AccountingPage.chercherDossier : cliquer sur la ligne dossier dans la liste.",
                () -> accountingPage.page.getByTestId(FOLDER_SELECTOR_ROW).filter(new Locator.FilterOptions().setHasText(codeDossier)).first().click());
    }

    public static int chercherCompte(String codeCompte, String libelleCompte) {
    AccountingPage accountingPage = PageObjectManager.getAccountingPage();
    
    accountingPage.executerAction("AccountingPage.chercherCompte : cliquer sur le selecteur compte",
            () -> accountingPage.page.getByTestId(INVOICE_DETAIL_PCI_FOLDER_SELECTOR).first().click());

    accountingPage.executerAction("AccountingPage.chercherCompte : remplir le champ de recherche du compte.",
            () -> accountingPage.page.getByTestId(PCI_FOLDER_SELECTOR_INPUT).pressSequentially(codeCompte, new Locator.PressSequentiallyOptions().setDelay(500)));

    // 1. On cible les éléments correspondants aux filtres
    Locator elementsTrouves = accountingPage.page.getByTestId(PCI_FOLDER_SELECTOR_ROW)
            .filter(new Locator.FilterOptions().setHasText(codeCompte))
            .filter(new Locator.FilterOptions().setHasText(libelleCompte));

    // 2. On vérifie s'il y a au moins un résultat
    if (elementsTrouves.count() > 0) {
        accountingPage.executerAction("AccountingPage.chercherCompte : cliquer sur la ligne du compte trouvée.",
                () -> elementsTrouves.first().click());
        return 1; // Trouvé
    } else {
        return 0; // Non trouvé
    }
}
    

}
