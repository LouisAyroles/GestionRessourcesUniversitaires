package utilisateurs;

public class Enseignant extends Utilisateur {
	private static final long serialVersionUID = 0;

	public Enseignant(String nom, String prenom, String username, String password) {
		super(nom, prenom, username, password, TypeUtilisateur.ENSEIGNANT);
	}

}
