package simulateur;

public class Horloge {
	private static double t = 0.0;
	
	public static void setTemps(double t) {
		Horloge.t = t;
	}
	
	public static double getTemps() {
		return Horloge.t;
	}
}
