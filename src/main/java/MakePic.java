import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MakePic {
    final private String text;
    final private int type;

    public MakePic(String messageText, int type){
        this.text = messageText;
        this.type = type;
    }

    public void getPicture(){
        String[] textBywords = text.split(" ");
        StringBuilder returnPath = new StringBuilder();
        if(typeOfPic.BAN.ordinal() == type){
            BufferedImage image = null;
            try {
                StringBuilder path = new StringBuilder();
                String tmp = new String(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath());
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
            StringBuilder tmp = new StringBuilder();
            int xStart = 100, yStart = 100;
            for(int i = 0; i < textBywords.length; i++){
                tmp.append(textBywords[i]).append(" ");
                if(i != 0 & i % 3 == 0){
                    g.drawString(tmp.toString(), xStart, yStart);
                    tmp.setLength(0); //remove words from builder
                    xStart += 40;
                    yStart += 40;
                }
            }
            g.drawString(tmp.toString(), xStart, yStart);
            g.dispose();

            try {
                ImageIO.write(image, "png", new File("test.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        new File(returnPath.append("test.png").toString());
    }

}
