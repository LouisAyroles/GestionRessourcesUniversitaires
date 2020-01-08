package messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import utilisateurs.Groupe;
import utilisateurs.Utilisateur;

public class Discussion implements Serializable{

	private static final long serialVersionUID = -56029639858834765L;
	private String titre;
	private Utilisateur createur;
	private Groupe groupe;
	private int idDiscussion;
	private List<Message> messages = new ArrayList<>();
	
	public Discussion (String titre, Utilisateur utilisateur, Groupe groupe, int id) {
		this.titre = titre;
		this.createur = utilisateur;
		this.groupe = groupe;
		this.idDiscussion = id;
	}
	
	public Discussion (String titre, Utilisateur utilisateur, Groupe groupe, int id, Message messageDebut) {
		this.titre = titre;
		this.createur = utilisateur;
		this.groupe = groupe;
		this.idDiscussion = id;
		this.messages.add(messageDebut);
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

	@Override
	public String toString() {
		return "Discussion [titre=" + titre + ", createur=" + createur + ", groupe=" + groupe + ", idDiscussion="
				+ idDiscussion + ", messages=" + messages + "]";
	}
}
