package wikidata.hashtaginclude.com.wikidataexplorer.ui.browse;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import wikidata.hashtaginclude.com.wikidataexplorer.R;

public class BrowseFragment extends Fragment {

    View root;
    GridView gridView;
    CategoryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_browse, container, false);

        gridView = (GridView) root.findViewById(R.id.browse_gridview);

        List<String> categories = new ArrayList<String>();
        categories.add("Music");
        categories.add("Books");
        categories.add("Media");
        categories.add("People");
        categories.add("Film");
        categories.add("TV");
        categories.add("Business");
        categories.add("Location");
        categories.add("Fictional Universes");
        categories.add("Organization");
        categories.add("Biology");
        categories.add("Sports");
        categories.add("Awards");
        categories.add("Education");
        categories.add("Time");
        categories.add("Government");
        categories.add("Soccer");
        categories.add("Architecture");
        categories.add("Medicine");
        categories.add("Video Games");
        categories.add("Projects");
        categories.add("Physical Geography");
        categories.add("Visual Art");
        categories.add("Olympics");
        categories.add("Music");
        categories.add("Music");
        categories.add("Music");
        categories.add("Music");
        categories.add("Music");
        categories.add("Music");
        categories.add("Music");
        categories.add("Music");
        categories.add("Music");
        categories.add("Music");






        adapter = new CategoryAdapter(getActivity(), categories);
        gridView.setAdapter(adapter);

        return root;
    }
}