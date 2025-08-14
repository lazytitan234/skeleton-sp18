package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        Random stringGenerator = new Random(seed);
        MemoryGame game = new MemoryGame(40, 40, stringGenerator);
        game.startGame();
    }

    public MemoryGame(int width, int height, Random rand) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        this.rand = rand;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public String generateRandomString(int n) {
        String randomString = "";
        for (int i = 0; i < n; i++) {
            int randomIndex = this.rand.nextInt(26);
            randomString = randomString + this.CHARACTERS[randomIndex];
        }
        return randomString;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(String.valueOf(letters.charAt(i)));
            StdDraw.pause(1000);
            StdDraw.clear(Color.BLACK);
            drawFrame("");
            StdDraw.pause(500);
        }
        drawFrame("");  // Clear any lingering display
        StdDraw.pause(300);
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String stringTyped = "";
        int count = 0;
        while (count < n) {
            if (StdDraw.hasNextKeyTyped()) {
                stringTyped = stringTyped + StdDraw.nextKeyTyped();
                count++;
                drawFrame(stringTyped);
            }
        }
        this.playerTurn = false;
        return stringTyped;
    }

    public void startGame() {
        this.round = 1;
        this.gameOver = false;
        this.playerTurn = false;
        String stringTyped = "";
        while (!this.gameOver) {
            String msg = "Round " + this.round;
            drawFrame(msg);
            StdDraw.pause(1000);
            String randomString = generateRandomString(this.round);
            flashSequence(randomString);
            this.playerTurn = true;
            if (playerTurn) {
                while (StdDraw.hasNextKeyTyped()) {
                    StdDraw.nextKeyTyped(); // discard
                }
                stringTyped = solicitNCharsInput(this.round);
            }
            if (randomString.equals(stringTyped)) {
                this.round++;
            } else {
                this.gameOver = true;
                String failureMSG = "Game Over! You made it to round: " + this.round;
                drawFrame(failureMSG);
            }
        }
    }
}
