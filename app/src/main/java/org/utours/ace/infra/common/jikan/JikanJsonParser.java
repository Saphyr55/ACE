package org.utours.ace.infra.common.jikan;

import org.json.JSONException;
import org.json.JSONObject;


public class JikanJsonParser {

    public String aboutCharacter(String json, String uid) {
        try {
            var jsonObject = new JSONObject(json);
            var data = jsonObject.getJSONObject("data");
            var malID = data.getInt("mal_id");
            if (malID == Integer.parseInt(uid)) {
                return data.isNull("about") ?
                        "No information." :
                        data.getString("about");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return "No information.";
    }

}
