package interfaces.fenetreUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class MainFrame extends Fenetre{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JSplitPane right = new JSplitPane();
	private JSplitPane fenetre = new JSplitPane();
	private JPanel conversations = new JPanel();
	private JPanel focus = new JPanel();
	private JButton [] boutton = new JButton[8];
	private JMenuBar barreMenu = new JMenuBar();		
	private JMenu menuNouveau = new JMenu("Nouveau");
	private ItemMenu deconnexion = new ItemMenu("Déconnexion");
	private JMenu menuFichier = new JMenu("Fichier");
	private ItemMenu itemTicket = new ItemMenu("Ticket"); 
	@SuppressWarnings("unused")
	private int nbConversations;
	
	public MainFrame(String title, int nbConversations) {
		super(title);
		this.nbConversations = nbConversations;
		setSize((int)getCurrentScreenSize().getWidth(),(int)getCurrentScreenSize().getHeight());
		initConversations();
		initfocus();
		initFenetre();
		initContainer();
		setContentPane(container);
		initMenuBar();
		setJMenuBar(barreMenu);
		positionnerCentre();
		for(int i = 0; i < nbConversations; i++) {
			boutton[i] = new JButton("BOUTON DE TEST "+ (i+1));
			conversations.add(boutton[i]);
		}
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == itemTicket) {
			new NouveauTicket();
		} else if(arg0.getSource() == deconnexion) {
			dispose();
			new Login();
		}
	}
	
	public void positionnerCentre() {
		setLocationRelativeTo(null);
	}
	
	public void initMenuBar() {
		menuNouveau.add(itemTicket);
		itemTicket.addActionListener(this);
		menuFichier.add(menuNouveau);
		deconnexion.addActionListener(this);
		menuFichier.add(deconnexion);
		barreMenu.add(menuFichier);
		
	}
	
	public void initContainer() {
		container.setBackground(Color.magenta);
		container.setLayout(new BorderLayout());
		container.add(fenetre, BorderLayout.CENTER);
	}
	
	public void initFenetre() {
		fenetre.setBackground(Color.black);
		fenetre.setLeftComponent(conversations);
		fenetre.setRightComponent(right);
		initRight();
		fenetre.setEnabled(false);
		fenetre.setDividerLocation((int)getCurrentScreenSize().getWidth()/4);
		fenetre.setDividerSize(4);
	}
	
	public void initRight() {
		right.setBackground(Color.BLUE);
		right.setOrientation(JSplitPane.VERTICAL_SPLIT);
		//Proportions non gardées lorsqu'on redimensionne la fenêtre. A voir !
		right.setDividerLocation((int)getCurrentScreenSize().getHeight()*3/4);
		right.setDividerSize(4);
		right.setTopComponent(focus);
		right.setBottomComponent(new JTextArea(5,10));
	}
	
	public void initfocus() {
		focus.setBackground(Color.CYAN);
		focus.setSize((int)getCurrentScreenSize().getWidth()*3/8, (int)getCurrentScreenSize().getHeight());
		
	}
	
	public void initConversations() {
		conversations.setLayout(new GridLayout(8,1));
		conversations.setBackground(Color.MAGENTA);
	}
}

