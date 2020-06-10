package flap;
import java.awt.*;//* used to be Graphics
import java.awt.event.*;// * used to be ActionListener, I don't even understand how this fixes the problem, but it does. 
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;//Used to be JFrame
import javax.swing.Timer;
import java.io.*;
import java.lang.System.*;

public class Flap implements ActionListener, MouseListener, KeyListener{
  public static Flap flap;
  public final int WIDTH=1080, HEIGHT=720;
  public Renderer renderer;
  public Rectangle miniPortal, reverseGravityPortal;
  public Bird bird;
  public Random rand;
  public static boolean gameOver, started = false;
  public int ticks, ymotion, score;
  public ArrayList<Rectangle> columns;
  JFrame jframe = new JFrame();
  JButton TryAgain = new JButton("Try again?");
  
  
  public Flap(){
    Timer timer = new Timer(20, this);
    renderer = new Renderer();
    rand = new Random();
    
    jframe.add(renderer);
    jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
    
    //Toolkit imported from java.awt.*
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();//only used for the screen size
    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();
    jframe.setSize(WIDTH,HEIGHT);
    jframe.setLocation(((int)screenWidth - WIDTH)/2, ((int)screenHeight - HEIGHT)/2);
    
    
    jframe.addMouseListener(this);
    jframe.addKeyListener(this);
    jframe.setResizable(false);
    jframe.setTitle("Flap");
    jframe.setVisible(true);
    
    bird = new Bird ();
    
    class Action implements ActionListener {
      public void actionPerformed(ActionEvent evt) {
        jframe.dispose();
      }
    }
    TryAgain.addActionListener(new Action());
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
    int columnSpacing = 150;
    int pipeWidth = 70;
    int pipeHeight = 50 + rand.nextInt(200);
    int portalType = rand.nextInt(4);
    
    if (start){
      //Adds column with specific dimensions to the array list
      //columnSpacing*2
      columns.add(new Rectangle(WIDTH + pipeWidth + columns.size()*columnSpacing,HEIGHT - pipeHeight - 120, pipeWidth, pipeHeight));//bottom column
      columns.add(new Rectangle(WIDTH + pipeWidth + (columns.size()-1)*columnSpacing, 0 ,pipeWidth, HEIGHT-pipeHeight-space)); //top column
      if (portalType == 1){
       //miniPortal = new Rectangle (WIDTH + width + columns.size()*columnSpacing, HEIGHT/2-10, 34,24);
      }
    }
    else{
      columns.add(new Rectangle(columns.get(columns.size()-1).x + columnSpacing*2, HEIGHT - pipeHeight - 120, pipeWidth, pipeHeight));
      columns.add(new Rectangle(columns.get(columns.size()-1).x, 0, pipeWidth, HEIGHT-pipeHeight-space)); 
    }
  }
  
 public void jump(){// this only activates on a click
   ymotion -= 10;
 }
  
 @Override
  public void actionPerformed(ActionEvent e){
    int speed = 10;
    ticks ++;
    
    if (started){
      for (int i=0; i<columns.size(); i++){
        Rectangle column = columns.get(i);//get the current column (1 pipe or half a set of pipes)

        column.x -= speed;//this is what makes columns go to the left
        if (column.x + column.width <0){ //when the column is offscreen
          columns.remove(column);
          if (column.y==0){// if it's a top pipe
           /*
            This may seem counter-intuitive, but addColumn() only adds a set of columns if its set to true. 
            If we set it to false, we activate addColumn()'s else statement which makes it iterate 
            through an infinite # of pipes based on the position of the last pipe. 
            Thus, this line of code is necessary if you want to play the game past 4 sets of pipes
            */
            addColumn(false);
          }
        }
      }

      if ((ticks % 2 == 0) && (ymotion<15)){
        ymotion += 2;//think of this as the gravity of the game (the accelration towards the ground)
      }
      bird.y += ymotion;
      for (Rectangle column: columns){
        
        if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width /2 -5 && bird.x+ bird.width /2 < column.x + column.width / 2  + 5 && gameOver == false){
          score ++;
        }
        
        if (column.intersects(bird)){// if bird crashes into the pipe
          gameOver = true;
          
          if (bird.x<=column.x){//if bird is behind current pipe
            bird.x = column.x-bird.width;
          }
          else{
            if (column.y != 0){
              bird.y = column.y-bird.height;
            }
            else if  (bird.y > column.height){
              bird.y = column.height;
            }
          }
        }
      }
    }
   
  
  //if bird hits ceiling or ground, its game over
  if ((bird.y > HEIGHT - 150) || (bird.y<0)) {
    gameOver = true;
  }
  
  //makes the bird fall to ground
  if (bird.y + ymotion >= HEIGHT - 150){
    bird.y = HEIGHT -120 - bird.height;
  } 

    renderer.repaint();
  }
  
  public void paintColumn(Graphics g, Rectangle column){
    g.setColor(Color.green.darker());
    g.fillRect(column.x,column.y,column.width,column.height);
  }
  
  public void repaint(Graphics g){
    g.setColor(Color.blue);
    g.fillRect(0,0,WIDTH,HEIGHT);// the background
    
    g.setColor(Color.orange);
    g.fillRect(0,HEIGHT-150,WIDTH,150);//the ground
    
    g.setColor(Color.green);
    g.fillRect(0,HEIGHT-150, WIDTH, 20);//the grass
    
    g.setColor(Color.RED);
    g.fillRect(bird.x, bird.y, bird.width, bird.height);// the birb
    //create a rectangle for the retry button
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
      g.drawString("Score: " + String.valueOf(score),WIDTH/3,HEIGHT/2+50);
      
      int tryWidth = 275, tryHeight = 70, tryX = (WIDTH - tryWidth)/2, tryY = (HEIGHT - tryHeight)*3/4;
      g.setColor(Color.WHITE);
      g.fillRect(tryX,tryY,tryWidth,tryHeight);
      g.setColor(Color.blue);
      g.setFont(new Font("Arial",1,50));
      g.drawString("Try Again?",tryX + 10,tryY+tryHeight - 10);
      
      jframe.add(TryAgain);
      TryAgain.setLocation(tryX,tryY);
      TryAgain.setSize(tryWidth,tryHeight);
      TryAgain.setText("I'm right here");
      TryAgain.setVisible(true);
    //current scores
    // highscores    
    //create a button here. In that button, add all the shit you'd do if the bird died
      
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
    //jump(); Why was this here?
  }
  
  @Override
  public void mousePressed(MouseEvent e){
    if (gameOver){
      try{
        FileWriter fw = new FileWriter("High Scores.txt", true);
        PrintWriter pw = new PrintWriter(fw);
        pw.println(String.valueOf(score) + "\n");
        pw.close();
      }
      catch(IOException i){
        System.out.println("Error saving score");
      }
    bird = new Bird ();
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
   
   else if (!gameOver && ymotion > 0) {// if the bird is descending, 
     ymotion = 0;//stop its descent immediately 
   }
    jump();
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
