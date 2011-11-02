package simulateur;

import java.awt.geom.Point2D;

public class SimulateurTaxis {

	private double dureeSimulation;
	private int nbEchantillons;
	private ReferentielTemps referentielTemps;

	private CentraleTaxis centrale;
	private GenerateurApparitionClient genApparitionClient;
	private GenerateurPositionDepart genPositionDepart;
	private GenerateurPositionArrivee genPositionArrivee;
	private GenerateurTempsAttente genTempsAttente;

	public SimulateurTaxis() {
	}

	public void simuler(double dureeSimulation, int nbEchantillons, int nbTaxis, Point2D.Double position,
			double vitesse, GenerateurApparitionClient genApparitionClient, GenerateurPositionDepart genPositionDepart,
			GenerateurPositionArrivee genPositionArrivee, GenerateurTempsAttente genTempsAttente) {
		this.nbEchantillons = nbEchantillons;
		this.dureeSimulation = dureeSimulation;
		this.referentielTemps = new ReferentielTemps(dureeSimulation / nbEchantillons);

		this.genApparitionClient = genApparitionClient;
		this.genPositionArrivee = genPositionArrivee;
		this.genPositionDepart = genPositionDepart;
		this.genTempsAttente = genTempsAttente;

		centrale = new CentraleTaxis(referentielTemps, nbTaxis, position, vitesse);

		// On effectue la boucle n+1 fois puisque la première itération sert
		// à l'initialisation, les taxis effectueront donc bien n mouvements.
		for (int i = 0; i <= nbEchantillons; i++) {
			// On affecte les clients en attente aux taxis disponibles,
			centrale.affecterTaxis();
			// on calcule le déplacement des taxis pendant l'intervalle dt
			centrale.deplacerTaxis();
			// puis on génère l'apparition d'éventuels nouveaux clients.
			simulerApparitionClients();
			// On incrémente l'horloge.
			referentielTemps.incrementerTemps();
		}
	}

	private void simulerApparitionClients() {
		int nbClients = genApparitionClient.genererNombreApparitionClient();
		Point2D.Double ptDepart;
		Point2D.Double ptArrivee;
		double tempsAttenteMax;

		for (int i = 0; i < nbClients; i++) {
			ptDepart = genPositionDepart.genererPositionDepart();
			ptArrivee = genPositionArrivee.genererPositionArrivee(ptDepart);
			tempsAttenteMax = genTempsAttente.genererTempsAttente();

			centrale.ajouterClient(new Client(referentielTemps, ptDepart, ptArrivee, tempsAttenteMax));
		}
	}

	public static void main(String[] args) {

	}
}
