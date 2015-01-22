package wikidata.hashtaginclude.com.wikidataexplorer.models;

/**
 * Created by matthewmichaud on 1/17/15.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

public class RecentResponseModel {

    @Expose
    @SerializedName("query-continue")
    QueryContinue queryContinue;

    @Expose
    @SerializedName("warnings")
    Warning warnings;

    @Expose
    @SerializedName("query")
    Query query;

    public QueryContinue getQueryContinue() {
        return queryContinue;
    }

    public void setQueryContinue(QueryContinue queryContinue) {
        this.queryContinue = queryContinue;
    }

    public Warning getWarnings() {
        return warnings;
    }

    public void setWarnings(Warning warnings) {
        this.warnings = warnings;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public class Query {
        @Expose
        @SerializedName("recentchanges")
        List<RecentItemModel> recentChanges;

        public List<RecentItemModel> getRecentChanges() {
            return recentChanges;
        }

        public void setRecentChanges(List<RecentItemModel> recentChanges) {
            this.recentChanges = recentChanges;
        }
    }

    public class Warning {
        @Expose
        @SerializedName("query")
        Query query;

        public class Query {
            @Expose
            @SerializedName("*")
            String star;

            public String getStar() {
                return star;
            }

            public void setStar(String star) {
                this.star = star;
            }
        }

        public Query getQuery() {
            return query;
        }

        public void setQuery(Query query) {
            this.query = query;
        }
    }


    public class QueryContinue {
        @Expose
        @SerializedName("recentchanges")
        RecentChanges recentChanges;

        public RecentChanges getRecentChanges() {
            return recentChanges;
        }

        public void setRecentChanges(RecentChanges recentChanges) {
            this.recentChanges = recentChanges;
        }

        public class RecentChanges {
            @Expose
            @SerializedName("rccontinue")
            String rcContinue;

            public String getRcContinue() {
                return rcContinue;
            }

            public void setRcContinue(String rcContinue) {
                this.rcContinue = rcContinue;
            }
        }
    }

}
