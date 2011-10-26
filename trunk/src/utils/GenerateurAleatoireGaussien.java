package utils;

import java.util.Random;

public class GenerateurAleatoireGaussien {
	
	private static Random rand = new Random();
	
	public static double generer(double moyenne, double ecartType){
		
		return ecartType*rand.nextGaussian()+ moyenne;
	}

}
