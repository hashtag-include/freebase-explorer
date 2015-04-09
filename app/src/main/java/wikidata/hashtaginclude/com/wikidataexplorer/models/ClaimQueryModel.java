package wikidata.hashtaginclude.com.wikidataexplorer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mmichaud on 4/3/15.
 */
public class ClaimQueryModel {

    @Expose
    @SerializedName("status")
    Status status;

    @Expose
    @SerializedName("items")
    int[] items;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int[] getItems() {
        return items;
    }

    public void setItems(int[] items) {
        this.items = items;
    }

    public class Status {
        @Expose
        @SerializedName("error")
        String error;

        @Expose
        @SerializedName("items")
        int items;

        @Expose
        @SerializedName("querytime")
        String queryTime;

        @Expose
        @SerializedName("parsed_query")
        String parsedQuery;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public int getItems() {
            return items;
        }

        public void setItems(int items) {
            this.items = items;
        }

        public String getQueryTime() {
            return queryTime;
        }

        public void setQueryTime(String queryTime) {
            this.queryTime = queryTime;
        }

        public String getParsedQuery() {
            return parsedQuery;
        }

        public void setParsedQuery(String parsedQuery) {
            this.parsedQuery = parsedQuery;
        }
    }
}
