import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.AttachmentOption;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.management.StringValueExp;
import javax.security.auth.login.LoginException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class Main extends ListenerAdapter {
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
        if(keyWords.length >= 3) {
            if ((keyWords[0] + keyWords[1] + keyWords[2]).equalsIgnoreCase("явамзапрещаю")) {
                StringBuilder msgMeaning = new StringBuilder();
                for(int i = 0; i < keyWords.length; i++){ //cutting part of the message
                    if(i % 3 == 0)
                        msgMeaning.append('\n');
                    msgMeaning.append(keyWords[i]).append(" ");
                } //removed text from the pic so we dont need to cut message
                event.getChannel().sendMessage(" ").addFile(makeImage(msgMeaning.toString(), typeOfPic.BAN.ordinal()), AttachmentOption.valueOf(msg)).queue();
                event.getChannel().sendMessage("Pong!").queue(); //important to call queue, or our messages wont be sent
            }
        }
        else if(keyWords.length == 2){
            if ((keyWords[0] + keyWords[1]).equalsIgnoreCase("акак")) {
                event.getChannel().sendMessage("Pong!").queue(); //important to call queue, or our messages wont be sent
            }
        }

    }

    public File makeImage(String text, int type){
        StringBuilder returnPath = new StringBuilder();
        if(typeOfPic.BAN.ordinal() == type){
            BufferedImage image = null;
            try {
                StringBuilder path = new StringBuilder();
                String tmp = new String(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
                /*path.append(".." + File.separator +"resources" + File.separator + "BAN");
                image = ImageIO.read(new File(path.toString()));*/
                String[] pathAr = tmp.split(File.separator);
                for(int i = 0; i < pathAr.length - 3; i++){
                    path.append(pathAr[i]).append(File.separator);
                }
                returnPath.append(String.valueOf(path.append("resources").append(File.separator).append("main").append(File.separator)));
                path.append("BAN.png");
                image = ImageIO.read(new File(path.toString()));

            }
            catch (IOException e){
                e.printStackTrace();
            }

            assert image != null;
            Graphics g = image.getGraphics();
            g.setFont(g.getFont().deriveFont(30f));
            g.setColor(Color.black);
            g.drawString(text, 100, 100);
            g.dispose();

            try {
                ImageIO.write(image, "png", new File("test.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return new File(returnPath.append("test.png").toString());
    }
}

enum typeOfPic{
    BAN,
    HOW
}
