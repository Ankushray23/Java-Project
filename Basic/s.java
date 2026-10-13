import javax.swing.*;
import java.awt.*;

public class s extends JFrame {
    private int numberToGuess;
    private int maxRange = 100;
    private int maxAttempts = 10;
    private int attempts = 0;
    private int bestScore = Integer.MAX_VALUE;

    private JComboBox<String> difficultyBox;
    private JTextField inputField;
    private JLabel hintLabel;
    private JLabel attemptLabel;

    // ✅ Proper constructor (no return type)
    public s() {
        setTitle("🎯 Guess the Number Game");
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        // UI Components
        add(new JLabel("Select Difficulty:", SwingConstants.CENTER));

        difficultyBox = new JComboBox<>(new String[]{"Easy (1–50)", "Medium (1–100)", "Hard (1–500)"});
        add(difficultyBox);

        JButton startButton = new JButton("Start Game");
        add(startButton);

        inputField = new JTextField();
        inputField.setEnabled(false);
        add(inputField);

        hintLabel = new JLabel(" ", SwingConstants.CENTER);
        add(hintLabel);

        attemptLabel = new JLabel("Attempts: 0", SwingConstants.CENTER);
        add(attemptLabel);

        // Event listeners
        startButton.addActionListener(_ -> startGame());
        inputField.addActionListener(_ -> checkGuess());

        setVisible(true);
    }

    private void startGame() {
        attempts = 0;
        attemptLabel.setText("Attempts: 0");
        hintLabel.setText(" ");
        inputField.setText("");
        inputField.setEnabled(true);

        int choice = difficultyBox.getSelectedIndex();
        switch (choice) {
            case 0: maxRange = 50; maxAttempts = 7; break;
            case 1: maxRange = 100; maxAttempts = 10; break;
            case 2: maxRange = 500; maxAttempts = 15; break;
        }

        numberToGuess = (int)(Math.random() * maxRange) + 1;
    }

    private void checkGuess() {
        try {
            int guess = Integer.parseInt(inputField.getText());
            attempts++;
            attemptLabel.setText("Attempts: " + attempts);

            if (guess < numberToGuess) {
                hintLabel.setText("📉 Too low!");
            } else if (guess > numberToGuess) {
                hintLabel.setText("📈 Too high!");
            } else {
                hintLabel.setText("🎉 Correct!");
                if (attempts < bestScore) {
                    bestScore = attempts;
                    JOptionPane.showMessageDialog(this, "🎉 You guessed it in " + attempts + " attempts!\n🏆 New Best Score!");
                } else {
                    JOptionPane.showMessageDialog(this, "🎉 You guessed it in " + attempts + " attempts!");
                }
                playAgainPrompt();
            }

            if (attempts >= maxAttempts && guess != numberToGuess) {
                JOptionPane.showMessageDialog(this, "❌ Out of attempts! The number was: " + numberToGuess);
                playAgainPrompt();
            }

            inputField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }

    private void playAgainPrompt() {
        inputField.setEnabled(false);
        int option = JOptionPane.showConfirmDialog(this, "Play again?", "Continue", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            startGame();
        } else {
            JOptionPane.showMessageDialog(this, "👋 Thanks for playing!\nBest Score: " +
                    (bestScore == Integer.MAX_VALUE ? "N/A" : bestScore + " attempts."));
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new s(); // ✅ Use the correct constructor
    }
}
