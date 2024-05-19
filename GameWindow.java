import javax.swing.JFrame;

public class GameWindow extends JFrame {
    public GameWindow() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        GameCanvas canvas = new GameCanvas();
        add(canvas);
        pack();
        
        setLocationRelativeTo(null);
        setVisible(true);
        canvas.startGame();
    }
    
    public static void main(String[] args) {
        new GameWindow();
    }
}
