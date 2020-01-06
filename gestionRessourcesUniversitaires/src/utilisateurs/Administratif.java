package utilisateurs;

public class Administratif extends Utilisateur{
	private static final long serialVersionUID = 0;
	
	public Administratif(String nom, String prenom, String username,String password) {
		super(nom,prenom,username,password, TypeUtilisateur.ADMINISTRATIF);
	}
}
