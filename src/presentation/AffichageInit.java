package presentation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

/**
 * Un panneau d'affichage pour rentrer les paramètres
 */
public class AffichageInit extends JPanel
{
	/**
	 *  Construit un affichage d'initialisation
	 */
	public AffichageInit()
	{
		super();
	}
	
	/**
	 * Dessine le panneau avec les champs pour renseigner les paramètres et lancer la simulation
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		
		// Le fond blanc de l'affichage
		g2D.setColor(Color.white);
		g2D.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
}
