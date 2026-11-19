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

    public static boolean waitForAnyImage(double timeoutInSeconds, Pattern... images) {
        validateImageWaits(images, timeoutInSeconds);
        TestExecutionLog.action("Recherche de l'une des images Sikuli (timeout : "
                + timeoutInSeconds + " s).");
        long deadline = System.nanoTime() + (long) (timeoutInSeconds * TimeUnit.SECONDS.toNanos(1));
        while (true) {
            for (Pattern image : images) {
                if (SCREEN.exists(image, 0) != null) {
                    TestExecutionLog.result("Image Sikuli trouvée : " + image);
                    return true;
                }
            }

            long remainingNanos = deadline - System.nanoTime();
            if (remainingNanos <= 0) {
                TestExecutionLog.result("Aucune image Sikuli trouvée après " + timeoutInSeconds + " s.");
                return false;
            }
            waitFor(Math.min(TimeUnit.MILLISECONDS.toNanos(250), remainingNanos), TimeUnit.NANOSECONDS);
        }
    }

    public static Match waitForAnyImageOrFail(double timeoutInSeconds, Pattern... images) throws FindFailed {
        validateImageWaits(images, timeoutInSeconds);
        TestExecutionLog.action("Attente obligatoire de l'une des images Sikuli (timeout : "
                + timeoutInSeconds + " s).");
        long deadline = System.nanoTime() + (long) (timeoutInSeconds * TimeUnit.SECONDS.toNanos(1));
        while (true) {
            for (Pattern image : images) {
                Match match = SCREEN.exists(image, 0);
                if (match != null) {
                    TestExecutionLog.result("Image Sikuli trouvée : " + image);
                    return match;
                }
            }

            long remainingNanos = deadline - System.nanoTime();
            if (remainingNanos <= 0) {
                FindFailed failure = new FindFailed("Aucune des images Sikuli attendues n'a été trouvée.");
                TestExecutionLog.failure("Aucune image Sikuli attendue n'a été trouvée après "
                        + timeoutInSeconds + " s.", failure);
                throw failure;
            }
            waitFor(Math.min(TimeUnit.MILLISECONDS.toNanos(250), remainingNanos), TimeUnit.NANOSECONDS);
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

    private static void validateImageWaits(Pattern[] images, double timeoutInSeconds) {
        Objects.requireNonNull(images, "La liste d'images Sikuli ne peut pas être nulle.");
        if (images.length == 0) {
            throw new IllegalArgumentException("Au moins une image Sikuli doit être fournie.");
        }
        for (Pattern image : images) {
            Objects.requireNonNull(image, "L'image Sikuli ne peut pas être nulle.");
        }
        if (!Double.isFinite(timeoutInSeconds) || timeoutInSeconds < 0) {
            throw new IllegalArgumentException("Le délai d'attente doit être positif ou nul.");
        }
    }
}
