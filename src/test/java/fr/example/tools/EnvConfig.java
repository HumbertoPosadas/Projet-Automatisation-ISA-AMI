package fr.example.tools;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class EnvConfig {
    private static final Path ENV_FILE = Path.of(".env");

    private EnvConfig() {
    }

    public static String get(String name) throws IOException {
        TestExecutionLog.action("Lecture de la variable " + name + " dans " + ENV_FILE + ".");
        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(ENV_FILE, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            TestExecutionLog.failure("Impossible de lire le fichier " + ENV_FILE + ".", e);
            throw e;
        }

        String value = properties.getProperty(name);
        if (value == null || value.isBlank()) {
            IllegalStateException error =
                    new IllegalStateException("La variable " + name + " est absente ou vide dans .env.");
            TestExecutionLog.failure("Variable d'environnement manquante : " + name, error);
            throw error;
        }
        TestExecutionLog.result("Variable " + name + " lue (valeur non journalisée).");
        return value.trim();
    }
}
