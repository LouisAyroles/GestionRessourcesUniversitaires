package ui;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bdd.BaseDeDonnees;
import bdd.BaseDeDonneesException;
import messages.Discussion;
import messages.Message;

public class testUI {
	static BaseDeDonnees bdd = new BaseDeDonnees();

	private static void fenPrincipale() throws BaseDeDonneesException, IOException {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 600);
		
		ajoutJPanel(frame, 1);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	private static void ajoutJPanel(JFrame frame, int id) throws BaseDeDonneesException, IOException {
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		List<Message> listeMsg = getMessageOfDiscussion(id);
		for(Message msg : listeMsg) {
			jp.add(boxDiscussion(msg));
		}
		frame.add(new JScrollPane(jp,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	}
	
	private static Box boxDiscussion(Message m) throws IOException {
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
	
	private static List<Message> getMessageOfDiscussion(int id) throws BaseDeDonneesException {
		Discussion d = bdd.getFilById(id);
		return bdd.getMessageOfFil(d.getIdDiscussion());
	}
	
	public static void main(String[] args) throws BaseDeDonneesException, IOException {
		fenPrincipale();

	}
}
