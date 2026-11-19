/** 
 * Ce test correspond à l'automatisation du test "Synchronisation partielle - modification compte et code TVA" -> https://squash.ami-compta.fr/squash/test-case-workspace/test-case/292/content?anchor=information
 * 
 * Le dossier STRSYNC1I.SVG contient le plan comptable et codes TVA dans son état initial avant toute modification.
 * Le dossier STRSYNC1M.SVG contient le plan comptable et codes TVA après modification.
 */

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
class TestAdaptationCaptures {

    
    Screen screen = HelperTools.getScreen();

    
    
    //Remonter le dossier initial
    @Test 
    @Order(1)
    void testLancementCompta() {
        SikuliLaunchTools.lancementCompta(60);
    }

    @Test 
    @Order(2)
    void testlancementComptaAuthentification() {
        SikuliLaunchTools.lancementComptaAuthentification("COMPT", "Pa$$w0rd",60);
    }

    @Test 
    @Order(3)
    void testlancementComptaComplet() {
        SikuliLaunchTools.lancementComptaComplet("COMPT", "Pa$$w0rd", "DTAS001",60);
        // Ajouter ici l'action à tester après l'authentification
    }

    @Test 
    @Order(4)
    void testlancementGi() {
        SikuliLaunchTools.lancementGi();
    }

    @Test 
    @Order(5)
    void testlancementGiAuthentification() {
        SikuliLaunchTools.lancementGiAuthentification("COMPT", "Pa$$w0rd",60);
    }
    
}
