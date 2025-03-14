package nl.ajax.alert.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import nl.ajax.alert.api.MatchDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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