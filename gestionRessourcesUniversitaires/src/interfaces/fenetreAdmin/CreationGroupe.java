/**
 * 
 */
package interfaces.fenetreAdmin;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import interfaces.fenetreUtil.Fenetre;
import interfaces.utilitaire.Bouton;

/**
 * @author lamp
 *
 */
public class CreationGroupe extends Fenetre {

	private JPanel top = new JPanel();
	private MainAdminFrame monParent;
	private JPanel mid = new JPanel();
	private JPanel bot = new JPanel();
	private Bouton valider = new Bouton("Valider");
	private Bouton retour = new Bouton("Retour");
	private JTextField saisieGroupe = new JTextField(20);
	
	public CreationGroupe(MainAdminFrame appelant) {
		super();
		monParent = appelant;
		setSize((int)getCurrentScreenSize().getWidth()/4,(int)(getCurrentScreenSize().getHeight()/3));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Création de groupe");
		
		initContainer();
		positionnerCentre();
		setResizable(false);
		setVisible(true);
	}
	
	public String getSaisie() {
		return saisieGroupe.getText();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == retour) {
			dispose();
		} else if(e.getSource() == valider) {
			System.out.println("Retour à l'envoyeur");
		}
	}

	@Override
	public void initContainer() {
		// TODO Auto-generated method stub
		container.setLayout(new GridLayout(3,1));
		top.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 40));
		top.add(new JLabel("Entrez le nom du groupe"));
		mid.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
		mid.add(saisieGroupe);
		bot.setLayout(new FlowLayout(FlowLayout.CENTER, 60,0));
		retour.addActionListener(this);
		valider.addActionListener(this);
		valider.addActionListener(monParent);
		bot.add(retour);
		bot.add(valider);
		container.add(top);
		container.add(mid);
		container.add(bot);
	}

	public Bouton getValider() {
		return valider;
	}

}
