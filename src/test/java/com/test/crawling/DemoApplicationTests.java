package com.test.crawling;

import com.google.gson.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    private Gson gson = new GsonBuilder().serializeNulls().create();

	@Test
	public void contextLoads() throws IOException {
        String url = "https://www.instagram.com/bavely_container/";
        String sharedData = findInstagramData(url);
        System.out.println(sharedData);

        JsonObject root = new JsonParser().parse(sharedData).getAsJsonObject();
        JsonObject user = root.get("entry_data").getAsJsonObject()
                                .get("ProfilePage").getAsJsonArray()
                                .get(0).getAsJsonObject()
                                .get("user").getAsJsonObject();

        JsonArray dataNode = user.get("media").getAsJsonObject()
                                    .get("nodes").getAsJsonArray();

        for( int i=0; i<dataNode.size(); i++ ){
            System.out.println( dataNode.get(i).getAsJsonObject().get("caption").getAsString() );
            System.out.println("========= ==========");
        }

        String profile = user.get("biography").getAsString();
        //System.out.print(profile);




	}

    private String findInstagramData(String url) throws IOException {
	    String tempHtml = null;
        Document doc = Jsoup.connect(url).get();
        Elements scripts = doc.select("script");

        for( Element script : scripts ){
            if( script.html().indexOf("window._sharedData") >= 0  ){
                tempHtml = script.html();
            }
        }
        Pattern pattern = Pattern.compile("window._sharedData[\\s]+?=(.*);");
        Matcher match = pattern.matcher(tempHtml);
        if( match.find() ){
            tempHtml = match.group(1);
        }
        return tempHtml;
    }

}
