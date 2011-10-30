package presentation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
 
import javax.swing.JFrame;

/**
 * Une fenêtre dans laquelle s'affiche l'interface.
 */
public class Fenetre extends JFrame {

	/** L'affichage du panneau de la ville */
	private AffichageVille affichageVille;
	/** L'affichage du panneau d'initialisation */
	private AffichageInit affichageInit;
	
	/**
	 * Construit une nouvelle fenêtre de taille 700x700
	 */
	public Fenetre()
	{
		this.setTitle("Simulation Taxi");
		this.setSize(700, 700);
		this.setLocationRelativeTo(null);               
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		this.setVisible(true);
	}
	
	public void initAffichageInit()
	{
		affichageInit = new AffichageInit();
		this.setContentPane(affichageInit);
		affichageInit.revalidate();
	}
	
	/**
	 * Initialise l'affichage de la ville avec le nombre de taxis et le rayon de la ville
	 * @param nbTaxis le nombre de taxis
	 * @param rayon le rayon de la ville
	 */
	public void initAffichageVille(int nbTaxis, int rayon)
	{
		affichageVille = new AffichageVille(nbTaxis, rayon);
		this.setContentPane(affichageVille);
		affichageVille.revalidate();
	}
	
	/**
	 * Modifie l'affichage de la ville
	 * @param listeTaxis la liste des positions des taxis dans le repère centré au centre du cercle
	 * @param listeClients la liste des position des clients dans le repère centré au centre du cercle
	 */
	public void setAffichageVille(Point2D.Double[] listeTaxis, ArrayList<Point2D.Double> listeClients)
	{
		affichageVille.setPosTaxis(listeTaxis);
		affichageVille.setPosClients(listeClients);
		affichageVille.repaint();
	}
}