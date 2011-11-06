package simulateur.generateurs.interfaces;

import java.awt.geom.Point2D;

/**
 * Interface définissant un générateur de position de départ pour les clients.
 */
public interface GenerateurPositionsDepart {
	/**
	 * Génère une position de départ située dans la ville pour un client.
	 * 
	 * @return un point dans le répère gradué en mètres ayant pour origine le
	 *         centre de la ville
	 */
	public Point2D.Double genererPositionDepart();
}
