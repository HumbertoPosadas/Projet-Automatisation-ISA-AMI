package fr.example.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class FileTools {

    public static final String DOSSIER_ORIGIN = "src\\test\\resources\\dossiers\\";
    public static final String DOSSIER_ZZ = "C:\\CO\\25.20\\IsaCowp.gi\\Maj_ZZ\\";

    public static int copieFile(String sourcePath, String destinationPath) {
        Path source = Paths.get(sourcePath);
        Path destinationDirectory = Paths.get(destinationPath);
        Path destination = destinationDirectory.resolve(source.getFileName());
        TestExecutionLog.action("Copie du fichier " + source + " vers " + destination);

        // Vérification que le fichier source existe
        if (!Files.exists(source)) {
            RuntimeException error = new RuntimeException("Le fichier source n'existe pas : " + sourcePath);
            TestExecutionLog.failure("La copie ne peut pas démarrer.", error);
            throw error;
        }

        try {
            Path hasFileCopied = Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            if (hasFileCopied != null){
                TestExecutionLog.result("Copie réussie : " + destination);
                // attente de 5 secondes pour permettre de finaliser la copie du dossier avant de lancer l'application
                HelperTools.waitFor(5, TimeUnit.SECONDS);
                return 1;
            } else {
                TestExecutionLog.result("La copie a échoué : " + destination);
                return 0;
            }
        } catch (IOException e) {
            RuntimeException error = new RuntimeException(
                    "Erreur lors de la copie du fichier de " + source + " vers " + destination + ": " + e.getMessage(),
                    e);
            TestExecutionLog.failure("La copie a échoué.", error);
            throw error;
        }
    }

    public static void emptyDirectory(String directoryPath) {
        Path directory = Paths.get(directoryPath);
        TestExecutionLog.action("Vidage du répertoire " + directory);

        // Vérification que le répertoire existe
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            RuntimeException error =
                    new RuntimeException("Le répertoire n'existe pas ou n'est pas un répertoire : " + directoryPath);
            TestExecutionLog.failure("Le vidage du répertoire ne peut pas démarrer.", error);
            throw error;
        }

        int[] deletedCount = {0};
        try (Stream<Path> files = Files.list(directory)) {
            files.forEach(file -> {
                try {
                    Files.delete(file);
                    deletedCount[0]++;
                    TestExecutionLog.result("Fichier supprimé : " + file);
                } catch (IOException e) {
                    RuntimeException error =
                            new RuntimeException("Erreur lors de la suppression du fichier : " + file, e);
                    TestExecutionLog.failure("La suppression du fichier a échoué.", error);
                    throw error;
                }
            });
            TestExecutionLog.result("Vidage terminé : " + deletedCount[0] + " fichier(s) supprimé(s).");
        } catch (IOException e) {
            RuntimeException error = new RuntimeException(
                    "Erreur lors de la lecture du répertoire : " + directoryPath, e);
            TestExecutionLog.failure("Le vidage du répertoire a échoué.", error);
            throw error;
        }
    }
    
}
