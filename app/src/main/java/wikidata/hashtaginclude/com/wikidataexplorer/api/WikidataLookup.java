package wikidata.hashtaginclude.com.wikidataexplorer.api;

import android.view.View;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataLog;
import wikidata.hashtaginclude.com.wikidataexplorer.models.GetEntityResponseModel;
import wikidata.hashtaginclude.com.wikidataexplorer.models.LabelListResponseModel;
import wikidata.hashtaginclude.com.wikidataexplorer.models.RecentResponseModel;
import wikidata.hashtaginclude.com.wikidataexplorer.models.SearchEntityResponseModel;

/**
 * Created by matthewmichaud on 1/18/15.
 */
public class WikidataLookup {

    private static String TAG = "WikidataLookup";

    static WikidataService wikidataService;

    static Map<String, String> properties;

    static {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://www.wikidata.org")
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        wikidataService = restAdapter.create(WikidataService.class);

        properties = new HashMap<String, String>();
    }

    public static void getRecent(final Callback<RecentResponseModel> callback) {
        wikidataService.getRecent("query", "recentchanges", "json", 25, "title|timestamp|parsedcomment", callback);
    }

    public static void getRecent(String timestamp, final Callback<RecentResponseModel> callback) {
        wikidataService.getRecent("query", "recentchanges", "json", 25, timestamp, "title|timestamp|parsedcomment", callback);
    }

    public static void getLabel(String q, Callback<String> callback) {
        getLabel(q, "en", callback);
    }

    public static void getLabel(String q, final int position, final Object viewHolder, final Callback<LabelListResponseModel> callback) {
        getLabel(q, "en", new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                LabelListResponseModel model = new LabelListResponseModel(s, position, viewHolder);
                callback.success(model, response);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    public static void getLabel(final String q, final String language, final Callback<String> callback) {
        wikidataService.getEntity("wbgetentities", q, language, "json", new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (jsonObject == null || jsonObject.getAsJsonObject("entities") == null) {
                    callback.success("", response);
                    return;
                }
                JsonObject entity = jsonObject.getAsJsonObject("entities").getAsJsonObject(q);

                if (entity != null) {
                    JsonObject labels = entity.getAsJsonObject("labels");
                    if (labels == null) {
                        callback.success("", response);
                        return;
                    }
                    JsonObject labelLang = labels.getAsJsonObject(language);
                    if (labelLang == null) {
                        // couldnt find it in the language you want, just giving you the first one then
                        Set<Map.Entry<String, JsonElement>> set = labels.entrySet();
                        Iterator<Map.Entry<String, JsonElement>> i = set.iterator();
                        while (i.hasNext()) {
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
                WikidataLog.e(TAG, "Failed to get label for: " + q, error.getCause());
                callback.failure(error);
            }
        });
    }

    public static void searchEntities(String search, Callback<SearchEntityResponseModel> callback) {
        wikidataService.searchEntities("wbsearchentities", search, "en", "item", 1, 0, "json", callback);
    }

    public static void searchEntities(String search, String language,
                                      String type, int limit, int continueQuery, Callback<SearchEntityResponseModel> callback) {
        wikidataService.searchEntities("wbsearchentities", search, language, type, limit, continueQuery, "json", callback);
    }

    public static void getEntities(String ids, String sites, String titles, String redirects, String props,
                                   String languages, String languageFallback, String normalize, String ungroupedlist,
                                   String siteFilter, Callback<JsonElement> callback) {
        wikidataService.getEntities("wbgetentities", ids, sites, titles, redirects, props, languages, languageFallback, normalize,
                ungroupedlist, siteFilter, "json", callback);
    }

    public static void getProperty(final String propertyId, final String valueId, final Callback<AbstractMap.SimpleEntry<String, String>> callback) {
        if (properties.containsKey(propertyId)) {
            getPropertyValue(properties.get(propertyId), valueId, callback);
            //callback.success(properties.get(properties), null);
            return;
        }

        getLabel(propertyId, "en", new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                //callback.success(s, response);
                getPropertyValue(s, valueId, callback);
            }

            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
            }
        });
    }

    private static void getPropertyValue(final String property, final String valueId, final Callback<AbstractMap.SimpleEntry<String, String>> callback) {
        WikidataLookup.getLabel("Q" + valueId, new Callback<String>() {
            @Override
            public void success(String label, Response response) {
                if (label != null) {
                    callback.success(new AbstractMap.SimpleEntry<String, String>(property, label), response);
                    WikidataLog.d(TAG, "property value = " + label);
                } else {
                    WikidataLog.e(TAG, "response was null");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                WikidataLog.e(TAG, "Could not get value from Q"+valueId, error);
            }
        });
    }
}
