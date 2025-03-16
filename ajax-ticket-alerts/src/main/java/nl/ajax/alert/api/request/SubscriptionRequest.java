package nl.ajax.alert.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequest {

    @NotNull
    @JsonProperty("email")
    private String email;

    @NotNull
    @JsonProperty("matchAgainst")
    private String matchAgainst;
}
