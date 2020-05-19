package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
public class DropwizardAuthConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private String template;


    @Valid
    @NotNull
    @JsonProperty
    private String defaultName;






    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();




    @NotNull
    @JsonProperty
    private Map<String, Object> defaultHystrixConfig;


}
