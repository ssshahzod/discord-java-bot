import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
        assert jsn != null; //??
        String token = (String) jsn.get("token"); //get the data to variable
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setActivity(Activity.playing("PC"));
        builder.setToken(token);
        builder.addEventListeners(new Main());

        try {
            builder.build();
        }
        catch(LoginException e){
            e.printStackTrace();
        }


        //MessageHistory hstr = new MessageHistory();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        System.out.println("We received message from " +
                event.getAuthor().getName() + ": " +
                event.getMessage().getContentDisplay());
        if(event.getMessage().getContentRaw().equals("!Ping")){
            event.getChannel().sendMessage("Pong!").queue(); //important to call queue, or our messages wont be sent
        }

        MessageHistory hstr = new MessageHistory(event.getChannel());
        List<Message> messages = hstr.getRetrievedHistory(); //list of the messages from the channel
        
        /*int sizeOfHistory = messages.size();
        int randomMessage = ThreadLocalRandom.current().nextInt();
        event.getChannel().sendMessage(messages.get(randomMessage % sizeOfHistory)).queue();*/


    }

    public void createPicture(){
        BufferedImage background = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
        background.getGraphics();
    }

}
