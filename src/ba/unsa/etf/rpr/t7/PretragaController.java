package ba.unsa.etf.rpr.t7;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PretragaController {
    public GridPane kutijaSlika;
    public TextField searchField;
    public ActionEvent actionEvent=new ActionEvent();
    PretragaController(){
    }

    @FXML public void initialize(){
        }
        public void searchAction(ActionEvent actionEvent){
            try {
                traziSlikuAction(searchField.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    public void traziSlikuAction(String search) throws IOException {
        String sURL="https://api.giphy.com/v1/gifs/search?api_key=AZYO2ufIW9rifNbEy2z3DbA9DEDa6C29&q="+search+"&limit=25&offset=0&rating=g&lang=en";
        URL url = new URL(sURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        String inline = "";
        int responsecode = conn.getResponseCode();
        Scanner scanner = new Scanner(url.openStream());
        while(scanner.hasNext()){
            String dodaj=""+scanner.next();
            if(dodaj.contains("https://media") && dodaj.contains(".gif")){inline+=dodaj;dodaj="";}
        }
        ucitajSearch(dajUrlIzAPIa(inline));
    }
    public static ArrayList<String> dajUrlIzAPIa(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);
        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }
        ArrayList<String> povratni= new ArrayList<>();
        for(String s:containedUrls){
            if(povratni.size()==25)return povratni;;
            if(s.length()>40 && s.contains("200_s.gif") && povratni.size()<25){
                povratni.add(s);
            }

        };
        return povratni;
    }
    public void ucitajSearch(ArrayList<String> url){
        int a=5;
        int x=0;
        int y=0;
        ArrayList<ImageView> imageViews=new ArrayList<>();
        for(String Surl:url ) {
            Button button1= new Button();
            button1.setMinSize(130,130);
            ImageView imageView = new ImageView();
            imageView.setFitWidth(128);
            imageView.setFitHeight(128);
            Image image = new Image("https://i."+Surl.substring(15));
            imageView.setImage(image);
            button1.graphicProperty().set(imageView);
            button1.setDefaultButton(true);
            kutijaSlika.add(button1,x,y);
            x++;
            if(x%5==0){
                x=0;
                y++;
            }
        }
    }

}
