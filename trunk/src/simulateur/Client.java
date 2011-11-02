package simulateur;

import java.awt.geom.Point2D;

public class Client {

	private ReferentielTemps referentielTemps;

	private Point2D.Double depart, arrivee;
	private double horaireAbandon;

	public enum Etat {
		ATTENTE_TAXI, PRIS_EN_CHARGE, ABANDON
	};

	private Etat etat;

	public Client(ReferentielTemps referentielTemps, Point2D.Double depart, Point2D.Double arrivee, double tempsAttente) {
		this.referentielTemps = referentielTemps;

		this.depart = depart;
		this.arrivee = arrivee;
		this.horaireAbandon = referentielTemps.getTemps() + tempsAttente;
		this.etat = Etat.ATTENTE_TAXI;
	}

	public void majEtat() {
		if (estEnAttenteTaxi() && horaireAbandon < referentielTemps.getTemps()) {
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

	public Point2D.Double getDepart() {
		return depart;
	}

	public Point2D.Double getArrivee() {
		return arrivee;
	}
}
