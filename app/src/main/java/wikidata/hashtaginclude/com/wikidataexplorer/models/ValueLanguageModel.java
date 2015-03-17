package wikidata.hashtaginclude.com.wikidataexplorer.models;

/**
 * Created by matthewmichaud on 2/28/15.
 */
public class ValueLanguageModel {
    String value;
    String language;

    public ValueLanguageModel() {
    }

    public ValueLanguageModel(String language, String value) {
        this.value = value;
        this.language = language;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
