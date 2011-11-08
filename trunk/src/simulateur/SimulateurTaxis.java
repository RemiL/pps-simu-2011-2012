package simulateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.HashMap;

import presentation.Fenetre;
import simulateur.generateurs.implementations.GenApparitionsClientsPoisson;
import simulateur.generateurs.implementations.GenPositionsArrivee;
import simulateur.generateurs.implementations.GenPositionsDepart;
import simulateur.generateurs.implementations.GenTempsAttenteGaussien;
import simulateur.generateurs.implementations.GenVitessesDistanceCentre;
import simulateur.generateurs.interfaces.GenerateurApparitionsClients;
import simulateur.generateurs.interfaces.GenerateurPositionsArrivee;
import simulateur.generateurs.interfaces.GenerateurPositionsDepart;
import simulateur.generateurs.interfaces.GenerateurTempsAttente;
import simulateur.generateurs.interfaces.GenerateurVitesses;

public class SimulateurTaxis implements ActionListener {

	private double dureeSimulation;
	private int nbEchantillons;
	private ReferentielTemps referentielTemps;
	private int nbRepetitions;
	private int nbTaxis;
	private int nbClientsMax;
	private double pourcentageClientsSatisfaitsRequis;
	private double pourcentageClientsSatisfaits;
	private double accelerationAnimation;

	private CentraleTaxis centrale;
	private Point2D.Double positionCentrale;
	private int rayonVille;
	private GenerateurApparitionsClients genApparitionClient;
	private GenerateurPositionsDepart genPositionDepart;
	private GenerateurPositionsArrivee genPositionArrivee;
	private GenerateurTempsAttente genTempsAttente;
	private GenerateurTempsAttente genTempsAttenteSuppl;
	private GenerateurVitesses genVitesse;
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
				"Position x de la centrale (km)", "Position y de la centrale (km)", "Vitesse dans le centre (km/h)",
				"Vitesse entre le centre et la périphérie (km/h)", "Vitesse en périphérie (km/h)",
				"Lambda poisson (clients/h)", "Rayon d'exclusion de l'arrivée (km)",
				"Temps d'attente moyen initial (min)", "Ecart type du temps d'attente initial (min)",
				"Temps d'attente moyen supplémentaire (min)", "Ecart type du temps d'attente supplémentaire (min)",
				"Nombre de clients max par taxi", "Pourcentage de clients satisfaits", "Nombre de répétitions",
				"Accélération de l'animation (0 pour aucune animation)" };
		int[] widths1 = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
		String[] defaults1 = { "10000", "2", "10", "0", "0", "30", "40", "50", "10", "1", "20", "10", "10", "5", "2",
				"80", "1", "100" };
		String[] labels2 = { "Nombre d'échantillons", "Durée de la simulation (h)", "Rayon de la ville (km)",
				"Position x de la centrale (km)", "Position y de la centrale (km)", "Vitesse dans le centre (km/h)",
				"Vitesse entre le centre et la périphérie (km/h)", "Vitesse en périphérie (km/h)",
				"Lambda poisson (clients/h)", "Rayon d'exclusion de l'arrivée (km)",
				"Temps d'attente moyen initial (min)", "Ecart type du temps d'attente initial (min)",
				"Temps d'attente moyen supplémentaire (min)", "Ecart type du temps d'attente supplémentaire (min)",
				"Nombre de taxis", "Nombre de clients max par taxi", "Nombre de répétitions",
				"Accélération de l'animation (0 pour aucune animation)" };
		int[] widths2 = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
		String[] defaults2 = { "10000", "2", "10", "0", "0", "30", "40", "50", "10", "1", "20", "10", "10", "5", "2",
				"2", "1", "100" };
		fenetre.initAffichageInit(labels1, defaults1, widths1, labels2, defaults2, widths2);
	}

	public void simuler() {
		pourcentageClientsSatisfaits = 0;
		stop = false;
		play = true;
		fenetre.changeBoutonPausePlay(play, stop);

		// On réalise un certain nombre de fois la simulation
		// pour avoir une moyenne
		for (int rep = 0; rep < nbRepetitions && !stop; rep++) {
			referentielTemps.reset();
			centrale = new CentraleTaxis(referentielTemps, nbTaxis, positionCentrale, genVitesse, nbClientsMax);
			fenetre.initAffichageVille(nbTaxis, rayonVille, positionCentrale);

			// On effectue la boucle n+1 fois puisque la première itération
			// sert à l'initialisation, les taxis effectueront donc bien n
			// mouvements.
			for (int i = 0; i <= nbEchantillons && !stop; i++) {
				if (play) {
					// On affecte les clients en attente aux taxis disponibles,
					centrale.affecterTaxis();
					// on calcule le déplacement des taxis
					// pendant l'intervalle dt
					centrale.deplacerTaxis();
					// puis on génère l'apparition d'éventuels nouveaux clients.
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
						// Le temps d'attente entre chaque rafraîchissement de
						// l'animation
						Thread.sleep((long) (referentielTemps.getDt() * 1000 / accelerationAnimation));
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			pourcentageClientsSatisfaits += 100.0 * (centrale.getNbClients() - centrale.getNbClientsPerdus())
					/ centrale.getNbClients();
		}
		pourcentageClientsSatisfaits /= nbRepetitions;

		fenetre.finaliserSimulation();
	}

	private void simulerApparitionClients() {
		int nbClients = genApparitionClient.genererNombreApparitionsClients();
		Point2D.Double ptDepart;
		Point2D.Double ptArrivee;
		double tempsAttenteInitial;

		for (int i = 0; i < nbClients; i++) {
			ptDepart = genPositionDepart.genererPositionDepart();
			ptArrivee = genPositionArrivee.genererPositionArrivee(ptDepart);
			tempsAttenteInitial = genTempsAttente.genererTempsAttente();

			centrale.ajouterClient(new Client(referentielTemps, ptDepart, ptArrivee, tempsAttenteInitial,
					genTempsAttenteSuppl));
		}
	}

	public void chercherNbTaxisNecessaires() {
		this.nbTaxis = 0;

		do {
			this.nbTaxis++;
			simuler();
		} while (pourcentageClientsSatisfaits < pourcentageClientsSatisfaitsRequis);
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
				this.pourcentageClientsSatisfaitsRequis = Double.parseDouble(parametres
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
			this.positionCentrale = new Point2D.Double(Double.parseDouble(parametres
					.get("Position x de la centrale (km)")) * 1000, Double.parseDouble(parametres
					.get("Position y de la centrale (km)")) * 1000);
			this.genApparitionClient = new GenApparitionsClientsPoisson(Double.parseDouble(parametres
					.get("Lambda poisson (clients/h)"))
					/ 3600 * referentielTemps.getDt());
			this.genPositionArrivee = new GenPositionsArrivee(rayonVille, Double.parseDouble(parametres
					.get("Rayon d'exclusion de l'arrivée (km)")) * 1000);
			this.genPositionDepart = new GenPositionsDepart(rayonVille);
			this.genTempsAttente = new GenTempsAttenteGaussien(Double.parseDouble(parametres
					.get("Temps d'attente moyen initial (min)")) * 60, Double.parseDouble(parametres
					.get("Ecart type du temps d'attente initial (min)")) * 60);
			this.genTempsAttenteSuppl = new GenTempsAttenteGaussien(Double.parseDouble(parametres
					.get("Temps d'attente moyen supplémentaire (min)")) * 60, Double.parseDouble(parametres
					.get("Ecart type du temps d'attente supplémentaire (min)")) * 60);
			this.genVitesse = new GenVitessesDistanceCentre(this.rayonVille, Double.parseDouble(parametres
					.get("Vitesse dans le centre (km/h)")) * 1000 / 3600, Double.parseDouble(parametres
					.get("Vitesse entre le centre et la périphérie (km/h)")) * 1000 / 3600, Double
					.parseDouble(parametres.get("Vitesse en périphérie (km/h)")) * 1000 / 3600);

			t = new Thread(new PlaySimulation());
			t.start();
		} else if (arg0.getSource() == fenetre.getBoutonStop()) {
			stop = true;
			fenetre.changeBoutonPausePlay(play, stop);
		} else if (arg0.getSource() == fenetre.getBoutonPausePlay()) {
			play = !play;
			fenetre.changeBoutonPausePlay(play, stop);
		} else if (arg0.getSource() == fenetre.getBoutonResultat()) {
			fenetre.afficherResultat(parametres, typeSimulation, pourcentageClientsSatisfaits, this.nbTaxis);
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
			if (typeSimulation == 0) {
				chercherNbTaxisNecessaires();
			} else if (typeSimulation == 1) {
				simuler();
			}
		}
	}
}
