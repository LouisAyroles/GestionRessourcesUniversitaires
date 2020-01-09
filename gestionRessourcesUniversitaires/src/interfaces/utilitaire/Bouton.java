package interfaces.utilitaire;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

/**
 * @author lampior
 *
 */
public class Bouton extends JButton implements MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Image img;
	@SuppressWarnings("unused")
	private boolean image = false;
	
	public Bouton(String intitule) {
		super(intitule);
		this.name = intitule;
		this.addMouseListener(this);
		try {
			img = ImageIO.read(new File("src/img/blanc.png"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
	    GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, 20, Color.WHITE, true);
	    g2d.setPaint(gp);
    	g2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
	    g2d.setColor(Color.BLACK);
	    g2d.drawString(this.name, this.getWidth() / 2 - (name.length()*(4)), (this.getHeight() / 2)+5);
	}
	
	  //Méthode appelée lors du clic de souris
	  public void mouseClicked(MouseEvent event) {
	  }

	  //Méthode appelée lors du survol de la souris
	  public void mouseEntered(MouseEvent event) { 
	    //Nous changeons le fond de notre image pour le jaune lors du survol, avec le fichier fondBoutonHover.png
	    try {
	      img = ImageIO.read(new File("src/img/rouge.png"));
	      image = true;
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }

	  //Méthode appelée lorsque la souris sort de la zone du bouton
	  public void mouseExited(MouseEvent event) { 
		//Nous changeons le fond de notre image pour le vert lorsque nous quittons le bouton, avec le fichier fondBouton.png
	    try {
	      img = ImageIO.read(new File("src/img/blanc.png"));
	      image = false;
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }

	  //Méthode appelée lorsque l'on presse le bouton gauche de la souris
	  public void mousePressed(MouseEvent event) { 
	    //Nous changeons le fond de notre image pour le jaune lors du clic gauche, avec le fichier fondBoutonClic.png
	    try {
	      img = ImageIO.read(new File("src/img/vert.png"));
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }

	  //Méthode appelée lorsque l'on relâche le clic de souris
	  public void mouseReleased(MouseEvent event) { 
		//Nous changeons le fond de notre image pour le orange lorsque nous relâchons le clic avec le fichier fondBoutonHover.png si la souris est toujours sur le bouton
		  if((event.getY() > 0 && event.getY() < this.getHeight()) && (event.getX() > 0 && event.getX() < this.getWidth())){
		    try {
		      img = ImageIO.read(new File("src/img/rouge.png"));
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		  }
		  //Si on se trouve à l'extérieur, on dessine le fond par défaut
		  else{
		    try {
		      img = ImageIO.read(new File("src/img/blanc.png"));
		      image = false;
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		  } 
	  }   
	  
}