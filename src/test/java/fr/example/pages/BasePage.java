package fr.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import fr.example.tools.TestExecutionLog;

import java.util.Objects;

public class BasePage {
    protected final Page page;

    public BasePage(Page page) {
        this.page = Objects.requireNonNull(page, "La page Playwright est obligatoire.");
    }

    public void naviguerVers(String url) {
        executerAction("BasePage.naviguerVers : navigation vers l'URL demandée.",
                () -> page.navigate(url));
    }
    
    public void remplir(Locator locator, String valeur) {
        Objects.requireNonNull(locator, "Le locator Playwright est obligatoire.");
        Objects.requireNonNull(valeur, "La valeur à saisir est obligatoire.");
        executerAction("BasePage.remplir : champ " + locator + ".",
                () -> locator.fill(valeur));
    }

    public void cliquer(Locator locator) {
        Objects.requireNonNull(locator, "Le locator Playwright est obligatoire.");
        executerAction("BasePage.cliquer : élément " + locator + ".",
                locator::click);
    }

    public void cocher(Locator locator) {
        Objects.requireNonNull(locator, "Le locator Playwright est obligatoire.");
        executerAction("BasePage.cocher : case " + locator + ".",
                locator::check);
    }

    public String obtenirTexte(Locator locator) {
        Objects.requireNonNull(locator, "Le locator Playwright est obligatoire.");
        return executer("BasePage.obtenirTexte : lecture du texte de " + locator + ".",
                locator::textContent);
    }

    public void attendreVisible(Locator locator) {
        Objects.requireNonNull(locator, "Le locator Playwright est obligatoire.");
        executerAction("BasePage.attendreVisible : élément " + locator + ".",
                () -> locator.waitFor(
                        new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE)));
    }

    public void attendreURL(String motif) {
        Objects.requireNonNull(motif, "Le motif d'URL est obligatoire.");
        executerAction("BasePage.attendreURL : attente de l'URL " + motif + ".",
                () -> page.waitForURL(motif));
    }

    public void attendreMillisecondes(double duree) {
        executerAction("BasePage.attendreMillisecondes : attente fixe de " + duree + " ms.",
                () -> page.waitForTimeout(duree));
    }

    protected final void executerAction(String action, Runnable operation) {
        TestExecutionLog.action(action);
        try {
            operation.run();
            TestExecutionLog.result("Action Playwright terminée : " + action);
        } catch (RuntimeException e) {
            TestExecutionLog.failure("Échec de l'action Playwright : " + action, e);
            throw e;
        }
    }

    protected final <T> T executer(String action, Operation<T> operation) {
        TestExecutionLog.action(action);
        try {
            T result = operation.run();
            TestExecutionLog.result("Action Playwright terminée : " + action);
            return result;
        } catch (RuntimeException e) {
            TestExecutionLog.failure("Échec de l'action Playwright : " + action, e);
            throw e;
        }
    }

    @FunctionalInterface
    protected interface Operation<T> {
        T run();
    }
}
