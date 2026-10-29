package fr.example.tools.sikuliimages;

import org.sikuli.script.Pattern;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public final class IsacomptaImages {

        private IsacomptaImages() {
    }

    private static Pattern loadPattern(String imageName) {
        URL imageResource = IsacomptaImages.class.getResource(imageName);
        if (imageResource == null) {
            throw new IllegalStateException("Image "+imageName+" introuvable dans le dossier de la classe : " + imageName);
        }

        try {
            return new Pattern(Path.of(imageResource.toURI()).toString());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Impossible de lire l'image Sikuli : " + imageName, e);
        }
    }

    //--------------------------- Images ISACOMPTA CONNECT ---------------------------

    public static final Pattern CO_ICONE_ISACOMPTA = loadPattern("co_isacompta_logo1.png").similar(0.90);
    public static final Pattern AUTHENTIFICATION_SCREEN = loadPattern("Authentification1.png").similar(0.89);
    public static final Pattern CO_LISTE_DES_ENTREPRISES_TITRE =loadPattern("Liste_des_entreprises_title1.png").similar(0.92);
    public static final Pattern CO_BOUTON_OUVRIR = loadPattern("Bouton_Ouvrir.png").similar(0.92);
    public static final Pattern CO_DOSSIER_EN_COPIE = loadPattern("co_copie_du_dossier.png").similar(0.89);
    public static final Pattern CO_CONFIRMATION_MODALE_TITLE = loadPattern("co_confirmation_modale.png").similar(0.90);
    public static final Pattern CO_BOUTON_FICHE_CLIENT = loadPattern("co_btn_fiche_client1.png").similar(0.92);
    public static final Pattern CO_BOUTON_ECHANGES = loadPattern("co_btn_echanges1.png").similar(0.90);
    public static final Pattern CO_BTN_AMI_COMPTA = loadPattern("co_btn_ami_compta1.png").similar(0.90);
    public static final Pattern CO_PUBLICATION_PARAMETRAGE_VERS_AMI_COMPTA = loadPattern("co_publier_paramettrage_ami_compta1.png").similar(0.90);
    public static final Pattern CO_DETAIL_TRAITEMENT_TITRE = loadPattern("co_detail_traitement_titre1.png").similar(0.90);
    public static final Pattern CO_STATUT_RAPPORT_TERMINE = loadPattern("co_statut_rapport_termine1.png").similar(0.90);


    //--------------------------- Images ISAGI CONNECT ---------------------------
    public static final Pattern GI_ICONE_ISAGI = loadPattern("gi-icone_isagi1.png").similar(0.92);
    public static final Pattern GI_BOUTON_SAUVEGARDER = loadPattern("gi_bouton_sauvegarder1.png").similar(0.90);
    public static final Pattern GI_ONGLET_SAISIE_CLIENTS = loadPattern("gi_saisie_client1.png").similar(0.92);
    public static final Pattern GI_COMPTABILITE = loadPattern("gi_option_comptabilité.png").similar(0.93);
    public static final Pattern GI_OPTION_AMI_COMPTA_ACTIVATED = loadPattern("gi_dlaac_activated1.png").similar(0.92);
    public static final Pattern GI_OPTION_AMI_COMPTA_NOT_ACTIVATED = loadPattern("gi_dlaac_desactivated.png").similar(0.92);
    public static final Pattern GI_OPTION_AMI_COMPTA_CONFIRMATION = loadPattern("gi_modale_information1.png").similar(0.88);
}























