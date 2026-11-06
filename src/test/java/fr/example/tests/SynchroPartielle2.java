/** 
 * Ce test correspond à l'automatisation du test "Synchronisation partielle - modification compte et code TVA" -> https://squash.ami-compta.fr/squash/test-case-workspace/test-case/292/content?anchor=information
 * 
 * Le dossier STRSYNC1I.SVG contient le plan comptable et codes TVA dans son état initial avant toute modification.
 * Le dossier STRSYNC1M.SVG contient le plan comptable et codes TVA après modification.
 */

package fr.example.tests;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;
import org.sikuli.script.*;

import fr.example.tools.FileTools;
import fr.example.tools.HelperTools;
import fr.example.tools.sikuliimages.*;
import fr.example.tools.SikuliHelperTools;
import fr.example.tools.SikuliLaunchTools;
import fr.example.tools.TestIdExtractor;
import fr.example.tools.TestLoggingExtension;


import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import fr.example.pages.*;
import fr.example.tools.EnvConfig;
import fr.example.tools.PlaywrightExtension;

import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({PlaywrightExtension.class, TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SynchroPartielle2 {

    private static final String codeDossier = "SYNSUP1";
    private static final String compte6aSupprimer = "6080000001";
    private static final String compte6aSupprimerLibelle = "COMPTE 6 A SUPPRIMER";
    private static final String compte7aSupprimer = "7080000002";
    private static final String compte7aSupprimerLibelle = "COMPTE 7 A SUPPRIMER";
    private static final String compte401aSupprimer = "4010000003";
    private static final String compte401aSupprimerLibelle = "COMPTE 401 A SUPPRIMER";
    private static final String compte411aSupprimer = "4110000004";
    private static final String compte411aSupprimerLibelle = "COMPTE 411 A SUPPRIMER";
    private static final String journalaSupprimer = "A1";
    private static final String journalaSupprimerLibelle = "JOURNAL A SUPPRIMER";
    private static final String codeTVAaSupprimer = "AA";
    private static final String codeTVAaSupprimerLibelle = "CODE TVA A SUPPRIMER";

    Screen screen = HelperTools.getScreen();
    
    
    //Remonter le dossier initial
    @Test 
    @Order(1)
    void remonterLeDossierInitial() {
        // Copie du fichier dans le dossier ZZ
        String fileName = "SSYNSUP1I.SVG";
        int isFileCopied = FileTools.copieFile(FileTools.DOSSIER_ORIGIN + fileName, FileTools.DOSSIER_ZZ);

        // Lancement de l'application ISACOMPTA avec authentification et ouverture du dossier
        int isIsacomptaCompletLaunched = SikuliLaunchTools.lancementComptaComplet("ADMACON", "Pa$$w0rd", codeDossier, 540);
        HelperTools.waitFor(5, TimeUnit.SECONDS);

        // Verifications
        assertTrue(isFileCopied == 1,"Le fichier doit être copié dans le dossier ZZ.");
        assertTrue(isIsacomptaCompletLaunched == 1, "L'application ISACOMPTA n'a pas été lancée avec succès, l'authentification a échoué ou le dossier n'a pas été ouvert.");

    }

    //Faire la publication du dossier initial vers Ami Compta
    @Test 
    @Order(2)
    void publicationDossierInitial() throws FindFailed {
        // L'utilisateur se trouve à l'accuei du dossier dans ISACOMPTA CONNECT, l'option AMI Compta est activée, il faut publier le dossier
        int publicationResult = SikuliHelperTools.publierLeDossierVersAmi();
        // Fermeture de l'application ISACOMPTA CONNECT
        SikuliHelperTools.clickWithLog(IsacomptaImages.CO_ICONE_ISACOMPTA, "Fermeture de l'application ISACOMPTA CONNECT par click sur l'icône + alt+F4");
        screen.type(Key.F4, KeyModifier.ALT);

        // Verifications
        assertTrue(publicationResult == 1, "La publication du dossier vers AMI Compta n'a pas été effectuée avec succès.");
        
    }

    //Chercher dans AMI la presence des concepts à supprimer
    @Test 
    @Order(3)
    void chercherConceptsASupprimerDansAmi(Page page) throws Exception {
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL"));
        ConnexionPage.refuserCookiesSiAffiche();
        ConnexionPage.connexionUtilisateur(
                EnvConfig.get("USER_COMPTABLE_USERNAME"),
                EnvConfig.get("USER_COMPTABLE_PASSWORD"));
        page.waitForURL("**/home"); 
        Boolean isOnHomePage = page.url().contains("/home");
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL")+"#/accounting");
        page.waitForURL("**/accounting");        
        page.waitForLoadState(LoadState.NETWORKIDLE);   //attendre le chargement complet de la page        
        AccountingPage.chercherDossier(codeDossier);
        page.waitForTimeout(5000);
        // chercher le compte 6* à supprimer
        int isCompte6Found = AccountingPage.chercherCompte(compte6aSupprimer, compte6aSupprimerLibelle);
        // chercher le compte 6* à supprimer
        int isCompte7Found = AccountingPage.chercherCompte(compte7aSupprimer, compte7aSupprimerLibelle);
        // chercher le compte 6* à supprimer
        int isCompte401Found = AccountingPage.chercherCompte(compte401aSupprimer, compte401aSupprimerLibelle);
        // chercher le compte 6* à supprimer
        int isCompte411Found = AccountingPage.chercherCompte(compte411aSupprimer, compte411aSupprimerLibelle);
        // chercher le journal à supprimer
        int isJournalFound = AccountingPage.chercherJournal(journalaSupprimer, journalaSupprimerLibelle);
        // Chercher le code TVA initial
        int isCodeTVAFound = AccountingPage.chercherCodeTVA(codeTVAaSupprimer, codeTVAaSupprimerLibelle);
        

        // Verifications
        assertTrue(isCompte6Found == 1, "Le compte 6* à supprimer n'a pas été trouvé.");
        assertTrue(isCompte7Found == 1, "Le compte 7* à supprimer n'a pas été trouvé.");
        assertTrue(isCompte401Found == 1, "Le compte 401* à supprimer n'a pas été trouvé.");
        assertTrue(isCompte411Found == 1, "Le compte 411* à supprimer n'a pas été trouvé.");
        assertTrue(isJournalFound == 1, "Le journal à supprimer n'a pas été trouvé.");
        assertTrue(isCodeTVAFound == 1, "Le code TVA à supprimer n'a pas été trouvé.");
        
    }

    //Remonter le dossier modifié qui comporte les concepts supprimés
    @Test 
    @Order(4)
    void remonterLeDossierModifie() {
        // Copie du fichier dans le dossier ZZ
        String fileName = "SSYNSUP1M.SVG";
        int isFileCopied = FileTools.copieFile(FileTools.DOSSIER_ORIGIN + fileName, FileTools.DOSSIER_ZZ);

        // Lancement de l'application ISACOMPTA avec authentification et ouverture du dossier
        int isIsacomptaCompletLaunched = SikuliLaunchTools.lancementComptaComplet("ADMACON", "Pa$$w0rd", codeDossier, 540);
        HelperTools.waitFor(5, TimeUnit.SECONDS);

        // Verifications
        assertTrue(isFileCopied == 1,"Le fichier doit être copié dans le dossier ZZ.");
        assertTrue(isIsacomptaCompletLaunched == 1, "L'application ISACOMPTA n'a pas été lancée avec succès, l'authentification a échoué ou le dossier n'a pas été ouvert.");

    }    


    //Faire la publication du dossier modifié vers Ami Compta
    @Test 
    @Order(5)
    void publicationDossierModifie() throws FindFailed {
        // L'utilisateur se trouve à l'accuei du dossier dans ISACOMPTA CONNECT, l'option AMI Compta est activée, il faut publier le dossier
        int publicationResult = SikuliHelperTools.publierLeDossierVersAmi();
        // Fermeture de l'application ISACOMPTA CONNECT
        SikuliHelperTools.clickWithLog(IsacomptaImages.CO_ICONE_ISACOMPTA, "Fermeture de l'application ISACOMPTA CONNECT par click sur l'icône + alt+F4");
        screen.type(Key.F4, KeyModifier.ALT);

        // Verifications
        assertTrue(publicationResult == 1, "La publication du dossier vers AMI Compta n'a pas été effectuée avec succès.");
        
    }

    //Chercher dans AMI l'absence de concepts qui ont été supprimés dans le dossier modifié
    @Test 
    @Order(6)
    void chercherConceptsAbsentsDansAmi(Page page) throws Exception {
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL"));
        ConnexionPage.refuserCookiesSiAffiche();
        ConnexionPage.connexionUtilisateur(
                EnvConfig.get("USER_COMPTABLE_USERNAME"),
                EnvConfig.get("USER_COMPTABLE_PASSWORD"));
        page.waitForURL("**/home"); 
        Boolean isOnHomePage = page.url().contains("/home");
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL")+"#/accounting");
        page.waitForURL("**/accounting");        
        page.waitForLoadState(LoadState.NETWORKIDLE);   //attendre le chargement complet de la page        
        AccountingPage.chercherDossier(codeDossier);
        page.waitForTimeout(5000);
        // chercher le compte 6* à supprimer
        int isCompte6Found = AccountingPage.chercherCompte(compte6aSupprimer, compte6aSupprimerLibelle);
        // chercher le compte 6* à supprimer
        int isCompte7Found = AccountingPage.chercherCompte(compte7aSupprimer, compte7aSupprimerLibelle);
        // chercher le compte 6* à supprimer
        int isCompte401Found = AccountingPage.chercherCompte(compte401aSupprimer, compte401aSupprimerLibelle);
        // chercher le compte 6* à supprimer
        int isCompte411Found = AccountingPage.chercherCompte(compte411aSupprimer, compte411aSupprimerLibelle);
        // chercher le journal à supprimer
        int isJournalFound = AccountingPage.chercherJournal(journalaSupprimer, journalaSupprimerLibelle);
        // Chercher le code TVA initial
        int isCodeTVAFound = AccountingPage.chercherCodeTVA(codeTVAaSupprimer, codeTVAaSupprimerLibelle);
        

        // Verifications
        assertTrue(isCompte6Found == 0, "Le compte 6* à supprimer n'a pas été supprimé.");
        assertTrue(isCompte7Found == 0, "Le compte 7* à supprimer n'a pas été supprimé.");
        assertTrue(isCompte401Found == 0, "Le compte 401* à supprimer n'a pas été supprimé.");
        assertTrue(isCompte411Found == 0, "Le compte 411* à supprimer n'a pas été supprimé.");
        assertTrue(isJournalFound == 0, "Le journal à supprimer n'a pas été supprimé.");
        assertTrue(isCodeTVAFound == 0, "Le code TVA à supprimer n'a pas été supprimé.");
        
    }

    //Remonter le dossier initial pour verifier que les concepts supprimés peuvent etre bien créés à nouveau
    @Test 
    @Order(7)
    void remonterLeDossierInitial2() {
        // Copie du fichier dans le dossier ZZ
        String fileName = "SSYNSUP1I.SVG";
        int isFileCopied = FileTools.copieFile(FileTools.DOSSIER_ORIGIN + fileName, FileTools.DOSSIER_ZZ);

        // Lancement de l'application ISACOMPTA avec authentification et ouverture du dossier
        int isIsacomptaCompletLaunched = SikuliLaunchTools.lancementComptaComplet("ADMACON", "Pa$$w0rd", codeDossier, 540);
        HelperTools.waitFor(5, TimeUnit.SECONDS);

        // Verifications
        assertTrue(isFileCopied == 1,"Le fichier doit être copié dans le dossier ZZ.");
        assertTrue(isIsacomptaCompletLaunched == 1, "L'application ISACOMPTA n'a pas été lancée avec succès, l'authentification a échoué ou le dossier n'a pas été ouvert.");

    }

    
    //Faire la publication du dossier initial vers Ami Compta 
    @Test 
    @Order(8)
    void publicationDossierInitial2() throws FindFailed {
        // L'utilisateur se trouve à l'accuei du dossier dans ISACOMPTA CONNECT, l'option AMI Compta est activée, il faut publier le dossier
        int publicationResult = SikuliHelperTools.publierLeDossierVersAmi();
        // Fermeture de l'application ISACOMPTA CONNECT
        SikuliHelperTools.clickWithLog(IsacomptaImages.CO_ICONE_ISACOMPTA, "Fermeture de l'application ISACOMPTA CONNECT par click sur l'icône + alt+F4");
        screen.type(Key.F4, KeyModifier.ALT);

        // Verifications
        assertTrue(publicationResult == 1, "La publication du dossier vers AMI Compta n'a pas été effectuée avec succès.");
        
    }
    

    //Chercher dans AMI la présence des concepts recréés
    @Test 
    @Order(9)
    void chercherConceptsRecreesDansAmi(Page page) throws Exception {
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL"));
        ConnexionPage.refuserCookiesSiAffiche();
        ConnexionPage.connexionUtilisateur(
                EnvConfig.get("USER_COMPTABLE_USERNAME"),
                EnvConfig.get("USER_COMPTABLE_PASSWORD"));
        page.waitForURL("**/home"); 
        Boolean isOnHomePage = page.url().contains("/home");
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL")+"#/accounting");
        page.waitForURL("**/accounting");        
        page.waitForLoadState(LoadState.NETWORKIDLE);   //attendre le chargement complet de la page        
        AccountingPage.chercherDossier(codeDossier);
        page.waitForTimeout(5000);
        // chercher le compte 6* recréé
        int isCompte6Found = AccountingPage.chercherCompte(compte6aSupprimer, compte6aSupprimerLibelle);
        // chercher le compte 7* recréé
        int isCompte7Found = AccountingPage.chercherCompte(compte7aSupprimer, compte7aSupprimerLibelle);
        // chercher le compte 401* recréé
        int isCompte401Found = AccountingPage.chercherCompte(compte401aSupprimer, compte401aSupprimerLibelle);
        // chercher le compte 411* recréé
        int isCompte411Found = AccountingPage.chercherCompte(compte411aSupprimer, compte411aSupprimerLibelle);
        // chercher le journal recréé
        int isJournalFound = AccountingPage.chercherJournal(journalaSupprimer, journalaSupprimerLibelle);
        // Chercher le code TVA recréé
        int isCodeTVAFound = AccountingPage.chercherCodeTVA(codeTVAaSupprimer, codeTVAaSupprimerLibelle);
        

        // Verifications
        assertTrue(isCompte6Found == 1, "Le compte 6* recréé n'a pas été trouvé.");
        assertTrue(isCompte7Found == 1, "Le compte 7* recréé n'a pas été trouvé.");
        assertTrue(isCompte401Found == 1, "Le compte 401* recréé n'a pas été trouvé.");
        assertTrue(isCompte411Found == 1, "Le compte 411* recréé n'a pas été trouvé.");
        assertTrue(isJournalFound == 1, "Le journal recréé n'a pas été trouvé.");
        assertTrue(isCodeTVAFound == 1, "Le code TVA recréé n'a pas été trouvé.");
        
    }
    
}
