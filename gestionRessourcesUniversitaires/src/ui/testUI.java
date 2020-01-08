package ui;

import java.util.Date;

import javax.swing.JFrame;

import bdd.BaseDeDonnees;
import bdd.BaseDeDonneesException;
import messages.Discussion;
import messages.Message;
import utilisateurs.Etudiant;
import utilisateurs.Groupe;
import utilisateurs.Utilisateur;

public class testUI {
	
	private static void fenPrincipale() {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 600);
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private Discussion getDiscussion() throws BaseDeDonneesException {
		Utilisateur user = new Etudiant("Maxime", "Didier", "slm41a", "pswd");
		Utilisateur user2 = new Etudiant("Louis", "Siuol", "liu41a", "pswd");
		Message msg = new Message(user, "Bonjour, j'ai détecté un problème sur le chauffage gauche", new Date());
		BaseDeDonnees bdd = new BaseDeDonnees();
		Groupe g1 = new Groupe("Groupe 41A");
		
		msg = bdd.creerMessage(msg);
		bdd.addUserToGroup(g1, user);
		bdd.addUserToGroup(g1, user2);
		bdd.creerFil(msg, "Problème chauffage salle A4", g1);
		Discussion fil = bdd.getFilById(msg.getIdMessage());
		bdd.addFilToGroup(g1.getNom(), fil.getIdDiscussion());
		
		bdd.get
		return fil;
	}
	
	public static void main(String[] args) {
		fenPrincipale();
	}
}
