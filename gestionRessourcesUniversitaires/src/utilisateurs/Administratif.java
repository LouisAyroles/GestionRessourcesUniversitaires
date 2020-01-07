package utilisateurs;

public class Administratif extends Utilisateur {

	private static final long serialVersionUID = 5079594094721334794L;

	public Administratif(String nom, String prenom, String username, String password) {
		super(nom, prenom, username, password, TypeUtilisateur.ADMINISTRATIF);
	}
}
