package nl.ajax.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AjaxAlertingConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @NotEmpty
    @JsonProperty("twilioAccountSid")
    private String twilioAccountSid;

    @NotEmpty
    @JsonProperty("twilioAuthToken")
    private String twilioAuthToken;

    @NotEmpty
    @JsonProperty("sendGridApiKey")
    private String sendGridApiKey;

    @JsonProperty("twilioEmail")
    private String twilioEmail;
}