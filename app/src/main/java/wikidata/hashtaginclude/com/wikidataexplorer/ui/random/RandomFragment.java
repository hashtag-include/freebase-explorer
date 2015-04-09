package wikidata.hashtaginclude.com.wikidataexplorer.ui.random;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.entity.EntityActivity;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.entity.EntityFragment;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.main.MainActivity;

public class RandomFragment extends Fragment {

    View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity)getActivity()).getMenu().findItem(R.id.action_search).setVisible(false);
        ((MainActivity)getActivity()).getMenu().findItem(R.id.action_refresh).setVisible(true);
        ((MainActivity)getActivity()).getMenu().findItem(R.id.action_refresh).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getRandomEntity();
                return true;
            }
        });

        getRandomEntity();
    }
    EntityFragment entityInstance = null;
    private void getRandomEntity() {
        String query = "Q"+((int)(Math.random()*14000000));
        WikidataLookup.getEntities(
                query, "", "", "yes", "info|sitelinks|aliases|labels|descriptions|claims|datatype", "", "", "", "", "",
                new Callback<JsonElement>() {
            @Override
            public void success(JsonElement getEntityResponseModel, Response response) {
                if (getEntityResponseModel != null) {
                    JsonObject entities = getEntityResponseModel.getAsJsonObject().getAsJsonObject("entities");
                    ArrayList<String> responses = new ArrayList<String>();
                    Iterator<Map.Entry<String, JsonElement>> iterator = entities.entrySet().iterator();
                    while(iterator.hasNext()) {
                        Map.Entry<String, JsonElement> entry = iterator.next();
                        responses.add(entry.getValue().toString());
                    }
                    if(responses.size() > 0) {
                        if (entityInstance!=null) {
                            getChildFragmentManager().beginTransaction().remove(entityInstance).commit();
                        }
                        entityInstance = EntityFragment.newInstance(responses.get(0));
                        getChildFragmentManager().beginTransaction().
                                add(R.id.random_entity, entityInstance)
                                .commit();
                    } else {
                        getRandomEntity();
                    }

                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_random, container, false);
        ButterKnife.inject(this, root);

        return root;
    }
}