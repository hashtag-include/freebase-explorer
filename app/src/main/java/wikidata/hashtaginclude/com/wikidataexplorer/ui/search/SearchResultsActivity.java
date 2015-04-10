package wikidata.hashtaginclude.com.wikidataexplorer.ui.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataLog;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataUtility;
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;
import wikidata.hashtaginclude.com.wikidataexplorer.models.SearchEntityResponseModel;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.entity.EntityActivity;

/**
 * Created by matthewmichaud on 1/15/15.
 */
public class SearchResultsActivity extends Activity {

    private static final String TAG = "SearchResultsActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        handleIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(getIntent());
    }

    public void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // show a loading page
            WikidataLookup.searchEntities(query, new Callback<SearchEntityResponseModel>() {
                @Override
                public void success(SearchEntityResponseModel searchEntityResponseModel, Response response) {
                    if(searchEntityResponseModel!=null && searchEntityResponseModel.getSearchModels().size()>0) {
                        WikidataLookup.getEntities(
                                searchEntityResponseModel.getSearchModels().get(0).getId(),
                                new Callback<JsonElement>() {
                                    @Override
                                    public void success(JsonElement getEntityResponseModel, Response response) {
                                        if (getEntityResponseModel != null) {
                                            JsonObject entities = getEntityResponseModel.getAsJsonObject().getAsJsonObject("entities");
                                            ArrayList<String> responses = new ArrayList<String>();
                                            Iterator<Map.Entry<String, JsonElement>> iterator = entities.entrySet().iterator();
                                            while (iterator.hasNext()) {
                                                Map.Entry<String, JsonElement> entry = iterator.next();
                                                responses.add(entry.getValue().toString());
                                            }

                                            // launch a new activity with the model
                                            Intent intent = new Intent(SearchResultsActivity.this, EntityActivity.class);
                                            intent.putExtra("responses", responses);
                                            SearchResultsActivity.this.startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        WikidataLog.e(TAG, "Failed to get entities", error);
                                        WikidataUtility.makeCroutonText("Could not complete request", SearchResultsActivity.this);
                                    }
                                }
                        );
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }
}
