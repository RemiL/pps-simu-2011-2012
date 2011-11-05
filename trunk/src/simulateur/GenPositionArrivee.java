package simulateur;

import java.awt.geom.Point2D;

import utils.GenerateurAleatoireUniforme;

public class GenPositionArrivee implements GenerateurPositionArrivee {

	private double rayon;
	private double distanceMin;

	public GenPositionArrivee(double rayon, double distanceMin) {
		this.rayon = rayon;
		this.distanceMin = distanceMin;
	}

	public Point2D.Double genererPositionArrivee(Point2D.Double pointDepart) {
		Point2D.Double arrivee = new Point2D.Double();

		do {
			arrivee.x = GenerateurAleatoireUniforme.genererDouble(-rayon, rayon);
			arrivee.y = GenerateurAleatoireUniforme.genererDouble(-rayon, rayon);
		} while ((arrivee.distanceSq(0, 0) > rayon * rayon)
				|| (arrivee.distanceSq(pointDepart) < distanceMin * distanceMin));

		return arrivee;
	}

}
