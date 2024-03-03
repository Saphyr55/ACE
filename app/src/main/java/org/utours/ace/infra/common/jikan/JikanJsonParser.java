package org.utours.ace.infra.common.jikan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JikanJsonParser {

    public String pictureURL(String json) throws JSONException {
        var jsonObject = new JSONObject(json);
        var array = (JSONArray) jsonObject.get("data");
        var object = (JSONObject) array.get(0);
        var imageObj = (JSONObject) object.get("jpg");
        return (String) imageObj.get("image_url");
    }


}
