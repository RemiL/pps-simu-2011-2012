package presentation.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Un panneau avec un formulaire pour rentrer les paramètres de la simulation
 */
public class AffichageInit extends JPanel
{	
	/**
	 *  Construit un affichage pour rentrer les paramètres
	 */
	public AffichageInit()
	{
		super();
	}
	
	/**
	 * Dessine le panneau avec les champs à remplir
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		
		// Pour améliorer le rendu
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		// Le fond blanc de l'affichage
		g2D.setColor(Color.white);
		g2D.fillRect(0, 0, this.getWidth(), this.getHeight());		
	}
}
