package tn.esprit.spring.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:h2:mem:autoloc_db-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "server.servlet.context-path="
})
class AutoLocApiIntegrationTest {

    @LocalServerPort
    private int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    void swaggerAndCrudEndpointsAreAvailable() throws Exception {
        HttpResponse<String> swaggerUi = send("GET", "/swagger-ui/index.html", null);
        assertThat(swaggerUi.statusCode()).isEqualTo(200);

        HttpResponse<String> openApi = send("GET", "/v3/api-docs", null);
        assertThat(openApi.statusCode()).isEqualTo(200);
        assertThat(openApi.body()).contains("/employes", "/vehicules", "/reservations");

        for (String path : new String[]{
                "/agences/listall", "/clients/listall", "/contrats/listall",
                "/employes/listall", "/equipements/listall", "/maintenances/listall",
                "/paiements/listall", "/reservations/listall", "/vehicules/listall"
        }) {
            HttpResponse<String> response = send("GET", path, null);
            assertThat(response.statusCode()).as(path).isEqualTo(200);
            assertThat(response.body()).as(path).isEqualTo("[]");
        }

        HttpResponse<String> created = send("POST", "/employes",
                "{\"idEmploye\":0,\"nom\":\"ahmed\",\"prenom\":\"zneidi\",\"role\":\"AGENT\"}");
        assertThat(created.statusCode()).isEqualTo(201);
        String id = extraireId(created.body(), "idEmploye");

        HttpResponse<String> updated = send("PUT", "/employes/" + id,
                "{\"nom\":\"ahmed\",\"prenom\":\"Updated\",\"role\":\"MANAGER\"}");
        assertThat(updated.statusCode()).isEqualTo(200);
        assertThat(updated.body()).contains("Updated", "MANAGER");

        HttpResponse<String> deleted = send("DELETE", "/employes/" + id, null);
        assertThat(deleted.statusCode()).isEqualTo(204);

        HttpResponse<String> missing = send("GET", "/employes/" + id, null);
        assertThat(missing.statusCode()).isEqualTo(404);

        HttpResponse<String> paiement = send("POST", "/paiements",
                "{\"idPaiement\":0,\"montant\":100.50,\"datePaiement\":\"2026-11-15\","
                        + "\"modePaiement\":\"CARTE\"}");
        assertThat(paiement.statusCode()).isEqualTo(201);
        String idPaiement = extraireId(paiement.body(), "idPaiement");

        HttpResponse<String> paiementCree = send("GET", "/paiements/" + idPaiement, null);
        assertThat(paiementCree.statusCode()).isEqualTo(200);

        HttpResponse<String> vehicule = send("POST", "/vehicules",
                "{\"idVehicule\":0,\"immatriculation\":\"TU-1234\",\"marque\":\"Renault\","
                        + "\"modele\":\"Clio\",\"categorie\":\"CITADINE\",\"tarifJournalier\":120.0,"
                        + "\"statut\":\"DISPONIBLE\"}");
        assertThat(vehicule.statusCode()).isEqualTo(201);
        String idVehicule = extraireId(vehicule.body(), "idVehicule");

        HttpResponse<String> maintenanceSansVehicule = send("POST", "/maintenances",
                "{\"dateDebut\":\"2026-11-15\",\"description\":\"Sans vehicule\"}");
        assertThat(maintenanceSansVehicule.statusCode()).isEqualTo(400);

        HttpResponse<String> maintenance = send("POST", "/maintenances",
                "{\"dateDebut\":\"2026-11-15\",\"dateFin\":null,\"description\":\"Revision\","
                        + "\"idVehicule\":" + idVehicule + "}");
        assertThat(maintenance.statusCode()).isEqualTo(201);
        assertThat(maintenance.body()).contains("Revision");

        HttpResponse<String> maintenancesVehicule = send("GET", "/maintenances/vehicule/" + idVehicule, null);
        assertThat(maintenancesVehicule.statusCode()).isEqualTo(200);
        assertThat(maintenancesVehicule.body()).contains("Revision");
    }

    private String extraireId(String body, String champ) {
        Matcher matcher = Pattern.compile("\"" + champ + "\"\\s*:\\s*(\\d+)").matcher(body);
        assertThat(matcher.find()).isTrue();
        return matcher.group(1);
    }

    private HttpResponse<String> send(String method, String path, String body) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path));
        if (body == null) {
            request.method(method, HttpRequest.BodyPublishers.noBody());
        } else {
            request.header("Content-Type", "application/json")
                    .method(method, HttpRequest.BodyPublishers.ofString(body));
        }
        return httpClient.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }
}
