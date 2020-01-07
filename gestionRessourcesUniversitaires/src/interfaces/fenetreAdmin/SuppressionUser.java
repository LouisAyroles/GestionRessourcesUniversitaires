/**
 * 
 */
package interfaces.fenetreAdmin;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interfaces.fenetreUtil.Fenetre;
import interfaces.utilitaire.Bouton;
import interfaces.utilitaire.Texte;

/**
 * @author lampior
 *
 */
public class SuppressionUser extends Fenetre {

	private JPanel bot = new JPanel();
	private Bouton oui = new Bouton("Oui");
	private Bouton non = new Bouton("Non");
	private Texte question = new Texte();
	
	public SuppressionUser() {
		setTitle("GRU");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize((int)getCurrentScreenSize().getWidth()/3,(int)getCurrentScreenSize().getHeight()/2);

		positionnerCentre();
		question.setText("Voulez-vous vraiment supprimer l'utilisateur ?");
		
		initBot();
		initContainer();
		setResizable(false);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initContainer() {
		// TODO Auto-generated method stub
		container.setLayout(new GridLayout(6,1));
		container.setBackground(Color.lightGray);
		container.add(new JLabel("Interface d'administration serveur"));
		container.add(new JPanel());
		container.add(question);
		container.add(new JLabel());
		container.add(bot);
		container.add(new JLabel());
		setContentPane(container);
	}
	
	public void initBot() {
		bot.setLayout(new FlowLayout(FlowLayout.CENTER, 150, 20));
		bot.add(oui);
		bot.add(non);
	}

}
