package tn.esprit.spring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI autoLocOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AutoLoc API")
                        .version("1.0.0")
                        .description("API REST de gestion des agences, clients, véhicules, "
                                + "réservations, contrats, paiements et maintenances.")
                        .contact(new Contact()
                                .name("AutoLoc")
                                .email("contact@autoloc.tn")))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Serveur local"));
    }
}
