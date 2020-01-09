/**
 * 
 */
package interfaces.fenetreUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bdd.BaseDeDonnees;
import interfaces.utilitaire.Bouton;
import messages.Discussion;
import messages.Message;
import utilisateurs.Groupe;
import utilisateurs.Utilisateur;

/**
 * @author Léo
 *
 */
public class NouveauTicket extends Fenetre {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel saisieTitre = new JPanel();
	private JPanel top = new JPanel();
	private JPanel saisieGroupeSource = new JPanel();
	private JPanel saisieGroupeDestination = new JPanel();
	private JPanel saisieMessage = new JPanel();
	private Vector<String> nomsDesGroupes = new Vector<>();
	private Vector<String> nomsIntervenants = new Vector<>();
	private JComboBox<String> choixGroupeDestination = new JComboBox<>(nomsIntervenants);
	private JComboBox<String> choixGroupe = new JComboBox<>(nomsDesGroupes);
	private Bouton valider = new Bouton("Valider");
	private Bouton retour = new Bouton("Retour");
	private BaseDeDonnees bdd;
	private Utilisateur connected;
	private JTextArea texteSaisi = new JTextArea(10, 40);
	private JTextField entreeTitre = new JTextField(20);
	
	public NouveauTicket(BaseDeDonnees bdd, Utilisateur connected) {
		super();
		this.bdd = bdd;
		this.connected = connected;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize((int)getCurrentScreenSize().getWidth()/3,(int)getCurrentScreenSize().getHeight()/2);
		setTitle("NouveauTicket");
		initSaisieTitre();
		initGroupeSource();
		initGroupeDestination();
		initSaisieMessage();
		initTop();
		initContainer();
		positionnerCentre();
		setResizable(false);
		setVisible(true);
	}

	public Discussion getDiscussion() {
		String saisie = texteSaisi.getText();
		Message msg;
		Discussion nouv = null;
		Groupe gs = null;
		Groupe gd = null;
		for(Groupe it : bdd.getAllGroup()) {
			if(it.toString().equals(choixGroupe.getSelectedItem())) {
				gs = it;
			}
			if(it.toString().equals(choixGroupeDestination.getSelectedItem())) {
				gd = it;
			}
		}
		int j = 0;
		char[] saisie2 = saisie.toCharArray();
		char[] saisie3 = new char[saisie2.length*2];
		texteSaisi.setText("");
		for(int i = 0; i < saisie2.length; i++) {
			saisie3[j] = saisie2[i];
			if(saisie2[i] == '\'') {
				j++;
				saisie3[j] = '\'';
			}
			j++;
		}
		saisie = new String(saisie3);
		msg = new Message(connected, saisie, new Date());
		j = 0;
		char[] saisie4 = entreeTitre.getText().toCharArray();
		char[] saisie5 = new char[saisie4.length*2];
		for(int i = 0; i < saisie4.length; i++) {
			saisie5[j] = saisie4[i];
			if(saisie4[i] == '\'') {
				j++;
				saisie5[j] = '\'';
			}
			j++;
		}
		nouv = new Discussion(new String(saisie5), connected, gs,gd, 0, msg);
		return nouv;
	}

	
	
	public void initSaisieMessage() {
		JPanel top = new JPanel();
		JPanel middle = new JPanel();
		JPanel bot = new JPanel();
		saisieMessage.setLayout(new BoxLayout(saisieMessage, BoxLayout.Y_AXIS));
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.add(new JLabel("Message :"));
		top.add(Box.createHorizontalGlue());
		top.add(Box.createHorizontalGlue());
		saisieMessage.add(top);
		middle.add(texteSaisi);
		saisieMessage.add(middle);
		bot.setLayout(new BoxLayout(bot, BoxLayout.X_AXIS));
		bot.add(Box.createHorizontalGlue());
		valider.addActionListener(this);
		bot.add(valider);
		bot.add(Box.createHorizontalGlue());
		retour.addActionListener(this);
		bot.add(retour);
		bot.add(Box.createHorizontalGlue());
		saisieMessage.add(bot);
	}
	
	public void initGroupeDestination() {
		JLabel groupe = new JLabel("destinataire : ");
		for(Groupe g : bdd.getAllGroup()) {
			if(!g.toString().substring(0, 1).equals("T"))
				nomsIntervenants.add(g.toString());
		}
		saisieGroupeDestination.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
		saisieGroupeDestination.add(groupe);
		choixGroupeDestination.setSelectedIndex(0);
		saisieGroupeDestination.add(choixGroupeDestination);
	}
	
	public void initGroupeSource() {
		JLabel groupe = new JLabel("Source : ");
		for(Groupe g : bdd.getGroupsOfUser(connected)) {
			if(g.toString().substring(0, 1).equals("T"))
				nomsDesGroupes.add(g.toString());
		}
		saisieGroupeSource.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
		saisieGroupeSource.add(groupe);
		choixGroupe.setSelectedIndex(0);
		saisieGroupeSource.add(choixGroupe);
	}
	
	public void initSaisieTitre() {
		JLabel titre = new JLabel("Titre :");
		entreeTitre.setForeground(Color.gray);
		saisieTitre.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
		saisieTitre.add(titre);
		saisieTitre.add(entreeTitre);
	}
	
	public void initTop() {
		top.setLayout(new GridLayout(3,1));
		top.add(saisieTitre);
		top.add(saisieGroupeSource);
		top.add(saisieGroupeDestination);
	}
	
	public void initContainer() {
		// TODO Auto-generated method stub
		container.setLayout(new GridLayout(2,1));
		container.add(top);
		container.add(saisieMessage);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == retour) {
			dispose();
		}
	}
	
	public Bouton getValider() {
		return valider;
	}
}
