package nl.ajax.alert.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchCallbackRequest {

        @NotNull(message = "Matches list cannot be null")
        @Size(min = 1, message = "At least one match is required")
        @JsonProperty("matches")
        private List<MatchDTO> matches;
}