package flappyBird;

import javafx.scene.shape.Circle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {
    public Random rand;
    public static FlappyBird flappyBird;
    public final int WIDTH = 1200, HEIGHT = 800;
    public Renderer renderer;
    public Rectangle bird;
    public Circle circle;
    public ArrayList<Rectangle> columns;
    public int ticks, yMotion, score;
    public boolean gameOver, started;
    Timer timer;

    public FlappyBird() {
        JFrame jframe = new JFrame();
        timer = new Timer(20, this);

        renderer = new Renderer();
        rand = new Random();
        jframe.add(renderer);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
        jframe.setResizable(false);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setTitle("Flappy Bird");
        jframe.setVisible(true);
        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20); // coordinates,size
        columns = new ArrayList<Rectangle>();
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        timer.start();
    }

    public void addColumn(boolean start) {
        int space = 300;
        int width = 100;

        int height = 50 + rand.nextInt(300);
        if (start) {
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }

    public void paintColumn(Graphics g, Rectangle column) {
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void repaint(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.ORANGE);
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);
        g.setColor(Color.GREEN);
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);
        g.setColor(Color.RED);
        g.fillOval(bird.x, bird.y, bird.width, bird.height);

        for (Rectangle column : columns) {
            paintColumn(g, column);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 1, 100));
        if (!started) {
            g.drawString("Click to start", 75, HEIGHT / 2 - 50);
            //timer.stop();
        }
        if (gameOver) {
            g.drawString("Game Over", WIDTH/2 - 200, HEIGHT / 2 - 50);
            g.drawString("Score : " +score, WIDTH/2 - 200, HEIGHT / 2+50);
            //timer.stop();
        }
        if(!gameOver && started){
            g.drawString(String.valueOf(score),WIDTH / 2 -25,100);
        }


    }

    public static void main(String[] args) {
        flappyBird = new FlappyBird();
    }


    public void jump() {
        if (gameOver) {
            bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20); // coordinates,size
            columns.clear();
            yMotion = 0;
            score = 0;
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            gameOver = false;
        }
        if (!started) {
            started = true;
        } else if (!gameOver) {
            if (yMotion > 0) {
                yMotion = 0;
            }
            yMotion -= 15;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ticks++;
        int speed = 15;
        if (started) {
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                column.x -= speed;
            }
            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0) {
                    columns.remove(column);
                    if (column.y == 0)
                        addColumn(false);
                }
            }
            bird.y += yMotion;
            //Checking collision
            for (Rectangle column : columns) {
                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < // to check if the bird goes between two rectangles
                        column.x + column.width / 2 + 10) {
                    if(!gameOver)
                        score++;
                }
                if (column.intersects(bird)) {
                    gameOver = true;
                    if(bird.x < column.x) {
                        bird.x = column.x - bird.width;
                    }else{
                        if(column.y != 0  ){ //If it is intersecting the second column
                            bird.y = column.y - bird.height;
                        }else if(bird.y < column.height){
                            bird.y= column.height;
                        }
                    }
                }
            }
            if (bird.y > HEIGHT - 120 || bird.y < 0) {
                gameOver = true;
            }
            if (bird.y + yMotion >= HEIGHT - 120) {
                bird.y = HEIGHT - 120 - bird.height;
                //timer.stop();
            }
        }

        renderer.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            jump();
        }
    }
}

