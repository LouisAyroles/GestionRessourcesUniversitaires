package bdd;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import utilisateurs.Administratif;
import utilisateurs.Enseignant;
import utilisateurs.Etudiant;
import utilisateurs.Groupe;
import utilisateurs.Technicien;
import utilisateurs.Utilisateur;

public class BaseDeDonnees implements Serializable {

	private static final long serialVersionUID = 1L;
	static String urlBDD = "jdbc:mysql://localhost:3306/BDD_gestion_ressources_universitaires";
	static String usernameBDD = "root";
	static String mdpBDD = "root";
	static Connection connexion = null;

	public void creationBDD() throws BaseDeDonneesException {
		try {

			Class.forName("com.mysql.cj.jdbc.Driver");

			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD);
	

			Statement s = connexion.createStatement();

			// Requetes qui vont permettre de construire la base de donnees
			
			//DROP DATABASE IF EXISTS BDD_gestion_ressources_universitaires
			String requeteSQL = "CREATE DATABASE BDD_gestion_ressources_universitaires;USE BDD_gestion_ressources_universitaires;";

			requeteSQL += "CREATE TABLE UTILISATEUR(USERNAME VARCHAR(100) NOT NULL, PASSWORD VARCHAR(100), NOM_USER VARCHAR(100), PRENOM_USER VARCHAR(100), TYPE_USER ENUM('ENSEIGNANT','ETUDIANT','TECHNICIEN','ADMINISTRATIF'), PRIMARY KEY (USERNAME));";
			requeteSQL += "CREATE TABLE MESSAGE( ID_MESSAGE INT NOT NULL AUTO_INCREMENT, CONTENU_MESSAGE VARCHAR(2048), PRIMARY KEY (ID_MESSAGE));";
			requeteSQL += "CREATE TABLE DISCUSSION( ID_DISCUSSION INT NOT NULL AUTO_INCREMENT, TITRE_DISCUSSION VARCHAR(100), PRIMARY KEY (ID_DISCUSSION));";
			requeteSQL += "CREATE TABLE GROUPE( ID_GROUPE INT NOT NULL AUTO_INCREMENT, NOM_GROUPE VARCHAR(100), PRIMARY KEY (ID_GROUPE));";
			requeteSQL += "CREATE TABLE APPARTENIR( ID_GROUPE INT NOT NULL, USERNAME VARCHAR(100) NOT NULL PRIMARY KEY (ID_GROUPE,USERNAME), FOREIGN KEY (ID_GROUPE) REFERENCES GROUPE(ID_GROUPE), FOREIGN KEY (USERNAME) REFERENCES UTILISATEUR(USERNAME));";
			requeteSQL += "CREATE TABLE ENVOYER( USERNAME VARCHAR(100) NOT NULL, ID_MESSAGE INT NOT NULL, DATE_ENVOI_MESSAGE DATETIME, PRIMARY KEY (USERNAME,ID_MESSAGE), FOREIGN KEY (USERNAME) REFERENCES UTILISATEUR(USERNAME), FOREIGN KEY (ID_MESSAGE) REFERENCES MESSAGE(ID_MESSAGE));";
			requeteSQL += "CREATE TABLE CONTIENT( ID_DISCUSSION INT NOT NULL, ID_MESSAGE INT NOT NULL, PRIMARY KEY (ID_DISCUSSION,ID_MESSAGE), FOREIGN KEY (ID_MESSAGE) REFERENCES MESSAGE(ID_MESSAGE), FOREIGN KEY (ID_DISCUSSION) REFERENCES DISCUSSION(ID_DISCUSSION));";
			requeteSQL += "CREATE TABLE CREER( ID_DISCUSSION INT NOT NULL, USERNAME VARCHAR(100) NOT NULL, PRIMARY KEY (ID_DISCUSSION,USERNAME), FOREIGN KEY (ID_DISCUSSION) REFERENCES DISCUSSION(ID_DISCUSSION), FOREIGN KEY (USERNAME) REFERENCES UTILISATEUR(USERNAME));";

			String[] requetes = requeteSQL.split(";");

			for (String r : requetes) {
				int res = s.executeUpdate(r + ";");
			}

			// ajoutGroupeBDD(new Groupe("Tous les utilisateurs"));

			connexion.close();

		} catch (Exception e) {
			e.printStackTrace();

			throw new BaseDeDonneesException();
		}
	}

	public Utilisateur connexion(String username, String password) {
		String usernameSearch = "";
		String passwordSearch = "";
		Utilisateur user = null;
		int idUser = 0;
		
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD);
			Statement s = connexion.createStatement();
			String requeteSQL = "SELECT USERNAME,PASSWORD,ID_USER FROM UTILISATEUR WHERE USERNAME='" + usernameSearch
					+ "';";
			ResultSet res = s.executeQuery(requeteSQL);
			if(res.next()) {
				usernameSearch = res.getString("USERNAME");
				passwordSearch = res.getString("PASSWORD");
				idUser = res.getInt("ID_USER");
			}
			
			if ( username.equals(usernameSearch) && password.equals(passwordSearch) && !password.equals("")) {
				user = usernameVersUtilisateur(username);
			}
		} catch ( SQLException e ) {
			
		} finally {
			if (connexion!= null) {
				try {
					connexion.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return user;
	}
	
	public Utilisateur usernameVersUtilisateur (String username) {
		Utilisateur user = null;
		String password = "";
		String nom = "";
		String prenom = "";
		String type = "";
		
		
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD);

			Statement statement = connexion.createStatement();
			ResultSet resultat = statement.executeQuery("SELECT * FROM UTILISATEUR WHERE USERNAME='" + username + "';");
			if (resultat.next()) {

				username = resultat.getString("USERNAME");
				password = resultat.getString("PASSWORD");
				nom = resultat.getString("NOM_USER");
				prenom = resultat.getString("PRENOM_USER");
				type = resultat.getString("TYPE_USER");
			}
			switch(type) {
			case "ETUDIANT":
				user = new Etudiant(nom,prenom,username,password);
				break;
			case "ENSEIGNANT":
				user = new Enseignant(nom, prenom, username, password);
				break;
			case "ADMINISTRATIF":
				user = new Administratif(nom, prenom, username, password);
				break;
			case "TECHNICIEN":
				user = new Technicien(nom, prenom, username, password);
				break;
			default:
				break;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public void ajoutUtilisateur(Utilisateur user) {
			
	}
}
