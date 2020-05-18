package merchant.preparemarketplace.merchantjson;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonPropertyOrder({
        "errorMessage",
        "notice",
        "formular",
        "button"
})
public class Data {

    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("notice")
    private String notice;
    @JsonProperty("formular")
    private String formular;
    @JsonProperty("button")
    private String button;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("errorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty("errorMessage")
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonProperty("notice")
    public String getNotice() {
        return notice;
    }

    @JsonProperty("notice")
    public void setNotice(String notice) {
        this.notice = notice;
    }

    @JsonProperty("formular")
    public String getFormular() {
        return formular;
    }

    @JsonProperty("formular")
    public void setFormular(String formular) {
        this.formular = formular;
    }

    @JsonProperty("button")
    public String getButton() {
        return button;
    }

    @JsonProperty("button")
    public void setButton(String button) {
        this.button = button;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
