package edu.sjsu.cmpe272.simpleblog.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
class JasonHandler {

    WebClient webclient = WebClient.builder().build();


    public String sendRawJsonPostRequest(String url, String rawJson) {

        return webclient.post() // Use the appropriate method (get, post, put, delete, etc.)
                .uri(url)
                .header("Content-Type", "application/json") // Set the Content-Type header
                .bodyValue(rawJson) // Pass the raw JSON string as the request body
                .retrieve() // Initiate the request
                .bodyToMono(String.class).block(); // Specify how to decode the response body
    }
}
public class JasonGetter {

    static class User {
        private String user;
        private String publicKey;
        public User() {}

        public User(String user, String publicKey) {
            this.user = user;
            this.publicKey = publicKey;
        }

        // Getters
        public String getUser() {
            return user;
        }

        public String getPublicKey() {
            return publicKey;
        }

        // Setters
        public void setUser(String user) {
            this.user = user;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }
    }





}

