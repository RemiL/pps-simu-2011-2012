package simulateur;

public class SimulateurTaxis {

	private CentraleTaxis centrale;
	private double dureeSimulation;
	private int nbEchantillons;
	private double dt;
	
	public SimulateurTaxis(double dureeSimulation, int nbEchantillons){
		this.nbEchantillons = nbEchantillons;
		this.dureeSimulation = dureeSimulation;
		this.dt = dureeSimulation/nbEchantillons;
	}
	
	public static void main(String[] args) {
		

	}

}
