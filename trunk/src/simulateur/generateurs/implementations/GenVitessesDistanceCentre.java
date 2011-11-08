package simulateur.generateurs.implementations;

import java.awt.geom.Point2D;

import simulateur.generateurs.interfaces.GenerateurVitesses;

/**
 * Classe d�finissant un g�n�rateur de vitesse pour les taxis bas� sur la
 * distance du taxi au centre de la ville.
 */
public class GenVitessesDistanceCentre implements GenerateurVitesses {
	/** Les bornes au carr� des diff�rentes zones de la ville */
	private double[] bornesCarre;
	/** Les vitesses (en m/s) dans les diff�rentes zones de la ville */
	private double[] vitesses;

	/**
	 * Construit un nouveau g�n�rateur de vitesse pour les taxis tel que la
	 * ville de rayon R soit d�coup�e en trois zones concentriques de rayon �gal
	 * et que la vitesse dans chacune des trois zones ainsi d�finies soit celle
	 * fournie.
	 * 
	 * @param R
	 *            le rayon de la ville (en m)
	 * @param vitesseZone1
	 *            la vitesse (en m/s) d'un taxi dans la zone allant de 0 � R/3
	 * @param vitesseZone2
	 *            la vitesse (en m/s) d'un taxi dans la zone allant de R/3 �
	 *            2R/3
	 * @param vitesseZone3
	 *            la vitesse (en m/s) d'un taxi dans la zone allant de 2R/3 � R
	 */
	public GenVitessesDistanceCentre(double R, double vitesseZone1, double vitesseZone2, double vitesseZone3) {
		// On stocke le carr� des bornes pour �viter d'avoir � calculer une
		// racine carr� plus tard
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
	 * G�n�re une vitesse (exprim�e en m/s) pour un taxi. Cette vitesse d�pend
	 * de la position du taxi dans la ville qui est d�coup�e � cet effet en
	 * trois parties concentriques de rayon �gal : � chaque portion de 0 � R/3,
	 * de R/3 � 2R/3 puis de 2R/3 � R correspond une vitesse.
	 * 
	 * @param position
	 *            la position du taxi dans le rep�re gradu� en m�tres ayant pour
	 *            origine le centre de la ville
	 * @return une vitesse (exprim�e en m/s) pour un taxi dans une certaine zone
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
