package simulateur;

import java.awt.geom.Point2D;

import simulateur.generateurs.interfaces.GenerateurTempsAttente;

/**
 * Classe repr�sentant un client de la soci�t� de taxi.
 * 
 * Un client est caract�ris� par son �tat (attente de l'envoi ou de l'arriv�e
 * d'un taxi, pris en charge par un taxi ou abandon) et par son point de d�part
 * et sa destination. S'il n'a pas �t� pris en charge par un taxi au bout d'un
 * certain temps, un client peut se lasser et abandonner l'id�e d'attendre un
 * taxi.
 */
public class Client {
	/**
	 * Le r�f�rentiel de temps partag� par tous les parties prenantes du
	 * probl�me (centrale des taxis, taxis et clients).
	 */
	private ReferentielTemps referentielTemps;
	/**
	 * Les points de d�part et d'arriv�e du client dans le r�p�re gradu� en
	 * m�tres ayant pour origine le centre de la ville
	 */
	private Point2D.Double depart, arrivee;
	/**
	 * L'horaire (dans le r�f�rentiel de temps partag�) apr�s lequel le client
	 * abandonne l'id�e de prendre un taxi
	 */
	private double horaireAbandon;
	/**
	 * Le g�n�rateur de temps d'attente permettant de tirer le temps d'attente
	 * suppl�mentaire qu'est pr�t � attendre le client quand la centrale de taxi
	 * lui annonce qu'un v�hicule va lui �tre envoy�
	 */
	private GenerateurTempsAttente genTempsAttenteSupplementaire;

	/**
	 * Enum�ration d�crivant les �tats possibles du client.
	 */
	public enum Etat {
		ATTENTE_ENVOI_TAXI, ATTENTE_ARRIVEE_TAXI, PRIS_EN_CHARGE, ABANDON
	};

	/** Etat courant du client */
	private Etat etat;

	/**
	 * Construit un nouveau client poss�dant les caract�ristiques fournies.
	 * 
	 * @param referentielTemps
	 *            le r�f�rentiel de temps commun � toutes les parties prenantes
	 * @param depart
	 *            le point de d�part du client dans le r�p�re gradu� en m�tres
	 *            ayant pour origine le centre de la ville
	 * @param arrivee
	 *            le destination du client dans le r�p�re gradu� en m�tres ayant
	 *            pour origine le centre de la ville
	 * @param tempsAttente
	 *            le temps d'attente apr�s lequel le client abandonne l'id�e de
	 *            prendre un taxi si la compagnie ne le rappelle pas pour le
	 *            pr�venir qu'on lui envoie un taxi
	 * @param genTempsAttenteSupplementaire
	 *            le g�n�rateur de temps d'attente permettant de tirer le temps
	 *            d'attente suppl�mentaire qu'est pr�t � attendre le client
	 *            quand la centrale de taxi lui annonce qu'un v�hicule va lui
	 *            �tre envoy�
	 */
	public Client(ReferentielTemps referentielTemps, Point2D.Double depart, Point2D.Double arrivee,
			double tempsAttente, GenerateurTempsAttente genTempsAttenteSupplementaire) {
		this.referentielTemps = referentielTemps;

		this.depart = depart;
		this.arrivee = arrivee;
		this.horaireAbandon = referentielTemps.getTemps() + tempsAttente;
		this.genTempsAttenteSupplementaire = genTempsAttenteSupplementaire;
		this.etat = Etat.ATTENTE_ENVOI_TAXI;
	}

	/**
	 * Met � jour l'�tat du client en fonction de l'horaire courant dans le
	 * r�f�rentiel de temps commun.
	 */
	public void majEtat() {
		// Si on a d�pass� l'horaire d'abandon, on change l'�tat du client pour
		// refl�ter ce fait.
		if (etat != Etat.PRIS_EN_CHARGE && horaireAbandon < referentielTemps.getTemps()) {
			etat = Etat.ABANDON;
		}
	}

	/**
	 * Teste si le client est toujours en train d'attendre l'envoi d'un taxi.
	 * 
	 * @return vrai si et seulement si le client est toujours en train
	 *         d'attendre l'envoi d'un taxi et faux sinon
	 */
	public boolean estEnAttenteEnvoiTaxi() {
		majEtat();

		return etat == Etat.ATTENTE_ENVOI_TAXI;
	}

	/**
	 * Signale au client l'envoi d'un taxi. Le client peut alors d�cider
	 * d'attendre un peu plus longtemps avant d'abandonner l'id�e de prendre un
	 * taxi.
	 */
	public void signalerEnvoiTaxi() {
		etat = Etat.ATTENTE_ARRIVEE_TAXI;

		// On met � jour l'horaire d'abandon
		horaireAbandon += genTempsAttenteSupplementaire.genererTempsAttente();
	}

	/**
	 * Teste si le client est toujours en train d'attendre l'arriv�e de son
	 * taxi.
	 * 
	 * @return vrai si et seulement si le client est toujours en train
	 *         d'attendre l'arriv�e de son taxi et faux sinon
	 */
	public boolean estEnAttenteArriveeTaxi() {
		majEtat();

		return etat == Etat.ATTENTE_ARRIVEE_TAXI;
	}

	/**
	 * Marque le client comme pris en charge par un taxi.
	 */
	public void prendreEnCharge() {
		etat = Etat.PRIS_EN_CHARGE;
	}

	/**
	 * Teste si le client a �t� pris en charge par un taxi.
	 * 
	 * @return vrai si et seulement si le client a �t� pris en charge par un
	 *         taxi et faux sinon
	 */
	public boolean estPrisEnCharge() {
		return etat == Etat.PRIS_EN_CHARGE;
	}

	/**
	 * Retourne le point de d�part du client dans le rep�re gradu� en m�tres
	 * ayant pour origine le centre de la ville.
	 * 
	 * @return le point de d�part du client dans le rep�re gradu� en m�tres
	 *         ayant pour origine le centre de la ville
	 */
	public Point2D.Double getDepart() {
		return depart;
	}

	/**
	 * Retourne la destination du client dans le rep�re gradu� en m�tres ayant
	 * pour origine le centre de la ville.
	 * 
	 * @return le point la destination du client dans le rep�re gradu� en m�tres
	 *         ayant pour origine le centre de la ville
	 */
	public Point2D.Double getArrivee() {
		return arrivee;
	}
}
