package presentation;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Test
{
	public static void main(String[] args)
	{
        Fenetre fenetre = new Fenetre();
        fenetre.initAffichageInit();
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
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
		
		for(int i=0; i<1000; i++)
        {
			fenetre.setTemps(i);
    		fenetre.setAffichageVille(listeTaxis, listeClients);
    		
    		listeTaxis[0].x += 10;
    		listeTaxis[0].y += 10;
    		
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}
