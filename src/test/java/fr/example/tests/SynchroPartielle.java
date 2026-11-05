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
class SynchroPartielle {

    private static final String codeDossier = "TRSYNC1";
    private static final String compte6Initiale = "6010001001";
    private static final String libelle6Initiale = "Libellé compte 6 initial"; 
    private static final String libelle6modifiee = "Libellé compte 6 modifié";
    private static final String codeTVAInitiale = "SP";
    public static final String codeTVAlibelleInitiale = "CODE TVA INITIAL";
    public static final String codeTVAlibelleModifiee = "CODE TVA MODIFIE";
    private static final String compte6rajoutee = "6010001002";
    private static final String libelle6rajoutee = "Libellé compte 6 rajouté";
    private static final String codeTVArajoutee = "SR";
    public static final String codeTVAlibelleRajoutee = "CODE TVA RAJOUTE";

    Screen screen = HelperTools.getScreen();
    
    
    //Remonter le dossier initial
    @Test 
    @Order(1)
    void remonterLeDossierInitial() {
        // Copie du fichier dans le dossier ZZ
        String fileName = "STRSYNC1I.SVG";
        int isFileCopied = FileTools.copieFile(FileTools.DOSSIER_ORIGIN + fileName, FileTools.DOSSIER_ZZ);

        // Lancement de l'application ISACOMPTA avec authentification et ouverture du dossier
        int isIsacomptaCompletLaunched = SikuliLaunchTools.lancementComptaComplet("ADMACON", "Pa$$w0rd", codeDossier, 540);
        HelperTools.waitFor(5, TimeUnit.SECONDS);

        // Verifications
        assertTrue(isFileCopied == 1,"Le fichier doit être copié dans le dossier ZZ.");
        assertTrue(isIsacomptaCompletLaunched == 1, "L'application ISACOMPTA n'a pas été lancée avec succès, l'authentification a échoué ou le dossier n'a pas été ouvert.");

    }

    //faire la publication du dossier initial vers ami-compta 
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

    //chercher dans AMI les concepts initiales
    @Test 
    @Order(3)
    void chercherConceptsInitiales(Page page) throws Exception {
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
        // chercher le compte initiale
        int isCompteFound = AccountingPage.chercherCompte(compte6Initiale, libelle6Initiale);
        // Chercher le code TVA initial
        int isCodeTVAFound = AccountingPage.chercherCodeTVA(codeTVAInitiale, codeTVAlibelleInitiale);
        // Chercher le compte rajouté
        int isCompteRajouteFound = AccountingPage.chercherCompte(compte6rajoutee, libelle6rajoutee);
        // Chercher le code TVA rajouté
        int isCodeTVARajouteFound = AccountingPage.chercherCodeTVA(codeTVArajoutee, codeTVAlibelleRajoutee);
        // Chercher le compte modifié
        int isCompteModifieFound = AccountingPage.chercherCompte(compte6Initiale, libelle6modifiee);
        // Chercher le code TVA modifié
        int isCodeTVAModifieFound = AccountingPage.chercherCodeTVA(codeTVAInitiale, codeTVAlibelleModifiee);

        // Verifications
        assertTrue(isOnHomePage, "L'utilisateur comptable n'est pas sur la page d'accueil.");
        assertTrue(isCompteFound == 1, "Le compte initiale n'a pas été trouvé.");
        assertTrue(isCodeTVAFound == 1, "Le code TVA initial n'a pas été trouvé.");
        assertTrue(isCompteRajouteFound == 0, "Le compte rajouté n'a pas été trouvé.");
        assertTrue(isCodeTVARajouteFound == 0, "Le code TVA rajouté n'a pas été trouvé.");
        assertTrue(isCompteModifieFound == 0, "Le compte modifié n'a pas été trouvé.");
        assertTrue(isCodeTVAModifieFound == 0, "Le code TVA modifié n'a pas été trouvé.");

    }

    //Remonter le dossier modifié
    @Test 
    @Order(4)
    void remonterDossierModifie() throws FindFailed {
        
        String fileName = "STRSYNC1M.SVG";  // Copie du fichier dans le dossier ZZ
        int isFileCopied = FileTools.copieFile(FileTools.DOSSIER_ORIGIN + fileName, FileTools.DOSSIER_ZZ);
        // Lancement de l'application ISACOMPTA avec authentification et ouverture du dossier
        int isIsacomptaCompletLaunched = SikuliLaunchTools.lancementComptaComplet("ADMACON", "Pa$$w0rd", codeDossier, 540);
        HelperTools.waitFor(5, TimeUnit.SECONDS);

        // Vérifications
        assertTrue(isFileCopied == 1,"Le fichier doit être copié dans le dossier ZZ.");
        assertTrue(isIsacomptaCompletLaunched == 1, "L'application ISACOMPTA n'a pas été lancée avec succès, l'authentification a échoué ou le dossier n'a pas été ouvert.");

    }

    //faire la publication du dossier modifié vers ami-compta
    @Test 
    @Order(5)
    void publicationDossierModifie() throws FindFailed {
        // L'utilisateur se trouve à l'accuei du dossier dans ISACOMPTA CONNECT, l'option AMI Compta est activée, il faut publier le dossier
        int publicationResult = SikuliHelperTools.publierLeDossierVersAmi();
        // Fermeture de l'application ISACOMPTA CONNECT
        SikuliHelperTools.clickWithLog(IsacomptaImages.CO_ICONE_ISACOMPTA, "Fermeture de l'application ISACOMPTA CONNECT par click sur l'icône + alt+F4");
        screen.type(Key.F4, KeyModifier.ALT);

        // Vérifications
        assertTrue(publicationResult == 1, "La publication du dossier vers AMI Compta n'a pas été effectuée avec succès.");
    }
    
    //chercher dans AMI les concepts modifiés
    @Test 
    @Order(6)   
    void chercherConceptsModifies(Page page) throws Exception {
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL"));
        ConnexionPage.refuserCookiesSiAffiche();
        ConnexionPage.connexionUtilisateur(
                EnvConfig.get("USER_COMPTABLE_USERNAME"),
                EnvConfig.get("USER_COMPTABLE_PASSWORD"));
        page.waitForURL("**/home"); 
        Boolean isOnHomePage = page.url().contains("/home");
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL")+"#/accounting");
        page.waitForURL("**/accounting");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        AccountingPage.chercherDossier(codeDossier);
        page.waitForTimeout(5000);
        // chercher le compte modifié
        int isComptemodifieFound = AccountingPage.chercherCompte(compte6Initiale, libelle6modifiee);
        // Chercher le code TVA modifié
        int isCodeTVAmodifieFound = AccountingPage.chercherCodeTVA(codeTVAInitiale, codeTVAlibelleModifiee);
        // chercher le compte rajouté
        int isCompteRajouteFound = AccountingPage.chercherCompte(compte6rajoutee, libelle6rajoutee);
        // chercher le code TVA rajouté
        int isCodeTVARajouteFound = AccountingPage.chercherCodeTVA(codeTVArajoutee, codeTVAlibelleRajoutee);
        // chercher le compte initiale
        int isCompteinitialeFound = AccountingPage.chercherCompte(compte6Initiale, libelle6Initiale);
        // Chercher le code TVA initial
        int isCodeTVAinitialeFound = AccountingPage.chercherCodeTVA(codeTVAInitiale, codeTVAlibelleInitiale);

        // Vérifications
        assertTrue(isOnHomePage, "L'utilisateur comptable n'est pas sur la page d'accueil.");
        assertTrue(isComptemodifieFound == 1, "Le compte modifié n'a pas été trouvé.");
        assertTrue(isCodeTVAmodifieFound == 1, "Le code TVA modifié n'a pas été trouvé.");
        assertTrue(isCompteRajouteFound == 1, "Le compte rajouté n'a pas été trouvé.");
        assertTrue(isCodeTVARajouteFound == 1, "Le code TVA rajouté n'a pas été trouvé.");  
        assertTrue(isCompteinitialeFound == 0, "Le compte initiale n'a pas été trouvé.");
        assertTrue(isCodeTVAinitialeFound == 0, "Le code TVA initial n'a pas été trouvé.");
    }

    //Remonter le dossier initial pour la remise en état
    @Test 
    @Order(7)
    void remonterDossierInitial(Page page) throws Exception {
        // Copie du fichier dans le dossier ZZ
        String fileName = "STRSYNC1I.SVG";
        int isFileCopied = FileTools.copieFile(FileTools.DOSSIER_ORIGIN + fileName, FileTools.DOSSIER_ZZ);
        // Lancement de l'application ISACOMPTA avec authentification et ouverture du dossier
        int isIsacomptaCompletLaunched = SikuliLaunchTools.lancementComptaComplet("ADMACON", "Pa$$w0rd", codeDossier, 540);
        HelperTools.waitFor(5, TimeUnit.SECONDS);

        // Vérifications
        assertTrue(isFileCopied == 1,"Le fichier doit être copié dans le dossier ZZ.");
        assertTrue(isIsacomptaCompletLaunched == 1, "L'application ISACOMPTA n'a pas été lancée avec succès, l'authentification a échoué ou le dossier n'a pas été ouvert.");
    }

    //faire la publication complète du dossier initial pour la remise en état
    @Test 
    @Order(8)
    void publicationDossierInitial2() throws FindFailed {
        // L'utilisateur se trouve à l'accuei du dossier dans ISACOMPTA CONNECT, l'option AMI Compta est activée, il faut publier le dossier
        int publicationResult = SikuliHelperTools.publierLeDossierVersAmi();
        // Fermeture de l'application ISACOMPTA CONNECT
        SikuliHelperTools.clickWithLog(IsacomptaImages.CO_ICONE_ISACOMPTA, "Fermeture de l'application ISACOMPTA CONNECT par click sur l'icône + alt+F4");
        screen.type(Key.F4, KeyModifier.ALT);

        //Vérifications
        assertTrue(publicationResult == 1, "La publication du dossier vers AMI Compta n'a pas été effectuée avec succès.");
    }

    //chercher dans AMI les concepts après la remise en état
    @Test 
    @Order(9)
    void chercherConceptsInitiales2(Page page) throws Exception {
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL"));
        ConnexionPage.refuserCookiesSiAffiche();
        ConnexionPage.connexionUtilisateur(
                EnvConfig.get("USER_COMPTABLE_USERNAME"),
                EnvConfig.get("USER_COMPTABLE_PASSWORD"));
        page.waitForURL("**/home"); 
        // assertion qui verifie que l'utilisateur comptable se trouve dans la page d'accueil
        Boolean isOnHomePage = page.url().contains("/home");
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL")+"#/accounting");
        page.waitForURL("**/accounting");
        //attendre le chargement complet de la page
        page.waitForLoadState(LoadState.NETWORKIDLE);
        AccountingPage.chercherDossier(codeDossier);
        //attente de 5 secondes
        page.waitForTimeout(5000);
        // chercher le compte initiale
        // chercher le compte initiale
        int isCompteFound = AccountingPage.chercherCompte(compte6Initiale, libelle6Initiale);
        // Chercher le code TVA initial
        int isCodeTVAFound = AccountingPage.chercherCodeTVA(codeTVAInitiale, codeTVAlibelleInitiale);
        // Chercher le compte rajouté
        int isCompteRajouteFound = AccountingPage.chercherCompte(compte6rajoutee, libelle6rajoutee);
        // Chercher le code TVA rajouté
        int isCodeTVARajouteFound = AccountingPage.chercherCodeTVA(codeTVArajoutee, codeTVAlibelleRajoutee);
        // Chercher le compte modifié
        int isCompteModifieFound = AccountingPage.chercherCompte(compte6Initiale, libelle6modifiee);
        // Chercher le code TVA modifié
        int isCodeTVAModifieFound = AccountingPage.chercherCodeTVA(codeTVAInitiale, codeTVAlibelleModifiee);

        //Vérifications
        assertTrue(isOnHomePage, "L'utilisateur comptable n'est pas sur la page d'accueil.");
        assertTrue(isCompteFound == 1, "Le compte initiale n'a pas été trouvé.");
        assertTrue(isCodeTVAFound == 1, "Le code TVA initial n'a pas été trouvé.");
        assertTrue(isCompteRajouteFound == 0, "Le compte rajouté n'a pas été trouvé.");
        assertTrue(isCodeTVARajouteFound == 0, "Le code TVA rajouté n'a pas été trouvé.");
        assertTrue(isCompteModifieFound == 0, "Le compte modifié n'a pas été trouvé.");
        assertTrue(isCodeTVAModifieFound == 0, "Le code TVA modifié n'a pas été trouvé.");
    }
}
