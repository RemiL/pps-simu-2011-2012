package presentation.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Un panneau d'affichage du temps qui passe
 */
public class AffichageTemps extends JPanel
{
	/** Le temps actuel */
	private int temps;
	
	/**
	 *  Construit un affichage du temps
	 */
	public AffichageTemps()
	{
		super();
		this.setPreferredSize(new Dimension(0, 30));
	}
	
	/**
	 * Dessine le panneau avec le temps actuel
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		
		// Pour améliorer le rendu
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		// Le fond noir de l'affichage
		g2D.setColor(Color.black);
		g2D.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		// Affichage du texte représentant le temps
		Font font = new Font("Arial", Font.BOLD, 20);
		g2D.setFont(font);
		g2D.setColor(Color.white);          
		g2D.drawString("Temps actuel : " + temps, 10, 20);
	}
	
	/**
	 * Modifie le temps actuel
	 * @param tempsActuel le temps actuel
	 */
	public void setTemps(int tempsActuel)
	{
		temps = tempsActuel;
	}
}
