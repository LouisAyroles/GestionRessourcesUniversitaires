package interfaces.fenetreUtil;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import bdd.BaseDeDonnees;
import bdd.BaseDeDonneesException;
import interfaces.utilitaire.BoutonImage;
import interfaces.utilitaire.MonRenderer;
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
	private DefaultMutableTreeNode node;
	private JButton envoyer = new BoutonImage("img/fleche.jpg");
	private JPanel jp = new JPanel();
	private JScrollPane top;
	private MonRenderer renderer = new MonRenderer();
	private JTextArea texteSaisie = new JTextArea();
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
		arbo.setCellRenderer(renderer);
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
		envoyer.addActionListener(this);
		arbo.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				node = (DefaultMutableTreeNode)arbo.getLastSelectedPathComponent();
				if(node == null) return;
				Object nodeInfo= node.getUserObject();
				if(nodeInfo.getClass() == Discussion.class) {
					Discussion selected = (Discussion) nodeInfo;
					try {
						jp = ajoutJPanel(selected.getIdDiscussion());
						top.setViewportView(jp);
					} catch(Exception ex) {
						System.out.println("J'em bas les couilles frère.");
					}
				}
			}
		});
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == itemTicket) {
			new NouveauTicket();
		} else if(arg0.getSource() == deconnexion) {
			dispose();
			new Login();
		} else if(arg0.getSource() == envoyer) {
			String saisie = texteSaisie.getText();
			int j = 0;
			char[] saisie2 = saisie.toCharArray();
			char[] saisie3 = new char[saisie2.length*2];
			for(int i = 0; i < saisie2.length; i++) {
				saisie3[j] = saisie2[i];
				if(saisie2[i] == '\'') {
					j++;
					saisie3[j] = '\'';
				}
				j++;
			}
			saisie = new String(saisie3);
			Discussion selected = null;
			Message msg;
			if(saisie != " ") {
				if(node != null && node.getUserObject().getClass().equals(Discussion.class)) {
					selected = (Discussion) node.getUserObject();
					msg = new Message(connected, saisie, new Date());
					try {
						msg = bdd.creerMessage(msg);
						if(bdd.addMessageToFil(msg.getIdMessage(), selected.getIdDiscussion()) != -1) {
							jp = ajoutJPanel(selected.getIdDiscussion());
							top.setViewportView(jp);
						} else {
							JOptionPane.showMessageDialog(this, "Envoie du message a échoué");
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(this, "Veuillez sélectionner un fil où envoyer votre message");
				}
			} else {
				JOptionPane.showMessageDialog(this, "Veuillez saisir un message");
			}
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
		fenetre.setResizeWeight(0.2);
		fenetre.setDividerSize(4);
	}
	
	public void initRight() {
		top = new JScrollPane(jp,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
		buttonHolder.add(envoyer);
		bottomright.add(buttonHolder,BorderLayout.EAST);
		bottomright.add(texteSaisie, BorderLayout.CENTER);
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
      jp = new JPanel();
      jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
      List<Message> listeMsg = getMessageOfDiscussion(id);
      for(Message msg : listeMsg) {
          jp.add(boxDiscussion(msg));
      }
      return jp;
  }
  
  private  Box boxDiscussion(Message m) throws IOException {
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
      box.setMinimumSize(new Dimension(top.getWidth()/2, 125));
      box.setMaximumSize(new Dimension(top.getWidth()/2, 150));
      return box;
  }
  	
  	private List<Message> getMessageOfDiscussion(int id) throws BaseDeDonneesException {
  		Discussion d = bdd.getFilById(id);
  		return bdd.getMessageOfFil(d.getIdDiscussion());
  	}
  	
}
