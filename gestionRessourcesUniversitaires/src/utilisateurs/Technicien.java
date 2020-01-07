package utilisateurs;

public class Technicien extends Utilisateur {

	private static final long serialVersionUID = 4599684215272064063L;

	public Technicien(String nom, String prenom, String username, String password) {
		super(nom, prenom, username, password, TypeUtilisateur.TECHNICIEN);
	}
}
