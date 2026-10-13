import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class Extreme extends JFrame {
    private int numberToGuess;
    private int maxRange = 100;
    private int maxAttempts = 10;
    private int attempts = 0;
    private int bestScore = Integer.MAX_VALUE;
    private String currentDifficulty = "Medium";

    private JComboBox<String> difficultyBox;
    private JTextField inputField;
    private JLabel hintLabel;
    private JLabel attemptLabel;
    private JTable scoreTable;
    private DefaultTableModel tableModel;
    private JPanel gamePanel;
    private JPanel infoPanel;

    public Extreme() {
        setTitle("🎯 Guess the Number Game");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Background gradient panel
        gamePanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(70, 130, 180); // Steel Blue
                Color color2 = new Color(173, 216, 230); // Light Blue
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gamePanel.setLayout(new GridLayout(7, 1));
        add(gamePanel, BorderLayout.CENTER);

        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        difficultyBox = new JComboBox<>(new String[]{"Easy (1–50)", "Medium (1–100)", "Hard (1–500)"});
        JButton startButton = new JButton("Start Game");
        JButton infoButton = new JButton("ℹ️");

        topPanel.add(new JLabel("Difficulty:"));
        topPanel.add(difficultyBox);
        topPanel.add(startButton);
        topPanel.add(infoButton);
        gamePanel.add(topPanel);

        // Input Field with Placeholder
        inputField = new JTextField("Enter your guess...");
        inputField.setEnabled(false);
        inputField.setForeground(Color.GRAY);
        inputField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (inputField.getText().equals("Enter your guess...")) {
                    inputField.setText("");
                    inputField.setForeground(Color.BLACK);
                }
            }
        });
        gamePanel.add(inputField);

        hintLabel = new JLabel(" ", SwingConstants.CENTER);
        hintLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gamePanel.add(hintLabel);

        attemptLabel = new JLabel("Attempts: 0", SwingConstants.CENTER);
        gamePanel.add(attemptLabel);

        // Info Panel (initially hidden)
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(0, 0));
        infoPanel.setBackground(new Color(245, 255, 250)); // Mint cream
        JLabel instructions = new JLabel("<html><h3>Instructions:</h3>" +
                "<ul>" +
                "<li>Select difficulty and start the game.</li>" +
                "<li>Guess a number between the selected range.</li>" +
                "<li>You'll get hints (Too High/Too Low).</li>" +
                "<li>Track your score in the table to the right.</li>" +
                "</ul></html>");
        infoPanel.add(instructions);
        add(infoPanel, BorderLayout.NORTH);

        // Score Table Panel
        String[] columns = {"Difficulty", "Result", "Attempts"};
        tableModel = new DefaultTableModel(columns, 0);
        scoreTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        scrollPane.setPreferredSize(new Dimension(250, 500));
        add(scrollPane, BorderLayout.EAST);

        // Event Listeners
        startButton.addActionListener(_ -> startGame());
        inputField.addActionListener(_ -> checkGuess());
        infoButton.addActionListener(_ -> toggleInfoPanel());

        setVisible(true);
    }

    private void toggleInfoPanel() {
        if (infoPanel.getPreferredSize().height == 0) {
            infoPanel.setPreferredSize(new Dimension(700, 120));
        } else {
            infoPanel.setPreferredSize(new Dimension(700, 0));
        }
        revalidate();
    }

    private void startGame() {
        attempts = 0;
        attemptLabel.setText("Attempts: 0");
        hintLabel.setText(" ");
        inputField.setText("Enter your guess...");
        inputField.setForeground(Color.GRAY);
        inputField.setEnabled(true);

        int choice = difficultyBox.getSelectedIndex();
        switch (choice) {
            case 0: maxRange = 50; maxAttempts = 7; currentDifficulty = "Easy"; break;
            case 1: maxRange = 100; maxAttempts = 10; currentDifficulty = "Medium"; break;
            case 2: maxRange = 500; maxAttempts = 15; currentDifficulty = "Hard"; break;
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
                if (attempts < bestScore) bestScore = attempts;
                addScore(currentDifficulty, "Success", attempts);
                showResultDialog("🎉 You guessed it in " + attempts + " attempts!");
                return;
            }

            if (attempts >= maxAttempts && guess != numberToGuess) {
                hintLabel.setText("❌ Game Over!");
                addScore(currentDifficulty, "Fail", attempts);
                showResultDialog("❌ Out of attempts! Number was: " + numberToGuess);
            }

            inputField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }

    private void addScore(String difficulty, String result, int attemptCount) {
        Vector<String> row = new Vector<>();
        row.add(difficulty);
        row.add(result);
        row.add(String.valueOf(attemptCount));
        tableModel.addRow(row);
    }

    private void showResultDialog(String message) {
        inputField.setEnabled(false);
        int option = JOptionPane.showConfirmDialog(this, message + "\nPlay again?", "Result", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            startGame();
        } else {
            JOptionPane.showMessageDialog(this, "👋 Thanks for playing!\nBest Score: " +
                    (bestScore == Integer.MAX_VALUE ? "N/A" : bestScore + " attempts."));
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Extreme();
    }
}
