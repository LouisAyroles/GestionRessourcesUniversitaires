package interfaces.fenetreUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import bdd.BaseDeDonnees;
import bdd.BaseDeDonneesException;
import interfaces.utilitaire.BoutonImage;
import messages.Discussion;
import messages.Message;
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
	private JTree arbo;
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
		arbo = filDiscussion(this.connected.getUsername());
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
		JPanel jp = null;
		try {
			jp = ajoutJPanel(2);
		} catch(Exception e) {
			System.out.println("J'em bas les couilles frère.");
		}
		JScrollPane top = new JScrollPane(jp,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		right.setBackground(Color.BLUE);
		right.setOrientation(JSplitPane.VERTICAL_SPLIT);
		right.setResizeWeight(0.8);
		right.setDividerSize(4);
		right.setTopComponent(top);
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
		conversations.add(arbo, BorderLayout.CENTER);
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
            treeD[i] = new DefaultMutableTreeNode(discussion);

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
  
  	
  	private JPanel ajoutJPanel(int id) throws BaseDeDonneesException, IOException {
  		JPanel jp = new JPanel();
  		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
  		List<Message> listeMsg = getMessageOfDiscussion(id);
  		for(Message msg : listeMsg) {
  			jp.add(boxDiscussion(msg));
  		}
  		return jp;
  	}
  	
  	private Box boxDiscussion(Message m) throws IOException {
  		Box box = new Box(BoxLayout.Y_AXIS);
  		
  		JLabel auteur = new JLabel(m.getAuteur().getNom() + " " + m.getAuteur().getPrenom() + " :");
  		Font fontAuteur = new Font("Arial", Font.BOLD, 13);
  		auteur.setFont(fontAuteur);
  		
  		JEditorPane corps = new JEditorPane();
  		corps.setContentType("text/plain");	
  		corps.setText(m.getMessage());
  		corps.setEditable(false);
  		corps.setBackground(auteur.getBackground());
  		
  		JLabel date = new JLabel(m.getDate().toString());
  		Font fontDate = new Font("Arial", Font.ITALIC ,12);
  		date.setFont(fontDate);
  		
  		//JLabel space = new JLabel(" ");
  		//box.add(space);
  		
  		box.add(auteur);
  		box.add(corps);
  		box.add(date);
  		box.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
  		return box;
  	}
  	
  	private List<Message> getMessageOfDiscussion(int id) throws BaseDeDonneesException {
  		Discussion d = bdd.getFilById(id);
  		return bdd.getMessageOfFil(d.getIdDiscussion());
  	}
  	
}
