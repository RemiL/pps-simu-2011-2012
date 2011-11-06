package simulateur.generateurs.interfaces;

import java.awt.geom.Point2D;

/**
 * Interface d�finissant un g�n�rateur de position de d�part pour les clients.
 */
public interface GenerateurPositionsDepart {
	/**
	 * G�n�re une position de d�part situ�e dans la ville pour un client.
	 * 
	 * @return un point dans le r�p�re gradu� en m�tres ayant pour origine le
	 *         centre de la ville
	 */
	public Point2D.Double genererPositionDepart();
}
