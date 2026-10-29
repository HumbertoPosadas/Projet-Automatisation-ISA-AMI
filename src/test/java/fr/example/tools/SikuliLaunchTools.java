package fr.example.tools;

import java.io.IOException;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.example.tools.sikuliimages.IsacomptaImages;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Key;
import org.sikuli.script.Pattern;

public class SikuliLaunchTools {

    private static String isacomptaAppPath = "C:\\CO\\25.20\\IsaCowp.gi\\Client\\CO.Application.exe";
    public static String isagiAppPath = "C:\\CO\\25.20\\IsaGiwp\\Client\\GI.Application.exe";
    private static String dataSetLabel = "C:\\CO\\25.20\\IsaGiwf\\ZZ.GI";
    private static String domainId = "86074";


    public static int lancementCompta(int timeoutInSeconds) {      
        TestExecutionLog.action("LaunchTools.lancementCompta : démarrage d'ISACOMPTA.");
        //lancement de l'application
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(isacomptaAppPath);
            Process process = processBuilder.start();
            TestExecutionLog.result("Processus ISACOMPTA démarré (PID " + process.pid() + ").");
            boolean authenticationScreenFound =
                    HelperTools.waitForImage(IsacomptaImages.AUTHENTIFICATION_SCREEN, timeoutInSeconds);
            TestExecutionLog.result("Écran d'authentification ISACOMPTA détecté : " + authenticationScreenFound);
            if (authenticationScreenFound) {
                TestExecutionLog.result("lancementCompta retourne 1.");
                return 1;
            } else {
                TestExecutionLog.result("lancementCompta retourne 0.");
                return 0;                
            }
        } catch (IOException e) {
            RuntimeException error =
                    new RuntimeException("Erreur lors du lancement d'ISACOMPTA : " + e.getMessage(), e);
            TestExecutionLog.failure("Le lancement d'ISACOMPTA a échoué.", error);
            throw error;
        } 
    }

    public static int lancementComptaAuthentification(String login, String password, int timeoutInSeconds) {
        TestExecutionLog.action("LaunchTools.lancementComptaAuthentification : démarrage et authentification ISACOMPTA.");
        // Construction du chemin de l'exécutable
        List<String> command = new ArrayList<>();
        command.add(isacomptaAppPath);
        command.add("/IP.AUTHENTICATION.DomainId="+domainId);
        command.add("/IP.AUTHENTICATION.DataSetLabel=" + dataSetLabel);
        command.add("/IP.AUTHENTICATION.Login=" + login);
        command.add("/IP.AUTHENTICATION.Password=" + password);

        // Lancement de l'application avec les paramètres d'authentification
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            TestExecutionLog.result("Processus ISACOMPTA démarré (PID " + process.pid() + ").");
            boolean companyListFound =
                    HelperTools.waitForImage(IsacomptaImages.CO_LISTE_DES_ENTREPRISES_TITRE, timeoutInSeconds);
            TestExecutionLog.result("Écran de liste des entreprises détecté : " + companyListFound);
            if (companyListFound) {
                TestExecutionLog.result("lancementComptaAuthentification retourne 1.");
                System.out.println("L'application ISACOMPTA a été lancée avec succès et l'authentification a réussi.");
                return 1;
            } else {
                TestExecutionLog.result("lancementComptaAuthentification retourne 0.");
                return 0;                
            }
        } catch (IOException e) {
            RuntimeException error =
                    new RuntimeException("Erreur lors du lancement d'ISACOMPTA : " + e.getMessage(), e);
            TestExecutionLog.failure("Le lancement authentifié d'ISACOMPTA a échoué.", error);
            throw error;
        }
        
    }

    public static int lancementComptaComplet(String login, String password, String codeDossier, int timeout){
        TestExecutionLog.action("LaunchTools.lancementComptaComplet : démarrage, authentification et ouverture du dossier.");
        // Construction du chemin de l'exécutable
        List<String> command = new ArrayList<>();
        command.add(isacomptaAppPath);
        command.add("/IP.AUTHENTICATION.DomainId="+domainId);
        command.add("/IP.AUTHENTICATION.DataSetLabel=" + dataSetLabel);
        command.add("/IP.AUTHENTICATION.Login=" + login);
        command.add("/IP.AUTHENTICATION.Password=" + password);
        command.add("/CO.Entree.Dossier=" + codeDossier);

        // Lancement de l'application avec les paramètres d'authentification
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            TestExecutionLog.result("Processus ISACOMPTA démarré (PID " + process.pid() + ").");
            boolean openButtonFound = waitForExpectedScreen(IsacomptaImages.CO_BOUTON_OUVRIR, timeout);
            TestExecutionLog.result("Bouton d'ouverture du dossier détecté : " + openButtonFound);
            if (openButtonFound) {
                TestExecutionLog.result("lancementComptaComplet retourne 1.");
                System.out.println("L'application ISACOMPTA a été lancée avec succès, l'authentification a réussi et le dossier a été ouvert.");
                return 1;
            } else {
                TestExecutionLog.result("lancementComptaComplet retourne 0.");
                return 0;                
            }
        } catch (IOException e) {
            RuntimeException error =
                    new RuntimeException("Erreur lors du lancement d'ISACOMPTA : " + e.getMessage(), e);
            TestExecutionLog.failure("Le lancement complet d'ISACOMPTA a échoué.", error);
            throw error;
        }
    }

    public static int lancementGi() {
        TestExecutionLog.action("LaunchTools.lancementGi : démarrage d'ISAGI.");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(isagiAppPath);
            Process process = processBuilder.start();
            TestExecutionLog.result("Processus ISAGI démarré (PID " + process.pid() + ").");
            boolean authenticationScreenFound =
                    HelperTools.waitForImage(IsacomptaImages.AUTHENTIFICATION_SCREEN, 30);
            TestExecutionLog.result("Écran d'authentification ISAGI détecté : " + authenticationScreenFound);
            if (authenticationScreenFound) {
                TestExecutionLog.result("lancementGi retourne 1.");
                System.out.println("L'application ISAGI a été lancée avec succès.");
                return 1;
            } else {
                TestExecutionLog.result("lancementGi retourne 0.");
                return 0;                
            }
        } catch (IOException e) {
            RuntimeException error =
                    new RuntimeException("Erreur lors du lancement d'ISAGI : " + e.getMessage(), e);
            TestExecutionLog.failure("Le lancement d'ISAGI a échoué.", error);
            throw error;
        }

    }

    public static int lancementGiAuthentification(String login, String password, int timeoutInSeconds) {
        TestExecutionLog.action("LaunchTools.lancementGiAuthentification : démarrage et authentification ISAGI.");
        // Construction du chemin de l'exécutable
        List<String> command = new ArrayList<>();
        command.add(isagiAppPath);
        command.add("/IP.AUTHENTICATION.DomainId="+domainId);
        command.add("/IP.AUTHENTICATION.DataSetLabel=" + dataSetLabel);
        command.add("/IP.AUTHENTICATION.Login=" + login);
        command.add("/IP.AUTHENTICATION.Password=" + password);

        // Lancement de l'application avec les paramètres d'authentification
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            TestExecutionLog.result("Processus ISAGI démarré (PID " + process.pid() + ").");
            boolean saveButtonFound =
                    HelperTools.waitForImage(IsacomptaImages.GI_BOUTON_SAUVEGARDER, timeoutInSeconds);
            TestExecutionLog.result("Bouton de sauvegarde ISAGI détecté : " + saveButtonFound);
            if (saveButtonFound) {
                TestExecutionLog.result("lancementGiAuthentification retourne 1.");
                System.out.println("L'application ISAGI a été lancée avec succès et l'authentification a réussi.");
                return 1;
            } else {
                TestExecutionLog.result("lancementGiAuthentification retourne 0.");
                return 0;                
            }
            
        } catch (IOException e) {
            RuntimeException error =
                    new RuntimeException("Erreur lors du lancement d'ISAGI : " + e.getMessage(), e);
            TestExecutionLog.failure("Le lancement authentifié d'ISAGI a échoué.", error);
            throw error;
        }
    }

    private static boolean waitForExpectedScreen(Pattern expectedScreen, int timeoutInSeconds) {
        if (timeoutInSeconds < 0) {
            throw new IllegalArgumentException("Le délai d'attente ne peut pas être négatif.");
        }

        List<IntermediateDialog> intermediateDialogs = List.of(
                new IntermediateDialog("CO_DOSSIER_EN_COPIE", IsacomptaImages.CO_DOSSIER_EN_COPIE),
                new IntermediateDialog("CO_CONFIRMATION_MODALE_TITLE", IsacomptaImages.CO_CONFIRMATION_MODALE_TITLE));
        Set<String> handledDialogs = new HashSet<>();
        long deadline = System.nanoTime() + timeoutInSeconds * 1_000_000_000L;

        while (true) {
            for (IntermediateDialog dialog : intermediateDialogs) {
                if (HelperTools.waitForImage(dialog.pattern(), 0)) {
                    // cliquer sur l'image afin de s'assurer que le focus est sur la fenêtre de dialogue avant d'envoyer la touche Entrée
                    try {
                        HelperTools.getScreen().click(dialog.pattern());
                        if (handledDialogs.add(dialog.name())) {
                            TestExecutionLog.action(
                                    "Fenêtre intermédiaire détectée : " + dialog.name() + ". Appui sur Entrée.");
                            HelperTools.getScreen().type(Key.ENTER);
                            TestExecutionLog.result("Entrée envoyée pour fermer/confirmer " + dialog.name() + ".");
                        }
                    } catch (FindFailed e) {
                        throw new RuntimeException("Impossible d'interagir avec la fenêtre intermédiaire "
                                + dialog.name() + ".", e);
                    }
                } else {
                    handledDialogs.remove(dialog.name());
                }
            }

            long remainingNanos = deadline - System.nanoTime();
            if (remainingNanos <= 0) {
                return HelperTools.waitForImage(expectedScreen, 0);
            }

            double pollSeconds = Math.min(0.25, remainingNanos / 1_000_000_000.0);
            if (HelperTools.waitForImage(expectedScreen, pollSeconds)) {
                return true;
            }
        }
    }

    private record IntermediateDialog(String name, Pattern pattern) {
    }

}
