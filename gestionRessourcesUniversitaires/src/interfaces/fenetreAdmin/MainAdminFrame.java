/**
 * 
 */
package interfaces.fenetreAdmin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import interfaces.fenetreUtil.Fenetre;
import interfaces.fenetreUtil.Login;
import interfaces.utilitaire.Bouton;

/**
 * @author lamp
 *
 */
public class MainAdminFrame extends Fenetre{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4343060755355986164L;
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
	private Bouton editGroup = new Bouton("Modifier groupe");
	private Bouton deleteGroup = new Bouton("Supprimer groupe");
	private Bouton editUser = new Bouton("Modifier utilisateur");
	private Bouton deleteUser = new Bouton("Supprimer utilisateur");
	private Bouton retour = new Bouton("Retour");
	private CreationGroupe nouv;
	private String saisieGroupe = "";
	private JScrollPane listGroupe = new JScrollPane(groupes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	
	public MainAdminFrame() {
		super("Interface d'administration Serveur (GRU)");
		setSize((int)(getCurrentScreenSize().getWidth()),(int)(getCurrentScreenSize().getHeight()));
		positionnerCentre();
		initLeft();
		initMiddle();
		initRight();
		initContainer();
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int option;
		if(e.getSource() == deleteGroup) {
			option = effaceDemande.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer le groupe ?",
					"Suppression Groupe", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		} else if(e.getSource() == editGroup) {
			System.out.println("Pas encore prêt");
		} else if(e.getSource() == deleteUser) {
			option = effaceDemande.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer l'utilisateur ?",
					"Suppression Groupe", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		}else if(e.getSource() == creerUser) {
			new CreationUser();
		}else if(e.getSource() == retour) {
			dispose();
			new Login();
		}else if(e.getSource() == creerGroup) {
			nouv = new CreationGroupe(this);
		}else if(e.getSource() == nouv.getValider()) {
			saisieGroupe = nouv.getSaisie();
			nouv.dispose();
			listGroup.add(saisieGroupe);
			listGroupe.setViewportView(groupes);
		}
	}
	
	public void initLeft() {
		JPanel bas = new JPanel();
		JPanel haut = new JPanel();
				haut.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 40));
		haut.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
		haut.setBackground(Color.LIGHT_GRAY);
		bas.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
		bas.setBackground(Color.LIGHT_GRAY);
		left.setLayout(new BorderLayout());
		haut.add(groupe);
		left.add(haut, BorderLayout.NORTH);
		bas.setLayout(new FlowLayout(FlowLayout.CENTER, 30,40));
		bas.add(editGroup);
		bas.add(deleteGroup);
		deleteGroup.addActionListener(this);
		editGroup.addActionListener(this);
		left.add(bas, BorderLayout.SOUTH);
		for(int i = 0; i < 6; i++) {
			listGroup.add("Groupe TD A"+(i+1));
		}
		listGroupe.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		left.add(listGroupe, BorderLayout.CENTER);
	}

	public void initMiddle() {
		JPanel bas = new JPanel();
		JPanel haut = new JPanel();
		JScrollPane mid = new JScrollPane(users, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
		middle.add(bas, BorderLayout.SOUTH);
		listUser.add("Jean P");
		listUser.add("Benoît S");
		listUser.add("François P");
		mid.setBorder(BorderFactory.createLineBorder(Color.black, 3));
		middle.add(mid, BorderLayout.CENTER);
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
