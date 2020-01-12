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
import utilisateurs.Administratif;
import utilisateurs.Enseignant;
import utilisateurs.Etudiant;
import utilisateurs.Technicien;
import utilisateurs.Utilisateur;

/**
 * @author lamp
 *
 */
public class ModifUser extends Fenetre {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4434396287449202192L;
	private Bouton valider = new Bouton("Valider");
	private Bouton retour = new Bouton("Retour");
	private MainAdminFrame monParent;
	private JPanel login = new JPanel();
	private JTextField saisieLogin;
	private JPanel nom = new JPanel();
	private JTextField saisieNom;
	private JPanel prenom = new JPanel();
	private JTextField saisiePrenom;
	private JPanel mdp = new JPanel();
	private JPasswordField saisieMdp;
	private JPanel confirmation = new JPanel();
	private JPasswordField saisieConfirmation;
	
	private JPanel categorie = new JPanel();
	private ButtonGroup saisieCategorie = new ButtonGroup();
	private JRadioButton categorie1 = new JRadioButton("Etudiant");
	private JRadioButton categorie2 = new JRadioButton("Enseignant");
	private JRadioButton categorie3 = new JRadioButton("Administratif");
	private JRadioButton categorie4 = new JRadioButton("Technicien");
	
	private JPanel bouttons = new JPanel();
	
	
	public ModifUser(MainAdminFrame parent, Utilisateur util) {
		super();
		monParent = parent;
		saisieLogin = new JTextField(util.getUsername(), 20);
		saisieLogin.setEnabled(false);
		saisieNom = new JTextField(util.getNom(), 20);
		saisiePrenom = new JTextField(util.getPrenom(), 20);
		saisieMdp = new JPasswordField(util.getPassword(), 20);
		saisieConfirmation = new JPasswordField(util.getPassword(), 20);
		setSize((int)getCurrentScreenSize().getWidth()/3,(int)(getCurrentScreenSize().getHeight()/1.5));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Création/Modification d'un utilisateur");
		positionnerCentre();
		
		initContainer();
		setResizable(false);
		setVisible(true);
	}
	
	
	public Bouton getValider() {
		return valider;
	}
	
	public Bouton getRetour() {
		return retour;
	}
	
	public Utilisateur getUtilisateurSaisi() {
		String password = new String(saisieMdp.getPassword());
		String id = saisieLogin.getText();
		String name = saisieNom.getText();
		String surname = saisiePrenom.getText();
		if(password.length() != 0 && 
				password.equals(new String(saisieConfirmation.getPassword()))) {
			if(id.length() != 0 && name.length() != 0 && surname.length() != 0)
				if(categorie2.isSelected()) {
					return new Enseignant(name, surname, id, password);
				} else if(categorie1.isSelected()) {
					return new Etudiant(name, surname, id, password);
				} else if(categorie3.isSelected()) {
					return new Administratif(name, surname, id, password);
				} else {
					return new Technicien(name, surname, id, password);
				}
		}
		return null;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == retour) {
		} else if(e.getSource() == valider) {
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
		saisieCategorie.add(categorie3);
		saisieCategorie.add(categorie4);
		categorie.add(categorie1);
		categorie.add(categorie2);
		categorie.add(categorie3);
		categorie.add(categorie4);
	}
	
	public void initBouttons() {
		bouttons.setLayout(new FlowLayout(FlowLayout.CENTER, 60, 0));
		valider.addActionListener(monParent);
		retour.addActionListener(monParent);
		bouttons.add(valider);
		bouttons.add(retour);
	}
}
