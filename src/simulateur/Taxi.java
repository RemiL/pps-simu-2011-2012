package simulateur;

import java.awt.geom.Point2D;
import java.util.LinkedList;

import utils.OutilsGeometriques;

public class Taxi {

	private ReferentielTemps referentielTemps;

	private CentraleTaxis centrale;
	private Point2D.Double position;
	private Point2D.Double destination;
	private double vitesse;
	private double dX, dY;
	private LinkedList<Client> clients;
	private Client clientPrioritaire;

	public Taxi(ReferentielTemps referentielTemps, CentraleTaxis centrale, Point2D.Double positionDepart, double vitesse) {
		this.centrale = centrale;
		this.position = positionDepart;
		this.vitesse = vitesse;
		this.dX = this.dY = 0.0;
		this.clients = new LinkedList<Client>();
	}

	public boolean estDisponible(Client client) {
		boolean estDispo = clients.size() < 2;

		// Si le taxi transporte déjà un client, on accepte de prendre
		// un nouveau passager uniquement si cela n'implique pas trop
		// de dérouter le taxi de son chemin actuel.
		if (estDispo && clients.size() > 0) {
			estDispo = false;

			// On vérifie que le point de départ du nouveau client soit
			// situé dans une bande semi-infinie de largueur distance
			// restante à parcourir divisée par 3 autour de la trajectoire
			// actuelle prolongée infiniement après la destination.
			if (OutilsGeometriques.distanceDroitePointCarre(position, destination, client.getDepart()) * 9 <= position
					.distanceSq(destination)
					&& OutilsGeometriques.produitScalaire(position, destination, position, client.getDepart()) >= 0) {
				// On vérifie les même conditions pour le point d'arrivée
				if (OutilsGeometriques.distanceDroitePointCarre(position, destination, client.getArrivee()) * 9 <= position
						.distanceSq(destination)
						&& OutilsGeometriques.produitScalaire(position, destination, position, client.getArrivee()) >= 0) {
					estDispo = true;
				}
			}
		}

		return estDispo;
	}

	public double getDistance(Client client) {
		return position.distance(client.getDepart());
	}

	public double getDistanceCarre(Client client) {
		return position.distanceSq(client.getDepart());
	}

	public void affecterClient(Client client) {
		// TODO : vérifier que l'opération est légale ?
		clients.add(client);

		// Si on est à vide ou si notre nouveau client est
		// plus proche que notre destination actuelle, on
		// se déroute pour aller le chercher.
		if (clients.isEmpty() || position.distanceSq(destination) > getDistanceCarre(client)) {
			allerChercher(client);
		}
	}

	private void setDestination(Point2D.Double destination) {
		// On modifie la destination de notre taxi.
		this.destination = destination;

		if (destination != null) {
			// On calcule les déplacements à effectuer
			// sur x et y à chaque déplacement.
			dX = vitesse * (destination.x - position.x) / position.distance(destination) * referentielTemps.getDt();
			dY = vitesse * (destination.y - position.y) / position.distance(destination) * referentielTemps.getDt();
		}
	}

	private void allerChercher(Client client) {
		clientPrioritaire = client;
		// On modifie la destination de notre taxi.
		setDestination(client.getDepart());
	}

	private void prendre(Client client) {
		// On s'assure que le client attend toujours
		// avant de le prendre en charge.
		if (client.estEnAttenteTaxi()) {
			client.prendreEnCharge();
		} else { // Si non alors on a perdu un client.
			centrale.signalerClientPerdu();
			clients.remove(client);
		}

		// Si on a au moins un client
		if (!clients.isEmpty()) {
			// On choisit la prochaine destination en minimisant
			// la distance à parcourir.
			double distanceCarre, distanceCarreMin = Double.MAX_VALUE;
			client = null;
			for (Client c : clients) {
				if (c.estPrisEnCharge()) {
					distanceCarre = c.getArrivee().distanceSq(position);
				} else {
					distanceCarre = c.getDepart().distanceSq(position);
				}

				if (distanceCarre < distanceCarreMin) {
					distanceCarreMin = distanceCarre;
					client = c;
				}
			}

			// Selon que le client qui minimise la distance à parcourir
			// soit déjà dans le taxi, on le conduit à destination ou
			// on va le chercher.
			if (client.estPrisEnCharge()) {
				conduire(client);
			} else {
				allerChercher(client);
			}
		} else { // Sinon le taxi reste sur place.
			attendreProchainClient();
		}
	}

	private void conduire(Client client) {
		clientPrioritaire = client;
		// On modifie la destination de notre taxi.
		setDestination(client.getArrivee());
	}

	private void deposer(Client client) {
		clients.remove(client);

		// Si on a encore un client, on s'en occupe.
		if (!clients.isEmpty()) {
			client = clients.getFirst();

			// Si le client est déjà dans le taxi, on
			// le conduit à sa nouvelle destination.
			if (client.estPrisEnCharge()) {
				conduire(client);
			} else { // Sinon on va le chercher
				allerChercher(client);
			}
		} else { // Sinon le taxi reste sur place.
			attendreProchainClient();
		}
	}

	private void gererDestinationAtteinte() {
		// Si le client prioritaire est arrivé à sa destination,
		// il descend du taxi.
		if (clientPrioritaire.getArrivee().equals(position)) {
			deposer(clientPrioritaire);
		} else {
			// Sinon on est arrivé au point de départ du client
			// et on le prend en charge.
			prendre(clientPrioritaire);
		}
	}

	private void attendreProchainClient() {
		clientPrioritaire = null;
		destination = null;
	}

	public void rouler(/* TODO : vitesse ? */) {
		if (destination != null) { // Si on a une destination
			// On vérifie si elle est atteinte lors du prochain déplacement
			if (position.distance(destination) <= vitesse * referentielTemps.getDt()) {
				// Si la destination est atteinte on s'y arrête
				position = destination;
				// et on décide de l'action à effectuer à la prochaine itération
				gererDestinationAtteinte();
			} else {
				// Si on n'est pas encore arrivé, on effectue le déplacement
				position.x += dX;
				position.y += dY;
			}
		}
	}
}
