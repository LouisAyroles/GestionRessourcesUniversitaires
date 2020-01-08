package interfaces.utilitaire;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
     
    public class BoutonImage extends JButton {
         
        /**
         * 
         */
        private static final long serialVersionUID = -949686672617045018L;
        private Image img;
        private String imageName;
         

        public BoutonImage(String imageName) {
            img = new ImageIcon(imageName).getImage();
        }
         
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img == null) return;
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this); 
    
         
        }
     
}