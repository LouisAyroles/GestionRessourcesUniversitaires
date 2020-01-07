package bdd;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import messages.Discussion;
import messages.Message;
import utilisateurs.Administratif;
import utilisateurs.Enseignant;
import utilisateurs.Etudiant;
import utilisateurs.Groupe;
import utilisateurs.Technicien;
import utilisateurs.Utilisateur;

public class BaseDeDonnees implements Serializable {
	
	private static String urlBDD = "jdbc:mysql://localhost:3306/BDD_gestion_ressources_universitaires?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC";
	private static String usernameBDD = "root";
	private static String mdpBDD = "caca";
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
		String requete = "DELETE FROM Utilisateur WHERE loginUser = '" + login + "'";
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
				int id = groupeId.getInt("idGroupe");
				requete = "SELECT * FROM GroupeUser WHERE idGroupe = " + id;
				ResultSet groupe = requeteExecuteQuerie(requete);
				
				if(groupe.next()) {
					String nomGroupe = groupe.getString("nomGroupe");
					listeReturn.add(new Groupe(nomGroupe, id));
				}//End if
			}//End while
		}//End try
		catch (SQLException e) {
			e.printStackTrace();
		}
		return listeReturn;
	}
	
	
	public int deleteUserFromGroup(Utilisateur user, Groupe group) {//Supprimer un user d'un groupe
		int idGroupe = group.getIdGroupe();
		String login = user.getUsername();
		String requete = "DELETE FROM Appartenir WHERE ";
		requete += "loginUser = '" + login + "' ";
		requete += "AND idGroupe = " + idGroupe;
		
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
		
		requete = "SELECT * FROM GroupeUser WHERE nomGroupe = '" + nom + "' ORDER BY 'idGroupe' DESC";
		ResultSet result = requeteExecuteQuerie(requete);
		
		try {
			if(result.next()) {
				int id = result.getInt("idGroupe");
				return new Groupe(nom, id);
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
				listeReturn.add(new Groupe(groupe.getString("nomGroupe"), groupe.getInt("idGroupe")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listeReturn;
	}
	
	public int deleteGroup(Groupe groupe) {
		/* Todo
		 * Supprimer tout ce qui est en lien ?
		 */
		String requete = "DELETE FROM GroupeUser WHERE idGroupe = " + groupe.getIdGroupe() + " AND nomGroupe = '" + groupe.getNom() + "'";
		return requeteExecuteUpdate(requete);
	}
		
	public int modifyGroup(Groupe groupe) {
		String requete = "UPDATE GroupeUser SET ";
		requete += "nomGroupe = '" + groupe.getNom() + "' ";
		requete += "WHERE idGroupe = '" + groupe.getIdGroupe() +"'";
		return requeteExecuteUpdate(requete);
	}
	
	public int addUserToGroup(Groupe groupe, Utilisateur user) {
		String requete ="INSERT INTO Appartenir VALUES(";
		requete += "'" + user.getUsername() + "', ";
		requete += groupe.getIdGroupe();
		requete += ")";
		return requeteExecuteUpdate(requete);
 	}
	
	public List<Utilisateur> getUsersOfGroup(Groupe groupe){
		
		//R�cup�ration de tous les Users qui correspondent au groupe
		int id = groupe.getIdGroupe();
		String requete = "SELECT * FROM Appartenir WHERE idGroupe =" + id;
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
	
	public Groupe getGroupeById(int id) {
		String requete = "SELECT * FROM GroupeUser WHERE idGroupe = " + id;
		ResultSet groupe = requeteExecuteQuerie(requete);
		
		try {
			if(groupe.next()) {
				return new Groupe(groupe.getString("nomGroupe"), 
						groupe.getInt("idGroupe"), 
						getUsersOfGroup(new Groupe(groupe.getString("nomGroupe"),
													groupe.getInt("idGroupe")))
						);
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
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
	
	
	//Attention, pour cette fonction le type du message est initialis� � null
	public Message getMessageById(int id) {
		String requete = "SELECT * FROM Message WHERE idMessage = " + id;
		ResultSet message = requeteExecuteQuerie(requete);
		
		try {
			if(message.next()) {
				int idMessage = message.getInt("idMessage");
				String corpsMessage = message.getString("corpsMessage");
				Date dateMessage = new Date(message.getLong("dateMessage"));
				String loginUser = message.getString("loginUser");
				Utilisateur user = usernameVersUtilisateur(loginUser);
				return new Message(user, dateMessage, corpsMessage, idMessage, getDiscussionOfMessage(idMessage).getIdDiscussion(), null);
			}
		} catch (SQLException | BaseDeDonneesException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//A donner : l'id du message pour lequel on veut la discussion qui lui est associ�e 
	public Discussion getDiscussionOfMessage(int id) {
		String requete = "SELECT * FROM Fil WHERE idFil = " + id; //idFil, car si c'est un message qui a cr�� un Fil, alors idFil = idMessage
		ResultSet discussion = requeteExecuteQuerie(requete);
		Discussion returnDiscussion = null;
		
		try {
			if(discussion.next()) {
				int idFil = discussion.getInt("idFil");
				returnDiscussion = new Discussion(discussion.getString("titre"), 
						usernameVersUtilisateur(getMessageById(idFil).getAuteur().getUsername()), //Je r�cup�re l'auteur du message, puis je r�cup�re l'username de cet auteur que j'envoie dans "getCreateurMessage" pour avoir l'auteur 
						getGroupeOfFil(idFil), 
						idFil);
			}
			else {//Ce message n'a pas cr�� de fil
				requete = "SELECT * FROM Contenir WHERE idMessage = " + id;
				discussion = requeteExecuteQuerie(requete);
				if(discussion.next()) {//S'il le message est contenu dans un fil de discussion
					int idFil = discussion.getInt("idFil");
					returnDiscussion = getFilById(idFil);
				}		
			}
		} catch (SQLException | BaseDeDonneesException e) {
			e.printStackTrace();
		}
		return returnDiscussion;
	}
	
	/********************************
	 * 								*
	 * 			  Fil		 		*
	 * 								*
	 ********************************/
	
	public int creerFil(Message message, String titre, Groupe groupe) {
		String requete = "INSERT INTO Fil VALUES(";
		requete += message.getIdMessage() + ", ";
		requete += "'" + titre +"'";
		requete += ")";
		TreeSet<Integer> returnInt = new TreeSet<>(); //TreeSet pour trier les deux nombres et renvoyer le plus petit. En cas d'erreurs, -1 sera renvoy�
		returnInt.add(requeteExecuteUpdate(requete));
		returnInt.add(addFilToGroup(groupe.getIdGroupe(), message.getIdMessage()));
		return returnInt.first();
	}
	
	public Discussion getFilById(int id) {
		String requete = "SELECT * FROM Fil WHERE idFil = " + id;
		ResultSet fil = requeteExecuteQuerie(requete);
		
		try {
			if(fil.next()) {
				return new Discussion(fil.getString("titre"), 
						getMessageById(id).getAuteur(), //On r�cup�re le message qui a cr�� le fil, puis on get son auteur 
						getGroupeOfFil(id), 
						id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public Groupe getGroupeOfFil(int id) {
		String requete = "SELECT * FROM Correspondre WHERE idFil = " + id;
		ResultSet groupe = requeteExecuteQuerie(requete);
		
		try {
			if(groupe.next()) {
				int idGroupe = groupe.getInt("idGroupe");
				return getGroupeById(idGroupe);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int addFilToGroup(int idGroupe, int idFil) {
		String requete = "INSERT INTO Correspondre VALUES(";
		requete += idGroupe + ", ";
		requete += idFil;
		requete += ")";
		return requeteExecuteUpdate(requete);
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
