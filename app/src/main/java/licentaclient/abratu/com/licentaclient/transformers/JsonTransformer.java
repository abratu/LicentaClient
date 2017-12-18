package licentaclient.abratu.com.licentaclient.transformers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apetho on 12/18/2017.
 */

public class JsonTransformer<T> {

    public List<T> transform2List(String jsonStr, Class clazz) {
        JsonParser jsonParser = new JsonParser();
        List<T> retList = new ArrayList<>();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(jsonStr);
        Gson gson = new Gson();
        for (int i = 0; i < jsonArray.size(); ++i) {
            JsonElement json = jsonArray.get(i);
            Type type = new TypeToken<T>() {
            }.getType();
            T element = (T) gson.fromJson(json, clazz);
            retList.add(element);
        }
        return retList;
    }

    public T transform2Object(String jsonStr, Class clazz) {
        JsonParser jsonParser = new JsonParser();
        Gson gson = new Gson();
        JsonElement json = jsonParser.parse(jsonStr);
        Type type = new TypeToken<T>() {
        }.getType();
        T element = (T) gson.fromJson(json, clazz);
        return element;
    }


}
