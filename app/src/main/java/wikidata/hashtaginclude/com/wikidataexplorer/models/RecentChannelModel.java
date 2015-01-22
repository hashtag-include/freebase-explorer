package wikidata.hashtaginclude.com.wikidataexplorer.models;

/**
 * Created by matthewmichaud on 1/17/15.
 */

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="channel")
public class RecentChannelModel {

    @Element
    String title;

    @Element
    String link;

    @Element
    String description;

    @Element
    String language;

    @Element
    String generator;

    @Element
    String lastBuildDate;

    @ElementList(entry = "item", inline=true)
    List<RecentItemModel> newsItems;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public List<RecentItemModel> getNewsItems() {
        return newsItems;
    }

    public void setNewsItems(List<RecentItemModel> newsItems) {
        this.newsItems = newsItems;
    }
}
