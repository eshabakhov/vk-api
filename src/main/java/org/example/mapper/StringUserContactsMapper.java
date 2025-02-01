package org.example.mapper;

import org.json.JSONObject;

public class StringUserContactsMapper implements Mapper<JSONObject, String> {
    @Override
    public String map(JSONObject jsonObject) {
        if (jsonObject == null) return null;

        String mobilePhone = "mobile_phone";
        String homePhone = "home_phone";
        StringBuilder userContacts = new StringBuilder();

        if (!jsonObject.isNull(mobilePhone)) {
            userContacts.append(jsonObject.getString(mobilePhone));
        }
        if (!jsonObject.isNull(homePhone)) {
            userContacts.append(jsonObject.getString(homePhone));
        }

        return userContacts.toString();
    }
}
