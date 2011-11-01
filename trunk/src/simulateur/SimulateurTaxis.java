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
			double vitesse, GenerateurApparitionClient genApparitionClient, GenerateurPositionDepart genPositionDepart,
			GenerateurPositionArrivee genPositionArrivee, GenerateurTempsAttente genTempsAttente) {
		this.nbEchantillons = nbEchantillons;
		this.dureeSimulation = dureeSimulation;
		this.dt = dureeSimulation / nbEchantillons;
		this.genApparitionClient = genApparitionClient;
		this.genPositionArrivee = genPositionArrivee;
		this.genPositionDepart = genPositionDepart;
		this.genTempsAttente = genTempsAttente;

		centrale = new CentraleTaxis(nbTaxis, position, vitesse, dt);

		// On effectue la boucle n+1 fois puisque la premi�re it�ration sert
		// � l'initialisation, les taxis effectueront donc bien n mouvements.
		for (int i = 0; i <= nbEchantillons; i++) {
			// On met � jour l'horloge, on d�cide de ne pas utiliser
			// l'incr�mentation pour limiter le bruit num�rique.
			Horloge.setTemps(i * dt);
			// On affecte les clients en attente aux taxis disponibles,
			centrale.affecterTaxis();
			// on calcule le d�placement des taxis pendant l'intervalle dt
			centrale.deplacerTaxis();
			// puis on g�n�re l'apparition d'�ventuels nouveaux clients.
			simulerApparitionClients();
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

			centrale.ajouterClient(new Client(ptDepart, ptArrivee, tempsAttenteMax));
		}
	}

	public static void main(String[] args) {

	}
}
