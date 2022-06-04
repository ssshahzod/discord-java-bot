import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;


public class Main extends ListenerAdapter {
    String helpMessage = "Discord bot that sends images/gifs by keywords. " +
        "!help - shows this message." +            
        "!add \"keyword *Tenor link to gif*\" - add gif and send it everytime when keyword was used." +
        "";

    public static void main(String[] args) {

        Object obj = null;
        FileReader tmp = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL strm = classLoader.getResource("config.json");
        String []path = (String.valueOf(strm)).split(":");
        try {
            if(path.length > 2){
                tmp = new FileReader(path[1] + ':' + path[2]);
            }
            else {
                tmp = new FileReader(path[1]);
            }
            obj = new JSONParser().parse(tmp);

        } catch(ParseException | IOException e){
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

    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        String msg;
        System.out.println("We received message from " +
                event.getAuthor().getName() + ": " +
                event.getMessage().getContentDisplay());
        msg = event.getMessage().getContentRaw();
        String[] keyWords = msg.split(" ");

        if(keyWords[0].equals("!help")){
            event.getChannel().sendMessage(helpMessage).queue();
        }

        //special case for own usage
        else if(keyWords.length >= 3) {
            if ((keyWords[0] + keyWords[1] + keyWords[2]).equalsIgnoreCase("явамзапрещаю")) {
                StringBuilder msgMeaning = new StringBuilder();
                for(int i = 0; i < keyWords.length; i++){ //cutting part of the message
                    if(i % 3 == 0)
                        msgMeaning.append('\n');
                    msgMeaning.append(keyWords[i]).append(" ");
                }

                MakePic attachment = new MakePic(msgMeaning.toString(), typeOfPic.BAN.ordinal());
                attachment.getPicture();
                event.getChannel().sendMessage(" ")
                        .addFile(new File("test.png"))
                        .queue();//important to call queue, or our messages wont be sent
            }
        }

    }

}


