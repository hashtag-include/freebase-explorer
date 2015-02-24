package wikidata.hashtaginclude.com.wikidataexplorer.ui.entity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.models.SearchEntityResponseModel;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.browse.BrowseFragment;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.query.QueryResponseFragment;

/**
 * Created by matthewmichaud on 2/17/15.
 */
public class EntityActivity extends ActionBarActivity {

    private static final String TAG = "[EntityActivity]";

    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);
        // Set the toolbar as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                    getSupportFragmentManager().popBackStack();
                } else{
                    finish();
                }
            }
        });

        // check what's in the intent. If there is more than 1 item then we need to show a fragment
        // with a list of the responses so they user can select one. When they select one the new
        // content fragment should be put on the backstack to allow for back navigation back to the
        // list of responses.

        Bundle args = this.getIntent().getExtras();

        ArrayList<String> responses = args.getStringArrayList("responses");

        if(responses.size() == 0) {
            // no content found
        } else if(responses.size() == 1) {
            // show the one content
            String content = responses.get(0);
            if(content!=null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, EntityFragment.newInstance(content))
                        .commit();
            }
        } else {
            // show a list of the responses
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, EntityListFragment.newInstance(responses))
                    .commit();
        }
    }
}
