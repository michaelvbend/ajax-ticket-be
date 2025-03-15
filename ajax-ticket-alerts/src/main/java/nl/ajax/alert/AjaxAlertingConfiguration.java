package nl.ajax.alert;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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

    @NotNull
    private String twilioAccountSid;

    @NotNull
    private String twilioAuthToken;

    public String getTwilioAccountSid() {
        return System.getenv("TWILIO_ACCOUNT_SID") != null
                ? System.getenv("TWILIO_ACCOUNT_SID")
                : twilioAccountSid;
    }

    public String getTwilioAuthToken() {
        return System.getenv("TWILIO_AUTH_TOKEN") != null
                ? System.getenv("TWILIO_AUTH_TOKEN")
                : twilioAuthToken;
    }
}