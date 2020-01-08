package interfaces.fenetreUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import bdd.BaseDeDonnees;
import bdd.BaseDeDonneesException;
import interfaces.utilitaire.BoutonImage;
import messages.Discussion;
import utilisateurs.Groupe;
import utilisateurs.Utilisateur;

public class MainFrame extends Fenetre{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BaseDeDonnees bdd;
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
	private Utilisateur connected;
	@SuppressWarnings("unused")
	private int nbConversations;
	
	public MainFrame(String title, BaseDeDonnees bdd, String connected) {
		super(title);
		this.bdd = bdd;
		this.nbConversations = nbConversations;
		for(Utilisateur u : bdd.getAllUser()) {
			if(u.getUsername().equalsIgnoreCase(connected)) {
				this.connected = u;
			}
		}
		setSize((int)getCurrentScreenSize().getWidth(),(int)getCurrentScreenSize().getHeight());
		initConversations();
		initfocus();
		initFenetre();
		initContainer();
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
		fenetre.setResizeWeight(0.04);
		fenetre.setDividerSize(4);
	}
	
	public void initRight() {
		right.setBackground(Color.BLUE);
		right.setOrientation(JSplitPane.VERTICAL_SPLIT);
		right.setResizeWeight(0.8);
		right.setDividerSize(4);
		right.setTopComponent(focus);
		initbottomright();
	}
	
	public void initfocus() {
		focus.setBackground(Color.CYAN);
		focus.setSize((int)getCurrentScreenSize().getWidth()*3/8, (int)getCurrentScreenSize().getHeight());
		
	}
	
	public void initbottomright() {
		JPanel bottomright = new JPanel();
		JPanel buttonHolder = new JPanel();
		buttonHolder.setLayout(new GridLayout(7,1));
		bottomright.setLayout(new BorderLayout());
		buttonHolder.add(new JPanel());
		buttonHolder.add(new JPanel());
		buttonHolder.add(new JPanel());
		buttonHolder.add(new JPanel());
		buttonHolder.add(new JPanel());
		buttonHolder.add(new JPanel());
		buttonHolder.add(new BoutonImage("img/fleche.jpg"));
		bottomright.add(buttonHolder,BorderLayout.EAST);
		bottomright.add(new JTextArea(), BorderLayout.CENTER);
		right.setBottomComponent(bottomright);
	}
	
	public void initConversations() {
		conversations.setLayout(new BorderLayout());
		conversations.add(filDiscussion(connected.getUsername()), BorderLayout.CENTER);
		conversations.setBackground(Color.LIGHT_GRAY);
	}
	
     
     
  public JTree filDiscussion(String username){
         
	  	Utilisateur u = null;
		try {
			u = bdd.usernameVersUtilisateur(username);
		} catch (BaseDeDonneesException e) {
			e.printStackTrace();
		}
		
	  	List<Discussion> discussions = new ArrayList<>();
	  	List<Groupe> groupes = bdd.getGroupsOfUser(u);
		for (Groupe groupe2 : groupes) {
			discussions.addAll(bdd.getFilOfGroupe(groupe2));
		}
	  	
        int i = 0, j=0;
        DefaultMutableTreeNode racine = new DefaultMutableTreeNode("Mes Groupes");
        DefaultMutableTreeNode treeGroupe[] = new DefaultMutableTreeNode[50];
        DefaultMutableTreeNode treeD[] = new DefaultMutableTreeNode[50];
        
        for (Groupe groupe : bdd.getGroupsOfUser(u)) {
            treeGroupe[i] = new DefaultMutableTreeNode(groupe);
            racine.add(treeGroupe[i]);
            i++;
        }
        
        i=0;
        for (Discussion discussion : discussions) {
            treeD[i] = new DefaultMutableTreeNode(discussion.getTitre());

            j=0;
            for (Groupe groupe : groupes) {
                if (discussion.getGroupe().equals(groupe)) {
                    treeGroupe[j].add(treeD[i]);
                }
                j++;
            }
            i++;
        }
        
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(null);
        renderer.setClosedIcon(null);
        renderer.setLeafIcon(null);
        
        JTree tree = new JTree(racine);
        tree.setVisibleRowCount(10);
     
        return tree;
  }  
}
