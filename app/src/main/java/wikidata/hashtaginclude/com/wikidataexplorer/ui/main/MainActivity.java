package wikidata.hashtaginclude.com.wikidataexplorer.ui.main;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.browse.BrowseFragment;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.main.drawer.DrawerAdapter;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.main.drawer.DrawerHeaderItem;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.main.drawer.DrawerItem;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.main.drawer.DrawerListItem;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.recent.RecentFragment;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.query.QueryFragment;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.random.RandomFragment;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.search.SearchResultsActivity;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener,
                        DrawerLayout.DrawerListener{

    private String[] navBarItems;
    private ArrayList<DrawerItem> items;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    Toolbar toolbar;

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
                    .add(R.id.container, new RecentFragment())
                    .commit();
        }

        // Set the toolbar as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawers();
                    toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu);
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                    toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                }
            }
        });

        navBarItems = getResources().getStringArray(R.array.nav_bar_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerListener(this);
        drawerList = (ListView) findViewById(R.id.drawer_list);

        items = new ArrayList<DrawerItem>();
        items.add(new DrawerHeaderItem("THIS IS A HEADER"));
        for(String s : navBarItems) {
            items.add(new DrawerListItem(s));
        }

        drawerList.setAdapter(new DrawerAdapter(this, items));
        drawerList.setOnItemClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        ComponentName cn = new ComponentName(this, SearchResultsActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));

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
            case 0: // profile

                break;
            case 1: // browse
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new BrowseFragment()).commit();
                drawerLayout.closeDrawers();
                break;
            case 2: // query
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new QueryFragment()).commit();
                drawerLayout.closeDrawers();
                break;
            case 3: // news
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new RecentFragment()).commit();
                drawerLayout.closeDrawers();
                break;
            case 4: // random
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new RandomFragment()).commit();
                drawerLayout.closeDrawers();
                break;
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu);
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
