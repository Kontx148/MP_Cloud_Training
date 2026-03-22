package gyak.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MainConfiguration {
    @JsonProperty("jdbc")
    private JdbcConfig jdbcConfig;
    @JsonProperty("connectionPool")
    private PoolConfig poolConfig;
}
