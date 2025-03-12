package nl.ajax.alert.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {

        @NotBlank(message = "Home team is required")
        @JsonProperty("homeTeam")
        private String homeTeam;

        @NotBlank(message = "Away team is required")
        @JsonProperty("awayTeam")
        private String awayTeam;

        @JsonProperty("soldOut")
        private boolean soldOut;

        @JsonProperty("matchLink")
        private String matchLink;
}