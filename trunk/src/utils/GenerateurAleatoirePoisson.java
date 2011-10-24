package utils;

public class GenerateurAleatoirePoisson {

	public static int generer(double lambda) {
		double l = Math.exp(-lambda);
		int k = 0;
		double p = 1;

		do {
			k++;
			p *= Math.random();
		} while (p > l);
		return k - 1;
	}
}
