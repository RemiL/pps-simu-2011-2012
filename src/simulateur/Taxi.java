package simulateur;

import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Taxi {

	private CentraleTaxis centrale;
	private Point2D.Double position;
	private double vitesse;
	private LinkedList<Client> clients;

	public Taxi(CentraleTaxis centrale, Point2D.Double positionDepart, double vitesse) {
		this.centrale = centrale;
		this.position = positionDepart;
		this.vitesse = vitesse;
		this.clients = new LinkedList<Client>();
	}

	public boolean estDisponible(Client client) {
		boolean estDispo = clients.size() < 2;

		if (estDispo && clients.size() > 0) {
			// TODO : Calculer si le client est acceptable ...
		}

		return estDispo;
	}

	public double getDistance(Client client) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void affecterClient(Client client) {
		// TODO : vérifier que l'opération est légale ?
		clients.add(client);
		// TODO : faut-il initier le déplacement du taxi vers le client
	}

	private void allerChercher(Client client) {
		// TODO : changer la direction du taxi pour aller vers le client
	}

	public void rouler(/* TODO : vitesse et dt ? */) {
		// TODO : faire avancer le taxi et vérifier si on a atteint notre
		// "cible"
		// cible --> client ou destination --> faire ce qu'il faut !
	}
}
