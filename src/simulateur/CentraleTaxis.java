package simulateur;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.ListIterator;

public class CentraleTaxis {

	private ReferentielTemps referentielTemps;

	private Taxi[] taxis;
	private LinkedList<Client> clientsEnAttente;
	private Point2D.Double position;
	private int nbClients;
	private int nbClientsPerdus;

	public CentraleTaxis(ReferentielTemps referentielTemps, int nombreTaxis, Point2D.Double position, double vitesse, int nbClientsMax) {
		this.referentielTemps = referentielTemps;

		this.position = position;
		this.clientsEnAttente = new LinkedList<Client>();
		this.taxis = new Taxi[nombreTaxis];

		for (int i = 0; i < nombreTaxis; i++) {
			taxis[i] = new Taxi(referentielTemps, this, position, vitesse, nbClientsMax);
		}

		this.nbClients = 0;
		this.nbClientsPerdus = 0;
	}

	public void affecterTaxis() {
		ListIterator<Client> it = clientsEnAttente.listIterator();
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
			if (client.estEnAttenteTaxi()) {
				// On envoi le taxi disponible le plus proche du client.
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
					// On peut supprimer le client de la file d'attente
					it.remove();
				}
			} else {
				// On a perdu un client ...
				signalerClientPerdu();
				// On supprime le client de la file d'attente
				it.remove();
			}
		}
	}

	public void deplacerTaxis() {
		for (Taxi taxi : taxis) {
			taxi.rouler();
		}
	}

	public void ajouterClient(Client client) {
		clientsEnAttente.add(client);
		nbClients++;
	}

	public void signalerClientPerdu() {
		nbClientsPerdus++;
	}

	public int getNbClients() {
		return nbClients;
	}

	public int getNbClientsPerdus() {
		return nbClientsPerdus;
	}

	public Taxi[] getTaxis() {
		return taxis;
	}

	public LinkedList<Client> getClientsNonPrisEnCharge() {
		return clientsEnAttente;
	}
}
