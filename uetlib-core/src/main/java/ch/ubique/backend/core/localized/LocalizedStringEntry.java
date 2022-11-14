package ch.ubique.backend.core.localized;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

public record LocalizedStringEntry(String langKey, String text) {

    @JsonIgnore
    public boolean isValid() {
        return StringUtils.isNotBlank(langKey) && StringUtils.isNotBlank(text);
    }

    @Override
    public String toString() {
        return langKey + ": " + text;
    }
}
