package simulateur;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Classe représentant une centrale de taxis.
 * 
 * Une centrale de taxis gère un certain nombre fixé de taxis qu'elle dispatche
 * en fonction des appels des clients. Elle maintient une liste d'attente pour
 * les clients qui n'ont pas encore pu être affectés à un taxi et un certain
 * nombre d'informations statistiques notammment le nombre de clients qui ont
 * été perdus parce que pas servis assez rapidement.
 */
public class CentraleTaxis {
	/**
	 * Le référentiel de temps partagé par tous les parties prenantes du
	 * problème (centrale des taxis, taxis et clients).
	 */
	private ReferentielTemps referentielTemps;

	/** Tableau des taxis rattaché à la centrale */
	private Taxi[] taxis;
	/** La liste des clients ayant demandé un taxi et non affectés */
	private LinkedList<Client> clientsEnAttenteAffectationTaxi;
	/** La liste des clients affectés à un taxi mais pas encore pris en charge */
	private LinkedList<Client> clientsEnAttentePriseEnCharge;
	/**
	 * La position de la centrale de taxi dans le repère gradué en mètres ayant
	 * pour origine le centre de la ville.
	 */
	private Point2D.Double position;
	/** Le nombre de clients ayant appelé la centrale depuis son ouverture */
	private int nbClients;
	/** Le nombre de clients perdus depuis l'ouverture de la centrale */
	private int nbClientsPerdus;

	/**
	 * Construit une centrale possédant les caractéristiques fournies et dont
	 * l'état initial correspond à celui à l'ouverture.
	 * 
	 * @param referentielTemps
	 *            le référentiel de temps commun à toutes les parties prenantes
	 * @param nombreTaxis
	 *            le nombre de taxis rattachés à la centrale
	 * @param position
	 *            la position de la centrale dans le repère gradué en mètres
	 *            ayant pour origine le centre de la ville
	 * @param vitesse
	 *            la vitesse des taxis
	 * @param nbClientsMax
	 *            le nombre maximal de clients pouvant être transportés par un
	 *            taxi
	 */
	public CentraleTaxis(ReferentielTemps referentielTemps, int nombreTaxis, Point2D.Double position, double vitesse,
			int nbClientsMax) {
		this.referentielTemps = referentielTemps;

		this.position = position;
		this.clientsEnAttenteAffectationTaxi = new LinkedList<Client>();
		this.clientsEnAttentePriseEnCharge = new LinkedList<Client>();
		this.taxis = new Taxi[nombreTaxis];

		for (int i = 0; i < nombreTaxis; i++) {
			taxis[i] = new Taxi(referentielTemps, this, position, vitesse, nbClientsMax);
		}

		this.nbClients = 0;
		this.nbClientsPerdus = 0;
	}

	/**
	 * Essaie d'affecter les taxis aux clients en attente en fonction de leur
	 * disponibilité et de leur proximité avec le client : pour chaque client le
	 * taxi disponible (s'il existe) le plus proche du point de départ du client
	 * est envoyé.
	 */
	public void affecterTaxis() {
		ListIterator<Client> it = clientsEnAttenteAffectationTaxi.listIterator();
		Client client;
		double distance, distanceMin;
		Taxi meilleurTaxi;

		while (it.hasNext()) {
			client = it.next();

			// On considère que la compagnie de taxi rappelle ses clients
			// avant de leur envoyer un taxi. On effectue le test avant de
			// savoir si un taxi est disponible ce qui ne correspond pas
			// à ce qui pourrait être fait dans la réalité mais permet
			// d'optimiser le processus au niveau informatique sans modifier
			// pour autant le résultat de la simulation.
			if (client.estEnAttenteEnvoiTaxi()) {
				// On envoie le taxi disponible le plus proche du client.
				distanceMin = Double.MAX_VALUE;
				meilleurTaxi = null;

				for (Taxi taxi : taxis) {
					if (taxi.estDisponible(client)) {
						distance = taxi.getDistanceCarre(client);

						if (distance < distanceMin) {
							distanceMin = distance;
							meilleurTaxi = taxi;
						}
					}
				}

				if (meilleurTaxi != null) {
					meilleurTaxi.affecterClient(client);
					// On peut prévenir le client qu'on va lui envoyer son taxi
					client.signalerEnvoiTaxi();
					// On peut supprimer le client de la file d'attente
					it.remove();
					// et l'ajouter à la liste des clients en attente de prise
					// en charge par leur taxi
					clientsEnAttentePriseEnCharge.add(client);
				}
			} else {
				// On a perdu un client ...
				nbClientsPerdus++;
				// On supprime le client de la file d'attente
				it.remove();
			}
		}
	}

	/**
	 * Fait rouler les taxis rattachés à la centrale vers leur destination
	 * actuelle (s'ils en ont une) pendant une durée dt.
	 */
	public void deplacerTaxis() {
		for (Taxi taxi : taxis) {
			taxi.rouler();
		}
	}

	/**
	 * Ajoute un nouveau client à la liste des clients en attente d'un taxi.
	 * 
	 * @param client
	 *            le nouveau client désirant prendre un taxi
	 */
	public void ajouterClient(Client client) {
		clientsEnAttenteAffectationTaxi.add(client);
		nbClients++;
	}

	/**
	 * Signale la perte d'un client affecté à un taxi.
	 * 
	 * @param client
	 *            le client qui a été perdu
	 */
	public void signalerClientAffectePerdu(Client client) {
		// Si le client était bien dans la liste de clients en attente de
		// prise en charge, on incrémente le compteur de clients perdus.
		if (clientsEnAttentePriseEnCharge.remove(client)) {
			nbClientsPerdus++;
		}
	}

	/**
	 * Signale la prise en charge d'un client par un taxi.
	 * 
	 * @param client
	 *            le client qui a été pris en charge
	 */
	public void signalerPriseEnChargeClient(Client client) {
		// On peut supprimer le client de la liste de clients en attente de
		// prise en charge
		clientsEnAttentePriseEnCharge.remove(client);
	}

	/**
	 * Retourne le nombre de clients ayant fait appel aux services de la
	 * centrale de taxi.
	 * 
	 * @return le nombre de clients ayant fait appel aux services de la centrale
	 *         de taxi
	 */
	public int getNbClients() {
		return nbClients;
	}

	/**
	 * Retourne le nombre de clients perdus par la centrale de taxis parce
	 * qu'ils n'ont pas été pris en charge par un taxi suffisemment rapidement à
	 * leur goût.
	 * 
	 * @return le nombre de clients perdus par la centrale de taxis parce qu'ils
	 *         n'ont pas été pris en charge par un taxi suffisemment rapidement
	 *         à leur goût
	 */
	public int getNbClientsPerdus() {
		return nbClientsPerdus;
	}

	/**
	 * Retourne les taxis rattachés à la centrale de taxis.
	 * 
	 * @return le tableau des taxis rattachés à la centrale
	 */
	public Taxi[] getTaxis() {
		return taxis;
	}

	/**
	 * Retourne la liste des clients en attente d'affectation à un taxi.
	 * 
	 * @return la liste des clients en attente d'affectation à un taxi
	 */
	public LinkedList<Client> getClientsEnAttenteAffectationTaxi() {
		return clientsEnAttenteAffectationTaxi;
	}

	/**
	 * Retourne la liste des clients affectés à un taxi mais pas encore pris en
	 * charge par leur taxi.
	 * 
	 * @return la liste des clients affectés à un taxi mais pas encore pris en
	 *         charge par leur taxi
	 */
	public LinkedList<Client> getClientsEnAttentePriseEnCharge() {
		return clientsEnAttentePriseEnCharge;
	}
}
