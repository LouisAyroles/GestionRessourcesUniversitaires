package utilisateurs;

public class Etudiant extends Utilisateur {

	private static final long serialVersionUID = 4896404077314261843L;

	public Etudiant(String nom, String prenom, String username, String password) {
		super(nom, prenom, username, password, TypeUtilisateur.ETUDIANT);
	}
}
