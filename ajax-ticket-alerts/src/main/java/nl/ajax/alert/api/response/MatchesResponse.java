package nl.ajax.alert.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.ajax.alert.api.MatchDTO;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MatchesResponse {
    @JsonProperty("matches")
    List<MatchDTO> matches;
}
