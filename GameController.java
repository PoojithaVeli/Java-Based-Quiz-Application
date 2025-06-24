package Project;

import java.util.*;

public class GameController {
    private Scanner sc = new Scanner(System.in);
    private Player player;
    private List<Question> questions = new ArrayList<>();
    private Lifeline audience = new AudiencePoll();
    private Lifeline fiftyFifty = new FiftyFifty();
    private Lifeline skip = new SkipQuestion();
    private int[] prizeStructure = {1000, 5000, 10000, 20000, 40000, 80000, 160000, 320000, 640000, 1250000};
    private int currentPrize = 0;
    private int fallbackPrize = 0;

    public GameController(Player player) {
        this.player = player;
        populateQuestions();
    }

    public void startGame() {
        System.out.println("=================================================");
        System.out.println("\n\u001B[33mPrize Chart:\u001B[0m");
        for (int i = 0; i < prizeStructure.length; i++) {
            System.out.println("Q" + (i + 1) + ": ₹" + prizeStructure[i]);
        }
        System.out.println("Safe Points: ₹5000 (Q2) and ₹80000 (Q6)");
        System.out.println("=================================================");

        boolean completedAll = true;

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);

            System.out.println("\n" + player.getName() + ", here is your question " + (i + 1) + ":");
            displayQuestion(q);

            int choice = getValidChoice(q);

            if (choice == -1) { // skipped
                System.out.println("\u001B[32mQuestion Skipped Successfully.\u001B[0m");
                currentPrize = prizeStructure[i];
                updateFallback(i);
                continue;
            }

            if (choice == q.getCorrectIndex() + 1) {
                System.out.println("\u001B[32mCongratulations, your answer is correct!\u001B[0m You win ₹" + prizeStructure[i]);
                currentPrize = prizeStructure[i];
                updateFallback(i);
            } else {
                System.out.println("\u001B[31mWrong answer!\u001B[0m You won ₹" + fallbackPrize);
                currentPrize = fallbackPrize;
                completedAll = false;
                break;
            }
        }

        System.out.println("-------------------------------------------------");
        System.out.println("\u001B[36mGame Over.\u001B[0m");
        System.out.println("Candidate Name: " + player.getName());
        System.out.println("City: " + player.getCity());
        System.out.println("Age: " + player.getAge());
        System.out.println("Total Winnings: ₹" + currentPrize);
        if (completedAll) {
            System.out.println("\n\u001B[32mCongratulations " + player.getName() + "! You answered all questions correctly!\u001B[0m");
        }
        System.out.println("================================================");
        System.out.println("\u001B[35m           Thank you for playing!     \u001B[0m");
        System.out.println("================================================");
    }

    private void displayQuestion(Question q) {
        System.out.println("-------------------------------------------------");
        System.out.println(q.getQuestionText());
        for (int j = 0; j < 4; j++) {
            System.out.println((j + 1) + ". " + q.getOptions()[j]);
        }
        System.out.println("5. Use Lifeline");
        System.out.println("-------------------------------------------------");
    }

    private int getValidChoice(Question q) {
        while (true) {
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            if (choice >= 1 && choice <= 4) {
                return choice;
            } else if (choice == 5) {
                if (!anyLifelineAvailable()) {
                    System.out.println("\u001B[31mNo Lifelines Available.\u001B[0m");
                } else {
                    boolean skipped = useLifeline(q);
                    if (skipped) {
                        return -1;
                    }
                }
                System.out.println("\n-------------------------------------------------");
                displayQuestion(q);
            } else {
                System.out.println("\u001B[31mInvalid choice. Try again.\u001B[0m");
            }
        }
    }

    private boolean useLifeline(Question q) {
        System.out.println("Available Lifelines:");
        if (!fiftyFifty.isUsed()) System.out.println("- 50/50");
        if (!audience.isUsed()) System.out.println("- Audience");
        if (!skip.isUsed()) System.out.println("- Skip");

        System.out.print("Choose a lifeline: ");
        String lifelineChoice = sc.next();

        try {
            switch (lifelineChoice.toLowerCase()) {
                case "50/50":
                    fiftyFifty.use(q);
                    return false;
                case "audience":
                    audience.use(q);
                    return false;
                case "skip":
                    skip.use(q);
                    return true;
                default:
                    System.out.println("\u001B[31mInvalid lifeline. Try again.\u001B[0m");
                    return false;
            }
        } catch (LifelineAlreadyUsedException e) {
            System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
            return false;
        }
    }

    private boolean anyLifelineAvailable() {
        return !fiftyFifty.isUsed() || !audience.isUsed() || !skip.isUsed();
    }

    private void updateFallback(int questionIndex) {
        if (questionIndex == 1) fallbackPrize = 5000;  // After Q2
        if (questionIndex == 5) fallbackPrize = 80000; // After Q6
    }

    private void populateQuestions() {
        questions.add(new Question("What is the size of int data type in Java?", 
            new String[]{"2 bytes", "4 bytes", "8 bytes", "16 bytes"}, 1));
        questions.add(new Question("Which of the following data types is used to store whole numbers in Java?", 
            new String[]{"int", "float", "char", "double"}, 0));
        questions.add(new Question("Which of the following is the default value of an integer in Java?", 
            new String[]{"0", "null", "undefined", "NaN"}, 0));
        questions.add(new Question("What is the result of 5 / 2 in Java?", 
            new String[]{"2.5", "2", "2.0", "3"}, 1));
        questions.add(new Question("Which of the following loops is guaranteed to run at least once?", 
            new String[]{"for", "while", "do-while", "foreach"}, 2));
        questions.add(new Question("What is the default value of a boolean variable in Java?", 
            new String[]{"true", "false", "null", "0"}, 1));
        questions.add(new Question("Which of these is a valid way to create an array in Java?", 
            new String[]{"int[] arr = new int[5];", "int arr[] = new int[5];", "int arr = new int[];", "All of the above"}, 3));
        questions.add(new Question("Which keyword is used to define a constant variable in Java?", 
            new String[]{"final", "constant", "static", "const"}, 0));
        questions.add(new Question("Which method is used to start a thread in Java?", 
            new String[]{"run()", "execute()", "start()", "begin()"}, 2));
        questions.add(new Question("Which of the following is not a primitive data type in Java?", 
            new String[]{"int", "boolean", "String", "char"}, 2));
    }
}

