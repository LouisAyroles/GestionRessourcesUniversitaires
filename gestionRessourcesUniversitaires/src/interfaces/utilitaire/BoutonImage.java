package interfaces.utilitaire;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;
     
    public class BoutonImage extends JButton {
         
        /**
         * 
         */
        private static final long serialVersionUID = -949686672617045018L;
        private Image img;
         

        public BoutonImage(String imageName) {
        	URL url = BoutonImage.class.getResource(imageName);
        	try {
    			img = ImageIO.read(url);
    		} catch(IOException e) {
    			e.printStackTrace();
    		}
        }
         
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img == null) return;
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this); 
    
         
        }
     
}