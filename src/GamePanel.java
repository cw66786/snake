import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int BANNER_HEIGHT = 50;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 90;

     int x[] = new int[GAME_UNITS];
     int y[] = new int[GAME_UNITS];

    int bodyParts = 6;
    int foodEaten;
    int foodX;
    int foodY;

    char direction = 'R';
    boolean running = false;
    boolean startScreen = true;
    boolean gameIsOver = false;

    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT + BANNER_HEIGHT));

        this.setBackground(Color.BLACK);

        

        this.setFocusable(true);

        
        this.addKeyListener( new MyKeyAdapter());

       
    }

    

    

    public void startGame() {
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {
        

        if(startScreen == true){
            createStartScreen(g);
        }else if (running == true) {

            g.setColor(Color.white);
            g.fillRect(0, SCREEN_HEIGHT , SCREEN_WIDTH, SCREEN_HEIGHT + BANNER_HEIGHT);

            // for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            //     g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            //     g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);

            // }

            g.setColor(Color.red);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+foodEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: "+foodEaten))/2, SCREEN_HEIGHT + BANNER_HEIGHT - 15);

        }else{
            gameOver(g);
        }




    }

    public void newFood() {
        foodX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((int) (SCREEN_HEIGHT  / UNIT_SIZE)) * UNIT_SIZE;

    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {

            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkFood() {
        if ((x[0] == foodX) && (y[0] == foodY)) {
            bodyParts++;
            foodEaten++;
            newFood();

           
            
        }

    }

    public void checkCollisions() {
        // checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;

            }
        }

        // checks if head touches left border
        if (x[0] < 0) {
            running = false;
        }

        // checks if head touches right bordrer

        if (x[0] + UNIT_SIZE > SCREEN_WIDTH) {
            running = false;
        }

        // checks if head touches top

        if (y[0] < 0) {
            running = false;
        }

        // checks if head touches bottom

        if (y[0] + UNIT_SIZE > SCREEN_HEIGHT  ) {
            running = false;
        }

        if (running == false) {
            timer.stop();
        }

    }

    public void gameOver(Graphics g) {
        //score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics scoreMetrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+foodEaten,(SCREEN_WIDTH - scoreMetrics.stringWidth("Score: "+foodEaten))/2, SCREEN_HEIGHT/2 + 110 );

        //game over
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);

        //restart text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        FontMetrics restartMetrics = getFontMetrics(g.getFont());
        g.drawString("Press SPACEBAR to Restart",(SCREEN_WIDTH - restartMetrics.stringWidth("Press SPACEBAR to Restart"))/2, SCREEN_HEIGHT/2 + 180 );

       gameIsOver = true;
       

    }

    public void createStartScreen(Graphics g){
         //score
         g.setColor(Color.red);
         g.setFont(new Font("Ink Free", Font.BOLD, 20));
         FontMetrics scoreMetrics = getFontMetrics(g.getFont());
         g.drawString("Press SPACEBAR to Start",(SCREEN_WIDTH - scoreMetrics.stringWidth("Press SPACEBAR to Start"))/2, SCREEN_HEIGHT/2 + 110 );
 
         //game over
         g.setColor(Color.red);
         g.setFont(new Font("Ink Free", Font.BOLD, 75));
         FontMetrics metrics = getFontMetrics(g.getFont());
         g.drawString("SNAKE",(SCREEN_WIDTH - metrics.stringWidth("SNAKE"))/2, SCREEN_HEIGHT/2);
 

    }

    public void resetSnake(){
        bodyParts = 6;
        foodEaten = 0;
        direction = 'R';
        

        
           
       x = new int[GAME_UNITS];
       y = new int[GAME_UNITS];
       
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();

            checkFood();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter implements KeyListener  {
      
      @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {

                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;

                case KeyEvent.VK_SPACE:
                    if (startScreen == true && running == false) {
                        startScreen = !startScreen;
                    
                        running = !running;
                        startGame();
                    }else if(gameIsOver == true && running == false){
                        gameIsOver = !gameIsOver;
                        running = !running;
                        
                        resetSnake();
                        
                        startGame();

                        
                    }
                    break;
            }
        }

   

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
    }

    

       
    }


   
}
