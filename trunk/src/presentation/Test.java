package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Test implements ActionListener
{
	private Fenetre fenetre;
	private boolean play;
	private boolean stop;
	private Thread t;
	
	public Test()
	{
		fenetre = new Fenetre();
		fenetre.getBoutonSimuler().addActionListener(this);
		fenetre.getBoutonStop().addActionListener(this);
		fenetre.getBoutonPausePlay().addActionListener(this);
		play = true;
		stop = false;
	}
	
	public void configurer()
	{
		String[] labels1 = { "First Name", "Middle Initial", "Last Name", "Age" };
	    int[] widths1 = { 15, 1, 15, 3 };
	    String[] labels2 = { "First Name2", "Middle Initial2", "Last Name2", "Age2" };
	    int[] widths2 = { 15, 1, 15, 3 };
		fenetre.initAffichageInit(labels1, widths1, labels2, widths2);
	}
	
	public void simuler()
	{
        fenetre.initAffichageVille(10, 1000);
        
        Point2D.Double[] listeTaxis = new Point2D.Double[10];
		ArrayList<Point2D.Double> listeClients = new ArrayList<Point2D.Double>();
		
		listeTaxis[0] = new Point2D.Double(0, 0);
		listeTaxis[1] = new Point2D.Double(50, 50);
		listeTaxis[2] = new Point2D.Double(100, 100);
		listeTaxis[3] = new Point2D.Double(350, 350);
		listeTaxis[4] = new Point2D.Double(80, 80);
		listeTaxis[5] = new Point2D.Double(500, 500);
		listeTaxis[6] = new Point2D.Double(10, 10);
		listeTaxis[7] = new Point2D.Double(300, 300);
		listeTaxis[8] = new Point2D.Double(200, 200);
		listeTaxis[9] = new Point2D.Double(600, 600);
		
		listeClients.add(new Point2D.Double(0, 0));
		listeClients.add(new Point2D.Double(1000, 0));
		listeClients.add(new Point2D.Double(0, 1000));
		
		for(int i=0; i<100; i++)
        {
			if(stop)
				break;
			if(play)
			{
				fenetre.setTemps(i, 10);
	    		fenetre.setAffichageVille(listeTaxis, listeClients);
	    		
	    		listeTaxis[0].x += 10;
	    		listeTaxis[0].y += 10;
			}
			else
				i--;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
        }
		
		fenetre.changeBoutonPausePlay(play, true);
	}
	
	public static void main(String[] args)
	{
        Test test = new Test();
        test.configurer();
	}
	
	public void actionPerformed(ActionEvent arg0)
	{
		if(arg0.getSource() == fenetre.getBoutonSimuler())
		{
			System.out.println(fenetre.getValues());
			System.out.println(fenetre.getSimulationType());
			t = new Thread(new PlaySimulation());
            t.start();
		}
		else if(arg0.getSource() == fenetre.getBoutonStop())
		{
			stop = true;
			fenetre.changeBoutonPausePlay(play, stop);
		}
		else if(arg0.getSource() == fenetre.getBoutonPausePlay())
		{
			play = !play;
			fenetre.changeBoutonPausePlay(play, stop);
		}
	}
	
	class PlaySimulation implements Runnable
	{
		@Override
		public void run() 
		{
			simuler();
		}
	}
}
