package fr.example.tests;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;
import org.sikuli.script.*;

import fr.example.tools.FileTools;
import fr.example.tools.HelperTools;
import fr.example.tools.sikuliimages.*;
import fr.example.tools.SikuliHelperTools;
import fr.example.tools.SikuliLaunchTools;
import fr.example.tools.TestLoggingExtension;


import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import fr.example.pages.AdministrationPage;
import fr.example.pages.ConnexionPage;
import fr.example.pages.GestionDossiersPage;
import fr.example.pages.PageObjectManager;
import fr.example.tools.EnvConfig;
import fr.example.tools.PlaywrightExtension;
import fr.example.tools.TestIdExtractor;
import fr.example.tools.TestLoggingExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({PlaywrightExtension.class, TestLoggingExtension.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PublicationDossier {

    private static final Path LOG_FILE = Path.of("target", "test-results", "testids.log");
    private static final String codeDossier = "TEST01";
    private static Boolean isAmiActivated = false;

    @Test
    @Order(1)
    void absenceDossierDansAMI(Page page) throws Exception {
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL"));
        ConnexionPage.refuserCookiesSiAffiche();
        ConnexionPage.connexionUtilisateur(
                EnvConfig.get("USER_ADMIN_USERNAME"),
                EnvConfig.get("USER_ADMIN_PASSWORD"));
        page.waitForURL("**/dashboard");


        PageObjectManager.getBasePage().naviguerVers("https://preprod.ami-compta.fr/#/admin");
        page.waitForURL("**/admin");
        //attendre le chargement complet de la page
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.getByTestId(AdministrationPage.ADMIN_FOLDERS_BTN).click();
        page.waitForURL("**/folders");


        GestionDossiersPage.chercherDossier(codeDossier);
        int dossiersTrouves = GestionDossiersPage.compteurDossiersListe();

        assertEquals(0, dossiersTrouves, "Le dossier " + codeDossier + " a été trouvé avant sa publication");

    }

    Screen screen = HelperTools.getScreen();

    @Test
    @Order(2)
    void remonterLeDossier() throws FindFailed {
        // Copie du fichier dans le dossier ZZ
        String fileName = "STEST01.SVG";
        int isFileCopied = FileTools.copieFile(FileTools.DOSSIER_ORIGIN + fileName, FileTools.DOSSIER_ZZ);
        assertTrue(isFileCopied == 1,"Le fichier doit être copié dans le dossier ZZ.");
        
        // Lancement de l'application ISACOMPTA avec authentification et ouverture du dossier
        int isIsacomptaCompletLaunched = SikuliLaunchTools.lancementComptaComplet("ADMACON", "Pa$$w0rd", codeDossier, 540);
        HelperTools.waitFor(5, TimeUnit.SECONDS);

        assertTrue(isIsacomptaCompletLaunched == 1, "L'application ISACOMPTA n'a pas été lancée avec succès, l'authentification a échoué ou le dossier n'a pas été ouvert.");

    }   

    @Test 
    @Order(3)
    void parametrerLeDossierPourAMICompta() throws FindFailed {
        // L'utilisateur se trouve à l'accuei du dossier dans ISACOMPTA CONNECT, il faut activer l'option AMI Compta dans le dossier si jamais elle n'est pas actvée
        int activationResult = SikuliHelperTools.activerAmiComptaDansDossier();
        if (activationResult == 2) {
            SikuliHelperTools.clickWithLog(IsacomptaImages.CO_ICONE_ISACOMPTA, "Retour à ISACOMPTA");
            screen.type(Key.F4, KeyModifier.ALT);
            HelperTools.waitFor(20, TimeUnit.SECONDS);

            int isIsacomptaCompletLaunched = SikuliLaunchTools.lancementComptaComplet("ADMACON", "Pa$$w0rd", codeDossier, 90);
            assertTrue(isIsacomptaCompletLaunched == 1, "L'application ISACOMPTA n'a pas été lancée avec succès, l'authentification a échoué ou le dossier n'a pas été ouvert.");
            HelperTools.waitFor(5, TimeUnit.SECONDS);   
        }   
        if (activationResult == 1 || activationResult == 2){
            isAmiActivated = true;
        }
        assertTrue(activationResult == 1 || activationResult == 2, "L'activation de l'option AMI Compta n'a pas été effectuée avec succès.");
        assertTrue(isAmiActivated,"Ami Compta n'a pas été activé sur le dossier de test");
    }

    @Test 
    @Order(4)
    void publierLeDossier() throws FindFailed {
        // L'utilisateur se trouve à l'accuei du dossier dans ISACOMPTA CONNECT, l'option AMI Compta est activée, il faut publier le dossier
        int publicationResult = SikuliHelperTools.publierLeDossierVersAmi();
        if (publicationResult == 1) {isAmiActivated = true;}

        // Fermeture de l'application ISACOMPTA CONNECT
        SikuliHelperTools.clickWithLog(IsacomptaImages.CO_ICONE_ISACOMPTA, "Fermeture de l'application ISACOMPTA CONNECT par click sur l'icône + alt+F4");
        screen.type(Key.F4, KeyModifier.ALT);
        assertTrue(publicationResult == 1, "La publication du dossier vers AMI Compta n'a pas été effectuée avec succès.");
        assertTrue(isAmiActivated,"Ami Compta n'a pas été activé sur le dossier de test");
    }

    @Test
    @Order(5)
    void presenceDossierDansAMI(Page page) throws Exception {
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL"));
        ConnexionPage.refuserCookiesSiAffiche();
        ConnexionPage.connexionUtilisateur(
                EnvConfig.get("USER_ADMIN_USERNAME"),
                EnvConfig.get("USER_ADMIN_PASSWORD"));
        page.waitForURL("**/dashboard");


        PageObjectManager.getBasePage().naviguerVers("https://preprod.ami-compta.fr/#/admin");
        page.waitForURL("**/admin");
        //attendre le chargement complet de la page
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.getByTestId(AdministrationPage.ADMIN_FOLDERS_BTN).click();
        page.waitForURL("**/folders");


        GestionDossiersPage.chercherDossier(codeDossier);
        int dossiersTrouves = GestionDossiersPage.compteurDossiersListe();

        assertEquals(1, dossiersTrouves, "Le dossier " + codeDossier + " n'a pas été trouvé après sa publication");

    }

    @Test 
    @Order(6)
    void desactiverAMIdansDossier() throws Exception {
        int isIsacomptaCompletLaunched = SikuliLaunchTools.lancementComptaComplet("ADMACON", "Pa$$w0rd", codeDossier, 540);
        HelperTools.waitFor(5, TimeUnit.SECONDS);

        SikuliHelperTools.desactiverAmiComptaDansDossier();
        HelperTools.waitFor(10, TimeUnit.SECONDS);

        SikuliHelperTools.clickWithLog(IsacomptaImages.CO_ICONE_ISACOMPTA,"Fermeture de l'application ISACOMPTA CONNECT");
        screen.type(Key.F4, KeyModifier.ALT);

    }

    @Test @Order(7)
    void absenceDossierDansAMI2(Page page) throws Exception {
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL"));
        ConnexionPage.refuserCookiesSiAffiche();
        ConnexionPage.connexionUtilisateur(
                EnvConfig.get("USER_ADMIN_USERNAME"),
                EnvConfig.get("USER_ADMIN_PASSWORD"));
        page.waitForURL("**/dashboard");

        PageObjectManager.getBasePage().naviguerVers("https://preprod.ami-compta.fr/#/admin");
        page.waitForURL("**/admin");
        //attendre le chargement complet de la page
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.getByTestId(AdministrationPage.ADMIN_FOLDERS_BTN).click();
        page.waitForURL("**/folders");


        GestionDossiersPage.chercherDossier(codeDossier);
        int dossiersTrouves = GestionDossiersPage.compteurDossiersListe();

        assertEquals(0, dossiersTrouves, "Le dossier " + codeDossier + " a été trouvé avant sa publication");

    }

}
