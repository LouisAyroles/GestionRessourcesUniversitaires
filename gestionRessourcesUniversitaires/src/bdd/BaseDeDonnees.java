package bdd;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import utilisateurs.Groupe;

public class BaseDeDonnees implements Serializable {
	
	private static final long serialVersionUID = -6347298237211490750L;
	static String url = "jdbc:mysql://localhost:3306/BDD_gestion_ressources_universitaires";
	static String username = "root";
	static String mdp = "";
	static Connection connexion = null;
	
	public void creationBDD() throws BaseDeDonneesException {
		try {

			Class.forName("com.mysql.jdbc.Driver");

			try {
				connexion = DriverManager.getConnection(url, username, mdp);
			} catch (Exception e) {
				connexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/", username, mdp);
			}

			Statement s = connexion.createStatement();

			//Requetes qui vont permettre de construire la base de donnees 
			String req = "DROP DATABASE IF EXISTS BDD_gestion_ressources_universitaires;CREATE DATABASE BDD_gestion_ressources_universitaires;USE BDD_gestion_ressources_universitaires;";
			
			
			req += "CREATE TABLE UTILISATEUR( ID_USER INT NOT NULL AUTO_INCREMENT, USERNAME VARCHAR(100), PASSWORD VARCHAR(100), NOM_USER VARCHAR(100), PRENOM_USER VARCHAR(100), TYPE_USER ENUM('ENSEIGNANT','ETUDIANT','TECHNICIEN','ADMINISTRATIF'), PRIMARY KEY (ID_USER));";
			req += "CREATE TABLE MESSAGE( ID_MESSAGE INT NOT NULL AUTO_INCREMENT, CONTENU_MESSAGE VARCHAR(2048), PRIMARY KEY (ID_MESSAGE));";
			req += "CREATE TABLE DISCUSSION( ID_DISCUSSION INT NOT NULL AUTO_INCREMENT, TITRE_DISCUSSION VARCHAR(100), PRIMARY KEY (ID_DISCUSSION));";
			req += "CREATE TABLE GROUPE( ID_GROUPE INT NOT NULL AUTO_INCREMENT, NOM_GROUPE VARCHAR(100), PRIMARY KEY (ID_GROUPE));";
			req += "CREATE TABLE APPARTENIR( ID_GROUPE INT NOT NULL, ID_USER INT NOT NULL, PRIMARY KEY (ID_GROUPE,ID_USER), FOREIGN KEY (ID_GROUPE) REFERENCES GROUPE(ID_GROUPE), FOREIGN KEY (ID_USER) REFERENCES UTILISATEUR(ID_USER));";
			req += "CREATE TABLE ENVOYER( ID_USER INT NOT NULL, ID_MESSAGE INT NOT NULL, DATE_ENVOI_MESSAGE DATETIME, PRIMARY KEY (ID_USER,ID_MESSAGE), FOREIGN KEY (ID_USER) REFERENCES UTILISATEUR(ID_USER), FOREIGN KEY (ID_MESSAGE) REFERENCES MESSAGE(ID_MESSAGE));";
			req += "CREATE TABLE CONTIENT( ID_DISCUSSION INT NOT NULL, ID_MESSAGE INT NOT NULL, PRIMARY KEY (ID_DISCUSSION,ID_MESSAGE), FOREIGN KEY (ID_MESSAGE) REFERENCES MESSAGE(ID_MESSAGE), FOREIGN KEY (ID_DISCUSSION) REFERENCES DISCUSSION(ID_DISCUSSION));";
			req += "CREATE TABLE CREER( ID_DISCUSSION INT NOT NULL, ID_USER INT NOT NULL, PRIMARY KEY (ID_DISCUSSION,ID_UTILISATEUR), FOREIGN KEY (ID_DISCUSSION) REFERENCES DISCUSSION(ID_DISCUSSION), FOREIGN KEY (ID_USER) REFERENCES UTILISATEUR(ID_USER));";
			req += "CREATE TABLE DESTINE( ID_DISCUSSION INT NOT NULL, ID_USER INT NOT NULL, ID_GROUPE INT NOT NULL, PRIMARY KEY (ID_DISCUSSION,ID_USER,ID_GROUPE), FOREIGN KEY (ID_DISCUSSION) REFERENCES DISCUSSION(ID_DISCUSSION), FOREIGN KEY (ID_USER) REFERENCES UTILISATEUR(ID_USER), FOREIGN KEY (ID_GROUPE) REFERENCES GROUPE(ID_GROUPE));";

			String[] reqs = req.split(";");

			for (String r : reqs) {
				int res = s.executeUpdate(r + ";");
			}

			//ajoutGroupeBDD(new Groupe("Tous les utilisateurs"));

			connexion.close();

		} catch (Exception e) {
			e.printStackTrace();

			throw new BaseDeDonneesException();
		}
	}
}
	
