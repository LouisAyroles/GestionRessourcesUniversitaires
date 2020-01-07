package tests;
import java.util.Date;

import bdd.BaseDeDonnees;
import bdd.BaseDeDonneesException;
import messages.Discussion;
import messages.Message;
import utilisateurs.Enseignant;
import utilisateurs.Etudiant;
import utilisateurs.Groupe;
import utilisateurs.Technicien;
import utilisateurs.Utilisateur;
public class TestBaseDeDonees {
	public static void main (String[] args) throws BaseDeDonneesException {
			BaseDeDonnees myBDD = new BaseDeDonnees();
			//myBDD.creationBDD();
			/*
			myBDD.createUser(new Etudiant("Leo","Etudiant","tdl41a","psswd"));
			myBDD.createUser(new Technicien("Louis","Technicien","tcl41a","psswd"));
			myBDD.createUser(new Enseignant("Michel","Enseignant","nsm41a","psswd"));
			myBDD.createUser(new Etudiant("Didier","Administratif","dmd41a","psswd"));
			myBDD.createUser(new Etudiant("Didier2","Administratif2","dmd41a2","psswd2"));
			myBDD.deleteUser("dmd41a2");
			myBDD.modifyUser(new Etudiant("LeoMODIF","Etudiant","tdl41a","psswd"));
			System.out.println(myBDD.connexion("tdl41a", "psswd"));*/
			Groupe g1 = myBDD.createGroup("Groupe 1");
			Groupe g2 = myBDD.createGroup("Groupe 2");
			Groupe g3 = myBDD.createGroup("Groupe 3");
			myBDD.deleteGroup(g3);
			g1.setNom("Groupe 1 MODIF");
			myBDD.modifyGroup(g1);
			Utilisateur u = myBDD.usernameVersUtilisateur("tdl41a");
			Utilisateur u2 = myBDD.usernameVersUtilisateur("tcl41a");
			myBDD.addUserToGroup(g1, u);
			myBDD.addUserToGroup(g2, u);
			myBDD.addUserToGroup(g1, u2);
			System.out.println(myBDD.getUsersOfGroup(g1));
			System.out.println(myBDD.getGroupsOfUser(u));
			myBDD.deleteUserFromGroup(u, g2);
			Message m = myBDD.creerMessage(new Message(u, "Test message corps", new Date()));
			System.out.println(myBDD.getAllUser());
			System.out.println(myBDD.getAllGroup());
			//A tester
			myBDD.creerFil(m, "Test Fil", g1);
			Discussion d = myBDD.getFilById(m.getIdMessage());
			System.out.println(myBDD.getGroupeById(g1.getIdGroupe()));
			System.out.println(myBDD.getGroupeOfFil(d.getIdDiscussion()));
			System.out.println(myBDD.getMessageById(m.getIdMessage()));
			System.out.println(myBDD.getDiscussionOfMessage(m.getIdMessage()));
	}
}
