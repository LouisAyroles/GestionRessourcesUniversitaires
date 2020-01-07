/**
 * 
 */
package interfaces.fenetreUtil;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Léo
 *
 */
public abstract class Fenetre extends JFrame implements ActionListener{

	protected static final long serialVersionUID = 1L;
	private Dimension currentScreenSize;
	protected JPanel container = new JPanel();
	
	public Fenetre() {
		currentScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(container);
	}
	
	public Fenetre(String titre) {
		currentScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(titre);
		setContentPane(container);
	}
	
	public void positionnerCentre() {
		setLocationRelativeTo(null);
	}
	
	public Dimension getCurrentScreenSize() {
		return currentScreenSize;
	}
	
	public abstract void initContainer();
}
