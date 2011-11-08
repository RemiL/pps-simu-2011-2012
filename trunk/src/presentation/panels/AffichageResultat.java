package presentation.panels;

import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Un panneau d'affichage pour le r�sultat de la simulation
 */
public class AffichageResultat extends JPanel {

	private static final long serialVersionUID = -6701394843784348742L;

	/**
	 * Construit un affichage du r�sultat
	 * 
	 * @param parametres
	 *            les param�tres de la simulation
	 * @param typeSimulation
	 *            le type de la simulation (0 ou 1)
	 * @param pourcentageClientsSatisfaits
	 *            le pourcentage de clients satisfaits
	 * @param nbTaxis
	 *            le nombre de taxis
	 */
	public AffichageResultat(HashMap<String, String> parametres, int typeSimulation,
			double pourcentageClientsSatisfaits, int nbTaxis) {
		super(new GridLayout(parametres.size() + 1, 2, 20, 10));

		// Affiche les param�tres de la simulation
		for (String param : parametres.keySet()) {
			JLabel label = new JLabel(param);
			label.setHorizontalAlignment(JLabel.RIGHT);
			this.add(label);
			this.add(new JLabel(parametres.get(param)));
		}

		// Affiche le r�sultat de la simulation
		if (typeSimulation == 0) {
			JLabel label = new JLabel("R�sultat : Nombre de taxis : ");
			label.setHorizontalAlignment(JLabel.RIGHT);
			this.add(label);
			this
					.add(new JLabel(nbTaxis + " (pourcentage de clients satisfaits : " + pourcentageClientsSatisfaits
							+ ")"));
		} else {
			JLabel label = new JLabel("R�sultat : Pourcentage de clients satisfaits : ");
			label.setHorizontalAlignment(JLabel.RIGHT);
			this.add(label);
			this.add(new JLabel(pourcentageClientsSatisfaits + "%"));
		}
	}
}
