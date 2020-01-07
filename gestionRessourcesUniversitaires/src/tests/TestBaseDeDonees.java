package tests;
import bdd.*;
import utilisateurs.Utilisateur;
public class TestBaseDeDonees {
	public static void main (String[] args) {
			BaseDeDonnees myBDD = new BaseDeDonnees();
			
			Utilisateur u = myBDD.usernameVersUtilisateur("rll2019a");
			System.out.println(u);

	}
}
