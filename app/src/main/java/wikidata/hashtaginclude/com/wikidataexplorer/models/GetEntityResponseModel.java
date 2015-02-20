package wikidata.hashtaginclude.com.wikidataexplorer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by matthewmichaud on 2/3/15.
 */
public class GetEntityResponseModel implements Serializable {
    @Expose
    @SerializedName("entities")
    SearchInfoModel searchInfo;

    @Expose
    @SerializedName("search")
    List<SearchModel> searchModels;

    public SearchInfoModel getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(SearchInfoModel searchInfo) {
        this.searchInfo = searchInfo;
    }

    public List<SearchModel> getSearchModels() {
        return searchModels;
    }

    public void setSearchModels(List<SearchModel> searchModels) {
        this.searchModels = searchModels;
    }

    public class SearchInfoModel implements Serializable {
        @Expose
        @SerializedName("search") String search;

        public String getSearch() {
            return search;
        }

        public void setSearch(String search) {
            this.search = search;
        }
    }

    public class SearchModel implements Serializable {
        @Expose
        @SerializedName("id")
        String id;

        @Expose
        @SerializedName("url")
        String url;

        @Expose
        @SerializedName("description")
        String description;

        @Expose
        @SerializedName("label")
        String label;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
