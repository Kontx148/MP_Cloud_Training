package gyak.config;


import lombok.Data;

@Data
public class JdbcConfig {
    private boolean createTables;
    private String driverClass;
    private String url;
    private String user;
    private String password;
}
