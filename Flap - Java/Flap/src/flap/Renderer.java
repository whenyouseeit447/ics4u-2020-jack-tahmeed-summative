package flap;
import javax.swing.JPanel;
import java.awt.Graphics;
public class Renderer extends JPanel {
  
  private static final long serialversionUID=1L;
  
  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g); //Calls from the super class JPanel 
    Flap.flap.repaint(g);
  }
}
