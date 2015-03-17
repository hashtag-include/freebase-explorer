package wikidata.hashtaginclude.com.wikidataexplorer.models;

import android.view.View;

/**
 * Created by matthewmichaud on 2/25/15.
 */
public class LabelListResponseModel {
    String label;
    int position;
    Object viewHolder;

    public LabelListResponseModel(String label, int position, Object viewHolder) {
        this.label = label;
        this.position = position;
        this.viewHolder = viewHolder;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Object getViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(View viewHolder) {
        this.viewHolder = viewHolder;
    }
}
