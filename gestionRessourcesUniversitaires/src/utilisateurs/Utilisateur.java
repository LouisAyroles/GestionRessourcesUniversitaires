package utilisateurs;
import java.io.Serializable;

public abstract class Utilisateur implements Serializable {
	private static final long serialVersionUID = 0;
	private String nom;
	private String prenom;
	private String username;
	private String password;
	private TypeUtilisateur type;
	
	public Utilisateur(String nom, String prenom, String username, String password, TypeUtilisateur type) {
		this.nom = nom;
		this.prenom = prenom;
		this.username = username;
		this.type = type;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Utilisateur) {
			Utilisateur u = (Utilisateur) o;
			return u.getUsername() == this.username;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return nom + " " + prenom;
	}

	public String getNom() {
		return nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public String getPassword() {
		return password;
	}
	public String getUsername() {
		return username;
	}
	public TypeUtilisateur getType() {
		return type;
	}
}
