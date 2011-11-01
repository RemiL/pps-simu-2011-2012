package presentation.panels;

import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class AffichageResultat extends JPanel
{
	public AffichageResultat(HashMap<String, String> parametres, int typeSimulation, double resultat)
	{
		super(new GridLayout(parametres.size() + 1, 2, 20, 10));
		
		for(String param : parametres.keySet())
		{
			JLabel label = new JLabel(param);
			label.setHorizontalAlignment(JLabel.RIGHT);
			this.add(label);
			this.add(new JLabel(parametres.get(param)));
		}
		
		if(typeSimulation == 0)
		{
			JLabel label = new JLabel("Résultat (Nombre de taxis) : ");
			label.setHorizontalAlignment(JLabel.RIGHT);
			this.add(label);
			this.add(new JLabel("" + (int)resultat));
		}
		else
		{
			JLabel label = new JLabel("Résultat (Pourcentage de clients satisfaits) : ");
			label.setHorizontalAlignment(JLabel.RIGHT);
			this.add(label);
			this.add(new JLabel("" + resultat));
		}
	}
}
