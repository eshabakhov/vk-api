package org.example.mapper;

import org.json.JSONObject;

public class JSONUserCityMapper implements Mapper<JSONObject, String>{
    @Override
    public String map(JSONObject jsonObject) {
        if (jsonObject == null) return null;
        String title = "title";
        if (!jsonObject.isNull(title)) {
            return jsonObject.getString(title);
        }
        return "";
    }
}
