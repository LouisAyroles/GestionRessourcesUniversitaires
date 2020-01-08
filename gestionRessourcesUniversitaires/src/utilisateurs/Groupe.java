package utilisateurs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Groupe implements Serializable{
	private static final long serialVersionUID = 0;
	private String nom;
	private List<Utilisateur> groupe = new ArrayList<>();
	
	public Groupe(String n, List<Utilisateur> g) {
		this.nom = n;
		this.groupe = g;
	}
	
	public Groupe(String n) {
		this.nom=n;
	}
	

	@Override
	public String toString() {
		return nom;
	}
	
	public void ajoutUtilisateur(Utilisateur u) {
		if (!groupe.contains(u)) {
			this.groupe.add(u);
		}
	}
	
	public void ajoutUtilisateurs(List<Utilisateur> l) {
		for(Utilisateur u : l) {
			ajoutUtilisateur(u);
		}
	}
	
	public void supprimerUtilisateur(Utilisateur u) {
		this.groupe.remove(u);
	}
	
	public List<Utilisateur> getGroupe() {
		return groupe;
	}
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
}

