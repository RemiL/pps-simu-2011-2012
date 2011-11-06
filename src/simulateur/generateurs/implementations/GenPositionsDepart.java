package simulateur.generateurs.implementations;

import java.awt.geom.Point2D;

import simulateur.generateurs.interfaces.GenerateurPositionsDepart;
import utils.GenerateurAleatoireUniforme;

/**
 * Classe repr�sentant un g�n�rateur de position de d�part pour les clients bas�
 * sur une loi de probabilit� uniforme sur un cercle.
 */
public class GenPositionsDepart implements GenerateurPositionsDepart {
	/** Le rayon (en m) du cercle repr�sentant la ville. */
	private double rayon;

	/**
	 * Construit un g�n�rateur de position de d�part pour les clients bas� sur
	 * une loi de probabilit� uniforme sur un cercle dont le rayon est fourni.
	 * 
	 * @param rayon
	 *            le rayon (en m) du cercle repr�sentant la ville
	 */
	public GenPositionsDepart(double rayon) {
		this.rayon = rayon;
	}

	/**
	 * G�n�re une position de d�part situ�e dans la ville pour un client. Le
	 * client a une probabilit� uniforme d'apparaitre en n'importe quel point du
	 * cercle repr�sentant la ville.
	 * 
	 * @return une point dans le r�p�re gradu� en m�tres ayant pour origine le
	 *         centre de la ville
	 */
	public Point2D.Double genererPositionDepart() {
		Point2D.Double depart = new Point2D.Double();

		do {
			depart.x = GenerateurAleatoireUniforme.genererDouble(-rayon, rayon);
			depart.y = GenerateurAleatoireUniforme.genererDouble(-rayon, rayon);
		} while (depart.distanceSq(0, 0) > rayon * rayon);

		return depart;
	}
}
