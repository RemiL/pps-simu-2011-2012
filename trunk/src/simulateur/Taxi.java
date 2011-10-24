package simulateur;

import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Taxi {

	private Point2D.Double position;
	private double vitesse;
	private LinkedList<Client> clients;

	public Taxi(Point2D.Double positionDepart, double vitesse) {
		this.position = positionDepart;
		this.vitesse = vitesse;
		this.clients = new LinkedList<Client>();
	}

}
