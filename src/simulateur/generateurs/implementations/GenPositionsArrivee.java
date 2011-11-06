package simulateur.generateurs.implementations;

import java.awt.geom.Point2D;

import simulateur.generateurs.interfaces.GenerateurPositionsArrivee;
import utils.GenerateurAleatoireUniforme;

/**
 * Classe repr�sentant un g�n�rateur de position d'arriv�e pour les clients bas�
 * sur une loi de probabilit� uniforme sur un cercle dont on exclut une zone
 * circulaire autour du point de d�part du client.
 */
public class GenPositionsArrivee implements GenerateurPositionsArrivee {
	/** Le rayon (en m) du cercle repr�sentant la ville. */
	private double rayon;
	/** La distance d'exclusion (en m) autour du point de d�part du client. */
	private double distanceMin;

	/**
	 * Construit un g�n�rateur de position d'arriv�e pour les clients bas� sur
	 * une loi de probabilit� uniforme sur un cercle dont le rayon est fourni et
	 * dont on exclut une zone circulaire de rayon fourni autour du point de
	 * d�part du client.
	 * 
	 * @param rayon
	 *            le rayon (en m) du cercle repr�sentant la ville
	 * @param distanceMin
	 *            La rayon (en m) de la zone d'exclusion autour du point de
	 *            d�part du client
	 */
	public GenPositionsArrivee(double rayon, double distanceMin) {
		this.rayon = rayon;
		this.distanceMin = distanceMin;
	}

	/**
	 * G�n�re une position de d'arriv�e situ�e dans la ville pour un client. Le
	 * client a une probabilit� uniforme de souhaiter se rendre en n'importe
	 * quel point du cercle repr�sentant la ville mais la distance � parcourir
	 * entre le point de d�part et la destination sera toujours sup�rieure �
	 * celle sp�cifi�e lors de la construction du g�n�rateur.
	 * 
	 * @param pointDepart
	 *            le point de d�part du client pour lequel on g�n�re la
	 *            destination
	 * @return une point dans le r�p�re gradu� en m�tres ayant pour origine le
	 *         centre de la ville
	 */
	public Point2D.Double genererPositionArrivee(Point2D.Double pointDepart) {
		Point2D.Double arrivee = new Point2D.Double();

		do {
			arrivee.x = GenerateurAleatoireUniforme.genererDouble(-rayon, rayon);
			arrivee.y = GenerateurAleatoireUniforme.genererDouble(-rayon, rayon);
		} while ((arrivee.distanceSq(0, 0) > rayon * rayon)
				|| (arrivee.distanceSq(pointDepart) < distanceMin * distanceMin));

		return arrivee;
	}
}
