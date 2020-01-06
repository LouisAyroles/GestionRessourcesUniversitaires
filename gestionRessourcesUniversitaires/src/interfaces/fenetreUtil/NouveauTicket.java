/**
 * 
 */
package interfaces.fenetreUtil;

import java.awt.Color;
import java.awt.Dimension;
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
		middle.add(new JTextArea("Entrez votre saisie ici...", 4, 35));
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
		saisieGroupeDestination.setLayout(new BoxLayout(saisieGroupeDestination, BoxLayout.X_AXIS));
		saisieGroupeDestination.add(groupe);
		saisieGroupeDestination.add(Box.createHorizontalGlue());
		saisieGroupeDestination.add(choixGroupeDestination);
		saisieGroupeDestination.add(Box.createHorizontalGlue());
	}
	
	public void initGroupeSource() {
		JLabel groupe = new JLabel("Source : ");
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 6; j++)
				nomsDesGroupes.add("A" + (i+1) + (j+1));
		}
		saisieGroupeSource.setLayout(new BoxLayout(saisieGroupeSource, BoxLayout.X_AXIS));
		saisieGroupeSource.add(groupe);
		saisieGroupeSource.add(Box.createHorizontalGlue());
		saisieGroupeSource.add(choixGroupe);
		saisieGroupeSource.add(Box.createHorizontalGlue());
	}
	
	public void initSaisieTitre() {
		JLabel titre = new JLabel("Titre :");
		JTextField entreeTitre = new JTextField();
		entreeTitre.setPreferredSize(new Dimension(50,10));
		entreeTitre.setForeground(Color.gray);
		saisieTitre.setLayout(new BoxLayout(saisieTitre, BoxLayout.X_AXIS));
		saisieTitre.add(titre);
		saisieTitre.add(Box.createHorizontalGlue());
		saisieTitre.add(entreeTitre);
		saisieTitre.add(Box.createHorizontalGlue());
	}
	
	public void initContainer() {
		// TODO Auto-generated method stub
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.add(saisieTitre);
		container.add(saisieGroupeSource);
		container.add(saisieGroupeDestination);	
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
