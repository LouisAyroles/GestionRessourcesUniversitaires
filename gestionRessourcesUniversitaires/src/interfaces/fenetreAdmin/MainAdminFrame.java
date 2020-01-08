/**
 * 
 */
package interfaces.fenetreAdmin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import bdd.BaseDeDonnees;
import interfaces.fenetreUtil.Fenetre;
import interfaces.fenetreUtil.Login;
import interfaces.utilitaire.Bouton;
import utilisateurs.Groupe;
import utilisateurs.Utilisateur;

/**
 * @author lamp
 *
 */
public class MainAdminFrame extends Fenetre{

	private BaseDeDonnees bdd;
	private String editionEnCours;
	private JOptionPane effaceDemande = new JOptionPane();
	private JPanel left = new JPanel();
	private JPanel middle = new JPanel();
	private JPanel right = new JPanel();
	private JLabel groupe = new JLabel("Groupe");
	private JLabel utilisateurs = new JLabel("Utilisateurs");
	private Vector<String> listGroup = new Vector<>();
	private JList<String> groupes = new JList<>(listGroup);
	private Vector<String> listUser = new Vector<>();
	private JList<String> users = new JList<>(listUser);
	private Bouton creerUser = new Bouton("Créer Utilisateur");
	private Bouton creerGroup = new Bouton("Créer Groupe");
	private Bouton addToGroup = new Bouton("Ajouter à un groupe");
	private Bouton deleteGroup = new Bouton("Supprimer groupe");
	private Bouton editUser = new Bouton("Modifier utilisateur");
	private Bouton deleteUser = new Bouton("Supprimer utilisateur");
	private Bouton retour = new Bouton("Retour");
	private Bouton voirGroupe = new Bouton("Voir groupe");
	private Bouton enleverUser = new Bouton("Enlever Utilisateur");
	private CreationGroupe nouv;
	private CreationUser nouvUser;
	private ModifUser editUsers;
	private String saisieGroupe = "";
	private JScrollPane listGroupe = new JScrollPane(groupes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JScrollPane midUser = new JScrollPane(users, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	
	private Vector<String> listUserGroup = new Vector<>();
	private JList<String> userGroup = new JList<>(listUserGroup);
	private JScrollPane listUserGroupe = new JScrollPane(userGroup, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	
	private List<Groupe> arrayGroup = new ArrayList<>();
	private List<Utilisateur> arrayUser = new ArrayList<>();
	private Utilisateur connected;
	
	
	public MainAdminFrame(BaseDeDonnees bdd, String connected) {
		super("Interface d'administration Serveur (GRU)");
		this.bdd = bdd;
		refresh();
		for(Utilisateur u : arrayUser) {
			if(u.getUsername().equalsIgnoreCase(connected)) {
				this.connected = u;
			}
		}
		addToGroup.addActionListener(this);
		enleverUser.addActionListener(this);
		setSize((int)(getCurrentScreenSize().getWidth()),(int)(getCurrentScreenSize().getHeight()));
		positionnerCentre();
		initLeft();
		initMiddle();
		initRight();
		initContainer();
		setVisible(true);
	}
	
	public void refresh() {
		arrayGroup = bdd.getAllGroup();
		arrayUser = bdd.getAllUser();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int option;
		if(e.getSource() == deleteGroup) {
			String groupeErase = groupes.getSelectedValue();
			if(groupeErase != null) {
				option = effaceDemande.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer le groupe "+ groupeErase+ "?",
						"Suppression Groupe", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(option == JOptionPane.OK_OPTION) {
					if(bdd.deleteGroup(bdd.getGroup(groupeErase)) == -1) {
						JOptionPane.showMessageDialog(this, "La suppression a échoué.");
					} else {
						JOptionPane.showMessageDialog(this, "Groupe " + groupeErase + " effacé.");
						listGroup.remove(groupeErase);
					}
					listGroupe.setViewportView(groupes);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Veuillez sélectionner un groupe");
			}
		} else if(e.getSource() == editUser) {
			Utilisateur modifie = null;
			for(Utilisateur u : arrayUser) {
				if(u.toString().equals(users.getSelectedValue())) {
					modifie = u;
				}
			}
			if(modifie != null){
				editionEnCours = users.getSelectedValue();
				listUser.remove(users.getSelectedValue());
				editUsers = new ModifUser(this, modifie);
			}
		} else if(editUsers != null && e.getSource() == editUsers.getValider()) {
			Utilisateur userModif = editUsers.getUtilisateurSaisi();
			editUsers.dispose();
			try {
				if(bdd.modifyUser(userModif) == -1) {
					listUser.add(editionEnCours);
					midUser.setViewportView(users);
					JOptionPane.showMessageDialog(this, "La modification a échoué.\n Erreur de base de données");
				} else {
					listUser.add(userModif.toString());
					midUser.setViewportView(users);
					JOptionPane.showMessageDialog(this, "Utilisateur modifié avec succès.");
				}
			} catch(Exception ex) {
				listUser.add(editionEnCours);
				midUser.setViewportView(users);
				JOptionPane.showMessageDialog(this, "La modification a échoué.\n Erreur de saisie");
			}
			refresh();
		}else if(editUsers != null && e.getSource() == editUsers.getRetour()) {
			listUser.add(editionEnCours);
			midUser.setViewportView(users);
			editUsers.dispose();
			refresh();
		} else if(e.getSource() == deleteUser) {
			Utilisateur efface = null;
			String userErase = users.getSelectedValue();
			if(userErase != null) {
				option = effaceDemande.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer l'utilisateur "+ userErase + "?",
						"Suppression Groupe", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if(option == JOptionPane.OK_OPTION) {
					for(Utilisateur u : arrayUser) {
						if(u.toString().equals(userErase)) {
							efface = u;
						}
					}
					if(bdd.deleteUser(efface.getUsername()) == -1) {
						JOptionPane.showMessageDialog(this, "La suppression a échoué.");
					} else {
						JOptionPane.showMessageDialog(this, "Utilisateur " + userErase + " effacé.");
						listUser.remove(userErase);
						midUser.setViewportView(users);
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur");
			}
		}else if(e.getSource() == creerUser) {
			nouvUser = new CreationUser(this);
		}else if(e.getSource() == retour) {
			dispose();
			new Login();
		}else if(e.getSource() == creerGroup) {
			nouv = new CreationGroupe(this);
		}else if(nouv != null && e.getSource() == nouv.getValider()) {
			saisieGroupe = nouv.getSaisie();
			nouv.dispose();
			if(saisieGroupe.length() != 0) {
				try {
					Groupe nouv = bdd.createGroup(saisieGroupe);
					listGroup.add(nouv.toString());
					listGroupe.setViewportView(groupes);
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(this, "La création a échoué.\n Erreur de base de données");
				}
				JOptionPane.showMessageDialog(this, "Groupe créé avec succès.");
			} else {
				JOptionPane.showMessageDialog(this, "La création a échoué.\n Erreur de saisie");
			}
			refresh();
		} else if(nouvUser != null && e.getSource() == nouvUser.getValider()) {
			Utilisateur userNouv = nouvUser.getUtilisateurSaisi();
			nouvUser.dispose();
			try {
				if(bdd.createUser(userNouv) == -1) {
					JOptionPane.showMessageDialog(this, "La création a échoué.\n Erreur de base de données");
				} else {
					listUser.add(userNouv.toString());
					midUser.setViewportView(users);
					JOptionPane.showMessageDialog(this, "Utilisateur créé avec succès.");
				}
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(this, "La création a échoué.\n Erreur de saisie");
			}
			refresh();
		} else if(e.getSource() == voirGroupe) {
			Groupe groupSelected = null;
			listUserGroup.removeAllElements();
			for(Groupe g : arrayGroup) {
				if(g.toString().equals(groupes.getSelectedValue())) {
					groupSelected = g;
				}
			}
			if(groupSelected != null) {
				for(Utilisateur u : bdd.getUsersOfGroup(groupSelected)) {
					listUserGroup.add(u.toString());
				}
				listUserGroupe.setViewportView(userGroup);
			}
			refresh();
		} else if(e.getSource() == enleverUser) {
			Groupe groupSelected = null;
			Utilisateur userSelected = null;
			for(Groupe g : arrayGroup) {
				if(g.toString().equals(groupes.getSelectedValue())) {
					groupSelected = g;
				}
			}
			for(Utilisateur u : arrayUser) {
				if(u.toString().equals(userGroup.getSelectedValue())) {
					userSelected = u;
				}
			}
			if(groupSelected != null && userSelected != null) {
				if(bdd.deleteUserFromGroup(userSelected, groupSelected) != -1) {
					JOptionPane.showMessageDialog(this, "Utilisateur "+ userSelected.getNom() + " "+ 
							userSelected.getPrenom() + " supprimé du groupe " + groupSelected.getNom());
				} else {
					JOptionPane.showMessageDialog(this, "Impossible d'enlever l'utilisateur du groupe.");
				}
			} else {
				JOptionPane.showMessageDialog(this, "Veuillez sélectionner un groupe et un utilisateur.");
			}
			listUserGroup.remove(userSelected.toString());
			listUserGroupe.setViewportView(userGroup);
		} else if(e.getSource() == addToGroup) {
			Groupe groupSelected = null;
			Utilisateur userSelected = null;
			for(Groupe g : arrayGroup) {
				if(g.toString().equals(groupes.getSelectedValue())) {
					groupSelected = g;
				}
			}
			for(Utilisateur u : arrayUser) {
				if(u.toString().equals(users.getSelectedValue())) {
					userSelected = u;
				}
			}
			if(groupSelected != null && userSelected != null) {
				if(bdd.addUserToGroup(groupSelected, userSelected) != -1) {
					JOptionPane.showMessageDialog(this, "Utilisateur "+ userSelected.getNom() + " "+ 
				userSelected.getPrenom() + " ajouté au groupe " + groupSelected.getNom());					
				} else {
					JOptionPane.showMessageDialog(this, "Impossible d'ajouter l'utilisateur du groupe.");
				}
			} else {
				JOptionPane.showMessageDialog(this, "Veuillez sélectionner un groupe et un utilisateur.");
			}
			refresh();
		}
	}
	
	public void initLeft() {
		JPanel bas = new JPanel();
		JPanel haut = new JPanel();
		JPanel mid = new JPanel();
		GridLayout gl = new GridLayout(2,1);
				haut.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 40));
		haut.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
		haut.setBackground(Color.LIGHT_GRAY);
		bas.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
		bas.setBackground(Color.LIGHT_GRAY);
		left.setLayout(new BorderLayout());
		haut.add(groupe);
		left.add(haut, BorderLayout.NORTH);
		bas.setLayout(new FlowLayout(FlowLayout.CENTER, 10,40));
		bas.add(deleteGroup);
		bas.add(enleverUser);
		bas.add(voirGroupe);
		deleteGroup.addActionListener(this);
		voirGroupe.addActionListener(this);
		groupes.setSelectedIndex(0);
		left.add(bas, BorderLayout.SOUTH);
		for(Groupe group : bdd.getAllGroup()) {
			listGroup.add(group.toString());
		}
		listGroupe.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		gl.setVgap(20);
		mid.setLayout(gl);
		listUserGroupe.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		mid.add(listGroupe);
		mid.add(listUserGroupe);
		left.add(mid, BorderLayout.CENTER);
	}

	public void initMiddle() {
		JPanel bas = new JPanel();
		JPanel haut = new JPanel();
		haut.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 40));
		haut.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
		haut.setBackground(Color.LIGHT_GRAY);
		bas.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
		bas.setBackground(Color.LIGHT_GRAY);
		middle.setLayout(new BorderLayout());
		haut.add(utilisateurs);
		middle.add(haut, BorderLayout.NORTH);
		bas.setLayout(new FlowLayout(FlowLayout.CENTER, 30,40));
		bas.add(editUser);
		bas.add(deleteUser);
		deleteUser.addActionListener(this);
		editUser.addActionListener(this);
		users.setSelectedIndex(0);
		middle.add(bas, BorderLayout.SOUTH);
		for(Utilisateur user : bdd.getAllUser()) {
			if(!user.equals(connected))
				listUser.add(user.toString());
		}
		midUser.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		middle.add(midUser, BorderLayout.CENTER);
	}
	
	public void initRight() {
		right.setBorder(BorderFactory.createLineBorder(Color.black, 3, true));
		right.setBackground(Color.LIGHT_GRAY);
		right.setLayout(new GridLayout(10,1));
		right.add(new JLabel());
		right.add(new JLabel());
		right.add(new JLabel());
		creerUser.addActionListener(this);
		creerGroup.addActionListener(this);
		right.add(creerUser);
		right.add(creerGroup);
		right.add(addToGroup);
		right.add(new JLabel());
		right.add(new JLabel());
		retour.addActionListener(this);
		right.add(retour);
		right.add(new JLabel());
	}
	
	
	@Override
	public void initContainer() {
		// TODO Auto-generated method stub
		setLayout(new GridLayout(1,3));
		
		container.add(left);
		middle.setBackground(Color.PINK);
		container.add(middle);
		container.add(right);
		
	}

}
