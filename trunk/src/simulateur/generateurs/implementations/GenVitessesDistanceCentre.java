package simulateur.generateurs.implementations;

import java.awt.geom.Point2D;

import simulateur.generateurs.interfaces.GenerateurVitesses;

/**
 * Classe définissant un générateur de vitesse pour les taxis basé sur la
 * distance du taxi au centre de la ville.
 */
public class GenVitessesDistanceCentre implements GenerateurVitesses {
	/** Les bornes au carré des différentes zones de la ville */
	private double[] bornesCarre;
	/** Les vitesses (en m/s) dans les différentes zones de la ville */
	private double[] vitesses;

	/**
	 * Construit un nouveau générateur de vitesse pour les taxis tel que la
	 * ville de rayon R soit découpée en trois zones concentriques de rayon égal
	 * et que la vitesse dans chacune des trois zones ainsi définies soit celle
	 * fournie.
	 * 
	 * @param R
	 *            le rayon de la ville (en m)
	 * @param vitesseZone1
	 *            la vitesse (en m/s) d'un taxi dans la zone allant de 0 à R/3
	 * @param vitesseZone2
	 *            la vitesse (en m/s) d'un taxi dans la zone allant de R/3 à
	 *            2R/3
	 * @param vitesseZone3
	 *            la vitesse (en m/s) d'un taxi dans la zone allant de 2R/3 à R
	 */
	public GenVitessesDistanceCentre(double R, double vitesseZone1, double vitesseZone2, double vitesseZone3) {
		// On stocke le carré des bornes pour éviter d'avoir à calculer une
		// racine carré plus tard
		this.bornesCarre = new double[3];
		this.bornesCarre[0] = R * R / 9;
		this.bornesCarre[1] = 4 * R * R / 9;
		this.bornesCarre[2] = R * R;

		this.vitesses = new double[3];
		this.vitesses[0] = vitesseZone1;
		this.vitesses[1] = vitesseZone2;
		this.vitesses[2] = vitesseZone3;
	}

	/**
	 * Génère une vitesse (exprimée en m/s) pour un taxi. Cette vitesse dépend
	 * de la position du taxi dans la ville qui est découpée à cet effet en
	 * trois parties concentriques de rayon égal : à chaque portion de 0 à R/3,
	 * de R/3 à 2R/3 puis de 2R/3 à R correspond une vitesse.
	 * 
	 * @param position
	 *            la position du taxi dans le repère gradué en mètres ayant pour
	 *            origine le centre de la ville
	 * @return une vitesse (exprimée en m/s) pour un taxi dans une certaine zone
	 */
	public double genererVitesse(Point2D.Double position) {
		int zone;
		double distanceCarre = position.distanceSq(0, 0);

		for (zone = 0; zone < bornesCarre.length - 1; zone++) {
			if (distanceCarre <= bornesCarre[zone]) {
				break;
			}
		}

		return vitesses[zone];
	}
}
