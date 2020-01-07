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
			
			String requeteSQL = "DROP DATABASE IF EXISTS BDD_gestion_ressources_universitaires;CREATE DATABASE BDD_gestion_ressources_universitaires;USE BDD_gestion_ressources_universitaires;";

			//table utilisateur
			requeteSQL += "CREATE TABLE Utilisateur(" + 
					"loginUser VARCHAR(100)," + 
					"passwordUser VARCHAR(50) NOT NULL," + 
					"nomUser VARCHAR(50) NOT NULL," + 
					"prenomUser VARCHAR(50) NOT NULL," + 
					"typeUser ENUM('ENSEIGNANT','ETUDIANT','TECHNICIEN','ADMINISTRATIF') NOT NULL," + 
					"PRIMARY KEY(loginUser));";
			
			//table message
			requeteSQL += "CREATE TABLE Message(" +
					 "idMessage INT AUTO_INCREMENT," +
					 "corpsMessage TEXT NOT NULL," +
					 "dateMessage INT NOT NULL," +
					 "loginUser VARCHAR(100) NOT NULL," +
					 "PRIMARY KEY(idMessage)," +
					 "FOREIGN KEY(loginUser) REFERENCES Utilisateur(loginUser));";
			
			//table GroupeUser
			requeteSQL += "CREATE TABLE GroupeUser(" + 
					"idGroupe INT AUTO_INCREMENT," + 
					"nomGroupe VARCHAR(50) NOT NULL," + 
					"PRIMARY KEY(idGroupe));";
			
			//table Fil
			requeteSQL += "CREATE TABLE Fil(" + 
					"idFil INT," + 
					"titre VARCHAR(50)," + 
					"PRIMARY KEY(idFil)," + 
					"FOREIGN KEY(idFil) REFERENCES Message(idMessage));";
			
			//table Appartenir qui lie un utilisateur a un groupe
			requeteSQL += "CREATE TABLE Appartenir(" + 
					"loginUser VARCHAR(100)," + 
					"idGroupe INT," + 
					"PRIMARY KEY(loginUser, idGroupe)," + 
					"FOREIGN KEY(loginUser) REFERENCES Utilisateur(loginUser)," + 
					"FOREIGN KEY(idGroupe) REFERENCES GroupeUser(idGroupe));";
			
			
			requeteSQL += "CREATE TABLE Correspondre(" + 
					"idGroupe INT," + 
					"idFil INT," + 
					"PRIMARY KEY(idGroupe, idFil)," + 
					"FOREIGN KEY(idGroupe) REFERENCES GroupeUser(idGroupe)," + 
					"FOREIGN KEY(idFil) REFERENCES Fil(idFil));";
			
			
			requeteSQL += "CREATE TABLE Contenir(" + 
					"idMessage INT," + 
					"idFil INT," + 
					"PRIMARY KEY(idMessage, idFil)," + 
					"FOREIGN KEY(idMessage) REFERENCES Message(idMessage)," + 
					"FOREIGN KEY(idFil) REFERENCES Fil(idFil));";

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
			String requeteSQL = "SELECT loginUser,passwordUser FROM Utilisateur WHERE loginUser='" + usernameSearch
					+ "';";
			ResultSet res = s.executeQuery(requeteSQL);
			if(res.next()) {
				usernameSearch = res.getString("loginUser");
				passwordSearch = res.getString("passwordUser");
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
			ResultSet resultat = statement.executeQuery("SELECT * FROM Utilisateur WHERE loginUser='" + username + "';");
			if (resultat.next()) {

				password = resultat.getString("passwordUser");
				nom = resultat.getString("nomUser");
				prenom = resultat.getString("prenomUser");
				type = resultat.getString("typeUser");
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
