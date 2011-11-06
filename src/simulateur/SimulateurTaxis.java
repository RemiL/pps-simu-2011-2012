package simulateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.HashMap;

import presentation.Fenetre;

public class SimulateurTaxis implements ActionListener {

	private double dureeSimulation;
	private int nbEchantillons;
	private ReferentielTemps referentielTemps;
	private int nbRepetitions;
	private int nbTaxis;
	private int nbClientsMax;
	private double pourcentageClientsSatisfaits;
	private double resultat;
	private double accelerationAnimation;

	private CentraleTaxis centrale;
	private Point2D.Double positionCentrale;
	private double vitesse;
	private int rayonVille;
	private GenerateurApparitionClient genApparitionClient;
	private GenerateurPositionDepart genPositionDepart;
	private GenerateurPositionArrivee genPositionArrivee;
	private GenerateurTempsAttente genTempsAttente;
	private HashMap<String, String> parametres;

	private Fenetre fenetre;
	private boolean play;
	private boolean stop;
	private Thread t;
	private int typeSimulation;

	public SimulateurTaxis() {
		fenetre = new Fenetre();
		fenetre.getBoutonSimuler().addActionListener(this);
		fenetre.getBoutonStop().addActionListener(this);
		fenetre.getBoutonPausePlay().addActionListener(this);
		fenetre.getBoutonResultat().addActionListener(this);
		fenetre.getBoutonNouvelleSimulation().addActionListener(this);
		fenetre.getBoutonResimuler().addActionListener(this);
	}

	public void configurer() {
		String[] labels1 = { "Nombre d'échantillons", "Durée de la simulation (h)", "Rayon de la ville (km)",
				"Position x de la centrale (km)", "Position y de la centrale (km)", "Vitesse des taxis (km/h)",
				"Lambda poisson (clients/h)", "Rayon d'exclusion de l'arrivée (km)", "Temps d'attente moyen (min)",
				"Ecart type du temps d'attente (min)", "Nombre de clients max par taxi",
				"Pourcentage de clients satisfaits", "Nombre de répétitions",
				"Accélération de l'animation (0 pour aucune animation)" };
		String[] defaults1 = { "10000", "2", "10", "0", "0", "45", "10", "1", "20", "10", "2", "80", "1", "100" };
		int[] widths1 = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
		String[] labels2 = { "Nombre d'échantillons", "Durée de la simulation (h)", "Rayon de la ville (km)",
				"Position x de la centrale (km)", "Position y de la centrale (km)", "Vitesse des taxis (km/h)",
				"Lambda poisson (clients/h)", "Rayon d'exclusion de l'arrivée (km)", "Temps d'attente moyen (min)",
				"Ecart type du temps d'attente (min)", "Nombre de taxis", "Nombre de clients max par taxi",
				"Nombre de répétitions", "Accélération de l'animation (0 pour aucune animation)" };
		int[] widths2 = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
		String[] defaults2 = { "10000", "2", "10", "0", "0", "45", "10", "1", "20", "10", "2", "2", "1", "100" };
		fenetre.initAffichageInit(labels1, defaults1, widths1, labels2, defaults2, widths2);
	}

	public void simuler() {
		resultat = 0;
		stop = false;
		play = true;
		fenetre.changeBoutonPausePlay(play, stop);

		// On fait la simulation en fonction de son type
		if (typeSimulation == 0) {

		} else {
			// On réalise un certain nombre de fois la simulation pour avoir
			// une moyenne
			for (int rep = 0; rep < nbRepetitions && !stop; rep++) {
				referentielTemps.reset();
				centrale = new CentraleTaxis(referentielTemps, nbTaxis, positionCentrale, vitesse, nbClientsMax);
				fenetre.initAffichageVille(nbTaxis, rayonVille);

				// On effectue la boucle n+1 fois puisque la première itération
				// sert à l'initialisation, les taxis effectueront donc bien n
				// mouvements.
				for (int i = 0; i <= nbEchantillons && !stop; i++) {
					if (play) {
						// On affecte les clients en attente aux taxis
						// disponibles,
						centrale.affecterTaxis();
						// on calcule le déplacement des taxis pendant
						// l'intervalle dt
						centrale.deplacerTaxis();
						// puis on génère l'apparition d'éventuels nouveaux
						// clients.
						simulerApparitionClients();
						// On incrémente l'horloge.
						referentielTemps.incrementerTemps();

						fenetre.setInfos(i, nbTaxis, rep);
						fenetre.setAffichageVille(centrale.getTaxis(), centrale.getClientsEnAttenteAffectationTaxi(),
								centrale.getClientsEnAttentePriseEnCharge());
					} else {
						i--;
					}
					try {
						if (accelerationAnimation != 0.0) {
							// Le temps d'attente entre chaque rafraîchissement
							// de l'animation
							Thread.sleep((long) (referentielTemps.getDt() * 1000 / accelerationAnimation));
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				resultat += 100.0 * (centrale.getNbClients() - centrale.getNbClientsPerdus()) / centrale.getNbClients();
			}
			resultat /= nbRepetitions;
		}

		fenetre.finaliserSimulation();
	}

	private void simulerApparitionClients() {
		int nbClients = genApparitionClient.genererNombreApparitionClient();
		Point2D.Double ptDepart;
		Point2D.Double ptArrivee;
		double tempsAttenteMax;

		for (int i = 0; i < nbClients; i++) {
			ptDepart = genPositionDepart.genererPositionDepart();
			ptArrivee = genPositionArrivee.genererPositionArrivee(ptDepart);
			tempsAttenteMax = genTempsAttente.genererTempsAttente();

			centrale.ajouterClient(new Client(referentielTemps, ptDepart, ptArrivee, tempsAttenteMax));
		}
	}

	public static void main(String[] args) {
		SimulateurTaxis simulateur = new SimulateurTaxis();
		simulateur.configurer();
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == fenetre.getBoutonSimuler()) {
			this.parametres = fenetre.getValues();
			typeSimulation = fenetre.getSimulationType();

			this.nbEchantillons = Integer.parseInt(parametres.get("Nombre d'échantillons"));
			this.dureeSimulation = Double.parseDouble(parametres.get("Durée de la simulation (h)")) * 3600;
			this.referentielTemps = new ReferentielTemps(dureeSimulation / nbEchantillons);

			if (typeSimulation == 0) {
				// On cherche le nombre de taxis à utiliser pour atteindre un
				// certain pourcentage de satisfaction des clients.
				this.pourcentageClientsSatisfaits = Double.parseDouble(parametres
						.get("Pourcentage de clients satisfaits"));
			} else if (typeSimulation == 1) {
				// On cherche le pourcentage de satisfaction des clients
				// en connaissant le nombre de taxis.
				this.nbTaxis = Integer.parseInt(parametres.get("Nombre de taxis"));
			}
			this.nbClientsMax = Integer.parseInt(parametres.get("Nombre de clients max par taxi"));
			this.accelerationAnimation = Double.parseDouble(parametres
					.get("Accélération de l'animation (0 pour aucune animation)"));
			this.nbRepetitions = Integer.parseInt(parametres.get("Nombre de répétitions"));
			this.rayonVille = Integer.parseInt(parametres.get("Rayon de la ville (km)")) * 1000;
			this.vitesse = Double.parseDouble(parametres.get("Vitesse des taxis (km/h)")) * 1000 / 3600;
			this.positionCentrale = new Point2D.Double(Double.parseDouble(parametres
					.get("Position x de la centrale (km)")) * 1000, Double.parseDouble(parametres
					.get("Position y de la centrale (km)")) * 1000);
			this.genApparitionClient = new GenApparitionClientPoisson(Double.parseDouble(parametres
					.get("Lambda poisson (clients/h)")) / 3600 * referentielTemps.getDt());
			this.genPositionArrivee = new GenPositionArrivee(rayonVille, Double.parseDouble(parametres
					.get("Rayon d'exclusion de l'arrivée (km)")) * 1000);
			this.genPositionDepart = new GenPositionDepart(rayonVille);
			this.genTempsAttente = new GenTempsAttenteGaussien(Double.parseDouble(parametres
					.get("Temps d'attente moyen (min)")) * 60, Double.parseDouble(parametres
					.get("Ecart type du temps d'attente (min)")) * 60);

			t = new Thread(new PlaySimulation());
			t.start();
		} else if (arg0.getSource() == fenetre.getBoutonStop()) {
			stop = true;
			fenetre.changeBoutonPausePlay(play, stop);
		} else if (arg0.getSource() == fenetre.getBoutonPausePlay()) {
			play = !play;
			fenetre.changeBoutonPausePlay(play, stop);
		} else if (arg0.getSource() == fenetre.getBoutonResultat()) {
			fenetre.afficherResultat(parametres, typeSimulation, resultat);
		} else if (arg0.getSource() == fenetre.getBoutonNouvelleSimulation()) {
			this.configurer();
		} else if (arg0.getSource() == fenetre.getBoutonResimuler()) {
			t.interrupt();
			t = new Thread(new PlaySimulation());
			t.start();
		}
	}

	class PlaySimulation implements Runnable {
		public void run() {
			simuler();
		}
	}
}
