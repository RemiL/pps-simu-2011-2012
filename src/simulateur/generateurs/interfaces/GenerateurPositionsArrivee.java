package simulateur.generateurs.interfaces;

import java.awt.geom.Point2D;

/**
 * Interface définissant un générateur de position d'arrivée pour les clients.
 */
public interface GenerateurPositionsArrivee {
	/**
	 * Génère une position de d'arrivée située dans la ville pour un client.
	 * Cette position d'arrivée peut dépendre du point de départ du client.
	 * 
	 * @param pointDepart
	 *            le point de départ du client pour lequel on génère la
	 *            destination
	 * @return une point dans le répère gradué en mètres ayant pour origine le
	 *         centre de la ville
	 */
	public Point2D.Double genererPositionArrivee(Point2D.Double pointDepart);
}
