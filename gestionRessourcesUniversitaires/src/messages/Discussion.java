package messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utilisateurs.Groupe;
import utilisateurs.Utilisateur;

public class Discussion implements Serializable{
	private static final long serialVersionUID = 0;
	private String titre;
	private Utilisateur createur;
	private Groupe groupe;
	private int idDiscussion;
	private List<Message> messages = new ArrayList<>();
	
	public Discussion (String t, Utilisateur c, Groupe g, int i) {
		this.titre = t;
		this.createur = c;
		this.groupe = g;
		this.idDiscussion = i;
	}
	public Utilisateur getCreateur() {
		return createur;
	}
	public String getTitre() {
		return titre;
	}
	public int getIdDiscussion() {
		return idDiscussion;
	}
	public Groupe getGroupe() {
		return groupe;
	}
	public List<Message> getMessages() {
		return messages;
	}
}
