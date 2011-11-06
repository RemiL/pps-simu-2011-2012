package simulateur;

import java.awt.geom.Point2D;
import java.util.LinkedList;

import utils.OutilsGeometriques;

/**
 * Classe représentant un taxi rattaché à une centrale de taxis.
 * 
 * Un taxi est caractérisé par sa position actuelle et par sa destination ainsi
 * que par sa vitesse. Un taxi peut transporter à un même instant jusqu'à deux
 * clients mais il décide de se dérouter pour aller chercher un deuxième client
 * que dans les cas où cela a un sens, c'est-à-dire dans les cas où ça ne
 * rallonge pas outre mesure le trajet du premier client. Dans notre
 * implémentation, on a fait le choix de n'aller chercher un deuxième client que
 * si son point de départ et sa destination sont compris dans une bande
 * semi-infinie de largeur égale à un tiers de la distance restant à parcourir
 * dans la course courante. Dans le cas où deux clients sont affectés au même
 * taxi, le taxi va toujours à la destination la plus proche que ça soit le
 * point de départ ou d'arrivée d'un de ses clients.
 */
public class Taxi {
	/**
	 * Le référentiel de temps partagé par tous les parties prenantes du
	 * problème (centrale des taxis, taxis et clients).
	 */
	private ReferentielTemps referentielTemps;

	/** La centrale de taxis à laquelle le taxi est rattaché. */
	private CentraleTaxis centrale;
	/**
	 * La position actuelle du taxi dans le repère gradué en mètres ayant pour
	 * origine le centre de la ville.
	 */
	private Point2D.Double position;
	/**
	 * La destination actuelle du taxi dans le repère gradué en mètres ayant
	 * pour origine le centre de la ville.
	 */
	private Point2D.Double destination;
	/** La vitesse du taxi en m/s */
	private double vitesse;
	/** Les déplacements du taxi lors du prochain incrément de temps */
	private double dX, dY;
	/** La liste des clients affectés au taxi */
	private LinkedList<Client> clients;
	/** Le client concerné par la destination actuelle */
	private Client clientPrioritaire;

	/**
	 * Construit un nouveau taxi avec les caractéristiques fournies.
	 * 
	 * @param referentielTemps
	 *            le référentiel de temps commun à toutes les parties prenantes
	 * @param centrale
	 *            la centrale de taxis à laquelle est rattaché le taxi
	 * @param positionDepart
	 *            la position de départ du taxi dans le répère gradué en mètres
	 *            ayant pour origine le centre de la ville
	 * @param vitesse
	 *            la vitesse du taxi
	 */
	public Taxi(ReferentielTemps referentielTemps, CentraleTaxis centrale, Point2D.Double positionDepart, double vitesse) {
		this.referentielTemps = referentielTemps;

		this.centrale = centrale;
		this.position = new Point2D.Double();
		this.position.setLocation(positionDepart);
		this.vitesse = vitesse;
		this.dX = this.dY = 0.0;
		this.clients = new LinkedList<Client>();
	}

	/**
	 * Teste si un taxi est disponible pour un client.
	 * 
	 * Si le taxi est vide, il est toujours considéré comme disponible pour tout
	 * client. Au contraire, s'il est déjà affecté à deux clients, il est
	 * toujours considéré comme non disponible quelque soit le client. Dans le
	 * cas où le taxi est affecté à un client, il est considéré comme disponible
	 * pour un deuxième client uniquement si les points de départ et d'arrivée
	 * du nouveau client sont compris dans une bande semie-infinie autour de la
	 * trajectoire de la course courante, de largueur égale à un tiers de la
	 * distance restant à parcourir pour la course actualle.
	 * 
	 * @param client
	 *            le client pour lequel on souhaite vérifier la disponibilité du
	 *            taxi
	 * @return vrai si et seulement si le taxi est considéré comme disponible
	 *         selon les critères définis ci-dessus pour le client considéré et
	 *         faux sinon
	 */
	public boolean estDisponible(Client client) {
		boolean estDispo = clients.size() < 2;

		// Si le taxi transporte déjà un client, on accepte de prendre
		// un nouveau passager uniquement si cela n'implique pas trop
		// de dérouter le taxi de son chemin actuel.
		if (estDispo && clients.size() > 0) {
			estDispo = false;

			// On vérifie que le point de départ du nouveau client soit situé
			// dans une bande semi-infinie de largueur égale à un tiers de la
			// distance restante à parcourir autour de la trajectoire actuelle
			// prolongée infiniement après la destination.
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

	/**
	 * Calcule la distance en mètre entre la position actuelle du taxi et le
	 * point de départ du client considéré.
	 * 
	 * @param client
	 *            le client dont on veut connaitre la distance au taxi
	 * @return la distance en mètre entre la position actuelle du taxi et le
	 *         point de départ du client
	 */
	public double getDistance(Client client) {
		return position.distance(client.getDepart());
	}

	/**
	 * Calcule le carré de la distance (exprimée en mètre) entre la position
	 * actuelle du taxi et le point de départ du client considéré.
	 * 
	 * @param client
	 *            le client dont on veut connaitre la distance au taxi
	 * @return le carré de la distance entre la position actuelle du taxi et le
	 *         point de départ du client
	 */
	public double getDistanceCarre(Client client) {
		return position.distanceSq(client.getDepart());
	}

	/**
	 * Affecte un client au taxi.
	 * 
	 * Selon la position du point de départ du client, le taxi peut décider
	 * d'aller chercher le client immédiatement ou de finir sa course actuelle
	 * s'il avait déjà un client qui lui était affecté et si la destination
	 * actuelle du taxi (qui peut être le point de départ ou la destination de
	 * son premier client) est plus proche que le point de départ du nouveau
	 * client.
	 * 
	 * On notera qu'il n'est fait aucune vérification sur la légitimité
	 * d'affecter le client au taxi, il est de la responsabilité de
	 * l'utilisateur d'utiliser la méthode estDisponible(Client client)
	 * préalablement pour s'assurer de ce fait.
	 * 
	 * @param client
	 *            le client à affecter au taxi
	 */
	public void affecterClient(Client client) {
		clients.add(client);

		// Si on a un seul client ou si notre nouveau client
		// est plus proche que notre destination actuelle,
		// on se déroute pour aller le chercher.
		if (clients.size() == 1 || position.distanceSq(destination) > getDistanceCarre(client)) {
			allerChercher(client);
		}
	}

	/**
	 * Assigne une nouvelle destination au taxi et recalcule en conséquence les
	 * mouvements à appliquer à chaque incrément de temps.
	 * 
	 * @param destination
	 *            la nouvelle destination du taxi
	 */
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

	/**
	 * Demande au taxi d'aller chercher un client (qui devrait lui avoir été
	 * affecté précédement), le client "prioritaire" devant alors le client
	 * considéré et la destination du taxi devient le point de départ de ce
	 * client.
	 * 
	 * @param client
	 *            le client à aller chercher
	 */
	private void allerChercher(Client client) {
		clientPrioritaire = client;
		// On modifie la destination de notre taxi.
		setDestination(client.getDepart());
	}

	/**
	 * Prend en charge le client considéré (ce qui correspond à la montée du
	 * client dans le taxi) et décide de la prochaine destination du taxi en
	 * fonction des clients affectés au taxi et de leurs points de départ ou
	 * d'arrivée (le taxi se rend toujours au point le plus proche quand il a
	 * plusieurs choix).
	 * 
	 * On notera que le client à prendre en charge peut avoir abandonné l'idée
	 * de prendre un taxi si le taxi a mis trop de temps pour venir le prendre
	 * en charge.
	 * 
	 * Cette méthode ne vérifie pas la légitimité d'effectuer cette action (en
	 * particulier l'appelant doit s'assurer que le taxi a atteint le point de
	 * départ du client considéré).
	 * 
	 * @param client
	 *            le client à prendre en charge
	 */
	private void prendreEnCharge(Client client) {
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

	/**
	 * Demande au taxi de conduire un client (qui devrait avoir été pris en
	 * charge précédement par le taxi) à sa destination, le client "prioritaire"
	 * devant alors le client considéré et la destination du taxi devient le
	 * point d'arrivée de ce client.
	 * 
	 * @param client
	 *            le client à conduire à sa destination
	 */
	private void conduire(Client client) {
		clientPrioritaire = client;
		// On modifie la destination de notre taxi.
		setDestination(client.getArrivee());
	}

	/**
	 * Dépose un client (qui devrait avoir été conduit jusqu'à sa destination
	 * par le taxi) et décide de la prochaine destination si un autre client est
	 * déjà affecté au taxi (point de départ ou d'arrivée selon que le client en
	 * question ait déjà été pris en charge ou non). Si le taxi n'a aucun
	 * client, il attend sur place.
	 * 
	 * @param client
	 */
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

	/**
	 * Permet de gérer les cas où le taxi a atteint sa destination : selon les
	 * cas le client qui était considéré comme prioritaire par le taxi est soit
	 * pris en charge soit déposé par le taxi.
	 */
	private void gererDestinationAtteinte() {
		// Si le client prioritaire est arrivé à sa destination,
		// il descend du taxi.
		if (clientPrioritaire.getArrivee().equals(position)) {
			deposer(clientPrioritaire);
		} else {
			// Sinon on est arrivé au point de départ du client
			// et on le prend en charge.
			prendreEnCharge(clientPrioritaire);
		}
	}

	/**
	 * Demande au taxi d'attendre le prochain client en restant à sa position
	 * actuelle.
	 */
	private void attendreProchainClient() {
		clientPrioritaire = null;
		destination = null;
	}

	/**
	 * Fait rouler le taxi vers sa destination actuelle (s'il en a une) pendant
	 * une durée dt.
	 */
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

	/**
	 * Retourne la position actuelle du taxi dans le repère gradué en mètres
	 * ayant pour origine le centre de la ville.
	 * 
	 * @return la position actuelle du taxi dans le repère gradué en mètres
	 *         ayant pour origine le centre de la ville.
	 */
	public Point2D getPosition() {
		return position;
	}

	/**
	 * Retourne le nombre de clients actuellement affectés au taxi. Ces clients
	 * ne sont pas forcément déjà dans le taxi.
	 * 
	 * @return le nombre de clients actuellement affectés au taxi
	 */
	public int getNbClientsAffectes() {
		return clients.size();
	}

	/**
	 * Retourne le nombre de clients actuellement dans le taxi.
	 * 
	 * @return le nombre de clients actuellement dans le taxi
	 */
	public int getNbClientsDansTaxi() {
		int res = 0;
		for (Client c : clients) {
			if (c.estPrisEnCharge())
				res++;
		}
		return res;
	}
}
