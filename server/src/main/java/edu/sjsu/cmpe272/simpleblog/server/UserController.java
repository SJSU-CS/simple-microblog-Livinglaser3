package edu.sjsu.cmpe272.simpleblog.server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {

    // Inject UserService for handling user-related operations
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // API endpoint to create a new user
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserVariables params) {
        // Extract and format the public key from incoming request
        String publicKey = params.getPublicKey()
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        // Call UserService to create the user with extracted public key
        userService.createUser(params.getUser(), publicKey);

        // Log successful creation and return response
        logger.info("New user created: {}", params.getUser());
        return ResponseEntity.ok(Map.of("message", "welcome"));
    }

    // API endpoint to list all user public keys
    @PostMapping("/list")
    public ResponseEntity<?> listMessages() {
        // Retrieve all user public keys from UserService
        Map<String, String> users = UserService.userPublicKeys;
        List<String> keyList = new ArrayList<>(users.keySet());

        // Log the keys being listed
        logger.info("Listing {} user public keys", keyList.size());
        return ResponseEntity.ok(keyList);
    }

    // API endpoint to retrieve a user's public key by username
    @GetMapping("/{username}/public-key")
    public ResponseEntity<?> getPublicKey(@PathVariable String username) {
        // Retrieve public key for the specified username from UserService
        String publicKey = userService.getPublicKeyByUsername(username);

        if (publicKey == null) {
            // Log error if no public key found for the username
            logger.error("Public key not found for username: {}", username);
            return ResponseEntity.badRequest().body(Map.of("error", "signature didn't match"));
        }

        // Log successful retrieval and return the public key
        logger.info("Public key retrieved for username: {}", username);
        return ResponseEntity.ok(Map.of("public-key", publicKey));
    }
}
