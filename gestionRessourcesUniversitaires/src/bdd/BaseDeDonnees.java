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
					 "FOREIGN KEY(loginUser) REFERENCES Utilisateur(loginUser) ON DELETE CASCADE);";
			
			//table GroupeUser
			requeteSQL += "CREATE TABLE GroupeUser(" + 
					"nomGroupe VARCHAR(50) NOT NULL," + 
					"PRIMARY KEY(nomGroupe));";
			
			//table Fil
			requeteSQL += "CREATE TABLE Fil(" + 
					"idFil INT," + 
					"titre VARCHAR(50)," + 
					"PRIMARY KEY(idFil)," + 
					"FOREIGN KEY(idFil) REFERENCES Message(idMessage)ON DELETE CASCADE);";
			
			//table Appartenir qui lie un utilisateur a un groupe
			requeteSQL += "CREATE TABLE Appartenir(" + 
					"loginUser VARCHAR(100)," + 
					"nomGroupe VARCHAR(50) NOT NULL," + 
					"PRIMARY KEY(loginUser, nomGroupe)," + 
					"FOREIGN KEY(loginUser) REFERENCES Utilisateur(loginUser)," + 
					"FOREIGN KEY(nomGroupe) REFERENCES GroupeUser(nomGroupe)ON DELETE CASCADE);";
			
			
			requeteSQL += "CREATE TABLE Correspondre(" + 
					"nomGroupe VARCHAR(50) NOT NULL," + 
					"idFil INT," + 
					"PRIMARY KEY(nomGroupe, idFil)," + 
					"FOREIGN KEY(nomGroupe) REFERENCES GroupeUser(nomGroupe)," + 
					"FOREIGN KEY(idFil) REFERENCES Fil(idFil)ON DELETE CASCADE);";
			
			
			requeteSQL += "CREATE TABLE Contenir(" + 
					"idMessage  INT," + 
					"idFil INT," + 
					"PRIMARY KEY(idMessage, idFil)," + 
					"FOREIGN KEY(idMessage) REFERENCES Message(idMessage)," + 
					"FOREIGN KEY(idFil) REFERENCES Fil(idFil)ON DELETE CASCADE);";

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
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
	}
	
	
	/********************************
	 * 								*
	 * 			Utilisateur 		*
	 * 								*
	 ********************************/
	
	public Utilisateur usernameVersUtilisateur(String login) throws BaseDeDonneesException {
			try {
				connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
				Statement statement = connexion.createStatement(); //Création de la future requête
				
				String requete = "SELECT * FROM Utilisateur WHERE loginUser = '" + login + "'";
				ResultSet utilisateur = statement.executeQuery(requete);
				
				if(utilisateur.next()) {
					//Récupération des champs
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
				utilisateur.close();
				statement.close();
				connexion.close();
			} //End try
			catch (SQLException e) {
				e.printStackTrace();
			}
			finally {
				if (connexion != null)
					try {
						/* Fermeture de la connexion */
						connexion.close();
					} catch (SQLException ignore) {
						/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
					}
			}
		return null; //Si null c'est qu'il n'y a aucun résultat
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
		//Récupérer tous les groupes et supprimer dans appartenir
		String requete = "DELETE FROM Utilisateur WHERE loginUser = '" + login + "'";
		return requeteExecuteUpdate(requete);
	}
	
	//Modifier un utilisateur. Ca remplace tous les champs d'un user par défaut
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
		List<Utilisateur> listeReturn = new ArrayList<>();
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			String requete = "SELECT * FROM Utilisateur";
			ResultSet utilisateur = statement.executeQuery(requete);
			
			while(utilisateur.next()) {
					//Récupération des champs
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
			utilisateur.close();
			statement.close();
			connexion.close();
		}//End try 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return listeReturn;
	}
	
	public int connexion(String login, String password) {
		int returnRes = -1;
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			String requete = "SELECT * FROM Utilisateur";
			requete += " WHERE loginUser = '" + login + "'";
			requete += " AND passwordUser = '" + password +"'";
			ResultSet result = statement.executeQuery(requete);
			
			if(result.next()) { //S'il y en a un, donc ça veut dire que c'est le bon login/mdp (Login unique donc combinaison des deux unique)
				returnRes = 1;
			}
			result.close();
			statement.close();
			connexion.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return returnRes;
	}
	
	
	public List<Groupe> getGroupsOfUser(Utilisateur user){

		List<Groupe> listeReturn = new ArrayList<>();
		
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			String login = user.getUsername();
			String requete = "SELECT * FROM Appartenir WHERE loginUser = '" + login +"'";
			ResultSet groupeId = statement.executeQuery(requete);
			Statement statement2 = connexion.createStatement(); //Création de la future requête

			while(groupeId.next()) {
				String nom = groupeId.getString("nomGroupe");
				requete = "SELECT * FROM GroupeUser WHERE nomGroupe = '" + nom + "';" ;
				ResultSet groupe = statement2.executeQuery(requete);
				
				if(groupe.next()) {
					String nomGroupe = groupe.getString("nomGroupe");
					listeReturn.add(new Groupe(nomGroupe));
				}//End if
				groupe.close();
			}//End while
			groupeId.close();
			statement2.close();
			statement.close();
			connexion.close();
		}//End try
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
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
		

		Groupe groupeReturn = null;
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			requete = "SELECT * FROM GroupeUser WHERE nomGroupe = '" + nom + "';";
			ResultSet result = statement.executeQuery(requete);
			
			if(result.next()) {
				groupeReturn =  new Groupe(nom);
			}
			else { //Si aucun r�sultat
				result.close();
				statement.close();
				connexion.close();
				throw new BaseDeDonneesException("Aucun r�sultat cr�ation de groupe");
			}
			result.close();
			statement.close();
			connexion.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return groupeReturn; //Si aucun r�sultat
	}
	
	
	public List<Groupe> getAllGroup(){

		List<Groupe> listeReturn = new ArrayList<>();
		
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			String requete = "SELECT * FROM GroupeUser";
			ResultSet groupe = statement.executeQuery(requete);
			
			while(groupe.next()) {
				listeReturn.add(new Groupe(groupe.getString("nomGroupe")));
			}
			groupe.close();
			statement.close();
			connexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return listeReturn;
	}
	
	public Groupe getGroup(String nomGroupe){

        Groupe retour = null;
        
        try {
        	connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
        	Statement statement = connexion.createStatement(); //Création de la future requête
            String requete = "SELECT * FROM GroupeUser WHERE nomGroupe='" + nomGroupe + "';";
            ResultSet groupe = statement.executeQuery(requete);
            
            if(groupe.next()) {
                retour = new Groupe(groupe.getString("nomGroupe"));
            }
            groupe.close();
            statement.close();
            connexion.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
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
		
	public int addUserToGroup(Groupe groupe, Utilisateur user) {
		String requete ="INSERT INTO Appartenir VALUES(";
		requete += "'" + user.getUsername() + "', ";
		requete += "'" + groupe.getNom() + "'";
		requete += ")";
		return requeteExecuteUpdate(requete);
 	}
	
	
	public List<Discussion> getFilOfGroupe(Groupe groupe) {

		List<Discussion> listeReturn = new ArrayList<>();
		
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			String requete = "SELECT * FROM Correspondre WHERE nomGroupe = '" + groupe.getNom() + "'";
			ResultSet discussions = statement.executeQuery(requete);
			Statement statement2 = connexion.createStatement(); //Création de la future requête

			while(discussions.next()) {//Itération de tous les résultats
				requete = "SELECT * FROM Fil WHERE idFil = " + discussions.getInt("idFil");
				ResultSet currentFil = statement2.executeQuery(requete);
				
				if(currentFil.next()) {//Fil en cours
					int idFil = currentFil.getInt("idFil");
					listeReturn.add(new Discussion(currentFil.getString("titre"), 
							getCreateurMessage(idFil), 
							groupe, 
							idFil,
							getMessageById(idFil)));
				}//End if
				currentFil.close();

			}//End while
			discussions.close();
			statement.close();
			statement2.close();
			connexion.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return listeReturn;
	}
	
	public List<Utilisateur> getUsersOfGroup(Groupe groupe){
		
		List<Utilisateur> listeReturn = new ArrayList<Utilisateur>();
		
		
		//Traitement des users
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			
			//R�cup�ration de tous les Users qui correspondent au groupe
			String requete = "SELECT * FROM Appartenir WHERE nomGroupe ='" + groupe.getNom() + "';";
			ResultSet loginUsers = statement.executeQuery(requete);
			Statement statement2 = connexion.createStatement(); //Création de la future requête

			while(loginUsers.next()) { //On parcours la liste des loginUser qu'on a r�cup
				
				String login = loginUsers.getString("loginUser");
				
				//R�cup�ration de l'user en cours
				requete = "SELECT * FROM Utilisateur WHERE loginUser = '" + login +"'";

				ResultSet currentUser = statement2.executeQuery(requete);
				
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
				currentUser.close();
			}//End while
			loginUsers.close();
			statement2.close();
			statement.close();
			connexion.close();
		}//End try
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return listeReturn;
	}
	
	public Groupe getGroupeById(Groupe grp) {
		Groupe returnGroupe = null;
		
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			String requete = "SELECT * FROM GroupeUser WHERE nomGroupe = '" + grp.getNom() + "'";
			ResultSet groupe = statement.executeQuery(requete);
			
			if(groupe.next()) {
				String nomGroupe = groupe.getString("nomGroupe");
				List<Utilisateur> listUser = getUsersOfGroup(new Groupe(groupe.getString("nomGroupe")));
				returnGroupe = new Groupe(nomGroupe, listUser);
			}
			groupe.close();
			statement.close();
			connexion.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return returnGroupe;
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
		
		//On récupère l'id pour renvoyer le Message correspondant
		requete = "SELECT * FROM Message WHERE ";
		requete += "corpsMessage = '" + message.getMessage() + "' ";
		requete += "AND dateMessage = " + message.getDate().getTime() + " ";
		requete += "AND loginUser = '" + message.getAuteur().getUsername() + "' ";
		requete += "ORDER BY idMessage DESC";
		
		Message messageReturn = null;
		
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			ResultSet messageRes = statement.executeQuery(requete);

			if(messageRes.next()) {
				int idMess = messageRes.getInt("idMessage");
				messageReturn = new Message(
						message.getAuteur(),
						message.getMessage(),
						message.getDate(),
						idMess);
			}
			messageRes.close();
			statement.close();
			connexion.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return messageReturn;
	}
	
	
	//Attention, pour cette fonction le type du message est initialisé à null
	public Message getMessageById(int id) {

		Message messageReturn = null;
		
		try { 
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			String requete = "SELECT * FROM Message WHERE idMessage = " + id;
			ResultSet message = statement.executeQuery(requete);
			
			if(message.next()) {
				int idMessage = message.getInt("idMessage");
				String corpsMessage = message.getString("corpsMessage");
				Date dateMessage = new Date(message.getLong("dateMessage"));
				String loginUser = message.getString("loginUser");
				Utilisateur user = usernameVersUtilisateur(loginUser);

				messageReturn =  new Message(user, dateMessage, corpsMessage, idMessage, getDiscussionOfMessage(idMessage).getIdDiscussion(), null);
			}
			message.close();
			statement.close();
			connexion.close();
		} 
		catch (SQLException | BaseDeDonneesException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return messageReturn;
	}
	
	//A donner : l'id du message pour lequel on veut la discussion qui lui est associée 
	public Discussion getDiscussionOfMessage(int id) {
		Discussion returnDiscussion = null;
		
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			String requete = "SELECT * FROM Fil WHERE idFil = " + id; //idFil, car si c'est un message qui a créé un Fil, alors idFil = idMessage
			ResultSet discussion = statement.executeQuery(requete);
			Statement statement2 = connexion.createStatement(); //Création de la future requête

			if(discussion.next()) {
				int idFil = discussion.getInt("idFil");
				returnDiscussion = new Discussion(discussion.getString("titre"), 
						usernameVersUtilisateur(getCreateurMessage(idFil).getUsername()),
						getGroupeOfFil(idFil), 
						idFil);

			}
			else {//Ce message n'a pas créé de fil
				requete = "SELECT * FROM Contenir WHERE idMessage = " + id;
				discussion = statement2.executeQuery(requete);
				if(discussion.next()) {//S'il le message est contenu dans un fil de discussion
					int idFil = discussion.getInt("idFil");
					returnDiscussion = getFilById(idFil);
				}
			}
			discussion.close();
			statement2.close();
			statement.close();
			connexion.close();
		} 
		catch (SQLException | BaseDeDonneesException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return returnDiscussion;
	}
	
	
	public Utilisateur getCreateurMessage(int id) {

		Utilisateur userReturn = null;
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			String requete = "SELECT * FROM Message WHERE idMessage = " + id;
			ResultSet message = statement.executeQuery(requete);
			
			if(message.next()) {
				String loginUser = message.getString("loginUser");
				userReturn =  usernameVersUtilisateur(loginUser);
			}
			message.close();
			statement.close();
			connexion.close();
		} 
		catch (SQLException | BaseDeDonneesException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return userReturn;
	}
	
	/********************************
	 * 								*
	 * 			  Fil		 		*
	 * 								*
	 ********************************/
	
    public int creerFil(Message message, String titre, Groupe groupeSource, Groupe groupeDest) {
        String requete = "INSERT INTO Fil VALUES(";
        requete += message.getIdMessage() + ", ";
        requete += "'" + titre +"'";
        requete += ")";
        TreeSet<Integer> returnInt = new TreeSet<>(); //TreeSet pour trier les deux nombres et renvoyer le plus petit. En cas d'erreurs, -1 sera renvoyÃ©
        returnInt.add(requeteExecuteUpdate(requete));
        returnInt.add(addFilToGroup(groupeSource.getNom(), message.getIdMessage()));
        returnInt.add(addFilToGroup(groupeDest.getNom(), message.getIdMessage()));
        return returnInt.first();
    }
	
	public Discussion getFilById(int id) {

		Discussion returnFil = null;
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			String requete = "SELECT * FROM Fil WHERE idFil = " + id;
			ResultSet fil = statement.executeQuery(requete);
			
			if(fil.next()) {
				String titre = fil.getString("titre");
				returnFil = new Discussion(titre, 
						getMessageById(id).getAuteur(), //On récupère le message qui a créé le fil, puis on get son auteur 
						getGroupeOfFil(id), 
						id);
			}
			fil.close();
			statement.close();
			connexion.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return returnFil;
	}
	
	
	public Groupe getGroupeOfFil(int id) {
		Groupe groupeReturn = null;
		
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			String requete = "SELECT * FROM Correspondre WHERE idFil = " + id;
			ResultSet groupe = statement.executeQuery(requete);
			
			if(groupe.next()) {
				String nom = groupe.getString("nomGroupe");
				groupeReturn = getGroupeById(new Groupe(nom));
			}
			groupe.close();
			statement.close();
			connexion.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return groupeReturn;
	}
	
	public int addFilToGroup(String nomGroupe, int idFil) {
		String requete = "INSERT INTO Correspondre VALUES(";
		requete += "'" + nomGroupe + "', ";
		requete += idFil;
		requete += ")";
		return requeteExecuteUpdate(requete);
	}
	
	public int addMessageToFil(int idMessage, int idFil) {
		String requete = "INSERT INTO Contenir VALUES(";
		requete += idMessage + ", ";
		requete += idFil;
		requete += ")";
		
		return requeteExecuteUpdate(requete);
	}
	
	public List<Message> getMessageOfFil(int id){
		List<Message> listReturn = new ArrayList<>();
		listReturn.add(getMessageById(id)); //On ajoute le message qui a créé le fil
		
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			String requete = "SELECT * FROM Contenir WHERE idFil = " + id;
			ResultSet message = statement.executeQuery(requete);
			
			while(message.next()) {
				listReturn.add(getMessageById(message.getInt("idMessage")));
			}
			message.close();
			statement.close();
			connexion.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return listReturn;
	}
	
	/********************************
	 * 								*
	 * 			Générique	 		*
	 * 								*
	 ********************************/
	
	//Envoi d'une requête de type "Update/Delete/Insert" à la BDD
	private int requeteExecuteUpdate(String requete) {
		try {
			connexion = DriverManager.getConnection(urlBDD, usernameBDD, mdpBDD); //Co à la BDD
			Statement statement = connexion.createStatement(); //Création de la future requête
			
			int res = statement.executeUpdate(requete); //Exécution
			statement.close(); //Fermeture
			connexion.close();
			
			return res;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally {
			if (connexion != null)
				try {
					/* Fermeture de la connexion */
					connexion.close();
				} catch (SQLException ignore) {
					/* Si une erreur survient lors de la fermeture, il suffit de l'ignorer. */
				}
		}
		return -1;
	}
	
	/*TODO
	 * ON DELETE CASCADE pour les clés étrangères
	 */
}
