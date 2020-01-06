package tests;
import java.util.ArrayList;
import java.util.List;

import messages.*;
import utilisateurs.*;

public class Test {
	public static void main (String[] args) {
		Utilisateur etudiant = new Etudiant("Louis","Ayroles","rll2019a", "mdp");
		Utilisateur enseignant = new Enseignant("Jean-Marc","Pierson","prj2020i", "mdp1");
		Utilisateur administratif = new Administratif("Nadege","Lamarque","lmn2020a","mdp2");
		Utilisateur technicien = new Technicien("Jean", "Peplu","pp2020e", "mdp3");
		List<Utilisateur> liste = new ArrayList<>();
		liste.add(etudiant);
		liste.add(enseignant);
		liste.add(administratif);
		liste.add(technicien);
		Groupe groupe = new Groupe("Nouveau Groupe",1, liste);
		for (Utilisateur u : groupe.getGroupe()) {
			System.out.println(u);
		}

	}
}
