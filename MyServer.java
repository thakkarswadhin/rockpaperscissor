import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * A HTTP server to play online rock paper scisssors game.
 * 
 * @author Swadhin Thakkar
 *
 */
@SuppressWarnings("restriction")
public class MyServer {
    private static HttpServer server = null;
    private static int numberOfGames;
    private static int gamesPlayed = 0;
    private static int serverScore = 0;
    private static int clientScore = 0;
    private static boolean flag = false;
    private static String ROCK_HASH = "";
    private static String PAPER_HASH = "";
    private static String SCISSORS_HASH = "";

    /**
     * The enum types for rock, paper, scissors.
     * 
     * @author Swadhin Thakkar
     *
     */
    private enum Game {
        ROCK, PAPER, SCISSORS;

        @Override
        public String toString() {
            return super.toString();
        }

        /**
         * Get the enum type for client.
         * 
         * @param clientString
         * @return
         */
        private static Game getEnum(String clientString) {
            if (clientString.equals(ROCK_HASH)) {
                return ROCK;
            } else if (clientString.equals(PAPER_HASH)) {
                return PAPER;
            } else if (clientString.equals(SCISSORS_HASH)) {
                return SCISSORS;
            } else {
                return null;
            }
        }

        /**
         * get the enum type for server.
         * 
         * @param clientString
         * @return
         */
        private static Game getServerEnum(String clientString) {
            if (clientString.equals("ROCK")) {
                return ROCK;
            } else if (clientString.equals("PAPER")) {
                return PAPER;
            } else if (clientString.equals("SCISSORS")) {
                return SCISSORS;
            } else {
                return null;
            }
        }

        /**
         * finds the winner.
         * 
         * @param server
         * @param client
         * @return
         */
        private static String findWinner(Game server, Game client) {
            if (server == client) {
                return "It's a tie";
            } else {
                switch (server) {
                case ROCK:
                    if (client == PAPER) {
                        clientScore++;
                        return "Client Wins";
                    } else {
                        serverScore++;
                        return "Server Wins";
                    }
                case PAPER:
                    if (client == SCISSORS) {
                        clientScore++;
                        return "Client Wins";
                    } else {
                        serverScore++;
                        return "Server Wins";
                    }
                case SCISSORS:
                    if (client == ROCK) {
                        clientScore++;
                        return "Client Wins";
                    } else {
                        serverScore++;
                        return "Server Wins";
                    }
                default:
                    return null;
                }
            }
        }
    }

    /**
     * Main method.
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // Set up the game server
        System.out.println("~~~~~~~~~~~~~~~Online ROCK-PAPER-SCISSORS begins~~~~~~~~~~~~~~~~~~~");
        System.out.println("Setting up game server");
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of rounds to be played (n should be an odd number) - ");
        try {
            numberOfGames = Integer.valueOf(scanner.nextLine());
        } catch (NumberFormatException n) {
            System.out.println("Please enter a valid number and try again.");
            return;
        }
        if (numberOfGames % 2 == 0) {
            System.out.println("Please enter an odd number and try again.");
            return;
        }
        System.out.println("Enter port - ");
        String port = scanner.nextLine();
        String url = "http://localhost" + ":" + port + "/play";
        System.out.println("Server running at: " + url);
        getHash();
        runServer(port);
    }

    /**
     * Handles HTTP GET requests.
     * 
     * @author Swadhin Thakkar
     *
     */
    private static class MyGetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            byte[] response = "~~~~~~~~~~~~~~~Online ROCK-PAPER-SCISSORS game~~~~~~~~~~~~~~~~~~~".getBytes();
            t.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }

    /**
     * Handles HTTP POST requsts.
     * 
     * @author Swadhin Thakkar
     *
     */
    private static class MyPostHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            // REQUEST Headers
            Headers requestHeaders = t.getRequestHeaders();
            int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));
            // REQUEST Body
            InputStream is = t.getRequestBody();
            // Request Data
            byte[] data = new byte[contentLength];
            @SuppressWarnings("unused")
            int length = is.read(data);
            String clientString = new String(data);
            // Play the game and get output
            String output = playGame(clientString);
            // RESPONSE Headers
            @SuppressWarnings("unused")
            Headers responseHeaders = t.getResponseHeaders();
            // Send RESPONSE Headers
            t.sendResponseHeaders(HttpURLConnection.HTTP_OK, output.length());
            System.out.println("Sending repsonse to client.. ");
            // RESPONSE Body
            OutputStream os = t.getResponseBody();
            os.write(output.getBytes());
            os.close();
            // Wait for enter key
            if (flag == true) {
                System.out.println("Online ROCK-PAPER-SCISSORS Game Ends...!");
                promptEnterKey();
            }
        }
    }

    /**
     * Checks if server won
     * 
     * @return
     */
    private static boolean checkForServerWin() {
        if (serverScore >= ((numberOfGames / 2) + 1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if client won.
     * 
     * @return
     */
    private static boolean checkForClientWin() {
        if (clientScore >= ((numberOfGames / 2) + 1)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Generate SHA-256 HASH for rock, paper, scissors
     */
    private static void getHash() {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
        }
        String text = "ROCK";
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        ROCK_HASH = Base64.getEncoder().encodeToString(hash);
        text = "PAPER";
        hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        PAPER_HASH = Base64.getEncoder().encodeToString(hash);
        text = "SCISSORS";
        hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        SCISSORS_HASH = Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Plays the rock-paper-scissor game
     * 
     * @param clientString
     * @return
     */
    private static String playGame(String clientString) {
        String output;
        // Server's play
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter server's play: {ROCK, PAPER, SCISSORS}");
        String serverString = scanner.nextLine();
        // Convert to enum type
        Game serverGame = Game.getServerEnum(serverString.toUpperCase());
        while (serverGame == null) {
            System.out.println("\nReceived invalid string");
            System.out.println("Please send correct value {ROCK, PAPER, SCISSORS}");
            System.out.println("Enter server's play: {ROCK, PAPER, SCISSORS}");
            serverString = scanner.nextLine();
            serverGame = Game.getServerEnum(serverString.toUpperCase());
        }
        // Convert to enum type
        Game clientGame = Game.getEnum(clientString);
        // Check the input stream for error strings
        if (clientGame == null) {
            System.out.println("\nReceived invalid string from client");
            output = "\nPlease send correct value {ROCK, PAPER, SCISSORS}";
            return output;
        } else {
            // Client's play
            System.out.println("\nClient plays ----->" + clientGame.toString());
            // Server's play
            System.out.println("Server plays ----->" + serverGame.toString());
            // Find the winner
            String result = Game.findWinner(serverGame, clientGame);
            gamesPlayed++;
            // Make the output string
            if (checkForServerWin()) {
                output = result + " the game. Server score:" + serverScore + " Client score:" + clientScore + " "
                        + "Games left:" + (numberOfGames - gamesPlayed);
                System.out.println(output);
                flag = true;
                return output;
            } else if (checkForClientWin()) {
                output = result + " the game. Server score:" + serverScore + " Client score:" + clientScore + " "
                        + "Games left:" + (numberOfGames - gamesPlayed);
                System.out.println(output);
                flag = true;
                return output;
            } else if (gamesPlayed < numberOfGames) {
                output = result + " in this round. Server score:" + serverScore + " Client score:" + clientScore + " "
                        + "Games left:" + (numberOfGames - gamesPlayed);
                System.out.println(output);
                return output + " Please reconnect..";
            } else {
                output = result + " in this round. Game is a tie! Server score:" + serverScore + " Client score:"
                        + clientScore + " " + "Games left:" + (numberOfGames - gamesPlayed);
                System.out.println(output);
                flag = true;
                return output;
            }
        }
    }

    /**
     * Runs the HTTP server.
     * 
     * @param port
     */
    private static void runServer(String port) {
        // Run the server
        try {
            server = HttpServer.create(new InetSocketAddress(Integer.valueOf(port)), 0);
            // For HTTP GET requests
            server.createContext("/about", new MyGetHandler());
            // For HTTP POST requests
            server.createContext("/play", new MyPostHandler());
            server.setExecutor(null);
            server.start();
        } catch (IllegalArgumentException iae) {
            System.out.println("\nPlease enter correct port and try again");
            return;
        } catch (Exception e) {
            System.out.println("\nError setting up server.. Please try again");
            return;
        }
    }

    /**
     * Waits for enter key.
     */
    private static void promptEnterKey() {
        System.out.println("\nServer shutting..");
        server.stop(0);
        System.out.println("Press \"ENTER\" to end...");
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.println("~~~~~~~~~~~~~~~Online ROCK-PAPER-SCISSORS ends~~~~~~~~~~~~~~~~~~~");
    }
}
