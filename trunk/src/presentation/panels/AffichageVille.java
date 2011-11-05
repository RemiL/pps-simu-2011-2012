package presentation.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;

import javax.swing.JPanel;

import simulateur.Client;
import simulateur.Taxi;

/**
 * Un panneau d'affichage de la ville
 */
public class AffichageVille extends JPanel 
{
	/** La liste des chemins des taxis */
	private GeneralPath[] listePaths;
	/** La liste des clients */
	private LinkedList<Client> listeClients;
	/** Le rayon de la ville */
	private int rayonVille;
	/** La liste des couleurs utilisés pour dessiner les chemins des taxis */
	private Color[] listeColors = {Color.green, Color.blue, Color.cyan, Color.gray, Color.magenta, Color.orange, Color.pink, Color.yellow};
	/** La taille basique d'un point sans mise à l'échelle */
	private int tailleBasePoint;
	
	/**
	 * Construit un affichage de la ville
	 * @param nbTaxis le nombre de taxis
	 * @param rayon le rayon de la ville
	 */
	public AffichageVille(int nbTaxis, int rayon)
	{
		super();
		
		rayonVille = rayon;
		listePaths = new GeneralPath[nbTaxis];
		listeClients = new LinkedList<Client>();
		tailleBasePoint = 20;
	}
	
	/**
	 * Dessine la ville
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		
		// La taille minimal entre la largeur et la hauteur de la fenêtre
		int taille;
		if(this.getWidth() <= this.getHeight())
			taille = this.getWidth();
		else
			taille = this.getHeight();
		
		// Pour améliorer le rendu
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		// le ratio de mise à l'échelle pour que le cercle réprésantant la ville prenne toute la place possible
		double scale = (double)taille / (double)(rayonVille * 2);
		
		// La position du coin haut gauche du carré contenant le cercle
		int posX = (this.getWidth() - (int)(rayonVille * 2 * scale)) / 2;
		int posY = (this.getHeight() - (int)(rayonVille * 2 * scale)) / 2;
		
		// Le fond noir de l'affichage
		g2D.setColor(Color.black);
		g2D.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		// Le cercle blanc représentant la ville
		g2D.setColor(Color.white);
		g2D.fillOval(posX, posY, (int)(rayonVille * 2 * scale), (int)(rayonVille * 2 * scale));
		
		// Affichage des clients. Ils sont représentés par des carrés rouges
		g2D.setColor(Color.red);
		for(Client c : listeClients)
		{
			g2D.fillRect((int) ((c.getDepart().getX() - tailleBasePoint / 2) * scale + this.getWidth() / 2),(int) ((c.getDepart().getY() - tailleBasePoint / 2) * scale + this.getHeight() / 2), (int)(tailleBasePoint * scale), (int)(tailleBasePoint * scale));
		}
		
		double translateX;
		double translateY;
		
		// Affichage des chemins des taxis
		for(int i=0; i<listePaths.length; i++)
		{
			if(listePaths[i] != null)
			{
				// Met le chemin à l'échelle
				GeneralPath path = (GeneralPath) listePaths[i].clone();
				translateX = path.getCurrentPoint().getX() - listePaths[i].getCurrentPoint().getX() + this.getWidth() / 2;
				translateY = path.getCurrentPoint().getY() - listePaths[i].getCurrentPoint().getY() + this.getHeight() / 2;	
				path.transform(new AffineTransform(scale, 0, 0, scale, translateX, translateY));
				
				// Un chemin est représenté par des lignes de couleur avec au bout un point représentant la position actuelle du taxi
				g2D.setColor(listeColors[i%8]);
				g2D.draw(path);
				g2D.fillOval((int) (path.getCurrentPoint().getX() - (tailleBasePoint / 2) * scale),(int) (path.getCurrentPoint().getY() - (tailleBasePoint / 2) * scale), (int)(tailleBasePoint * scale), (int)(tailleBasePoint * scale));
			}
		}
	}
	
	/**
	 * Modifie le chemin des taxis dans la liste
	 * @param taxis la liste des taxis présents dans la ville
	 */
	public void setPosTaxis(Taxi[] taxis)
	{
		// Pour chaque taxi, on complète son chemin
		for(int i=0; i<taxis.length; i++)
		{
			// Si le chemin n'existe pas on en crée un nouveau sinon il est complété
			if(listePaths[i] == null)
			{
				listePaths[i] = new GeneralPath();
				listePaths[i].moveTo(taxis[i].getPosition().getX(), taxis[i].getPosition().getY());
			}
			else
				listePaths[i].lineTo(taxis[i].getPosition().getX(), taxis[i].getPosition().getY());
		}
	}
	
	/**
	 * Modifie la liste des positions des clients
	 * @param clients la liste des clients non pris en charge dans la ville
	 */
	public void setPosClients(LinkedList<Client> clients)
	{
		listeClients = clients;
	}
}
