import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main extends ListenerAdapter {
    public static void main(String[] args) {
        FileReader file = null;
        Object obj = null;

        try { //read config file with token
            file = new FileReader("/home/cronion/Documents/discord_bot/src/main/resources/config.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try{ //parse data from file
            obj = new JSONParser().parse(file);
        }
        catch(IOException | ParseException e){
            e.printStackTrace();
        }

        JSONObject jsn = (JSONObject) obj;
        assert jsn != null;
        String token = (String) jsn.get("token"); //get the data to variable
        System.out.println("Bot Token: " + token);
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setActivity(Activity.playing("PC"));
        builder.setToken(token);

        try {
            builder.build();
        }
        catch(LoginException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        System.out.println("We received message from " +
                event.getAuthor().getName() + ": " +
                event.getMessage().getContentDisplay());
        if(event.getMessage().getContentRaw().equals("!Ping")){
            event.getChannel().sendMessage("Pong!").queue(); //important to call queue, or our messages wont be sent
        }
    }

}
