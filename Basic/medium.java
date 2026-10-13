import java.util.Scanner;

public class medium {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int bestScore = Integer.MAX_VALUE;
        boolean playAgain;

        System.out.println("🎯 Welcome to the Guess the Number Game!");

        do {
            int maxRange = 100;
            int maxAttempts = 10;

            // Choose difficulty
            System.out.println("\nChoose difficulty level:");
            System.out.println("1. Easy (1–50)");
            System.out.println("2. Medium (1–100)");
            System.out.println("3. Hard (1–500)");
            System.out.print("Enter choice (1/2/3): ");
            int difficulty = scanner.nextInt();

            switch (difficulty) {
                case 1:
                    maxRange = 50;
                    maxAttempts = 7;
                    break;
                case 2:
                    maxRange = 100;
                    maxAttempts = 10;
                    break;
                case 3:
                    maxRange = 500;
                    maxAttempts = 15;
                    break;
                default:
                    System.out.println("Invalid choice. Defaulting to Medium.");
                    difficulty = 2;
            }

            int numberToGuess = (int)(Math.random() * maxRange) + 1;
            int guess = 0;
            int attempts = 0;
            boolean guessedCorrectly = false;

            System.out.println("\nI've picked a number between 1 and " + maxRange + ". Try to guess it!");

            while (attempts < maxAttempts) {
                System.out.print("Guess (" + (maxAttempts - attempts) + " attempts left): ");
                guess = scanner.nextInt();
                attempts++;

                if (guess < numberToGuess) {
                    System.out.println("📉 Too low!");
                } else if (guess > numberToGuess) {
                    System.out.println("📈 Too high!");
                } else {
                    System.out.println("🎉 Congratulations! You guessed it in " + attempts + " attempts.");
                    guessedCorrectly = true;
                    if (attempts < bestScore) {
                        bestScore = attempts;
                        System.out.println("🏆 New Best Score!");
                    }
                    break;
                }
            }

            if (!guessedCorrectly) {
                System.out.println("❌ Out of attempts! The number was: " + numberToGuess);
            }

            System.out.print("\nWould you like to play again? (yes/no): ");
            playAgain = scanner.next().equalsIgnoreCase("yes");

        } while (playAgain);

        System.out.println("\n👋 Thanks for playing! Your best score: " + (bestScore == Integer.MAX_VALUE ? "N/A" : bestScore + " attempts."));
        scanner.close();
    }
}
