package simulateur.generateurs.interfaces;

import java.awt.geom.Point2D;

/**
 * Interface d�finissant un g�n�rateur de position d'arriv�e pour les clients.
 */
public interface GenerateurPositionsArrivee {
	/**
	 * G�n�re une position de d'arriv�e situ�e dans la ville pour un client.
	 * Cette position d'arriv�e peut d�pendre du point de d�part du client.
	 * 
	 * @param pointDepart
	 *            le point de d�part du client pour lequel on g�n�re la
	 *            destination
	 * @return une point dans le r�p�re gradu� en m�tres ayant pour origine le
	 *         centre de la ville
	 */
	public Point2D.Double genererPositionArrivee(Point2D.Double pointDepart);
}
