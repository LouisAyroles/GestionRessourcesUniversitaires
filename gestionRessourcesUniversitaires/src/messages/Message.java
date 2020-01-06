package messages;

import java.io.Serializable;
import java.util.Date;

import utilisateurs.Utilisateur;

public class Message implements Serializable {
	private static final long serialVersionUID = 0;
	private Utilisateur auteur;
	private Date date;
	private String message;
	private int idMessage;
	private int idDiscussion;
	private TypeMessage type;
	
	public Message(Utilisateur auteur, Date date, String message, int idMsg, int idFil, TypeMessage t) {
		this.auteur = auteur;
		this.date = date;
		this.message = message;
		this.idDiscussion= idFil;
		this.idMessage = idMsg;
		this.type = t;
	}
	
	public String toString() {
		return message;
	}
	
	public Utilisateur getAuteur() {
		return auteur;
	}
	public Date getDate() {
		return date;
	}
	public int getIdDiscussion() {
		return idDiscussion;
	}
	public String getMessage() {
		return message;
	}
	public int getIdMessage() {
		return idMessage;
	}
	public TypeMessage getType() {
		return type;
	}
}
