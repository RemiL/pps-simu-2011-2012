package presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import presentation.panels.AffichageInit;
import presentation.panels.AffichageInfos;
import presentation.panels.AffichageResultat;
import presentation.panels.AffichageVille;
import simulateur.Client;
import simulateur.Taxi;

/**
 * Une fenêtre dans laquelle s'affiche l'interface.
 */
public class Fenetre extends JFrame {

	private static final long serialVersionUID = -7730867283004374558L;

	/** L'affichage du panneau de la ville */
	private AffichageVille affichageVille;
	/** L'affichage du panneau d'initialisation */
	private AffichageInit affichageInit;
	/** L'affichage du panneau représentant le temps actuel */
	private AffichageInfos affichageInfos;
	/** Le bouton pour lancer la simulation */
	private JButton boutonSimuler;
	/** Le bouton pour arrêter la simulation */
	private JButton boutonStop;
	/** Le bouton pour reprendre/mettre en pause la simulation */
	private JButton boutonPausePlay;

	/**
	 * Construit une nouvelle fenêtre de taille 700x700
	 */
	public Fenetre() {
		this.setTitle("Simulation Taxi");
		this.setSize(700, 700);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(600, 600));
		this.setVisible(true);

		boutonStop = new JButton("Stop");
		boutonSimuler = new JButton("Simuler");
		boutonPausePlay = new JButton("Pause");
	}

	public void initAffichageInit(String[] labels1, int[] widths1, String[] labels2, int[] widths2) {
		this.getContentPane().removeAll();
		affichageInit = new AffichageInit(labels1, widths1, labels2, widths2);
		this.setLayout(new BorderLayout());
		this.getContentPane().add(affichageInit, BorderLayout.NORTH);

		JPanel pan = new JPanel();
		pan.add(boutonSimuler);

		this.add(pan, BorderLayout.SOUTH);
		affichageInit.revalidate();
	}

	/**
	 * Initialise l'affichage de la ville avec le nombre de taxis et le rayon de
	 * la ville
	 * 
	 * @param nbTaxis
	 *            le nombre de taxis
	 * @param rayon
	 *            le rayon de la ville
	 */
	public void initAffichageVille(int nbTaxis, int rayon) {
		this.getContentPane().removeAll();
		affichageVille = new AffichageVille(nbTaxis, rayon);
		affichageInfos = new AffichageInfos();

		this.setLayout(new BorderLayout());
		this.getContentPane().add(affichageVille, BorderLayout.CENTER);
		this.add(affichageInfos, BorderLayout.NORTH);

		JPanel pan = new JPanel();
		pan.add(boutonPausePlay);
		pan.add(boutonStop);

		this.add(pan, BorderLayout.SOUTH);
		affichageVille.revalidate();
		affichageInfos.revalidate();
	}

	public void afficherResultat(HashMap<String, String> parametres, int typeSimulation, double resultat) {
		this.getContentPane().removeAll();
		AffichageResultat pan = new AffichageResultat(parametres, typeSimulation, resultat);
		this.add(pan, BorderLayout.CENTER);
		pan.revalidate();
	}

	/**
	 * Modifie l'affichage de la ville s'il existe
	 * 
	 * @param taxis
	 *            la liste des taxis dans la ville
	 * @param clients
	 *            la liste des clients non pris en charge dans la ville
	 */
	public void setAffichageVille(Taxi[] taxis, LinkedList<Client> clients) {
		if (affichageVille != null) {
			affichageVille.setPosTaxis(taxis);
			affichageVille.setPosClients(clients);
			affichageVille.repaint();
		}
	}

	/**
	 * Modifie le temps actuel et le nombre de taxis de affichageInfos s'il
	 * existe
	 * 
	 * @param tempsActuel
	 *            le temps actuel
	 * @param nbTaxis
	 *            le nombre de taxis
	 */
	public void setInfos(int tempsActuel, int nbTaxis) {
		if (affichageInfos != null) {
			affichageInfos.setTemps(tempsActuel);
			affichageInfos.setNbTaxis(nbTaxis);
			affichageInfos.repaint();
		}
	}

	/**
	 * Return l'instance du bouton pour lancer la simulation
	 * 
	 * @return l'instance du bouton pour lancer la simulation
	 */
	public JButton getBoutonSimuler() {
		return boutonSimuler;
	}

	/**
	 * Return l'instance du bouton pour arrêter la simulation
	 * 
	 * @return l'instance du bouton pour arrêter la simulation
	 */
	public JButton getBoutonStop() {
		return boutonStop;
	}

	/**
	 * Return l'instance du bouton pour mettre en pause ou reprendre la
	 * simulation
	 * 
	 * @return l'instance du bouton pour mettre en pause ou reprendre la
	 *         simulation
	 */
	public JButton getBoutonPausePlay() {
		return boutonPausePlay;
	}

	/**
	 * Change l'état des bouton play/pause et stop
	 * 
	 * @param play
	 *            l'état du bouton play/pause
	 * @param stop
	 *            l'état du bouton stop
	 */
	public void changeBoutonPausePlay(boolean play, boolean stop) {
		// Si la simulation est arrêtée, les boutons ne sont plus cliquables
		if (stop) {
			boutonPausePlay.setText("Play");
			boutonPausePlay.setEnabled(false);
			boutonStop.setEnabled(false);
		} else {
			// Si la simulation est en cours, on peut la mettre en pause
			// Si elle est en pause on peut la reprendre
			if (play)
				boutonPausePlay.setText("Pause");
			else
				boutonPausePlay.setText("Play");
		}
	}

	/**
	 * Retourne une map qui à chaque nom de paramètre associe sa valeur
	 * 
	 * @return une map qui à chaque nom de paramètre associe sa valeur. null si
	 *         l'affichage n'existe pas.
	 */
	public HashMap<String, String> getValues() {
		if (affichageInit != null)
			return affichageInit.getValues();
		else
			return null;
	}

	/**
	 * Retourne le type de simulation
	 * 
	 * @return le type de simulation. 0 pour la recherche du nombre de taxis. 1
	 *         pour le calcul du pourcentage. -1 si aucun n'est choisi
	 */
	public int getSimulationType() {
		if (affichageInit != null)
			return affichageInit.getSelectedIndex();
		else
			return -1;
	}
}