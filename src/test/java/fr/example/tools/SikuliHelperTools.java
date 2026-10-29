package fr.example.tools;

import fr.example.tools.HelperTools;
import fr.example.tools.TestExecutionLog;
import fr.example.tools.sikuliimages.IsacomptaImages;
import java.util.concurrent.TimeUnit;

import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.KeyModifier;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

public class SikuliHelperTools { 

    private static final Screen screen = HelperTools.getScreen();

    public static int activerAmiComptaDansDossier() throws FindFailed {
        // Méthode qui permet d'activer l'option AMI Compta dans un dossier ouvert à l'accueil dans ISACOMPTA CONNECT
        // Si la methode retourne 1 : l'option AMI Compta était déjà activée
        // Si la methode retourne 2 : l'option AMI Compta a été activée avec succès par la methode
        TestExecutionLog.action("Activation de l'option AMI Compta dans le dossier.");
        try {
            clickWithLog(IsacomptaImages.CO_BOUTON_ECHANGES, "Ouverture du menu Échanges");

            if (HelperTools.waitForImage(IsacomptaImages.CO_BTN_AMI_COMPTA, 3)) {
                TestExecutionLog.result("Le bouton AMI Compta est déjà présent ; aucune activation n'est nécessaire.");
                return 1;
            }

            clickWithLog(IsacomptaImages.CO_BOUTON_FICHE_CLIENT, "Ouverture de la fiche client");
            HelperTools.waitForImageOrFail(IsacomptaImages.GI_COMPTABILITE, 60);
            //Wait de 5 secondes pour s'assurer que la section Comptabilité est bien chargée
            HelperTools.waitFor(5, TimeUnit.SECONDS);
            clickWithLog(IsacomptaImages.GI_COMPTABILITE, "Ouverture de la section Comptabilité");
            clickWithLog(IsacomptaImages.GI_OPTION_AMI_COMPTA_NOT_ACTIVATED, "Activation de l'option AMI Compta");
            HelperTools.waitForImageOrFail(IsacomptaImages.GI_OPTION_AMI_COMPTA_CONFIRMATION, 30);
            clickWithLog(IsacomptaImages.GI_OPTION_AMI_COMPTA_CONFIRMATION, "Confirmation de l'activation AMI Compta");
            // taper sur enter pour valider la confirmation
            typeWithLog(Key.ENTER, "Validation de la confirmation par Enter");
            TestExecutionLog.result("L'option AMI Compta a été activée et la confirmation a été validée par Enter.");
            // Fermer ISAGI pour revenir à ISACOMPTA CONNECT
            clickWithLog(IsacomptaImages.GI_ICONE_ISAGI, "Fermeture de l'application ISAGI par raccourci Alt+F4");
            typeWithLog(Key.F4, KeyModifier.ALT, "Fermeture d'ISAGI par Alt+F4");
            return 2;
        } catch (FindFailed e) {
            TestExecutionLog.failure("L'activation de l'option AMI Compta a échoué.", e);
            throw e;
        } catch (RuntimeException e) {
            TestExecutionLog.failure("L'activation de l'option AMI Compta a échoué.", e);
            throw e;
        }
    }

    public static void desactiverAmiComptaDansDossier() throws FindFailed, InterruptedException {
        // Méthode qui va desactiver la case a cocher Dossier lié avec Ami cOmpta dans la GI.
        // Cette méthode part du principe que l'utilisateur se trouve dans l'accueil du dossier dans ISACOMPTA
        TestExecutionLog.action("Désactivation de l'option AMI Compta dans le dossier.");
        try {
            clickWithLog(IsacomptaImages.CO_BOUTON_FICHE_CLIENT, "Ouverture de la fiche client");
            HelperTools.waitForImageOrFail(IsacomptaImages.GI_COMPTABILITE, 60);
            // Attendre un court instant pour s'assurer que la section Comptabilité est bien visible
            HelperTools.waitFor(5, TimeUnit.SECONDS);
            clickWithLog(IsacomptaImages.GI_COMPTABILITE, "Ouverture de la section Comptabilité");
            HelperTools.waitForImageOrFail(IsacomptaImages.GI_OPTION_AMI_COMPTA_ACTIVATED, 20);
            clickWithLog(IsacomptaImages.GI_OPTION_AMI_COMPTA_ACTIVATED, "Désactivation de l'option AMI Compta");
            HelperTools.waitForImageOrFail(IsacomptaImages.GI_OPTION_AMI_COMPTA_CONFIRMATION, 30);
            clickWithLog(IsacomptaImages.GI_OPTION_AMI_COMPTA_CONFIRMATION, "Confirmation de la désactivation AMI Compta");
            // taper sur enter pour valider la confirmation
            typeWithLog(Key.ENTER, "Validation de la confirmation par Enter");
            HelperTools.waitFor(5, java.util.concurrent.TimeUnit.SECONDS);

            clickWithLog(IsacomptaImages.GI_ICONE_ISAGI, "Fermeture de l'application ISAGI par raccourci Alt+F4");
            typeWithLog(Key.F4, KeyModifier.ALT, "Fermeture d'ISAGI par Alt+F4");

            HelperTools.waitFor(5, java.util.concurrent.TimeUnit.SECONDS);
            TestExecutionLog.result("L'option AMI Compta a été désactivée dans le dossier.");
        } catch (FindFailed | RuntimeException e) {
            TestExecutionLog.failure("La désactivation de l'option AMI Compta a échoué.", e);
            throw e;
        }
    }

    public static int publierLeDossierVersAmi() throws FindFailed {
        clickWithLog(IsacomptaImages.CO_ICONE_ISACOMPTA, "Cliquer sur l'icône ISACOMPTA pour ouvrir liberer le ruban si jamais le regroupement était cliqué");
        clickWithLog(IsacomptaImages.CO_BOUTON_ECHANGES, "Ouverture du menu Échanges");
        clickWithLog(IsacomptaImages.CO_BTN_AMI_COMPTA, "Cliquer sur le bouton AMI Compta pour chercher l'option de publication");
        clickWithLog(IsacomptaImages.CO_PUBLICATION_PARAMETRAGE_VERS_AMI_COMPTA, "Cliquer sur le bouton Publier le paramétrage vers AMI Compta");
        Match detailTraitement = HelperTools.waitForImageOrFail(IsacomptaImages.CO_DETAIL_TRAITEMENT_TITRE, 60);
        if(detailTraitement != null) {
            TestExecutionLog.result("Le détail du traitement est affiché, la publication est en cours.");
        } else {
            FindFailed failure = new FindFailed("Le détail du traitement n'a pas été trouvé, la publication a échoué.");
            TestExecutionLog.failure("Le détail du traitement n'a pas été trouvé, la publication a échoué.", failure);
            throw failure;
        }
        Match statutRapportTermine = HelperTools.waitForImageOrFail(IsacomptaImages.CO_STATUT_RAPPORT_TERMINE, 120);
        if(statutRapportTermine != null) {
            TestExecutionLog.result("La publication du dossier vers AMI Compta est terminée.");
            clickWithLog(IsacomptaImages.CO_DETAIL_TRAITEMENT_TITRE, "Fermeture du détail du traitement par click sur le titre + alt+F4");
            typeWithLog(Key.F4, KeyModifier.ALT, "Fermeture du détail du traitement par Alt+F4");
            TestExecutionLog.result("Le détail du traitement a été fermé avec succès.");
            return 1;
        } else {
            FindFailed failure = new FindFailed("Le statut du rapport terminé n'a pas été trouvé, la publication a échoué.");
            TestExecutionLog.failure("Le statut du rapport terminé n'a pas été trouvé, la publication a échoué.", failure);
            throw failure;
        }

    }

    public static void typeWithLog(String key, String description) {
        typeWithLog(key, 0, description);
    }

    public static void typeWithLog(String key, int modifiers, String description) {
        TestExecutionLog.action(description + ".");
        try {
            screen.type(key, modifiers);
            TestExecutionLog.result(description + " : saisie clavier réussie.");
        } catch (RuntimeException e) {
            TestExecutionLog.failure(description + " : saisie clavier échouée.", e);
            throw e;
        }
    }

    public static void clickWithLog(Pattern target, String description) throws FindFailed {
        TestExecutionLog.action(description + ".");
        try {
            screen.click(target);
            TestExecutionLog.result(description + " : action réussie.");
        } catch (FindFailed e) {
            TestExecutionLog.failure(description + " : image cible introuvable, action échouée.", e);
            throw e;
        } catch (RuntimeException e) {
            TestExecutionLog.failure(description + " : action échouée.", e);
            throw e;
        }
    }


}
