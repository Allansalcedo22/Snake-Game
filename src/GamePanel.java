import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 650;
    static final int SCREEN_HEIGHT = 650;
    static final int UNIT_SIZE = 30;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 115;
    int currentDelay = DELAY;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    boolean directionChanged = false;
    Timer timer;
    Random random;


    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {
        if (running) {
            // g.setColor(Color.gray);
        //for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
           // g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            //g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);

        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        //Draw Snake
            for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                //head
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                //eyes
                g.setColor(Color.BLACK);
                int eyeSize = UNIT_SIZE / 5;
                int eyeOffset = UNIT_SIZE / 4;
                switch (direction) {
                    case 'U':
                        g.fillOval(x[i] + eyeOffset, y[i] + eyeOffset / 2, eyeSize, eyeSize);
                        g.fillOval(x[i] + UNIT_SIZE - eyeOffset - eyeSize, y[i] + eyeOffset / 2, eyeSize, eyeSize);
                        break;
                    case 'D':
                        g.fillOval(x[i] + eyeOffset, y[i] + UNIT_SIZE - eyeOffset - eyeSize, eyeSize, eyeSize);
                        g.fillOval(x[i] + UNIT_SIZE - eyeOffset - eyeSize, y[i] + UNIT_SIZE - eyeOffset - eyeSize, eyeSize, eyeSize);
                        break;
                    case 'L':
                        g.fillOval(x[i] + eyeOffset / 2, y[i] + eyeOffset, eyeSize, eyeSize);
                        g.fillOval(x[i] + eyeOffset / 2, y[i] + UNIT_SIZE - eyeOffset - eyeSize, eyeSize, eyeSize);
                        break;
                    case 'R':
                        g.fillOval(x[i] + UNIT_SIZE - eyeOffset - eyeSize / 2, y[i] + eyeOffset, eyeSize, eyeSize);
                        g.fillOval(x[i] + UNIT_SIZE - eyeOffset - eyeSize / 2, y[i] + UNIT_SIZE - eyeOffset - eyeSize, eyeSize, eyeSize);
                        break;
                }
                
            } else {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
        } else {
            gameOver(g);
        }
        g.setColor(Color.yellow);
        g.setFont( new Font("Ink Free", Font.BOLD,40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
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
    public void checkApples() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();

            if (currentDelay > 40) {
                currentDelay -= 2;
                timer.setDelay(currentDelay);
            }
        }
    }
    public void checkCollisions() {
        //checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // checks if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        //check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        //check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        //check of head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.yellow);
        g.setFont( new Font("Ink Free", Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten,(SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
        //Gameover text
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free", Font.BOLD,75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        //restart instructions
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.PLAIN, 30));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press SPACE to Restart", (SCREEN_WIDTH - metrics3.stringWidth("Press SPACE to Restart")) /2, SCREEN_HEIGHT / 2+ 120);
    }
    public void restartGame() {
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        newApple();
        running = true;
        currentDelay= DELAY;
        timer.setDelay(currentDelay);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            directionChanged = false;
            checkApples();
            checkCollisions();
        }
        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                        directionChanged = true;
                    }
                    break;
                    case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                        directionChanged = true;
                    }
                    break;
                    case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                        directionChanged = true;
                    }
                    break;
                    case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                        directionChanged = true;
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (!running) {
                        restartGame();
                    }
                    break;
            }
        }
    }
}
