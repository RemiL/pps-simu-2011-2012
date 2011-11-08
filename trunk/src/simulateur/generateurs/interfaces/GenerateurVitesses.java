package simulateur.generateurs.interfaces;

import java.awt.geom.Point2D;

/**
 * Interface d�finissant un g�n�rateur de vitesse pour les taxis.
 */
public interface GenerateurVitesses {
	/**
	 * G�n�re une vitesse (exprim�e en m/s) pour un taxi. Cette vitesse peut
	 * d�pendre de la position du taxi dans la ville.
	 * 
	 * @param position
	 *            la position du taxi dans le rep�re gradu� en m�tres ayant pour
	 *            origine le centre de la ville
	 * @return une vitesse (exprim�e en m/s) pour un taxi
	 */
	public double genererVitesse(Point2D.Double position);
}
