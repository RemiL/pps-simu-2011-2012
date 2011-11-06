package simulateur;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Classe repr�sentant une centrale de taxis.
 * 
 * Une centrale de taxis g�re un certain nombre fix� de taxis qu'elle dispatche
 * en fonction des appels des clients. Elle maintient une liste d'attente pour
 * les clients qui n'ont pas encore pu �tre affect�s � un taxi et un certain
 * nombre d'informations statistiques notammment le nombre de clients qui ont
 * �t� perdus parce que pas servis assez rapidement.
 */
public class CentraleTaxis {
	/**
	 * Le r�f�rentiel de temps partag� par tous les parties prenantes du
	 * probl�me (centrale des taxis, taxis et clients).
	 */
	private ReferentielTemps referentielTemps;

	/** Tableau des taxis rattach� � la centrale */
	private Taxi[] taxis;
	/** La liste des clients ayant demand� un taxi et non affect�s */
	private LinkedList<Client> clientsEnAttenteAffectationTaxi;
	/** La liste des clients affect�s � un taxi mais pas encore pris en charge */
	private LinkedList<Client> clientsEnAttentePriseEnCharge;
	/**
	 * La position de la centrale de taxi dans le rep�re gradu� en m�tres ayant
	 * pour origine le centre de la ville.
	 */
	private Point2D.Double position;
	/** Le nombre de clients ayant appel� la centrale depuis son ouverture */
	private int nbClients;
	/** Le nombre de clients perdus depuis l'ouverture de la centrale */
	private int nbClientsPerdus;

	/**
	 * Construit une centrale poss�dant les caract�ristiques fournies et dont
	 * l'�tat initial correspond � celui � l'ouverture.
	 * 
	 * @param referentielTemps
	 *            le r�f�rentiel de temps commun � toutes les parties prenantes
	 * @param nombreTaxis
	 *            le nombre de taxis rattach�s � la centrale
	 * @param position
	 *            la position de la centrale dans le rep�re gradu� en m�tres
	 *            ayant pour origine le centre de la ville
	 * @param vitesse
	 *            la vitesse des taxis
	 * @param nbClientsMax
	 *            le nombre maximal de clients pouvant �tre transport�s par un
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
	 * disponibilit� et de leur proximit� avec le client : pour chaque client le
	 * taxi disponible (s'il existe) le plus proche du point de d�part du client
	 * est envoy�.
	 */
	public void affecterTaxis() {
		ListIterator<Client> it = clientsEnAttenteAffectationTaxi.listIterator();
		Client client;
		double distance, distanceMin;
		Taxi meilleurTaxi;

		while (it.hasNext()) {
			client = it.next();

			// On consid�re que la compagnie de taxi rappelle ses clients
			// avant de leur envoyer un taxi. On effectue le test avant de
			// savoir si un taxi est disponible ce qui ne correspond pas
			// � ce qui pourrait �tre fait dans la r�alit� mais permet
			// d'optimiser le processus au niveau informatique sans modifier
			// pour autant le r�sultat de la simulation.
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
					// On peut pr�venir le client qu'on va lui envoyer son taxi
					client.signalerEnvoiTaxi();
					// On peut supprimer le client de la file d'attente
					it.remove();
					// et l'ajouter � la liste des clients en attente de prise
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
	 * Fait rouler les taxis rattach�s � la centrale vers leur destination
	 * actuelle (s'ils en ont une) pendant une dur�e dt.
	 */
	public void deplacerTaxis() {
		for (Taxi taxi : taxis) {
			taxi.rouler();
		}
	}

	/**
	 * Ajoute un nouveau client � la liste des clients en attente d'un taxi.
	 * 
	 * @param client
	 *            le nouveau client d�sirant prendre un taxi
	 */
	public void ajouterClient(Client client) {
		clientsEnAttenteAffectationTaxi.add(client);
		nbClients++;
	}

	/**
	 * Signale la perte d'un client affect� � un taxi.
	 * 
	 * @param client
	 *            le client qui a �t� perdu
	 */
	public void signalerClientAffectePerdu(Client client) {
		// Si le client �tait bien dans la liste de clients en attente de
		// prise en charge, on incr�mente le compteur de clients perdus.
		if (clientsEnAttentePriseEnCharge.remove(client)) {
			nbClientsPerdus++;
		}
	}

	/**
	 * Signale la prise en charge d'un client par un taxi.
	 * 
	 * @param client
	 *            le client qui a �t� pris en charge
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
	 * qu'ils n'ont pas �t� pris en charge par un taxi suffisemment rapidement �
	 * leur go�t.
	 * 
	 * @return le nombre de clients perdus par la centrale de taxis parce qu'ils
	 *         n'ont pas �t� pris en charge par un taxi suffisemment rapidement
	 *         � leur go�t
	 */
	public int getNbClientsPerdus() {
		return nbClientsPerdus;
	}

	/**
	 * Retourne les taxis rattach�s � la centrale de taxis.
	 * 
	 * @return le tableau des taxis rattach�s � la centrale
	 */
	public Taxi[] getTaxis() {
		return taxis;
	}

	/**
	 * Retourne la liste des clients en attente d'affectation � un taxi.
	 * 
	 * @return la liste des clients en attente d'affectation � un taxi
	 */
	public LinkedList<Client> getClientsEnAttenteAffectationTaxi() {
		return clientsEnAttenteAffectationTaxi;
	}

	/**
	 * Retourne la liste des clients affect�s � un taxi mais pas encore pris en
	 * charge par leur taxi.
	 * 
	 * @return la liste des clients affect�s � un taxi mais pas encore pris en
	 *         charge par leur taxi
	 */
	public LinkedList<Client> getClientsEnAttentePriseEnCharge() {
		return clientsEnAttentePriseEnCharge;
	}
}
