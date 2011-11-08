package simulateur.generateurs.interfaces;

import java.awt.geom.Point2D;

/**
 * Interface définissant un générateur de vitesse pour les taxis.
 */
public interface GenerateurVitesses {
	/**
	 * Génère une vitesse (exprimée en m/s) pour un taxi. Cette vitesse peut
	 * dépendre de la position du taxi dans la ville.
	 * 
	 * @param position
	 *            la position du taxi dans le repère gradué en mètres ayant pour
	 *            origine le centre de la ville
	 * @return une vitesse (exprimée en m/s) pour un taxi
	 */
	public double genererVitesse(Point2D.Double position);
}
