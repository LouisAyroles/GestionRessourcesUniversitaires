/**
 * 
 */
package interfaces.utilitaire;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextPane;

/**
 * @author lampior
 *
 */
public class Texte extends JTextPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Texte() {
		this.setFont(new Font("Tahoma", Font.BOLD, 12));
		this.setEditable(false);
		this.setForeground(Color.BLACK);
	}
	
	public void setTailleFont(int x) {
		this.setFont(new Font("Tahoma", Font.BOLD, x));
	}
}
