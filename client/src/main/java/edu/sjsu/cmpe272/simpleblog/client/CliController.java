package edu.sjsu.cmpe272.simpleblog.client;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Callable;
import static edu.sjsu.cmpe272.simpleblog.client.GenerateKeyPair.encodePublicKeyToPEM;
import static edu.sjsu.cmpe272.simpleblog.client.SaveToINI.retrivePrivateKeyofUser;
import static edu.sjsu.cmpe272.simpleblog.client.SaveToINI.retriveUserId;

@Slf4j
@Component
@Command(name = "cliController", mixinStandardHelpOptions = true, subcommands = {CliController.Post.class, CliController.CreateUser.class})
public class CliController {
    static String base_url = "https://vishallzr007.twilightparadox.com/";

    @Component
    @Command(name = "create", mixinStandardHelpOptions = true, exitCodeOnExecutionException = 34)
    static class CreateUser implements Callable<Integer> {

        @Option(
                names = "userid",
                required = true,
                description = "save-attachment of message from author",
                defaultValue = "false")
        private String user_id;
        @Override
        public Integer call() throws Exception {
            String public_key="";

            Map<String,String> keys = GenerateKeyPair.generateRSAKeyPair();
            if (keys.containsKey("privateKey")) {
                SaveToINI.saveToINIFile(user_id, keys.get("privateKey"));
            }

            if(keys.containsKey("publicKey")){
                public_key = keys.get("publicKey");
            }
            String publicKeyPem = encodePublicKeyToPEM(public_key);
            JasonGetter.User user = new JasonGetter.User(user_id,publicKeyPem);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(user);

            JasonHandler jasonHandler = new JasonHandler();
            String response = String.valueOf(jasonHandler.sendRawJsonPostRequest(base_url+"user/create",json));
            System.out.println(response);
            return 9;
        }
    }

    @Component
    @Command(name = "post", mixinStandardHelpOptions = true, exitCodeOnExecutionException = 34)
    static class Post implements Callable<Integer>{
        @Option(
                names = "message",required = true,
                description = "message from author")
        private String message;
        @Option(
                names = "file-to-attach",
                description = "save-attachment of message from author",
                defaultValue = "false")
        private String file_to_attach;
        @Override
        public Integer call() throws Exception {
            log.info(" Post Information");
            System.out.println("Post successful");

            LocalDateTime dateTime = LocalDateTime.now();
            String author ;
            String privateKey = retrivePrivateKeyofUser();
            String username = retriveUserId();
            PostMessage postApi = new PostMessage(dateTime.toString(),username,message,file_to_attach,"");
            String signature = SignatureSetter.createSignature(postApi,privateKey);
            postApi = new PostMessage(dateTime.toString(),username,message,file_to_attach,signature);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(postApi);
            System.out.println(json);

            JasonHandler jasonHandler = new JasonHandler();
            String response = String.valueOf(jasonHandler.sendRawJsonPostRequest(base_url+"messages/create",json));
            System.out.println(response);
            return 9;
        }
    }
}
