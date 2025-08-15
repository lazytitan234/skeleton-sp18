package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.Random;

public class Game implements Serializable {
    TERenderer ter = new TERenderer();
    public final int WIDTH = 80;
    public final int HEIGHT = 30;
    public TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
    public long SEED = 0;
    public Random RANDOM;
    public char[] commands;
    private Player player;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() throws IOException, ClassNotFoundException {
        //set up the stage for drawing
        setDrawingStage();

        //display main menu
        displayMainMenu();

        //get player input
        String input = solicitNCharsInput(1);

        //handling new game
        if (input.equalsIgnoreCase("N")) {
            //get seed from user and draw world
            drawFrame("Enter A Random Seed (numbers), followed by 'S'");
            String seed = solicitSeed();
            SEED = Long.parseLong(seed);
            finalWorldFrame = playWithInputString("N" + SEED + "S");

            //execute gameplay
            boolean gameOn = true;
            while (gameOn) {
                displayBlock();
                if (StdDraw.hasNextKeyTyped()) {
                    gameOn = playGame();
                }
            }
            playWithKeyboard();

            //handling loading of saved game
        } else if (input.equalsIgnoreCase("L")) {
            Game previousGame = loadSavedGame();
            //restore previous world and draw it
            this.finalWorldFrame = previousGame.finalWorldFrame;
            this.player = previousGame.player;
            this.SEED = previousGame.SEED;
            this.RANDOM = previousGame.RANDOM;
            //execute movement commands and save + quit if necessary
            boolean gameOn = true;
            while (gameOn) {
                displayBlock();
                if (StdDraw.hasNextKeyTyped()) {
                    gameOn = playGame();
                }
            }
            playWithKeyboard();
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) throws IOException, ClassNotFoundException {
        //parse input
        char[] inputs = input.toCharArray();
        int[] seedDigits = new int[20];
        //get seed if player initialises new game
        if (inputs[0] == 'N') {
            int i = 1;
            while (inputs[i] != 'S') {
                seedDigits[i - 1] = Character.getNumericValue(inputs[i]);
                i++;
            }
            int trueArraySize = i - 1;
            for (int j = 0; j < trueArraySize; j++) {
                SEED = (long) (SEED + seedDigits[j] * Math.pow(10, trueArraySize - j - 1));
            }
            RANDOM = new Random(SEED);
            commands = new char[inputs.length - i];
            for (int k = 0; k < inputs.length - i; k++) {
                commands[k] = inputs[i + k];
            }
        } else {
            commands = new char[inputs.length];
            for (int i = 0; i < inputs.length; i++) {
                commands[i] = inputs[i];
            }
        }
        // commands always include L or S as the first letter.

        // initialize tiles
        if (commands[0] == 'S') {
            for (int x = 0; x < WIDTH; x += 1) {
                for (int y = 0; y < HEIGHT; y += 1) {
                    finalWorldFrame[x][y] = Tileset.NOTHING;
                }
            }
            //get position of first room (bottom left block)
            //this should be in the left 1/8th and bottom 1/8th of the world
            int xCor = RANDOM.nextInt(WIDTH / 8);
            int yCor = RANDOM.nextInt(HEIGHT / 8);
            Position firstRoomBL = new Position(xCor, yCor, true);

            //get size of first room(includes walls too. width x height)
            int width = RANDOM.nextInt(WIDTH / 8) + 3;
            int height = RANDOM.nextInt(HEIGHT / 6) + 3;

            //kickstart the generating process by generating first room
            generateRooms(finalWorldFrame, firstRoomBL, width, height);

            //create player and place it down
            player = new Player(xCor + 1, yCor + 1);
            finalWorldFrame[player.xCor][player.yCor] = Tileset.PLAYER;

            //execute movement commands and save + quit if necessary
            for (int i = 1; i < commands.length; i++) {
                if (i + 1 < commands.length && (commands[i] == ':' && commands[i + 1] == 'Q')) {
                    saveAndQuit();
                } else {
                    executeMovementCommand(finalWorldFrame, commands[i]);
                }
            }
            return finalWorldFrame;
        } else {
            //load previous game in
            Game previousGame = loadSavedGame();
            //restore previous world
            this.finalWorldFrame = previousGame.finalWorldFrame;
            this.player = previousGame.player;
            this.SEED = previousGame.SEED;
            this.RANDOM = previousGame.RANDOM;
            //execute movement commands and save + quit if necessary
            for (int i = 1; i < commands.length; i++) {
                if (i + 1 < commands.length && (commands[i] == ':' && commands[i + 1] == 'Q')) {
                    saveAndQuit();
                } else {
                    executeMovementCommand(finalWorldFrame, commands[i]);
                }
            }
            return finalWorldFrame;
        }
    }

    // tree recursive method that recursively generates rooms. generates one room,
    // then tries to generate one room above, one below, one to the right.
    private void generateRooms(TETile[][] world, Position position, int width, int height) {
        if (positionAllowed(world, position, width, height)) {
            //generate a single room
            generateSingleRoom(world, position, width, height);

            //get 3 positions
            int x0;
            int y0;
            Position p0;
            int newWidth0;
            int newHeight0;
            int x1;
            int y1;
            Position p1;
            int newWidth1;
            int newHeight1;
            int x2;
            int y2;
            Position p2;
            int newWidth2;
            int newHeight2;

            if (position.BL) {
                //consider width or height being 3
                if (width == 3|| height == 3) {
                    x0 = position.xCor;
                    y0 = position.yCor + height - 1;
                    p0 = new Position(x0, y0, true);
                    newWidth0 = RANDOM.nextInt(WIDTH / 8) + 3;
                    newHeight0 = RANDOM.nextInt(HEIGHT / 6) + 3;
                    if (positionAllowed(world, p0, newWidth0, newHeight0)) {
                        generateRooms(world, p0, newWidth0, newHeight0);
                        world[x0 + 1][y0] = Tileset.FLOOR;
                    }
                    //generate first room. check if it's generated. make connection.
                    // consider position allowed to see if it's generated??
                    x1 = position.xCor + width - 1;
                    y1 = position.yCor;
                    p1 = new Position(x1, y1, true);
                    newWidth1 = RANDOM.nextInt(WIDTH / 8) + 3;
                    newHeight1 = RANDOM.nextInt(HEIGHT / 6) + 3;
                    if (positionAllowed(world, p1, newWidth1, newHeight1)) {
                        generateRooms(world, p1, newWidth1, newHeight1);
                        world[x1][y1 + 1] = Tileset.FLOOR;
                    }
                    //don't bother with third room
                } else {
                    //top
                    x0 = position.xCor + RANDOM.nextInt(width - 3);
                    y0 = position.yCor + height - 1;
                    p0 = new Position(x0, y0, true);
                    newWidth0 = RANDOM.nextInt(WIDTH / 8) + 3;
                    newHeight0 = RANDOM.nextInt(HEIGHT / 6) + 3;
                    if (positionAllowed(world, p0, newWidth0, newHeight0)) {
                        generateRooms(world, p0, newWidth0, newHeight0);
                        world[x0 + 1][y0] = Tileset.FLOOR;
                    }

                    //right
                    x1 = position.xCor + width - 1;
                    y1 = position.yCor + RANDOM.nextInt(height - 2);
                    p1 = new Position(x1, y1, true);
                    newWidth1 = RANDOM.nextInt(WIDTH / 8) + 3;
                    newHeight1 = RANDOM.nextInt(HEIGHT / 6) + 3;
                    if (positionAllowed(world, p1, newWidth1, newHeight1)) {
                        generateRooms(world, p1, newWidth1, newHeight1);
                        world[x1][y1 + 1] = Tileset.FLOOR;
                    }

                    //bottom
                    x2 = position.xCor + 2 + RANDOM.nextInt(width - 3);
                    y2 = position.yCor;
                    p2 = new Position(x2, y2, false);
                    newWidth2 = RANDOM.nextInt(WIDTH / 8) + 3;
                    newHeight2 = RANDOM.nextInt(HEIGHT / 6) + 3;
                    if (positionAllowed(world, p2, newWidth2, newHeight2)) {
                        generateRooms(world, p2, newWidth2, newHeight2);
                        world[x2 - 1][y2] = Tileset.FLOOR;
                    }
                }
            } else {
                //consider width or height being 3
                if (width == 3|| height == 3) {
                    x0 = position.xCor - width + 1;
                    y0 = position.yCor;
                    p0 = new Position(x0, y0, true);
                    newWidth0 = RANDOM.nextInt(WIDTH / 8) + 3;
                    newHeight0 = RANDOM.nextInt(HEIGHT / 6) + 3;
                    if (positionAllowed(world, p0, newWidth0, newHeight0)) {
                        generateRooms(world, p0, newWidth0, newHeight0);
                        world[x0 + 1][y0] = Tileset.FLOOR;
                    }
                    //generate first room. check if it's generated. make connection.
                    // consider position allowed to see if it's generated??
                    x1 = position.xCor;
                    y1 = position.yCor - height + 1;
                    p1 = new Position(x1, y1, true);
                    newWidth1 = RANDOM.nextInt(WIDTH / 8) + 3;
                    newHeight1 = RANDOM.nextInt(HEIGHT / 6) + 3;
                    if (positionAllowed(world, p1, newWidth1, newHeight1)) {
                        generateRooms(world, p1, newWidth1, newHeight1);
                        world[x1][y1 + 1] = Tileset.FLOOR;
                    }
                    //don't bother with third room
                } else {
                    //top
                    x0 = position.xCor - 2 - RANDOM.nextInt(width - 2);
                    y0 = position.yCor;
                    p0 = new Position(x0, y0, true);
                    newWidth0 = RANDOM.nextInt(WIDTH / 8) + 3;
                    newHeight0 = RANDOM.nextInt(HEIGHT / 6) + 3;
                    if (positionAllowed(world, p0, newWidth0, newHeight0)) {
                        generateRooms(world, p0, newWidth0, newHeight0);
                        world[x0 + 1][y0] = Tileset.FLOOR;
                    }

                    //right
                    x1 = position.xCor;
                    y1 = position.yCor - 2 - RANDOM.nextInt(height - 2);
                    p1 = new Position(x1, y1, true);
                    newWidth1 = RANDOM.nextInt(WIDTH / 8) + 3;
                    newHeight1 = RANDOM.nextInt(HEIGHT / 6) + 3;
                    if (positionAllowed(world, p1, newWidth1, newHeight1)) {
                        generateRooms(world, p1, newWidth1, newHeight1);
                        world[x1][y1 + 1] = Tileset.FLOOR;
                    }

                    //bottom
                    x2 = position.xCor - 1 - RANDOM.nextInt(width - 3);
                    y2 = position.yCor - height + 1;
                    p2 = new Position(x2, y2, false);
                    newWidth2 = RANDOM.nextInt(WIDTH / 8) + 3;
                    newHeight2 = RANDOM.nextInt(HEIGHT / 6) + 3;
                    if (positionAllowed(world, p2, newWidth2, newHeight2)) {
                        generateRooms(world, p2, newWidth2, newHeight2);
                        world[x2 - 1][y2] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    private void generateSingleRoom(TETile[][] world, Position position, int width, int height) {
        if (position.BL) {
            for (int i = position.xCor; i < position.xCor + width; i++) {
                world[i][position.yCor] = Tileset.WALL;
                world[i][position.yCor + height - 1] = Tileset.WALL;
            }
            for (int i = position.yCor; i < position.yCor + height; i++) {
                world[position.xCor][i] = Tileset.WALL;
                world[position.xCor + width - 1][i] = Tileset.WALL;
            }
            for (int i = position.xCor + 1; i < position.xCor + width - 1; i++) {
                for (int j =position.yCor + 1; j < position.yCor + height - 1; j++) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        } else {
            for (int i = position.xCor; i > position.xCor - width; i--) {
                world[i][position.yCor] = Tileset.WALL;
                world[i][position.yCor - height + 1] = Tileset.WALL;
                }
            for (int i = position.yCor; i > position.yCor - height; i--) {
                world[position.xCor][i] = Tileset.WALL;
                world[position.xCor - width + 1][i] = Tileset.WALL;
                }
            for (int i = position.xCor - 1; i > position.xCor - width + 1; i--) {
                for (int j = position.yCor - 1; j > position.yCor - height + 1; j--) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        }
    }

    //ensures the proposed room does not exceed edges of map or cut into other rooms' floor
    private boolean positionAllowed(TETile[][] world, Position position, int width, int height) {
        if (position.BL) {
            if (position.xCor < 0 || position.xCor + width > WIDTH) {
                return false;
            }
            if (position.yCor < 0 || position.yCor + height > HEIGHT) {
                return false;
            }
            for (int i = position.xCor; i < position.xCor + width; i++) {
                for (int j = position.yCor; j < position.yCor + height; j++) {
                    if ((world[i][j]).character() == '·') {
                        return false;
                    }
                }
            }
        } else {
            if (position.xCor + 1 > WIDTH || position.xCor - width + 1 < 0) {
                return false;
            }
            if (position.yCor + 1 > HEIGHT || position.yCor - height + 1 < 0) {
                return false;
            }
            for (int i = position.xCor; i > position.xCor - width; i--) {
                for (int j = position.yCor; j > position.yCor - height; j--) {
                    if ((world[i][j]).character() == '·') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //executes movement commands
    private void executeMovementCommand(TETile[][] world, char command) throws IOException {
        if (command == 'W' || command == 'w') {
            //check if it's possible to move up, then move up if necessary
            if (world[player.xCor][player.yCor + 1].character() == Tileset.FLOOR.character()) {
                world[player.xCor][player.yCor] = Tileset.FLOOR;
                world[player.xCor][player.yCor + 1] = Tileset.PLAYER;
                player.yCor++;
            }
        } else if (command == 'A'|| command == 'a') {
            //check if it's possible to move left, then move left if necessary
            if (world[player.xCor - 1][player.yCor].character() == Tileset.FLOOR.character()) {
                world[player.xCor][player.yCor] = Tileset.FLOOR;
                world[player.xCor - 1][player.yCor] = Tileset.PLAYER;
                player.xCor--;
            }
        } else if (command == 'S'|| command == 's') {
            //down
            if (world[player.xCor][player.yCor - 1].character() == Tileset.FLOOR.character()) {
                world[player.xCor][player.yCor] = Tileset.FLOOR;
                world[player.xCor][player.yCor - 1] = Tileset.PLAYER;
                player.yCor--;
            }
        } else if (command == 'D'|| command == 'd') {
            //right
            if (world[player.xCor + 1][player.yCor].character() == Tileset.FLOOR.character()) {
                world[player.xCor][player.yCor] = Tileset.FLOOR;
                world[player.xCor + 1][player.yCor] = Tileset.PLAYER;
                player.xCor++;
            }
        }
    }

    public void saveAndQuit() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("gamestate.ser"));
        out.writeObject(this);
        out.close();
    }

    public Game loadSavedGame() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("gamestate.ser"));
        Game previousGame = (Game) in.readObject();
        in.close();
        return previousGame;
    }

    public boolean playGame() throws IOException {
        String command = solicitNCharsInput(1);
        String nextCommand;
        if (command.equals(":")) {
            while (true) {
                if (StdDraw.hasNextKeyTyped()) {
                    nextCommand = solicitNCharsInput(1);
                    String fullCommand = command + nextCommand;
                    if (fullCommand.equalsIgnoreCase(":Q")) {
                        saveAndQuit();
                        return false;
                    } else {
                        executeMovementCommand(finalWorldFrame, nextCommand.charAt(0));
                        drawWorld(finalWorldFrame);
                        return true;
                    }
                } else {
                    displayBlock();
                }
            }
        } else {
            executeMovementCommand(finalWorldFrame, command.charAt(0));
            drawWorld(finalWorldFrame);
            return true;
        }
    }

    public void setDrawingStage() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16 + 100);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public void displayMainMenu() {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(WIDTH / 2, 7 * HEIGHT / 10, "Painful Project");
        StdDraw.text(WIDTH / 2, 4 * HEIGHT / 10, "New Game (N)");
        StdDraw.text(WIDTH / 2, 3 * HEIGHT / 10, "Load Game (L)");
        StdDraw.text(WIDTH / 2, 2 * HEIGHT / 10, "Quit (Q)");
        StdDraw.show();
    }

    public void drawFrame(String s) {
        //displays a string at the centre of the screen
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        StdDraw.show();
    }

    public void displayBlockHelper(double x, double y) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < WIDTH) {
            String description = finalWorldFrame[(int) x][(int) y].description();
            Font font = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(font);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.text(WIDTH / 20, 9 * HEIGHT / 10, description);
            StdDraw.show();
        }
    }

    public void displayBlock() {
        drawWorld(finalWorldFrame);
        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();
        displayBlockHelper(mouseX, mouseY);
        StdDraw.pause(20);
        StdDraw.clear();
    }

    public void drawWorld(TETile[][] world) {
        //displays a world
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 10);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                char symbol = world[x][y].character();
                StdDraw.text(x, y, String.valueOf(symbol));
            }
        }
        StdDraw.show();
    }

    public String solicitNCharsInput(int n) {
        //read n letters of player input
        String stringTyped = "";
        int count = 0;
        while (count < n) {
            if (StdDraw.hasNextKeyTyped()) {
                stringTyped = stringTyped + StdDraw.nextKeyTyped();
                count++;
            }
        }
        return stringTyped;
    }

    public String solicitSeed() {
        //gets seed from player
        String seed = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char stringTyped = StdDraw.nextKeyTyped();
                if (stringTyped == 's' || stringTyped == 'S') {
                    return seed;
                }
                seed = seed + stringTyped;
                drawFrame(seed);
            }
        }
    }

    private class Position implements Serializable {
        int xCor;
        int yCor;
        boolean BL; // this set of coordinates either refer to BL or TR

        private Position(int x, int y, boolean BL) {
            this.xCor = x;
            this.yCor = y;
            this.BL = BL;
        }
    }

    private class Player implements Serializable {
        int xCor;
        int yCor;

        private Player(int x, int y) {
            this.xCor = x;
            this.yCor = y;
        }
    }
}
