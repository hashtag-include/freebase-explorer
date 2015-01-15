package wikidata.hashtaginclude.com.wikidataexplorer.ui.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wikidata.hashtaginclude.com.wikidataexplorer.R;

/**
 * Created by matthewmichaud on 1/14/15.
 */
public class NewsFragment extends Fragment {

    View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_news, container, false);
        return root;
    }
}
