package simulateur;

import java.awt.geom.Point2D;

public class Client {

	private Point2D.Double depart, arrivee;
	private double horaireAbandon;

	public enum Etat {
		ATTENTE_TAXI, PRIS_EN_CHARGE, ABANDON
	};

	private Etat etat;

	public Client(Point2D.Double depart, Point2D.Double arrivee, double tempsAttente) {
		this.depart = depart;
		this.arrivee = arrivee;
		this.horaireAbandon = Horloge.getTemps() + tempsAttente;
		this.etat = Etat.ATTENTE_TAXI;
	}

	public void majEtat() {
		if (estEnAttenteTaxi() && horaireAbandon < Horloge.getTemps()) {
			etat = Etat.ABANDON;
		}
	}

	public boolean estEnAttenteTaxi() {
		majEtat();

		return etat == Etat.ATTENTE_TAXI;
	}

	public void prendreEnCharge() {
		etat = Etat.PRIS_EN_CHARGE;
	}
}
