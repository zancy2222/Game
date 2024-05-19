import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class GamePanel extends JPanel {
    private static final int ROWS = 6;
    private static final int COLS = 5;
    private static final String TARGET_WORD = "smart"; // Example target word

    private final JTextField[][] grid;
    private int currentRow;

    public GamePanel() {
        setLayout(new GridLayout(ROWS, COLS));
        grid = new JTextField[ROWS][COLS];
        currentRow = 0;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = createGridCell();
                add(grid[row][col]);
            }
        }

        JTextField inputField = new JTextField();
        inputField.addActionListener(new InputHandler());
        add(inputField, BorderLayout.SOUTH);
    }

    private JTextField createGridCell() {
        JTextField cell = new JTextField();
        cell.setHorizontalAlignment(JTextField.CENTER);
        cell.setEditable(false);
        return cell;
    }

    private class InputHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField inputField = (JTextField) e.getSource();
            String guess = inputField.getText().trim().toLowerCase();

            if (guess.length() == COLS && currentRow < ROWS) {
                updateGrid(guess);
                inputField.setText("");
                currentRow++;
            }
        }

        private void updateGrid(String guess) {
            for (int col = 0; col < COLS; col++) {
                grid[currentRow][col].setText(String.valueOf(guess.charAt(col)));
                if (TARGET_WORD.charAt(col) == guess.charAt(col)) {
                    grid[currentRow][col].setBackground(Color.GREEN);
                } else if (TARGET_WORD.contains(String.valueOf(guess.charAt(col)))) {
                    grid[currentRow][col].setBackground(Color.YELLOW);
                } else {
                    grid[currentRow][col].setBackground(Color.GRAY);
                }
            }
        }
    }
}
