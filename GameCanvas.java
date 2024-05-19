import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class GameCanvas extends Canvas implements ActionListener {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final int UNIT_SIZE = 20;
    private static final int DELAY = 100;

    private final int[] x = new int[WIDTH * HEIGHT / (UNIT_SIZE * UNIT_SIZE)];
    private final int[] y = new int[WIDTH * HEIGHT / (UNIT_SIZE * UNIT_SIZE)];
    private int bodyParts = 6;
    private int foodX;
    private int foodY;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private int score = 0;
    private int highScore = 0;

    public GameCanvas() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.YELLOW);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') direction = 'L';
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') direction = 'R';
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D') direction = 'U';
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') direction = 'D';
                        break;
                }
            }
        });
        loadHighScore();
    }

    public void startGame() {
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (running) {
            // Draw food
            g.setColor(Color.BLACK);
            g.fillRect(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.BLUE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Draw score
            g.setColor(Color.BLACK);
            g.setFont(new Font("Ink Free", Font.BOLD, 25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + score, (WIDTH - metrics.stringWidth("Score: " + score)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newFood() {
        foodX = (int) (Math.random() * (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = (int) (Math.random() * (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
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
            score++;
            newFood();
        }
    }

    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // Check if head touches the border
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
            saveHighScore();
        }
    }

    public void gameOver(Graphics g) {
        // Draw game over text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics1.stringWidth("Game Over")) / 2, HEIGHT / 2);

        // Draw score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + score, (WIDTH - metrics2.stringWidth("Score: " + score)) / 2, g.getFont().getSize());
        g.drawString("High Score: " + highScore, (WIDTH - metrics2.stringWidth("High Score: " + highScore)) / 2, g.getFont().getSize() * 2);
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

    public void loadHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                highScore = Integer.parseInt(line);
            } else {
                highScore = 0;
            }
        } catch (IOException e) {
            highScore = 0;
        } catch (NumberFormatException e) {
            highScore = 0;
        }
    }

    public void saveHighScore() {
        if (score > highScore) {
            highScore = score;
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"))) {
                writer.write(String.valueOf(highScore));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
