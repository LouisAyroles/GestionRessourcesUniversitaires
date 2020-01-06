/**
 * 
 */
package interfaces.fenetreUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import interfaces.utilitaire.Bouton;

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
	
	public NouveauTicket() {
		super();
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
		middle.add(new JTextArea("Entrez votre saisie ici...", 10, 40));
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
		nomsIntervenants.add("Plombier");
		nomsIntervenants.add("Electricien");
		nomsIntervenants.add("Securite");
		nomsIntervenants.add("Serveurs");
		nomsIntervenants.add("Entretien");
		saisieGroupeDestination.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
		saisieGroupeDestination.add(groupe);
		choixGroupeDestination.setSelectedIndex(0);
		saisieGroupeDestination.add(choixGroupeDestination);
	}
	
	public void initGroupeSource() {
		JLabel groupe = new JLabel("Source : ");
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 6; j++)
				nomsDesGroupes.add("A" + (i+1) + (j+1));
		}
		saisieGroupeSource.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
		saisieGroupeSource.add(groupe);
		choixGroupe.setSelectedIndex(0);
		saisieGroupeSource.add(choixGroupe);
	}
	
	public void initSaisieTitre() {
		JLabel titre = new JLabel("Titre :");
		JTextField entreeTitre = new JTextField(20);
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
		setContentPane(container);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == valider) {
			dispose();
		} else if(e.getSource() == retour) {
			dispose();
		}
	}
}
