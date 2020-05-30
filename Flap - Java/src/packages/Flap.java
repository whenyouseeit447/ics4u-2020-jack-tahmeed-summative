package flap;
import java.awt.*;//* used to be Graphics
import java.awt.event.*;// * used to be ActionListener, I don't even understand how this fixes the problem, but it does. 
import java.util.*;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Flap implements ActionListener{
  public static Flap flap;
  public final int WIDTH=1080, HEIGHT=720;//Should all these be public static?
  public Renderer renderer;
  public Rectangle bird;
  public Random rand;
  public int ticks, ymotion;
  public ArrayList<Rectangle> columns;
  
  public Flap(){
    JFrame jframe = new JFrame();
    Timer timer = new Timer(20, this);
    renderer = new Renderer();
    rand = new Random();
    
    jframe.add(renderer);
    jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
    jframe.setSize(WIDTH,HEIGHT);
    jframe.setResizable(false);
    jframe.setTitle("Flap");
    jframe.setVisible(true);
    
    bird = new Rectangle(WIDTH/2-10, HEIGHT/2-10, 20,20);
    columns = new ArrayList();
    
    addColumn(true);
    addColumn(true);
    addColumn(true);
    addColumn(true);
    
    timer.start();
  
  }
  
  public void addColumn(boolean start){
    int space = 300;
    int width = 100;
    int height = 50 + rand.nextInt(200);
    
    if (start){
      columns.add(new Rectangle(WIDTH + width + columns.size()*300,HEIGHT - height - 120, width, height));
      columns.add(new Rectangle(WIDTH + width + (columns.size()-1)*300, 0 ,width, HEIGHT-height-space)); 
    }
    else{
      columns.add(new Rectangle(columns.get(columns.size()-1).x + 600, HEIGHT - height - 120, width, height));
      columns.add(new Rectangle(columns.get(columns.size()-1).x, 0, width, HEIGHT-height-space)); 
    }
  }

 public void paintColumn(Graphics g, Rectangle column){
    g.setColor(Color.green.darker());
    g.fillRect(column.x,column.y,column.width,column.height);
  }
  
  public void actionPerformed(ActionEvent e){
    if (ticks % 2 == 0){
      ymotion += 2;
    }
    renderer.repaint();
  }
  
  public void repaint(Graphics g){
    g.setColor(Color.blue);
    g.fillRect(0,0,WIDTH,HEIGHT);
    
    g.setColor(Color.orange);
    g.fillRect(0,HEIGHT-150,WIDTH,150);
    
    g.setColor(Color.green);
    g.fillRect(0,HEIGHT-150, WIDTH, 20);
    
    g.setColor(Color.RED);
    g.fillRect(bird.x, bird.y, bird.width, bird.height);

  }
  
  
  public static void main(String[] args) {
   flap = new Flap(); 
  }
  
}
