package wikidata.hashtaginclude.com.wikidataexplorer.ui.main;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.browse.BrowseFragment;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.news.NewsFragment;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.query.QueryFragment;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.random.RandomFragment;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private String[] navBarItems;
    private DrawerLayout drawerLayout;
    private ListView drawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Crashlytics
        Fabric.with(this, new Crashlytics());

        // Set the first content vieqw
        setContentView(R.layout.activity_main);

        // TODO: check to see the condition of the user,
        //  if it's their first time then put the welcome fragment up
        //  else start with the news
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new NewsFragment())
                    .commit();
        }

        // Set the toolbar as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.dr);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        });

        navBarItems = getResources().getStringArray(R.array.nav_bar_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.drawer_list);

        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navBarItems));
        drawerList.setOnItemClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {
            case 0: // news
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new NewsFragment()).commit();
                drawerLayout.closeDrawers();
                break;
            case 1: // browse
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new BrowseFragment()).commit();
                drawerLayout.closeDrawers();
                break;
            case 2: // query
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new QueryFragment()).commit();
                drawerLayout.closeDrawers();
                break;
            case 3: // random
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new RandomFragment()).commit();
                drawerLayout.closeDrawers();
                break;
        }
    }
}
