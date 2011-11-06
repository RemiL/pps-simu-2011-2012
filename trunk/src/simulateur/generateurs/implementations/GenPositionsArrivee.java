package simulateur.generateurs.implementations;

import java.awt.geom.Point2D;

import simulateur.generateurs.interfaces.GenerateurPositionsArrivee;
import utils.GenerateurAleatoireUniforme;

/**
 * Classe représentant un générateur de position d'arrivée pour les clients basé
 * sur une loi de probabilité uniforme sur un cercle dont on exclut une zone
 * circulaire autour du point de départ du client.
 */
public class GenPositionsArrivee implements GenerateurPositionsArrivee {
	/** Le rayon (en m) du cercle représentant la ville. */
	private double rayon;
	/** La distance d'exclusion (en m) autour du point de départ du client. */
	private double distanceMin;

	/**
	 * Construit un générateur de position d'arrivée pour les clients basé sur
	 * une loi de probabilité uniforme sur un cercle dont le rayon est fourni et
	 * dont on exclut une zone circulaire de rayon fourni autour du point de
	 * départ du client.
	 * 
	 * @param rayon
	 *            le rayon (en m) du cercle représentant la ville
	 * @param distanceMin
	 *            La rayon (en m) de la zone d'exclusion autour du point de
	 *            départ du client
	 */
	public GenPositionsArrivee(double rayon, double distanceMin) {
		this.rayon = rayon;
		this.distanceMin = distanceMin;
	}

	/**
	 * Génère une position de d'arrivée située dans la ville pour un client. Le
	 * client a une probabilité uniforme de souhaiter se rendre en n'importe
	 * quel point du cercle représentant la ville mais la distance à parcourir
	 * entre le point de départ et la destination sera toujours supérieure à
	 * celle spécifiée lors de la construction du générateur.
	 * 
	 * @param pointDepart
	 *            le point de départ du client pour lequel on génère la
	 *            destination
	 * @return une point dans le répère gradué en mètres ayant pour origine le
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
