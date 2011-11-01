package simulateur;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.ListIterator;

public class CentraleTaxis {

	private Taxi[] taxis;
	private LinkedList<Client> clientsEnAttente;
	private Point2D.Double position;
	private double dt;
	private int nbClients;
	private int nbClientsPerdus;

	public CentraleTaxis(int nombreTaxis, Point2D.Double position, double vitesse, double dt) {
		this.position = position;
		this.clientsEnAttente = new LinkedList<Client>();
		this.taxis = new Taxi[nombreTaxis];

		for (int i = 0; i < nombreTaxis; i++) {
			taxis[i] = new Taxi(this, position, vitesse);
		}

		this.dt = dt;
		this.nbClients = 0;
		this.nbClientsPerdus = 0;
	}

	public void affecterTaxis() {
		ListIterator<Client> it = clientsEnAttente.listIterator();
		Client client;
		double distance, distanceMin = Double.MAX_VALUE;
		Taxi meilleurTaxi = null;

		while (it.hasNext()) {
			client = it.next();

			// On considère que la compagnie de taxi rappelle ses clients
			// avant de leur envoyer un taxi. On effectue le test avant de
			// savoir si un taxi est disponible ce qui ne correspond pas
			// à ce qui pourrait être fait dans la réalité mais permet
			// d'optimiser le processus au niveau informatique sans modifier
			// pour autant le résultat de la simulation.
			if (client.estEnAttenteTaxi()) {
				for (Taxi taxi : taxis) {
					if (taxi.estDisponible(client)) {
						distance = taxi.getDistance(client);

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
}
