package me.trumpetplayer2.Pyroshot.Utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


public class SkinFetcher {
    static private String API_PROFILE_LINK = "https://sessionserver.mojang.com/session/minecraft/profile/";
    
    HashMap<String, String> playerInfo = new HashMap<String, String>();
    HashMap<String, HashMap<String, String>> properties = new HashMap<String, HashMap<String, String>>();
    
    public SkinFetcher(UUID player) {
        String data = getContent(API_PROFILE_LINK + player.toString() + "?unsigned=false");
        
        JsonReader reader = new Gson().newJsonReader(new StringReader(data));
        try {
            //Start reading Json Object
            reader.beginObject();
            //Grab the reader name and value for id and name
            playerInfo.put(reader.nextName(), reader.nextString());
            playerInfo.put(reader.nextName(), reader.nextString()); 
            //Grab name for properties
            reader.nextName();
            //Start Array
            reader.beginArray();
            //Loop through for all properties
            while(reader.hasNext()) {
                //Begin Object
                reader.beginObject();
                reader.nextName();
                String temp = reader.nextString();
                HashMap<String, String> value = new HashMap<String, String>();
                while(reader.hasNext()) {
                    //Store the "Name" value and the "Value" value - In this case, Textures and UUID
                    value.put(reader.nextName(), reader.nextString());
                }
                properties.put(temp, value);
                //End Object
                reader.endObject();
            }
            //End Array
            reader.endArray();
            //Nothing more I care about, we can close
            reader.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public String getPlayerName() {
        String temp = "";
        if(playerInfo.containsKey("name")) {
            temp = playerInfo.get("name");
        }
        return temp;
    }

    public String getPlayerInfo(String key) {
        if(!playerInfo.containsKey(key)) return null;
        return playerInfo.get(key);
    }
    
    public HashMap<String, String> getProperty(String key){
        if(!properties.containsKey(key)) return null;
        return properties.get(key);
    }
    
    public static String getContent(String link){
            try {
                StringBuilder result = new StringBuilder();
                URL url = new URL(link);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                        result.append(inputLine);
                }
                br.close();
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
    }
}
