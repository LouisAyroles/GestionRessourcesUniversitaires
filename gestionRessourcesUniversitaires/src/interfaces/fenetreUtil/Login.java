/**
 * 
 */
package interfaces.fenetreUtil;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import interfaces.utilitaire.Bouton;

/**
 * @author Léo
 *
 */
public class Login extends Fenetre{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel topTitre = new JLabel("Veuillez entrer vos identifiants");
	private JPanel top = new JPanel();
	private Bouton login = new Bouton("Connexion");
	private Bouton exit = new Bouton("Quitter");
	private JPanel bot = new JPanel();
	private JPanel middle = new JPanel();
	private JLabel utilisateur = new JLabel("nom d'utilisateur :");
	private JLabel mdp = new JLabel("mot de passe :");
	private JLabel adresseIP = new JLabel("Ip serveur :");
	private JTextField utilisateurSaisie = new JTextField(20);
	private JPasswordField motDePasseSaisie = new JPasswordField(20);
	private JTextField adresseIPSaisie = new JTextField(20);

	
	public Login() {
		super();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize((int)getCurrentScreenSize().getWidth()/3,(int)getCurrentScreenSize().getHeight()/2);
		setTitle("Identification");
		initBot();
		initTop();
		initMiddle();
		initContainer();
		positionnerCentre();
		setResizable(false);
		setVisible(true);
	}
	@Override
	public void initContainer() {
		// TODO Auto-generated method stub
		container.setLayout(new BorderLayout());
		container.add(top, BorderLayout.NORTH);
		container.add(bot, BorderLayout.SOUTH);
		container.add(middle, BorderLayout.CENTER);
		setContentPane(container);
	}
	
	public void initMiddle() {
		JPanel haut = new JPanel();
		JPanel milieu = new JPanel();
		JPanel bas = new JPanel();
		middle.setLayout(new GridLayout(7,1));
		haut.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
		haut.add(utilisateur);
		haut.add(utilisateurSaisie);

		milieu.setLayout(new FlowLayout(FlowLayout.CENTER,57,20));
		milieu.add(mdp);
		milieu.add(motDePasseSaisie);
		
		bas.setLayout(new FlowLayout(FlowLayout.CENTER, 78, 20));
		bas.add(adresseIP);
		bas.add(adresseIPSaisie);
		
		
		middle.add(new JLabel());
		middle.add(new JLabel());
		middle.add(haut);
		middle.add(milieu);
		middle.add(bas);
		middle.add(new JLabel());
		middle.add(new JLabel());
	}

	public void initBot() {
		bot.setLayout(new BoxLayout(bot, BoxLayout.X_AXIS));
		login.addActionListener(this);
		bot.add(Box.createHorizontalGlue());
		bot.add(login);
		bot.add(Box.createHorizontalGlue());
		exit.addActionListener(this);
		bot.add(exit);
		bot.add(Box.createHorizontalGlue());
	}
	
	public void initTop() {
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.add(Box.createHorizontalGlue());
		top.add(topTitre);
		top.add(Box.createHorizontalGlue());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == login) {
			new MainFrame("GRU", 4);
			dispose();
		} else if(e.getSource() == exit) {
			dispose();
		}
	}
}
