package simulateur.generateurs.implementations;

import java.awt.geom.Point2D;

import simulateur.generateurs.interfaces.GenerateurPositionsDepart;
import utils.GenerateurAleatoireUniforme;

/**
 * Classe représentant un générateur de position de départ pour les clients basé
 * sur une loi de probabilité uniforme sur un cercle.
 */
public class GenPositionsDepart implements GenerateurPositionsDepart {
	/** Le rayon (en m) du cercle représentant la ville. */
	private double rayon;

	/**
	 * Construit un générateur de position de départ pour les clients basé sur
	 * une loi de probabilité uniforme sur un cercle dont le rayon est fourni.
	 * 
	 * @param rayon
	 *            le rayon (en m) du cercle représentant la ville
	 */
	public GenPositionsDepart(double rayon) {
		this.rayon = rayon;
	}

	/**
	 * Génère une position de départ située dans la ville pour un client. Le
	 * client a une probabilité uniforme d'apparaitre en n'importe quel point du
	 * cercle représentant la ville.
	 * 
	 * @return une point dans le répère gradué en mètres ayant pour origine le
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
