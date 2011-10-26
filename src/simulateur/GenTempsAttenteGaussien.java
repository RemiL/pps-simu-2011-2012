package simulateur;

import utils.GenerateurAleatoireGaussien;

public class GenTempsAttenteGaussien implements GenerateurTempsAttente{

	private double tempsAttenteMoyen;
	private double ecartType;
	
	public GenTempsAttenteGaussien(double tempsAttenteMoyen, double ecartType){
		this.tempsAttenteMoyen = tempsAttenteMoyen;
		this.ecartType = ecartType;
		
	}
	public double genererTempsAttente() {
	
		return GenerateurAleatoireGaussien.generer(tempsAttenteMoyen, ecartType);
	}

}
