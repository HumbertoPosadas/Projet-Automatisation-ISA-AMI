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

    private static final String codeDossier = "SYNCPAR";
    private static final String compte6Initiale = "6010001001";
    private static final String libelle6Initiale = "Libellé compte 6 initiale"; 
    private static final String libelle6modifiee = "Libellé compte 6 modifiée";
    private static final String codeTVAInitiale = "SP";
    public static final String codeTVAlibelleInitiale = "SYNCPAR INITIALE";
    public static final String codeTVAlibelleModifiee = "SYNCPAR MODIFIÉE";
    private static final String compte6rajoutee = "6010001002";
    private static final String libellé6rajoutee = "Libellé compte 6 rajoutée";
    private static final String codeTVArajoutee = "SR";
    public static final String codeTVAlibelleRajoutee = "SYNCPAR RAJOUTÉE";

    Screen screen = HelperTools.getScreen();
    
    
    //Remonter le dossier initial
    @Test 
    void remonterLeDossierInitial() {
        // Copie du fichier dans le dossier ZZ
        String fileName = "SSYNCPAR1.SVG";
        int isFileCopied = FileTools.copieFile(FileTools.DOSSIER_ORIGIN + fileName, FileTools.DOSSIER_ZZ);
        assertTrue(isFileCopied == 1,"Le fichier doit être copié dans le dossier ZZ.");

        // Lancement de l'application ISACOMPTA avec authentification et ouverture du dossier
        int isIsacomptaCompletLaunched = SikuliLaunchTools.lancementComptaComplet("ADMACON", "Pa$$w0rd", codeDossier, 540);
        HelperTools.waitFor(5, TimeUnit.SECONDS);

        assertTrue(isIsacomptaCompletLaunched == 1, "L'application ISACOMPTA n'a pas été lancée avec succès, l'authentification a échoué ou le dossier n'a pas été ouvert.");

    }

    //faire la publication du dossier initial vers ami-compta 
    @Test 
    void publicationDossierInitial() throws FindFailed {
        // L'utilisateur se trouve à l'accuei du dossier dans ISACOMPTA CONNECT, l'option AMI Compta est activée, il faut publier le dossier
        int publicationResult = SikuliHelperTools.publierLeDossierVersAmi();
        

        // Fermeture de l'application ISACOMPTA CONNECT
        SikuliHelperTools.clickWithLog(IsacomptaImages.CO_ICONE_ISACOMPTA, "Fermeture de l'application ISACOMPTA CONNECT par click sur l'icône + alt+F4");
        screen.type(Key.F4, KeyModifier.ALT);
        assertTrue(publicationResult == 1, "La publication du dossier vers AMI Compta n'a pas été effectuée avec succès.");
        
    }

    //chercher dans AMI les concepts initiales
    @Test 
    void chercherConceptsInitiales(Page page) throws Exception {
        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL"));
        ConnexionPage.refuserCookiesSiAffiche();
        ConnexionPage.connexionUtilisateur(
                EnvConfig.get("USER_COMPTABLE_USERNAME"),
                EnvConfig.get("USER_COMPTABLE_PASSWORD"));
        page.waitForURL("**/home"); 
        // assertion qui verifie que l'utilisateur comptable se trouve dans la page d'accueil
        assertTrue(page.url().contains("/home"), "L'utilisateur comptable n'est pas sur la page d'accueil.");

        PageObjectManager.getBasePage().naviguerVers(EnvConfig.get("AMI_PREPROD_URL")+"#/accounting");
        page.waitForURL("**/accounting");
        //attendre le chargement complet de la page
        page.waitForLoadState(LoadState.NETWORKIDLE);

        //recuperer les datatestid de la page en cours
        
        AccountingPage.chercherDossier(codeDossier);
        //attente de 5 secondes
        page.waitForTimeout(5000);

        // chercher le compte initiale
        int isCompteFound = AccountingPage.chercherCompte(compte6Initiale, libelle6Initiale);
        assertTrue(isCompteFound == 1, "Le compte initiale n'a pas été trouvé.");

    }

    //Remonter le dossier modifié

    //faire la publication du dossier modifié vers ami-compta
    
    //chercher dans AMI les concepts modifiés

    //Remonter le dossier initial pour la remise en état

    //faire la publication complète du dossier initial pour la remise en état

    //chercher dans AMI les concepts après la remise en état
    
}
