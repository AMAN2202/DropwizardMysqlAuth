package org.example.empApi.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

////import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
//import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class DropwizardAuthConfiguration extends Configuration {

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;
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
