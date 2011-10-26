package simulateur;

import java.awt.geom.Point2D;

public class SimulateurTaxis {

	private CentraleTaxis centrale;
	private double dureeSimulation;
	private int nbEchantillons;
	private double dt;
	private GenerateurApparitionClient genApparitionClient;
	private GenerateurPositionDepart genPositionDepart;
	private GenerateurPositionArrivee genPositionArrivee;
	private GenerateurTempsAttente genTempsAttente;

	public SimulateurTaxis() {
	}

	public void simuler(double dureeSimulation, int nbEchantillons, int nbTaxis, Point2D.Double position,
			double vitesse, GenerateurApparitionClient genApparitionClient, 
			GenerateurPositionDepart genPositionDepart,GenerateurPositionArrivee genPositionArrivee,
			GenerateurTempsAttente genTempsAttente) {
		this.nbEchantillons = nbEchantillons;
		this.dureeSimulation = dureeSimulation;
		this.dt = dureeSimulation / nbEchantillons;
		this.genApparitionClient = genApparitionClient;
		this.genPositionArrivee = genPositionArrivee;
		this.genPositionDepart = genPositionDepart;
		this.genTempsAttente = genTempsAttente;

		centrale = new CentraleTaxis(nbTaxis, position, vitesse);

		for (int i = 0; i <= nbEchantillons; i++) {
			// bouger taxis
			centrale.deplacerTaxis();
			simulerApparitionClients();

		}

	}

	private void simulerApparitionClients() {
		int nbClients = genApparitionClient.genererNombreApparitionClient();
		Client client;
		Point2D.Double ptDepart;
		Point2D.Double ptArrivee;
		
		for (int i = 0; i < nbClients; i++) {
			ptDepart = genPositionDepart.genererPositionDepart();
			ptArrivee = genPositionArrivee.genererPositionArrivee(ptDepart);
			client = new Client(ptDepart, ptArrivee, genTempsAttente.genererTempsAttente());
			centrale.ajouterClient(client);
		}

	}

	public static void main(String[] args) {

	}

}
