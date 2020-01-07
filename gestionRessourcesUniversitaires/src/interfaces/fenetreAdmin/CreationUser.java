/**
 * 
 */
package interfaces.fenetreAdmin;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import interfaces.fenetreUtil.Fenetre;
import interfaces.utilitaire.Bouton;

/**
 * @author lamp
 *
 */
public class CreationUser extends Fenetre {

	private Bouton valider = new Bouton("Valider");
	private Bouton retour = new Bouton("Retour");
	private JPanel login = new JPanel();
	private JTextField saisieLogin = new JTextField(20);
	private JPanel nom = new JPanel();
	private JTextField saisieNom = new JTextField(20);
	private JPanel prenom = new JPanel();
	private JTextField saisiePrenom = new JTextField(20);
	private JPanel mdp = new JPanel();
	private JPasswordField saisieMdp = new JPasswordField(20);
	private JPanel confirmation = new JPanel();
	private JPasswordField saisieConfirmation = new JPasswordField(20);
	
	private JPanel categorie = new JPanel();
	private ButtonGroup saisieCategorie = new ButtonGroup();
	private JRadioButton categorie1 = new JRadioButton("Campus");
	private JRadioButton categorie2 = new JRadioButton("Agents de Service");
	
	private JPanel bouttons = new JPanel();
	
	
	public CreationUser() {
		super();
		setSize((int)getCurrentScreenSize().getWidth()/3,(int)(getCurrentScreenSize().getHeight()/1.5));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Création/Modification d'un utilisateur");
		positionnerCentre();
		
		initContainer();
		setResizable(false);
		setVisible(true);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == retour) {
			dispose();
		} else if(e.getSource() == valider) {
			dispose();
		}
	}

	@Override
	public void initContainer() {
		// TODO Auto-generated method stub
		container.setLayout(new GridLayout(10,1));
		container.add(new JPanel());
		container.add(new JPanel());
		initLogin();
		container.add(login);
		initNom();
		container.add(nom);
		initPrenom();
		container.add(prenom);
		initMdp();
		container.add(mdp);
		initConfirmation();
		container.add(confirmation);
		initCategorie();
		container.add(categorie);
		initBouttons();
		container.add(bouttons);
	}
	
	public void initLogin() {
		login.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 0));
		login.add(new JLabel("Pseudonyme"));
		login.add(saisieLogin);
	}
	
	public void initNom() {
		nom.setLayout(new FlowLayout(FlowLayout.CENTER, 120, 0));
		nom.add(new JLabel("Nom"));
		nom.add(saisieNom);
	}
	
	public void initPrenom() {
		prenom.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 0));
		prenom.add(new JLabel("Prénom"));
		prenom.add(saisiePrenom);
	}
	
	public void initMdp() {
		mdp.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 0));
		mdp.add(new JLabel("Mot de Passe"));
		mdp.add(saisieMdp);
	}
	
	public void initConfirmation() {
		confirmation.setLayout(new FlowLayout(FlowLayout.CENTER, 65, 0));
		confirmation.add(new JLabel("Confirmation"));
		confirmation.add(saisieConfirmation);
	}
	
	public void initCategorie() {
		categorie.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 0));
		categorie.add(new JLabel("Categorie"));
		categorie1.setSelected(true);
		saisieCategorie.add(categorie1);
		saisieCategorie.add(categorie2);
		categorie.add(categorie1);
		categorie.add(categorie2);
	}
	
	public void initBouttons() {
		bouttons.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 0));
		valider.addActionListener(this);
		retour.addActionListener(this);
		bouttons.add(valider);
		bouttons.add(retour);
	}
}
