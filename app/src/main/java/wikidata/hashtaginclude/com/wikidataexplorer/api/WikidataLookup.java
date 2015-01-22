package wikidata.hashtaginclude.com.wikidataexplorer.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataLog;
import wikidata.hashtaginclude.com.wikidataexplorer.models.RecentResponseModel;

/**
 * Created by matthewmichaud on 1/18/15.
 */
public class WikidataLookup {

    private static String TAG = "WikidataLookup";

    static WikidataService wikidataService;

    static {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://www.wikidata.org")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        wikidataService = restAdapter.create(WikidataService.class);
    }

    public static void getRecent(final Callback<RecentResponseModel> callback) {
        wikidataService.getRecent("query", "recentchanges", "json", 25, callback);
    }

    public static void getRecent(String timestamp, final Callback<RecentResponseModel> callback) {
        wikidataService.getRecent("query", "recentchanges", "json", 25, timestamp, callback);
    }

    public static void lookupQ(int q, Callback<JSONObject> callback) {

    }

    public static void lookupP(int p, Callback<JSONObject> callback) {

    }

    public static void getLabel(String q, Callback<String> callback) {
        getLabel(q, "en", callback);
    }

    public static void getLabel(final String q, final String language, final Callback<String> callback) {
        wikidataService.getEntity("wbgetentities", q, language, "json", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if(jsonObject==null || jsonObject.getAsJsonObject("entities")==null) {
                    callback.success("", response);
                    return;
                }
                JsonObject entity = jsonObject.getAsJsonObject("entities").getAsJsonObject(q);

                if(entity!=null) {
                    JsonObject labels = entity.getAsJsonObject("labels");
                    if(labels==null) {
                        callback.success("", response);
                        return;
                    }
                    JsonObject labelLang = labels.getAsJsonObject(language);
                    if(labelLang==null) {
                        // couldnt find it in the language you want, just giving you the first one then
                        Set<Map.Entry<String, JsonElement>> set = labels.entrySet();
                        Iterator<Map.Entry<String, JsonElement>> i = set.iterator();
                        while(i.hasNext()) {
                            Map.Entry<String, JsonElement> entry = i.next();
                            JsonObject obj = entry.getValue().getAsJsonObject();
                            JsonPrimitive prim = obj.getAsJsonPrimitive("value");

                            callback.success(prim.getAsString(), response);
                            return;
                        }
                        callback.success("", response);
                        return;
                    } else {
                        JsonPrimitive labelValue = labelLang.getAsJsonPrimitive("value");
                        String label = labelValue.getAsString();
                        callback.success(label, response);
                        return;
                    }
                }

                callback.success("", response);
            }

            @Override
            public void failure(RetrofitError error) {
                WikidataLog.e(TAG, "Failed to get label for: "+q, error.getCause());
                callback.failure(error);
            }
        });
    }
}
