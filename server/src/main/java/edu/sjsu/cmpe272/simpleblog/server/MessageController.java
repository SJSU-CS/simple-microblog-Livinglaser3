package edu.sjsu.cmpe272.simpleblog.server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
public class MessageController {

    // Logger for the MessageController
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Endpoint to create a new message after verifying its signature.
     * @param messageHandler Message object parsed from the request body.
     * @return ResponseEntity with the message ID if creation is successful, or an error if not.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createMessage(@RequestBody MessageHandler messageHandler) throws Exception {
        // Retrieve public key associated with the author from a static map
        PublicKey publicKey = UserService.getPublicKeyFromString(UserService.userPublicKeys.get(messageHandler.getAuthor()));

        // Check if public key is available
        if(publicKey != null && VerifySignature.verifySignature(messageHandler, publicKey)) {
            logger.info("Signature verification successful for author: {}", messageHandler.getAuthor());
            // Save the message to the repository
            MessageHandler savedMessageHandler = messageRepository.save(messageHandler);
            logger.debug("Saved message with ID: {}", savedMessageHandler.getMessageId());
            return ResponseEntity.ok(Map.of("message-id", savedMessageHandler.getMessageId()));
        } else {
            logger.warn("Failed to verify signature for message from author: {}", messageHandler.getAuthor());
            return ResponseEntity.ok(Map.of("error", "failed to create message"));
        }
    }

    /**
     * Endpoint to list messages based on provided parameters.
     * @param params Map of request parameters including 'limit', 'next', and 'starting_id'.
     * @return ResponseEntity with a list of messages or an error message.
     */
    @PostMapping("/list")
    public ResponseEntity<?> listMessages(@RequestBody Map<String, Object> params) {
        Integer limit = (Integer) params.getOrDefault("limit", 10);
        Integer next = (Integer) params.getOrDefault("next", -1);
        Integer starting_id = (Integer) params.getOrDefault("starting_id", 0);

        if (limit > 20) {
            logger.error("Limit exceeds the maximum value of 20");
            return ResponseEntity.badRequest().body("Error: Limit cannot be greater than 20");
        }

        // Retrieve messages based on order
        List<MessageHandler> messageHandlers = next == -1 ? messageRepository.findAllByOrderByMessageIdDesc() : messageRepository.findAllByOrderByMessageIdAsc();
        logger.debug("Retrieved {} messages", messageHandlers.size());

        // Apply limit and starting index to the list of messages
        if (messageHandlers.size() > limit) {
            messageHandlers = messageHandlers.subList(starting_id, starting_id + limit);
            logger.debug("Trimmed messages list to the specified limit and starting index");
        }

        return ResponseEntity.ok(messageHandlers);
    }

}
