package fr.example.tools;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.microsoft.playwright.Locator;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

public final class HelperTools {

    private static final Screen SCREEN = new Screen();

    private HelperTools() {
    }

    public static Screen getScreen() {
        return SCREEN;
    }

    public static void waitFor(long duration, TimeUnit unit) {
        Objects.requireNonNull(unit, "L'unité de temps ne peut pas être nulle.");
        if (duration < 0) {
            throw new IllegalArgumentException("La durée d'attente ne peut pas être négative.");
        }

        TestExecutionLog.action("Attente fixe de " + duration + " " + unit.toString().toLowerCase() + ".");
        try {
            unit.sleep(duration);
            TestExecutionLog.result("Attente fixe terminée.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            TestExecutionLog.failure("Attente fixe interrompue.", e);
            throw new IllegalStateException("Le thread a été interrompu pendant l'attente.", e);
        }
    }

    public static boolean waitForImage(Pattern image, double timeoutInSeconds) {
        validateImageWait(image, timeoutInSeconds);
        TestExecutionLog.action("Recherche de l'image Sikuli " + image + " (timeout : "
                + timeoutInSeconds + " s).");
        try {
            boolean found = SCREEN.exists(image, timeoutInSeconds) != null;
            TestExecutionLog.result(found
                    ? "Image Sikuli trouvée : " + image
                    : "Image Sikuli non trouvée après " + timeoutInSeconds + " s : " + image);
            return found;
        } catch (RuntimeException e) {
            TestExecutionLog.failure("Erreur pendant la recherche de l'image Sikuli : " + image, e);
            throw e;
        }
    }

    public static Match waitForImageOrFail(Pattern image, double timeoutInSeconds) throws FindFailed {
        validateImageWait(image, timeoutInSeconds);
        TestExecutionLog.action("Attente obligatoire de l'image Sikuli " + image + " (timeout : "
                + timeoutInSeconds + " s).");
        try {
            Match match = SCREEN.wait(image, timeoutInSeconds);
            TestExecutionLog.result("Image Sikuli trouvée : " + image);
            return match;
        } catch (FindFailed e) {
            TestExecutionLog.failure("Image Sikuli non trouvée après " + timeoutInSeconds + " s : " + image, e);
            throw e;
        } catch (RuntimeException e) {
            TestExecutionLog.failure("Erreur pendant l'attente de l'image Sikuli : " + image, e);
            throw e;
        }
    }

    public static void waitForElement(Locator locator) {
        Objects.requireNonNull(locator, "L'élément Playwright ne peut pas être nul.");
        TestExecutionLog.action("Attente de l'élément Playwright " + locator + ".");
        try {
            locator.waitFor();
            TestExecutionLog.result("Élément Playwright disponible : " + locator);
        } catch (RuntimeException e) {
            TestExecutionLog.failure("Échec de l'attente de l'élément Playwright : " + locator, e);
            throw e;
        }
    }

    private static void validateImageWait(Pattern image, double timeoutInSeconds) {
        Objects.requireNonNull(image, "L'image Sikuli ne peut pas être nulle.");
        if (!Double.isFinite(timeoutInSeconds) || timeoutInSeconds < 0) {
            throw new IllegalArgumentException("Le délai d'attente doit être positif ou nul.");
        }
    }
}
