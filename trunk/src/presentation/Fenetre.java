package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import presentation.buttons.BoutonSimuler;
import presentation.buttons.BoutonStop;
import presentation.panels.AffichageInit;
import presentation.panels.AffichageTemps;
import presentation.panels.AffichageVille;

/**
 * Une fenêtre dans laquelle s'affiche l'interface.
 */
public class Fenetre extends JFrame 
{
	/** L'affichage du panneau de la ville */
	private AffichageVille affichageVille;
	/** L'affichage du panneau d'initialisation */
	private AffichageInit affichageInit;
	/** L'affichage du panneau représentant le temps actuel */
	private AffichageTemps affichageTemps;
	
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
		this.getContentPane().removeAll();
		affichageInit = new AffichageInit();
		this.setLayout(new BorderLayout());
		this.getContentPane().add(affichageInit, BorderLayout.CENTER);
		this.add(new BoutonSimuler(), BorderLayout.SOUTH);
		affichageInit.revalidate();
	}
	
	/**
	 * Initialise l'affichage de la ville avec le nombre de taxis et le rayon de la ville
	 * @param nbTaxis le nombre de taxis
	 * @param rayon le rayon de la ville
	 */
	public void initAffichageVille(int nbTaxis, int rayon)
	{
		this.getContentPane().removeAll();
		affichageVille = new AffichageVille(nbTaxis, rayon);
		affichageTemps =new AffichageTemps();
		
		this.setLayout(new BorderLayout());
		this.getContentPane().add(affichageVille, BorderLayout.CENTER);
		this.add(affichageTemps, BorderLayout.NORTH);
		this.add(new BoutonStop(), BorderLayout.SOUTH);
		affichageVille.revalidate();
		affichageTemps.revalidate();
	}
	
	/**
	 * Modifie l'affichage de la ville s'il existe
	 * @param listeTaxis la liste des positions des taxis dans le repère centré au centre du cercle
	 * @param listeClients la liste des position des clients dans le repère centré au centre du cercle
	 */
	public void setAffichageVille(Point2D.Double[] listeTaxis, ArrayList<Point2D.Double> listeClients)
	{
		if(affichageVille != null)
		{
			affichageVille.setPosTaxis(listeTaxis);
			affichageVille.setPosClients(listeClients);
			affichageVille.repaint();
		}
	}
	
	/**
	 * Modifie le temps actuel de affichageTemps s'il existe
	 * @param tempsActuel le temps actuel
	 */
	public void setTemps(int tempsActuel)
	{
		if(affichageTemps != null)
		{
			affichageTemps.setTemps(tempsActuel);
			affichageTemps.repaint();
		}
	}
}