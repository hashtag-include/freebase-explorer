package wikidata.hashtaginclude.com.wikidataexplorer.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by matthewmichaud on 1/17/15.
 */
public class RecentItemModel {

    String titleText = null;

    @Expose
    @SerializedName("type")
    String type;

    @Expose
    @SerializedName("ns")
    int ns;

    @Expose
    @SerializedName("title")
    String titleId;

    @Expose
    @SerializedName("parsedcomment")
    String parsedComment;

    @Expose
    @SerializedName("timestamp")
    String timeStamp;

    public RecentItemModel() {
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNs() {
        return ns;
    }

    public void setNs(int ns) {
        this.ns = ns;
    }

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId;
    }

    public String getParsedComment() {
        return parsedComment;
    }

    public void setParsedComment(String parsedComment) {
        this.parsedComment = parsedComment;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
