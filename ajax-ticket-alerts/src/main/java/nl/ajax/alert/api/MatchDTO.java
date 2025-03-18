package nl.ajax.alert.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;


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

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                MatchDTO matchDTO = (MatchDTO) o;
                return Objects.equals(homeTeam, matchDTO.homeTeam) &&
                        Objects.equals(awayTeam, matchDTO.awayTeam);        }


        @Override
        public int hashCode() {
                return Objects.hash(homeTeam, awayTeam);
        }

}