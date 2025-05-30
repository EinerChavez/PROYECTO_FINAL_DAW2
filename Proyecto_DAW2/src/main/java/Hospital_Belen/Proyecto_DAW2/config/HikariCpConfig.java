package Hospital_Belen.Proyecto_DAW2.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class HikariCpConfig {
    @Value("${DB_USE_URL}")
    private String dbUseUrl;

    @Value("${DB_USE_USER}")
    private String dbUseUser;

    @Value("${DB_USE_PASS}")
    private String dbUsePass;

    @Value("${DB_USE_DRIVER}")
    private String dbUseDriver;

    @Bean
    public HikariDataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUseUrl);
        config.setUsername(dbUseUser);
        config.setPassword(dbUsePass);
        config.setDriverClassName(dbUseDriver);

        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setIdleTimeout(300000);
        config.setConnectionTimeout(30000);

        HikariDataSource dataSource = new HikariDataSource(config);
        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isClosed()) {
                System.out.println("✅ Conexión exitosa a la base de datos desde HikariCpConfig.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Falló la conexión a la base de datos: " + e.getMessage());
        }

        System.out.println("###### HikariCP initialized ######");
        return dataSource;
    }
}
