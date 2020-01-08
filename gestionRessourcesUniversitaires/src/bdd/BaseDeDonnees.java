package bdd;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import messages.Message;
import utilisateurs.Administratif;
import utilisateurs.Enseignant;
import utilisateurs.Etudiant;
import utilisateurs.Groupe;
import utilisateurs.Technicien;
import utilisateurs.Utilisateur;

public class BaseDeDonnees implements Serializable {
	
	private static String urlBDD = "jdbc:mysql://localhost:3306/BDD_gestion_ressources_universitaires";
	private static String usernameBDD = "root";
	private static String mdpBDD = "root";
	private static Connection connexion = null;
	
	private static final long serialVersionUID = 6429907995625047492L;
	
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
					 "dateMessage BIGINT NOT NULL," +
					 "loginUser VARCHAR(100) NOT NULL," +
					 "PRIMARY KEY(idMessage)," +
					 "FOREIGN KEY(loginUser) REFERENCES Utilisateur(loginUser));";
			
			//table GroupeUser
			requeteSQL += "CREATE TABLE GroupeUser(" + 
					"nomGroupe VARCHAR(50) NOT NULL," + 
					"PRIMARY KEY(nomGroupe));";
			
			//table Fil
			requeteSQL += "CREATE TABLE Fil(" + 
					"idFil INT," + 
					"titre VARCHAR(50)," + 
					"PRIMARY KEY(idFil)," + 
					"FOREIGN KEY(idFil) REFERENCES Message(idMessage));";
			
			//table Appartenir qui lie un utilisateur a un groupe
			requeteSQL += "CREATE TABLE Appartenir(" + 
					"loginUser VARCHAR(100)," + 
					"nomGroupe VARCHAR(50) NOT NULL," + 
					"PRIMARY KEY(loginUser, nomGroupe)," + 
					"FOREIGN KEY(loginUser) REFERENCES Utilisateur(loginUser)," + 
					"FOREIGN KEY(nomGroupe) REFERENCES GroupeUser(nomGroupe));";
			
			
			requeteSQL += "CREATE TABLE Correspondre(" + 
					"nomGroupe VARCHAR(50) NOT NULL," + 
					"idFil INT," + 
					"PRIMARY KEY(nomGroupe, idFil)," + 
					"FOREIGN KEY(nomGroupe) REFERENCES GroupeUser(nomGroupe)," + 
					"FOREIGN KEY(idFil) REFERENCES Fil(idFil));";
			
			
			requeteSQL += "CREATE TABLE Contenir(" + 
					"idMessage  INT," + 
					"idFil INT," + 
					"PRIMARY KEY(idMessage, idFil)," + 
					"FOREIGN KEY(idMessage) REFERENCES Message(idMessage)," + 
					"FOREIGN KEY(idFil) REFERENCES Fil(idFil));";

			String[] requetes = requeteSQL.split(";");

			for (String r : requetes) {
				s.executeUpdate(r + ";");
			}

			// ajoutGroupeBDD(new Groupe("Tous les utilisateurs"));

			connexion.close();

		} catch (Exception e) {
			e.printStackTrace();

			throw new BaseDeDonneesException();
		}
	}
	
	
	/********************************
	 * 								*
	 * 			Utilisateur 		*
	 * 								*
	 ********************************/
	
	public Utilisateur usernameVersUtilisateur(String login) throws BaseDeDonneesException {
			String requete = "SELECT * FROM Utilisateur WHERE loginUser = '" + login + "'";
			ResultSet utilisateur = requeteExecuteQuerie(requete);

			try {
				if(utilisateur.next()) {
					//R�cup�ration des champs
					String loginUser = utilisateur.getString("loginUser");
					String password = utilisateur.getString("passwordUser");
					String nom = utilisateur.getString("nomUser");
					String prenom = utilisateur.getString("prenomUser");
					String type = utilisateur.getString("typeUser");
					
					switch(type) {
						case "ETUDIANT":
							return new Etudiant(nom, prenom, loginUser, password);
							
						case "ENSEIGNANT":
							return new Enseignant(nom, prenom, loginUser, password);
							
						case "ADMINISTRATIF":
							return new Administratif(nom, prenom, loginUser, password);
							
						case "TECHNICIEN":
							return new Technicien(nom, prenom, loginUser, password);
							
						default:
							break;
					}//End switch
				}//End if
				else {
					return null;
				}
			} //End try
			catch (SQLException e) {
				e.printStackTrace();
			}
		return null; //Si null c'est qu'il n'y a aucun r�sultat
	}
	
	
	public int createUser(Utilisateur user) throws BaseDeDonneesException {
		if(usernameVersUtilisateur(user.getUsername()) != null)
			return -1;
		String requete = "INSERT INTO Utilisateur VALUES (";
		requete += "'" + user.getUsername() + "',";
		requete += "'" + user.getPassword() + "',";
		requete += "'" + user.getNom() + "',";
		requete += "'" + user.getPrenom() + "',";
		requete += "'" + user.getType() + "'";
		requete += ")";
		return requeteExecuteUpdate(requete);
	}
	
	public int deleteUser(String login) {
        //R�cup�rer tous les groupes et supprimer dans appartenir
        String requete = "DELETE FROM Appartenir WHERE loginUser ='" + login + "';";
        requeteExecuteUpdate(requete);
        requete = "DELETE FROM Message WHERE loginUser ='" + login + "';";
        requeteExecuteUpdate(requete);
        requete = "DELETE FROM Utilisateur WHERE loginUser = '" + login + "'";
        return requeteExecuteUpdate(requete);
    }
	
	//Modifier un utilisateur. Ca remplace tous les champs d'un user par d�faut
	public int modifyUser(Utilisateur user) {
			String requete = "UPDATE Utilisateur SET ";
			requete += "loginUser = '" + user.getUsername() + "', ";
			requete += "passwordUser = '" + user.getPassword() + "', ";
			requete += "nomUser = '" + user.getNom() + "', ";
			requete += "prenomUser = '" + user.getPrenom() + "',";
			requete += "typeUser = '" + user.getType() +"' ";
			requete += "WHERE loginUser = '" + user.getUsername() + "'";
		return requeteExecuteUpdate(requete);
	}
	
	public List<Utilisateur> getAllUser(){
		String requete = "SELECT * FROM Utilisateur";
		List<Utilisateur> listeReturn = new ArrayList<>();
		ResultSet utilisateur = requeteExecuteQuerie(requete);
		
		try {
			while(utilisateur.next()) {
					//R�cup�ration des champs
					String loginUser = utilisateur.getString("loginUser");
					String password = utilisateur.getString("passwordUser");
					String nom = utilisateur.getString("nomUser");
					String prenom = utilisateur.getString("prenomUser");
					String type = utilisateur.getString("typeUser");
					switch(type) {
						case "ETUDIANT":
							listeReturn.add(new Etudiant(nom, prenom, loginUser, password));
							break;
							
						case "ENSEIGNANT":
							listeReturn.add(new Enseignant(nom, prenom, loginUser, password));
							break;
							
						case "ADMINISTRATIF":
							listeReturn.add(new Administratif(nom, prenom, loginUser, password));
							break;
							
						case "TECHNICIEN":
							listeReturn.add(new Technicien(nom, prenom, loginUser, password));
							break;
							
						default:
							break;
					}//End switch
			}//End while
		}//End try 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return listeReturn;
	}
	
	public int connexion(String login, String password) {
		String requete = "SELECT * FROM Utilisateur";
		requete += " WHERE loginUser = '" + login + "'";
		requete += " AND passwordUser = '" + password +"'";
		ResultSet result = requeteExecuteQuerie(requete);
		
		try {
			if(result.next()) //S'il y en a un, donc �a veut dire que c'est le bon login/mdp (Login unique donc combinaison des deux unique)
				return 1;
			else
				return -1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
	public List<Groupe> getGroupsOfUser(Utilisateur user){
		String login = user.getUsername();
		String requete = "SELECT * FROM Appartenir WHERE loginUser = '" + login +"'";
		ResultSet groupeId = requeteExecuteQuerie(requete);
		List<Groupe> listeReturn = new ArrayList<>();
		
		try {
			while(groupeId.next()) {
				String nom = groupeId.getString("nomGroupe");
				requete = "SELECT * FROM GroupeUser WHERE nomGroupe = '" + nom + "';" ;
				ResultSet groupe = requeteExecuteQuerie(requete);
				
				if(groupe.next()) {
					String nomGroupe = groupe.getString("nomGroupe");
					listeReturn.add(new Groupe(nomGroupe));
				}//End if
			}//End while
		}//End try
		catch (SQLException e) {
			e.printStackTrace();
		}
		return listeReturn;
	}
	
	
	public int deleteUserFromGroup(Utilisateur user, Groupe group) {//Supprimer un user d'un groupe
		String nomGroupe = group.getNom();
		String login = user.getUsername();
		String requete = "DELETE FROM Appartenir WHERE ";
		requete += "loginUser = '" + login + "' ";
		requete += "AND nomGroupe = '" + nomGroupe + "';";
		
		return requeteExecuteUpdate(requete);
	}
	
	/********************************
	 * 								*
	 * 			Groupe		 		*
	 * 								*
	 ********************************/
	
	public Groupe createGroup(String nom) throws BaseDeDonneesException {
		String requete = "INSERT INTO GroupeUser(nomGroupe) VALUES('" + nom +"')";
		int res = requeteExecuteUpdate(requete);
		
		if(res < 0)
			throw new BaseDeDonneesException("Erreur cr�ation groupe");
		
		requete = "SELECT * FROM GroupeUser WHERE nomGroupe = '" + nom + "';";
		ResultSet result = requeteExecuteQuerie(requete);
		
		try {
			if(result.next()) {
				return new Groupe(nom);
			}
			else { //Si aucun r�sultat
				throw new BaseDeDonneesException("Aucun r�sultat cr�ation de groupe");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null; //Si aucun r�sultat
	}
	
	
	public List<Groupe> getAllGroup(){
		String requete = "SELECT * FROM GroupeUser";
		ResultSet groupe = requeteExecuteQuerie(requete);
		List<Groupe> listeReturn = new ArrayList<>();
		
		try {
			while(groupe.next()) {
				listeReturn.add(new Groupe(groupe.getString("nomGroupe")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listeReturn;
	}
	
	public Groupe getGroup(String nomGroupe){
        String requete = "SELECT * FROM GroupeUser WHERE nomGroupe='" + nomGroupe + "';";
        ResultSet groupe = requeteExecuteQuerie(requete);
        Groupe retour = null;
        
        try {
            if(groupe.next()) {
                retour = new Groupe(groupe.getString("nomGroupe"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retour;
    }
	
	public int deleteGroup(Groupe groupe) {
		/* Todo
		 * Supprimer tout ce qui est en lien ?
		 */
		String requete = "DELETE FROM GroupeUser WHERE nomGroupe = '" + groupe.getNom() + "';";
		return requeteExecuteUpdate(requete);
	}
		
	public int modifyGroup(Groupe groupe) {
		String requete = "UPDATE GroupeUser SET ";
		requete += "nomGroupe = '" + groupe.getNom() + "';";
		return requeteExecuteUpdate(requete);
	}
	
	public int addUserToGroup(Groupe groupe, Utilisateur user) {
		String requete ="INSERT INTO Appartenir VALUES(";
		requete += "'" + user.getUsername() + "', ";
		requete += groupe.getNom();
		requete += ")";
		return requeteExecuteUpdate(requete);
 	}
	
	public List<Utilisateur> getUsersOfGroup(Groupe groupe){
		
		//R�cup�ration de tous les Users qui correspondent au groupe
		String requete = "SELECT * FROM Appartenir WHERE nomGroupe ='" + groupe.getNom() + "';";
		ResultSet loginUsers = requeteExecuteQuerie(requete);
		List<Utilisateur> listeReturn = new ArrayList<Utilisateur>();
		
		
		//Traitement des users
		try {
			while(loginUsers.next()) { //On parcours la liste des loginUser qu'on a r�cup
				
				String login = loginUsers.getString("loginUser");
				
				//R�cup�ration de l'user en cours
				requete = "SELECT * FROM Utilisateur WHERE loginUser = '" + login +"'";
				ResultSet currentUser = requeteExecuteQuerie(requete);
				
				if(currentUser.next()) {
					String nom = currentUser.getString("nomUser");
					String prenom = currentUser.getString("prenomUser");
					String password = currentUser.getString("passwordUser");
					String type = currentUser.getString("typeUser");
					
					switch(type) {
						case "ETUDIANT":
							listeReturn.add(new Etudiant(nom, prenom, nom, password));
							break;
						case "ENSEIGNANT":
							listeReturn.add(new Enseignant(nom, prenom, nom, password));
							break;
						case "ADMINISTRATIF":
							listeReturn.add(new Administratif(nom, prenom, nom, password));
							break;
						case "TECHNICIEN":
							listeReturn.add(new Technicien(nom, prenom, nom, password));
							break;
						default:
							break;
					}//End switch
				}//End if
			}//End while
		}//End try
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return listeReturn;
	}
	
	/********************************
	 * 								*
	 * 			Message		 		*
	 * 								*
	 * @throws BaseDeDonneesException 
	 ********************************/
	
	public Message creerMessage(Message message) throws BaseDeDonneesException {
		
		//Insert dans la table Message
		String requete = "INSERT INTO Message(corpsMessage, dateMessage, loginUser) VALUES(";
		requete += "'" + message.getMessage() + "', ";
		requete += message.getDate().getTime() + ", ";
		requete += "'" + message.getAuteur().getUsername() +"'";
		requete += ")";
		
		int res = requeteExecuteUpdate(requete);
		if(res < 0)
			throw new BaseDeDonneesException("Erreur creer message");
		
		//On r�cup�re l'id pour renvoyer le Message correspondant
		requete = "SELECT * FROM Message WHERE ";
		requete += "corpsMessage = '" + message.getMessage() + "' ";
		requete += "AND dateMessage = " + message.getDate().getTime() + " ";
		requete += "AND loginUser = '" + message.getAuteur().getUsername() + "' ";
		requete += "ORDER BY idMessage DESC";
		ResultSet messageRes = requeteExecuteQuerie(requete);
		
		try {
			if(messageRes.next()) {
				return new Message(
						message.getAuteur(),
						message.getMessage(),
						message.getDate(),
						messageRes.getInt("idMessage"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	/********************************
	 * 								*
	 * 			G�n�rique	 		*
	 * 								*
	 ********************************/
	
	//Envoi d'une requ�te de type "Update/Delete/Insert" � la BDD
	private int requeteExecuteUpdate(String requete) {
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co � la BDD
			Statement statement = connexion.createStatement(); //Cr�ation de la future requ�te
			
			int res = statement.executeUpdate(requete); //Ex�cution
			statement.close(); //Fermeture
			
			return res;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	
	//Envoi d'une requ�te de type "Select" � la BDD
	private ResultSet requeteExecuteQuerie(String requete) {
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co � la BDD
			Statement statement = connexion.createStatement(); //Cr�ation de la future requ�te
			
			ResultSet resultSet = statement.executeQuery(requete);
			return resultSet;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/*TODO
	 * Supprimer groupe : supprimer appartenir aussi
	 * Supprimer utilisateur : supprimer appartenir
	 * M�thode supprimer message d'un user
	 * R�cup�rer tous les users de la BDD
	 * R�cup�rer tous les groupes de la BDD
	 */
}
