package wikidata.hashtaginclude.com.wikidataexplorer.ui.query;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.models.SearchEntityResponseModel;

/**
 * Created by matthewmichaud on 2/10/15.
 */
public class QueryResponseActivity extends ActionBarActivity {

    public enum ResponseType {
        SEARCH_ENTITY_RESPONSE
    }

    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_entities);
        // Set the toolbar as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int type = getIntent().getIntExtra("type", ResponseType.SEARCH_ENTITY_RESPONSE.ordinal());
        if(type == ResponseType.SEARCH_ENTITY_RESPONSE.ordinal()) {
            SearchEntityResponseModel model = (SearchEntityResponseModel)(getIntent().getSerializableExtra("responseModel"));
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, QueryResponseFragment.newInstance(model, type))
                        .commit();
            }
            toolbar.setTitle(model.getSearchInfo().getSearch());
        }








    }
}
