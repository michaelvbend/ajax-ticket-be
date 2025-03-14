package nl.ajax.alert.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import nl.ajax.alert.api.MatchDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchCallbackRequest {

        @NotNull(message = "Matches list cannot be null")
        @Size(min = 1, message = "At least one match is required")
        @JsonProperty("matches")
        private List<MatchDTO> matches;
}