package utils;

public class GenerateurAleatoireUniforme {

	public static double genererDouble(double min, double max) {

		return (max - min) * Math.random() + min;
	}

}
