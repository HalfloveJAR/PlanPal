package me.kobeplane;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class FirebaseAuthHelper {
    private static final String API_KEY = Main.dotenv.get("FIREBASE_API_KEY");
    // Retrieve Firebase API key securely from environment variable
    // This prevents hardcoding sensitive information in the source code,
    // reducing the risk of exposing the key if the code is uploaded to GitHub.

    // Method to sign in a user using email and password
    public static JSONObject signIn(String email, String password) throws Exception {
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;

        // Create the payload JSON with email, password, and a flag to return a secure token
        JSONObject payload = new JSONObject();
        payload.put("email", email);
        payload.put("password", password);
        payload.put("returnSecureToken", true);

        HttpURLConnection conn = null;
        BufferedReader in = null;
        try {
            // Set up the HTTP connection
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // Write the payload to the connection
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.toString().getBytes("UTF-8"));
            }

            // Read the response from Firebase
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String input;
            while ((input = in.readLine()) != null) {
                response.append(input);
            }

            // Return the response as a JSON object
            return new JSONObject(response.toString());
        } catch (Exception e) {
            throw new Exception("Error during sign-in: " + e.getMessage());
        } finally {
            if (in != null) {
                in.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
