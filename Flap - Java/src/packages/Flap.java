package flap;
import java.awt.*;//* used to be Graphics
import java.awt.event.*;// * used to be ActionListener, I don't even understand how this fixes the problem, but it does. 
import java.util.Random;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Flap implements ActionListener, MouseListener, KeyListener{
  public static Flap flap;
  public final int WIDTH=1080, HEIGHT=720;
  public Renderer renderer;
  public Rectangle bird;
  public Random rand;
  public boolean gameOver, started = false;
  public int ticks, ymotion, score;
  public ArrayList<Rectangle> columns;
  
  public Flap(){
    JFrame jframe = new JFrame();
    Timer timer = new Timer(20, this);
    renderer = new Renderer();
    rand = new Random();
    
    jframe.add(renderer);
    jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
    jframe.setSize(WIDTH,HEIGHT);
    jframe.addMouseListener(this);
    jframe.addKeyListener(this);
    jframe.setResizable(false);
    jframe.setTitle("Flap");
    jframe.setVisible(true);
    
    bird = new Rectangle(WIDTH/2-10, HEIGHT/2-10, 34,24);
    columns = new ArrayList();//Column array
    
    //create a set of columns on launch
    addColumn(true); 
    addColumn(true);  
    addColumn(true);  
    addColumn(true);  
    
    timer.start();
  
  }
  
  public void addColumn(boolean start){
    int space = 300;
    int columnSpacing = 200;
    int width = 70;
    int height = 50 + rand.nextInt(200);
    
    if (start){
      //Adds column with specific dimensions to the array list
      //columnSpacing*2
      columns.add(new Rectangle(WIDTH + width + columns.size()*columnSpacing,HEIGHT - height - 120, width, height));
      columns.add(new Rectangle(WIDTH + width + (columns.size()-1)*columnSpacing, 0 ,width, HEIGHT-height-space)); 
    }
    else{
      columns.add(new Rectangle(columns.get(columns.size()-1).x + columnSpacing*2, HEIGHT - height - 120, width, height));
      columns.add(new Rectangle(columns.get(columns.size()-1).x, 0, width, HEIGHT-height-space)); 
    }
  }

 public void paintColumn(Graphics g, Rectangle column){
    g.setColor(Color.green.darker());
    g.fillRect(column.x,column.y,column.width,column.height);
  }
  
 public void jump(){
   if (gameOver){
    bird = new Rectangle(WIDTH/2-10, HEIGHT/2-10, 34,24);
    columns.clear();
    ymotion = 0;
    score = 0;
    
    //add 4 more columns on death for next round
    addColumn(true);
    addColumn(true);
    addColumn(true);
    addColumn(true);  
    
    gameOver= false;
   }
   if (!started){
     started = true;
   }
   
   else if (!gameOver) {
     if (ymotion >0){
       ymotion = 0;
     }
   }
  
   ymotion -= 10;
 }
 
 @Override
  public void actionPerformed(ActionEvent e){
    int speed = 10;
    ticks ++;
    
    if (started){
      for (int i=0; i<columns.size(); i++){
        Rectangle column = columns.get(i);
        column.x -= speed;
        if (column.x + column.width <0){
          columns.remove(column);
          if (column.y==0){
           addColumn(false);
          }
        }
      }

      if ((ticks % 2 == 0) && (ymotion<15)){
        ymotion += 2;
      }
      bird.y += ymotion;
      for (Rectangle column: columns){
        
        if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width /2 -5 && bird.x+ bird.width /2 < column.x + column.width / 2  + 5){
          score ++;
        }
        
        if (column.intersects(bird)){
          gameOver = true;
          if (bird.x<=column.x){
            bird.x = column.x-bird.width;
          }
          else{
            if (column.y != 0){
              bird.y = column.y-bird.height;
            }
            else if  (bird.y<column.height){
              bird.y = column.height;
            }
          }
          bird.x = column.x - bird.width;
        }
      }
    }
  
  //if bird hits ceiling or ground, its game over
  if ((bird.y > HEIGHT - 150) ||(bird.y<0)) {
    gameOver = true;
  }
  
  //makes the bird fall to ground
  if (bird.y + ymotion >= HEIGHT - 150){
    bird.y = HEIGHT -120 - bird.height;
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
    
    //iterator
    for (Rectangle column : columns){
      paintColumn(g, column);
    }
    
    g.setColor(Color.white);
    g.setFont(new Font("Arial",1,100));
    
    if (!started){
      g.drawString("Click to start!", 250, HEIGHT / 2 -100);
    }
    
    if (gameOver){
      g.drawString("Game Over!",250,HEIGHT/2-100);
    }
    
    if (!gameOver && started){
      g.drawString (String.valueOf(score), WIDTH / 2 -25, 100);
    }
  }
  
  @Override
  public void keyReleased(KeyEvent e){
    if (e.getKeyCode() == KeyEvent.VK_SPACE){
	jump();
      }
  }
  
  
  //These override statements are necessary even if they dont do anything atm. We'll see if we can delete them later
  //If it works, it works. 
  
  @Override
  public void mouseClicked(MouseEvent e){
    jump();
  }
  
  @Override
  public void mousePressed(MouseEvent e){
  }
  
  @Override
  public void mouseReleased(MouseEvent e){
  }
  
  @Override
  public void mouseEntered(MouseEvent e){
  }
  
  @Override
  public void mouseExited(MouseEvent e){
  }
  
  @Override
  public void keyTyped(KeyEvent e){
  }

  @Override
  public void keyPressed(KeyEvent e){

  }
  
  public static void main(String[] args) {
   flap = new Flap(); 
  }
  
}
