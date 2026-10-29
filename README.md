# Tests de regression desktop et web

Projet Java de tests d'integration pour un logiciel desktop dont les actions
peuvent mettre a jour une interface web :

- **SikuliX** identifie les elements visuels de l'application desktop et interagit avec eux.
- **Playwright** verifie l'interface web et les resultats visibles apres une action desktop.
- **JUnit 5** organise les tests.

## Prerequis

- JDK 17 ou plus recent
- Apache Maven 3.9 ou plus recent
- Windows, macOS ou Linux avec une session graphique active pour les tests SikuliX
- L'application desktop et le service web accessibles pendant les tests

Les tests SikuliX pilotent l'ecran et dependent de la resolution, de l'echelle d'affichage,
du theme et de l'etat de la fenetre. Il est recommande de stabiliser ces parametres dans
l'environnement de regression.

## Installation

Depuis la racine du projet, installer les navigateurs geres par Playwright :

```powershell
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI "-Dexec.args=install chromium"
```

Sur Linux, Playwright peut aussi installer les dependances systeme requises avec :

```powershell
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI "-Dexec.args=install --with-deps chromium"
```

Ces commandes installent uniquement Chromium. Ne lancez pas la commande `install` sans
preciser `chromium`, car cela installerait egalement les autres navigateurs pris en charge.
La configuration du projet lance egalement uniquement Chromium via
`playwright.chromium().launch()` dans `PlaywrightExtension`.

## Configuration et execution

Les tests Playwright utilisent `PlaywrightExtension` pour initialiser une session Chromium
unique pour toute l'execution JUnit. Le navigateur, le contexte et la meme `Page` sont
conserves entre les classes et les methodes de test, puis fermes a la fin de l'execution.
Ajoutez `@ExtendWith(PlaywrightExtension.class)` aux classes Playwright. Un test peut recevoir
`Page page` en parametre ou recuperer la page partagee via `PlaywrightManager.getPage()`.
Comme la page et son etat sont partages, les tests Playwright ne doivent pas s'executer en
parallele et chaque test doit naviguer ou nettoyer l'etat dont il depend.
Chromium est headless par defaut. Pour afficher sa fenetre lors d'un lancement Maven :

```powershell
mvn -Dtest=ConnexionPageTest -Dplaywright.headless=false test
```

Dans une configuration de lancement/debug VS Code, ajoutez `-Dplaywright.headless=false`
aux arguments JVM.

`ConnexionPage` expose aussi des methodes statiques de facade
(`ConnexionPage.remplirChampsConnexion(...)`, `ConnexionPage.soumettreConnexion()`) qui
deleguent a son instance partagee via `PageObjectManager`. Elles necessitent l'initialisation
de `PlaywrightExtension` avant leur appel.

Les Page Objects heritent de `fr.example.pages.BasePage`, qui fournit les interactions
generiques `remplir`, `cliquer`, `obtenirTexte` et `attendreVisible`, qui prennent un `Locator`
Playwright. Le locator peut etre construit a partir d'un `data-testid`, d'un role accessible,
d'un label ou d'un selecteur CSS, par exemple `page.getByTestId("login-submit")`,
`page.getByRole(AriaRole.BUTTON)` ou `page.locator("#login-submit")`. Une page enfant recoit
la `Page` Playwright dans son constructeur et peut appeler ces methodes ou les redefinir si
elle a un comportement particulier.
Les navigations, interactions, lectures et echecs des Page Objects sont inscrits dans le
fichier `execution.txt` du repertoire `logs/test-executions/` cree pour chaque test. Les
valeurs saisies dans les champs ne sont pas journalisees.

Exemple de test de l'extracteur d'identifiants :

```java
@ExtendWith(PlaywrightExtension.class)
class ExempleTest {
    @Test
    void verifierLesTestIds(Page page) throws Exception {
        page.navigate("https://preprod.ami-compta.fr/");
        TestIdExtractor.recupererTousLesTestIds(page);
    }
}
```

L'extracteur ajoute les resultats dans `target/test-results/testids.log` et inscrit chaque
identifiant dans le journal de l'execution courante sous `logs/test-executions/`. Les tests
Playwright qui doivent produire ces journaux utilisent `TestLoggingExtension`. Le test
`TestIdExtractorTest` visite l'URL `AMI_PREPROD_URL` définie dans le fichier `.env` à la
racine du projet, puis extrait les identifiants du véritable HTML servi par le site.
Le fichier `.env` n'est pas versionné. Lancer le test avec :

```powershell
mvn -Dtest=TestIdExtractorTest test
```

Le test nécessite un accès réseau à l'environnement de préproduction.

## Structure

```text
src/
  test/
    java/fr/example/
      pages/
      regression/
        DesktopWebRegressionTest.java
      tests/
        TestIdExtractorTest.java
      tools/
        PlaywrightExtension.java
        TestIdExtractor.java
```

## Environnement graphique
Connecxion RDP Size : {Width=1536, Height=960}
IsaCompta : 
  Gestion du facteur de zoom : 
    Taille en pouces : 19
    Facteur de zoom : 100%
  Réglages graphiques :
    Moteur de rendu graphique : Auto
    Niveau de rendu : Rendu allégé
IsaGI : 
  Gestion du facteur de zoom : 
    Taille en pouces : 19
    Facteur de zoom : 100%
  Réglages graphiques :
    Moteur de rendu graphique : Auto
    Niveau de rendu : Rendu allégé