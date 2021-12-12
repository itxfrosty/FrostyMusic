package me.itxfrosty.frostymusic.audio.sources.applemusic.entites;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Data;

@Data
public class QueryToken {

    private String kid;

    private String tid;

    public String toJSON(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("kid", getKid());
            jsonObject.put("tid", getTid());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
