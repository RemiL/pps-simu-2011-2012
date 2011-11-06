package simulateur;

import java.awt.geom.Point2D;
import java.util.LinkedList;

import utils.OutilsGeometriques;

/**
 * Classe repr�sentant un taxi rattach� � une centrale de taxis.
 * 
 * Un taxi est caract�ris� par sa position actuelle et par sa destination ainsi
 * que par sa vitesse. Un taxi peut transporter � un m�me instant jusqu'� deux
 * clients mais il d�cide de se d�router pour aller chercher un deuxi�me client
 * que dans les cas o� cela a un sens, c'est-�-dire dans les cas o� �a ne
 * rallonge pas outre mesure le trajet du premier client. Dans notre
 * impl�mentation, on a fait le choix de n'aller chercher un deuxi�me client que
 * si son point de d�part et sa destination sont compris dans une bande
 * semi-infinie de largeur �gale � un tiers de la distance restant � parcourir
 * dans la course courante. Dans le cas o� deux clients sont affect�s au m�me
 * taxi, le taxi va toujours � la destination la plus proche que �a soit le
 * point de d�part ou d'arriv�e d'un de ses clients.
 */
public class Taxi {
	/**
	 * Le r�f�rentiel de temps partag� par tous les parties prenantes du
	 * probl�me (centrale des taxis, taxis et clients).
	 */
	private ReferentielTemps referentielTemps;

	/** La centrale de taxis � laquelle le taxi est rattach�. */
	private CentraleTaxis centrale;
	/**
	 * La position actuelle du taxi dans le rep�re gradu� en m�tres ayant pour
	 * origine le centre de la ville.
	 */
	private Point2D.Double position;
	/**
	 * La destination actuelle du taxi dans le rep�re gradu� en m�tres ayant
	 * pour origine le centre de la ville.
	 */
	private Point2D.Double destination;
	/** La vitesse du taxi en m/s */
	private double vitesse;
	/** Les d�placements du taxi lors du prochain incr�ment de temps */
	private double dX, dY;
	/** La liste des clients affect�s au taxi */
	private LinkedList<Client> clients;
	/** Le client concern� par la destination actuelle */
	private Client clientPrioritaire;

	/**
	 * Construit un nouveau taxi avec les caract�ristiques fournies.
	 * 
	 * @param referentielTemps
	 *            le r�f�rentiel de temps commun � toutes les parties prenantes
	 * @param centrale
	 *            la centrale de taxis � laquelle est rattach� le taxi
	 * @param positionDepart
	 *            la position de d�part du taxi dans le r�p�re gradu� en m�tres
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
	 * Si le taxi est vide, il est toujours consid�r� comme disponible pour tout
	 * client. Au contraire, s'il est d�j� affect� � deux clients, il est
	 * toujours consid�r� comme non disponible quelque soit le client. Dans le
	 * cas o� le taxi est affect� � un client, il est consid�r� comme disponible
	 * pour un deuxi�me client uniquement si les points de d�part et d'arriv�e
	 * du nouveau client sont compris dans une bande semie-infinie autour de la
	 * trajectoire de la course courante, de largueur �gale � un tiers de la
	 * distance restant � parcourir pour la course actualle.
	 * 
	 * @param client
	 *            le client pour lequel on souhaite v�rifier la disponibilit� du
	 *            taxi
	 * @return vrai si et seulement si le taxi est consid�r� comme disponible
	 *         selon les crit�res d�finis ci-dessus pour le client consid�r� et
	 *         faux sinon
	 */
	public boolean estDisponible(Client client) {
		boolean estDispo = clients.size() < 2;

		// Si le taxi transporte d�j� un client, on accepte de prendre
		// un nouveau passager uniquement si cela n'implique pas trop
		// de d�router le taxi de son chemin actuel.
		if (estDispo && clients.size() > 0) {
			estDispo = false;

			// On v�rifie que le point de d�part du nouveau client soit situ�
			// dans une bande semi-infinie de largueur �gale � un tiers de la
			// distance restante � parcourir autour de la trajectoire actuelle
			// prolong�e infiniement apr�s la destination.
			if (OutilsGeometriques.distanceDroitePointCarre(position, destination, client.getDepart()) * 9 <= position
					.distanceSq(destination)
					&& OutilsGeometriques.produitScalaire(position, destination, position, client.getDepart()) >= 0) {
				// On v�rifie les m�me conditions pour le point d'arriv�e
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
	 * Calcule la distance en m�tre entre la position actuelle du taxi et le
	 * point de d�part du client consid�r�.
	 * 
	 * @param client
	 *            le client dont on veut connaitre la distance au taxi
	 * @return la distance en m�tre entre la position actuelle du taxi et le
	 *         point de d�part du client
	 */
	public double getDistance(Client client) {
		return position.distance(client.getDepart());
	}

	/**
	 * Calcule le carr� de la distance (exprim�e en m�tre) entre la position
	 * actuelle du taxi et le point de d�part du client consid�r�.
	 * 
	 * @param client
	 *            le client dont on veut connaitre la distance au taxi
	 * @return le carr� de la distance entre la position actuelle du taxi et le
	 *         point de d�part du client
	 */
	public double getDistanceCarre(Client client) {
		return position.distanceSq(client.getDepart());
	}

	/**
	 * Affecte un client au taxi.
	 * 
	 * Selon la position du point de d�part du client, le taxi peut d�cider
	 * d'aller chercher le client imm�diatement ou de finir sa course actuelle
	 * s'il avait d�j� un client qui lui �tait affect� et si la destination
	 * actuelle du taxi (qui peut �tre le point de d�part ou la destination de
	 * son premier client) est plus proche que le point de d�part du nouveau
	 * client.
	 * 
	 * On notera qu'il n'est fait aucune v�rification sur la l�gitimit�
	 * d'affecter le client au taxi, il est de la responsabilit� de
	 * l'utilisateur d'utiliser la m�thode estDisponible(Client client)
	 * pr�alablement pour s'assurer de ce fait.
	 * 
	 * @param client
	 *            le client � affecter au taxi
	 */
	public void affecterClient(Client client) {
		clients.add(client);

		// Si on a un seul client ou si notre nouveau client
		// est plus proche que notre destination actuelle,
		// on se d�route pour aller le chercher.
		if (clients.size() == 1 || position.distanceSq(destination) > getDistanceCarre(client)) {
			allerChercher(client);
		}
	}

	/**
	 * Assigne une nouvelle destination au taxi et recalcule en cons�quence les
	 * mouvements � appliquer � chaque incr�ment de temps.
	 * 
	 * @param destination
	 *            la nouvelle destination du taxi
	 */
	private void setDestination(Point2D.Double destination) {
		// On modifie la destination de notre taxi.
		this.destination = destination;

		if (destination != null) {
			// On calcule les d�placements � effectuer
			// sur x et y � chaque d�placement.
			dX = vitesse * (destination.x - position.x) / position.distance(destination) * referentielTemps.getDt();
			dY = vitesse * (destination.y - position.y) / position.distance(destination) * referentielTemps.getDt();
		}
	}

	/**
	 * Demande au taxi d'aller chercher un client (qui devrait lui avoir �t�
	 * affect� pr�c�dement), le client "prioritaire" devant alors le client
	 * consid�r� et la destination du taxi devient le point de d�part de ce
	 * client.
	 * 
	 * @param client
	 *            le client � aller chercher
	 */
	private void allerChercher(Client client) {
		clientPrioritaire = client;
		// On modifie la destination de notre taxi.
		setDestination(client.getDepart());
	}

	/**
	 * Prend en charge le client consid�r� (ce qui correspond � la mont�e du
	 * client dans le taxi) et d�cide de la prochaine destination du taxi en
	 * fonction des clients affect�s au taxi et de leurs points de d�part ou
	 * d'arriv�e (le taxi se rend toujours au point le plus proche quand il a
	 * plusieurs choix).
	 * 
	 * On notera que le client � prendre en charge peut avoir abandonn� l'id�e
	 * de prendre un taxi si le taxi a mis trop de temps pour venir le prendre
	 * en charge.
	 * 
	 * Cette m�thode ne v�rifie pas la l�gitimit� d'effectuer cette action (en
	 * particulier l'appelant doit s'assurer que le taxi a atteint le point de
	 * d�part du client consid�r�).
	 * 
	 * @param client
	 *            le client � prendre en charge
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
			// la distance � parcourir.
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

			// Selon que le client qui minimise la distance � parcourir
			// soit d�j� dans le taxi, on le conduit � destination ou
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
	 * Demande au taxi de conduire un client (qui devrait avoir �t� pris en
	 * charge pr�c�dement par le taxi) � sa destination, le client "prioritaire"
	 * devant alors le client consid�r� et la destination du taxi devient le
	 * point d'arriv�e de ce client.
	 * 
	 * @param client
	 *            le client � conduire � sa destination
	 */
	private void conduire(Client client) {
		clientPrioritaire = client;
		// On modifie la destination de notre taxi.
		setDestination(client.getArrivee());
	}

	/**
	 * D�pose un client (qui devrait avoir �t� conduit jusqu'� sa destination
	 * par le taxi) et d�cide de la prochaine destination si un autre client est
	 * d�j� affect� au taxi (point de d�part ou d'arriv�e selon que le client en
	 * question ait d�j� �t� pris en charge ou non). Si le taxi n'a aucun
	 * client, il attend sur place.
	 * 
	 * @param client
	 */
	private void deposer(Client client) {
		clients.remove(client);

		// Si on a encore un client, on s'en occupe.
		if (!clients.isEmpty()) {
			client = clients.getFirst();

			// Si le client est d�j� dans le taxi, on
			// le conduit � sa nouvelle destination.
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
	 * Permet de g�rer les cas o� le taxi a atteint sa destination : selon les
	 * cas le client qui �tait consid�r� comme prioritaire par le taxi est soit
	 * pris en charge soit d�pos� par le taxi.
	 */
	private void gererDestinationAtteinte() {
		// Si le client prioritaire est arriv� � sa destination,
		// il descend du taxi.
		if (clientPrioritaire.getArrivee().equals(position)) {
			deposer(clientPrioritaire);
		} else {
			// Sinon on est arriv� au point de d�part du client
			// et on le prend en charge.
			prendreEnCharge(clientPrioritaire);
		}
	}

	/**
	 * Demande au taxi d'attendre le prochain client en restant � sa position
	 * actuelle.
	 */
	private void attendreProchainClient() {
		clientPrioritaire = null;
		destination = null;
	}

	/**
	 * Fait rouler le taxi vers sa destination actuelle (s'il en a une) pendant
	 * une dur�e dt.
	 */
	public void rouler(/* TODO : vitesse ? */) {
		if (destination != null) { // Si on a une destination
			// On v�rifie si elle est atteinte lors du prochain d�placement
			if (position.distance(destination) <= vitesse * referentielTemps.getDt()) {
				// Si la destination est atteinte on s'y arr�te
				position = destination;
				// et on d�cide de l'action � effectuer � la prochaine it�ration
				gererDestinationAtteinte();
			} else {
				// Si on n'est pas encore arriv�, on effectue le d�placement
				position.x += dX;
				position.y += dY;
			}
		}
	}

	/**
	 * Retourne la position actuelle du taxi dans le rep�re gradu� en m�tres
	 * ayant pour origine le centre de la ville.
	 * 
	 * @return la position actuelle du taxi dans le rep�re gradu� en m�tres
	 *         ayant pour origine le centre de la ville.
	 */
	public Point2D getPosition() {
		return position;
	}

	/**
	 * Retourne le nombre de clients actuellement affect�s au taxi. Ces clients
	 * ne sont pas forc�ment d�j� dans le taxi.
	 * 
	 * @return le nombre de clients actuellement affect�s au taxi
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
