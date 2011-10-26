package simulateur;

import utils.GenerateurAleatoirePoisson;

public class GenApparitionClientPoisson implements GenerateurApparitionClient {

	private double lambda;
	
	public GenApparitionClientPoisson(double lambda){
		this.lambda = lambda;
	}
	
	public int genererNombreApparitionClient() {
		
		return GenerateurAleatoirePoisson.generer(lambda);
		
	}

}
