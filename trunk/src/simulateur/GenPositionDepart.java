package simulateur;

import java.awt.geom.Point2D;

import utils.GenerateurAleatoireUniforme;

public class GenPositionDepart implements GenerateurPositionDepart {

	private double rayon;

	public GenPositionDepart(double rayon) {
		this.rayon = rayon;
	}

	public Point2D.Double genererPositionDepart() {
		Point2D.Double depart = new Point2D.Double();

		do {
			depart.x = GenerateurAleatoireUniforme.genererDouble(-rayon, rayon);
			depart.y = GenerateurAleatoireUniforme.genererDouble(-rayon, rayon);
		} while (depart.distanceSq(0, 0) > rayon * rayon);

		return depart;
	}

}
