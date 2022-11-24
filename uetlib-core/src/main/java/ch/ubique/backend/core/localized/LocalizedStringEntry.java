package ch.ubique.backend.core.localized;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;

@Schema(description = "A localized entry.")
public record LocalizedStringEntry(
        @NotBlank
        @Schema(example = "en")
        String langKey,
        @NotBlank
        @Schema(example = "I'm a localized string.")
        String text
) {

    @JsonIgnore
    public boolean isValid() {
        return StringUtils.isNotBlank(langKey) && StringUtils.isNotBlank(text);
    }

    @Override
    public String toString() {
        return langKey + ": " + text;
    }
}
