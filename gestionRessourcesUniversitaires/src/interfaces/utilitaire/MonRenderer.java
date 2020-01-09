package interfaces.utilitaire;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import messages.Discussion;
import utilisateurs.Groupe;

public class MonRenderer extends DefaultTreeCellRenderer {
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 366612211746604049L;
	private JLabel label;
 
	@Override
	public Component getTreeCellRendererComponent(final JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
 
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
		
		if (node.getUserObject().getClass() == Discussion.class) {
			
			label = new JLabel(value.toString());
			label.setFont(new Font("Arial", Font.PLAIN, 12)); //police et couleur de la feuille
			label.setIcon(new ImageIcon("img/conv.png")); // pour mettre un icone personnalisé
			label.setForeground(Color.BLUE);//couleur de police
			if(sel) {
				label.setForeground(Color.GRAY);
		    }
 
			return label;
		}else if (node.getUserObject().getClass() == Groupe.class) {
			label = new JLabel(value.toString());
			label.setFont(new Font("Arial", Font.PLAIN, 12)); //police et couleur de la feuille
			label.setIcon(new ImageIcon("img/groupe.png")); // pour mettre un icone personnalisé
			label.setForeground(Color.BLACK);//couleur de police
			if(sel) {
				label.setForeground(Color.GRAY);
		    }
			return label;
		}else if (node.getUserObject().getClass() == String.class) {
			label = new JLabel(value.toString());
			label.setFont(new Font("Arial", Font.PLAIN, 18)); //police et couleur de la feuille
			label.setIcon(null); // pour mettre un icone personnalisé
			label.setForeground(Color.BLACK);//couleur de police
			if(sel) {
				label.setForeground(Color.GRAY);
		    }
			return label;
		}
		return this;
	}
	
}