package presentation.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * Des onglets pour les différentes simulations avec pour chacun un formulaire
 * pour rentrer les paramètres de la simulation
 */
public class AffichageInit extends JTabbedPane {

	private static final long serialVersionUID = 2757677226841283740L;

	/** L'onglet pour la configuration de la recherche du nombre de taxis */
	private JPanel tabNbTaxis;
	/** L'onglet pour la configuration du calcul du pourcentage */
	private JPanel tabNbClients;
	/**
	 * Les champs des paramètres avec leurs valeurs pour la configuration de la
	 * recherche du nombre de taxis
	 */
	private HashMap<String, JTextField> champsValeurs1;
	/**
	 * Les champs des paramètres avec leurs valeurs pour la configuration du
	 * calcul du pourcentage
	 */
	private HashMap<String, JTextField> champsValeurs2;

	/**
	 * Construit un affichage pour rentrer les paramètres sous forme de deux
	 * onglets. Le premier pour paramétrer la recherche du nombre de taxis à
	 * avoir pour prendre en charge un certain pourcentage de clients. Le second
	 * pour paramétrer le calcul du pourcentage de clients pris en charge par un
	 * certain nombre de taxis.
	 */
	public AffichageInit(String[] labels1, int[] widths1, String[] labels2, int[] widths2) {
		super();

		tabNbTaxis = new JPanel(new BorderLayout());
		tabNbClients = new JPanel(new BorderLayout());

		champsValeurs1 = new HashMap<String, JTextField>();
		champsValeurs2 = new HashMap<String, JTextField>();

		// Crée le premier formulaire pour la recherche du nombre de taxis
		JPanel labelPanel1 = new JPanel(new GridLayout(labels1.length, 1));
		JPanel fieldPanel1 = new JPanel(new GridLayout(labels1.length, 1));
		tabNbTaxis.add(labelPanel1, BorderLayout.WEST);
		tabNbTaxis.add(fieldPanel1, BorderLayout.CENTER);
		JTextField[] fields1 = new JTextField[labels1.length];

		for (int i = 0; i < labels1.length; i += 1) {
			fields1[i] = new JTextField();
			if (i < widths1.length)
				fields1[i].setColumns(widths1[i]);

			JLabel lab = new JLabel(labels1[i], JLabel.RIGHT);
			lab.setLabelFor(fields1[i]);

			labelPanel1.add(lab);
			JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
			p.add(fields1[i]);
			fieldPanel1.add(p);

			champsValeurs1.put(labels1[i], fields1[i]);
		}

		// Crée le second formulaire pour le calcul du pourcentage
		JPanel labelPanel2 = new JPanel(new GridLayout(labels2.length, 1));
		JPanel fieldPanel2 = new JPanel(new GridLayout(labels2.length, 1));
		tabNbClients.add(labelPanel2, BorderLayout.WEST);
		tabNbClients.add(fieldPanel2, BorderLayout.CENTER);
		JTextField[] fields2 = new JTextField[labels2.length];

		for (int i = 0; i < labels2.length; i += 1) {
			fields2[i] = new JTextField();
			if (i < widths2.length)
				fields2[i].setColumns(widths2[i]);

			JLabel lab = new JLabel(labels2[i], JLabel.RIGHT);
			lab.setLabelFor(fields2[i]);

			labelPanel2.add(lab);
			JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
			p.add(fields2[i]);
			fieldPanel2.add(p);

			champsValeurs2.put(labels2[i], fields2[i]);
		}

		this.addTab("Chercher le nombre de taxis", tabNbTaxis);
		this.addTab("Calculer le pourcentage de clients pris en charge", tabNbClients);
	}

	/**
	 * Retourne une map qui à chaque nom de paramètre associe sa valeur
	 * 
	 * @return une map qui à chaque nom de paramètre associe sa valeur
	 */
	public HashMap<String, String> getValues() {
		HashMap<String, String> result = new HashMap<String, String>();
		HashMap<String, JTextField> values = null;

		if (this.getSelectedIndex() == 0)
			values = champsValeurs1;
		else
			values = champsValeurs2;

		for (String label : values.keySet()) {
			result.put(label, values.get(label).getText());
		}
		return result;
	}
}
