package simulateur;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

public class CentraleTaxis {

	private Taxi[] taxis;
	private LinkedList<Client> clientsEnAttente;
	private Point2D.Double position;

	public CentraleTaxis(int nombreTaxis, Point2D.Double position, double vitesse) {

		this.position = position;
		this.clientsEnAttente = new LinkedList<Client>();
		this.taxis = new Taxi[nombreTaxis];

		for (int i = 0; i < nombreTaxis; i++) {
			taxis[i]= new Taxi(position, vitesse);
		}
	}

	public void deplacerTaxis() {
		
		
	}

	public void ajouterClient(Client client) {
		clientsEnAttente.add(client);
		
	}

}
