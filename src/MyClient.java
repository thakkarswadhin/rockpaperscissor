import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

/**
 * A HTTP client to play online rock paper scisssors game.
 * @author Swadhin Thakkar
 *
 */
public class MyClient {
    private static final String USER_AGENT = "Mozilla/5.0";
    public MyClient() {
    }
    /**
     * Main method.
     * @param args input args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // Set up the client
        System.out.println("~~~~~~~~~~~~~~~Online ROCK-PAPER-SCISSORS begins~~~~~~~~~~~~~~~~~~~");
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter server details..");
        System.out.println("IP address: ");
        String ipAddress = scanner.nextLine();
        System.out.println("Port: ");
        String port = scanner.nextLine();
        String url = "http://" + ipAddress + ":" + port + "/play";
        System.out.println("Conneting to server running at: " + url);
        try {
            sendPost(ipAddress, port);
        } catch (IOException e) {
            System.out.println("Error connecting. Try again!");
            return;
        }
    }

    /**
     * Method to send GET requests.
     * @param ipAddress
     * @param port
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private static void sendGet(String ipAddress, String port) throws IOException {
        String url = "http://" + ipAddress + ":" + port + "/about";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // GET command
        con.setRequestMethod("GET");
        // add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());
        } else {
            System.out.println("GET request not worked");
        }
    }

    /**
     * Method to send post requests
     * @param ipAddress
     * @param port
     * @throws IOException
     */
    private static void sendPost(String ipAddress, String port) throws IOException {
        // make the URL
        String url = "http://" + ipAddress + ":" + port + "/play";
        // connect to the server
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        // Get the client's play
        String POST_PARAMS = getClientPlay();
        System.out.println("Sending HTTP POST request..");
        // For POST command
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // Response from server
        int responseCode = con.getResponseCode();
        // Read data
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            System.out.println("\nResponse form server: \n" + response.toString());
            // Wait for enter key
            promptEnterKey();
        } else {
            System.out.println("POST request not worked");
        }
    }

    /**
     * To get's client play.
     * @return
     */
    private static String getClientPlay() {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter client's play: {ROCK, PAPER, SCISSORS}");
        String clientString = scanner.nextLine();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
        }
        byte[] hash = digest.digest(clientString.toUpperCase().getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Wait for enter key.
     */
    private static void promptEnterKey() {
        System.out.println("\nClient shutting..");
        System.out.println("Press \"ENTER\" to end...");
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.println("~~~~~~~~~~~~~~~Online ROCK-PAPER-SCISSORS ends~~~~~~~~~~~~~~~~~~~");
    }
}
