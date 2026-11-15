package tn.esprit.spring.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import tn.esprit.spring.domain.ModePaiement;
import tn.esprit.spring.service.CartePaiementService;
import tn.esprit.spring.service.EspecesPaiementService;
import tn.esprit.spring.service.IMoyenPaiementService;
import tn.esprit.spring.service.PaiementModeService;

import java.math.BigDecimal;
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

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PaiementModeService paiementModeService;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    void swaggerRestValidationAndSearchEndpointsAreAvailable() throws Exception {
        HttpResponse<String> swaggerUi = send("GET", "/swagger-ui/index.html", null);
        assertThat(swaggerUi.statusCode()).isEqualTo(200);

        HttpResponse<String> openApi = send("GET", "/v3/api-docs", null);
        assertThat(openApi.statusCode()).isEqualTo(200);
        assertThat(openApi.body())
                .contains("AutoLoc API")
                .contains("/employes", "/vehicules", "/paiements", "/paiements/modes")
                .contains("VehiculeRequest", "PaiementRequest", "MaintenanceRequest", "PaiementModeResponse");

        for (String path : new String[]{
                "/agences/listall", "/clients/listall", "/contrats/listall",
                "/employes/listall", "/equipements/listall", "/maintenances/listall",
                "/paiements/listall", "/reservations/listall", "/vehicules/listall"
        }) {
            HttpResponse<String> response = send("GET", path, null);
            assertThat(response.statusCode()).as(path).isEqualTo(200);
            assertThat(response.body()).as(path).isEqualTo("[]");
        }

        HttpResponse<String> invalidEmail = send("POST", "/clients",
                "{\"nom\":\"Zneidi\",\"prenom\":\"Ahmed\",\"email\":\"pas-un-email\","
                        + "\"telephone\":\"+216 20 000 000\",\"numPermis\":\"PERMIS-VALIDATION\","
                        + "\"dateInscription\":\"2026-11-15\"}");
        assertThat(invalidEmail.statusCode()).isEqualTo(400);
        assertThat(invalidEmail.body()).contains("fieldErrors", "email");

        HttpResponse<String> invalidTarif = send("POST", "/vehicules",
                "{\"immatriculation\":\"TU-NEGATIF\",\"marque\":\"Renault\",\"modele\":\"Clio\","
                        + "\"categorie\":\"CITADINE\",\"tarifJournalier\":-10,\"statut\":\"DISPONIBLE\"}");
        assertThat(invalidTarif.statusCode()).isEqualTo(400);

        HttpResponse<String> maintenanceSansVehicule = send("POST", "/maintenances",
                "{\"dateDebut\":\"2026-11-15\",\"description\":\"Sans vehicule\"}");
        assertThat(maintenanceSansVehicule.statusCode()).isEqualTo(400);
        assertThat(maintenanceSansVehicule.body()).contains("fieldErrors", "idVehicule");

        HttpResponse<String> reservationInvalide = send("POST", "/reservations",
                "{\"dateDebut\":\"2026-11-20\",\"dateFin\":\"2026-11-10\",\"statut\":\"EN_ATTENTE\"}");
        assertThat(reservationInvalide.statusCode()).isEqualTo(400);
        assertThat(reservationInvalide.body()).contains("periodeValide");

        HttpResponse<String> agence = send("POST", "/agences",
                "{\"nom\":\"AutoLoc Tunis\",\"ville\":\"Tunis\",\"adresse\":\"12 rue de la Liberte\","
                        + "\"telephone\":\"+216 71 000 000\"}");
        assertThat(agence.statusCode()).isEqualTo(201);
        assertThat(agence.body()).contains("AutoLoc Tunis");
        String idAgence = extraireId(agence.body(), "idAgence");

        HttpResponse<String> client = send("POST", "/clients",
                "{\"nom\":\"Zneidi\",\"prenom\":\"Ahmed\",\"email\":\"ahmed.zneidi@example.com\","
                        + "\"telephone\":\"+216 20 000 000\",\"numPermis\":\"PERMIS-2026-001\","
                        + "\"dateInscription\":\"2026-11-15\"}");
        assertThat(client.statusCode()).isEqualTo(201);
        assertThat(client.body()).contains("ahmed.zneidi@example.com");
        String idClient = extraireId(client.body(), "idClient");

        HttpResponse<String> employe = send("POST", "/employes",
                "{\"nom\":\"Zneidi\",\"prenom\":\"Ahmed\",\"role\":\"AGENT\"}");
        assertThat(employe.statusCode()).isEqualTo(201);
        String idEmploye = extraireId(employe.body(), "idEmploye");

        HttpResponse<String> employeAffecte = send("PATCH",
                "/employes/" + idEmploye + "/agence/" + idAgence, null);
        assertThat(employeAffecte.statusCode()).isEqualTo(200);
        assertThat(employeAffecte.body()).contains("\"idAgence\":" + idAgence);

        HttpResponse<String> employesAgence = send("GET", "/employes/agence/" + idAgence, null);
        assertThat(employesAgence.statusCode()).isEqualTo(200);
        assertThat(employesAgence.body()).contains("Zneidi");

        HttpResponse<String> updated = send("PUT", "/employes/" + idEmploye,
                "{\"nom\":\"ahmed\",\"prenom\":\"Updated\",\"role\":\"MANAGER\"}");
        assertThat(updated.statusCode()).isEqualTo(200);
        assertThat(updated.body()).contains("Updated", "MANAGER");

        HttpResponse<String> employeRecherche = send("GET", "/employes/recherche?q=Updated", null);
        assertThat(employeRecherche.statusCode()).isEqualTo(200);
        assertThat(employeRecherche.body()).contains("Updated");

        HttpResponse<String> deleted = send("DELETE", "/employes/" + idEmploye, null);
        assertThat(deleted.statusCode()).isEqualTo(204);

        HttpResponse<String> missing = send("GET", "/employes/" + idEmploye, null);
        assertThat(missing.statusCode()).isEqualTo(404);

        HttpResponse<String> paiement = send("POST", "/paiements",
                "{\"montant\":100.50,\"datePaiement\":\"2026-11-15\",\"modePaiement\":\"CARTE\"}");
        assertThat(paiement.statusCode()).isEqualTo(201);
        String idPaiement = extraireId(paiement.body(), "idPaiement");

        HttpResponse<String> paiementCree = send("GET", "/paiements/" + idPaiement, null);
        assertThat(paiementCree.statusCode()).isEqualTo(200);
        assertThat(paiementCree.body()).contains("100.50", "CARTE");

        HttpResponse<String> paiementsCarte = send("GET", "/paiements/mode/CARTE", null);
        assertThat(paiementsCarte.statusCode()).isEqualTo(200);
        assertThat(paiementsCarte.body()).contains("100.50");

        HttpResponse<String> vehicule = send("POST", "/vehicules",
                "{\"immatriculation\":\"TU-1234\",\"marque\":\"Renault\",\"modele\":\"Clio\","
                        + "\"categorie\":\"CITADINE\",\"tarifJournalier\":120.0,\"statut\":\"DISPONIBLE\"}");
        assertThat(vehicule.statusCode()).isEqualTo(201);
        assertThat(vehicule.body()).contains("TU-1234", "Renault");
        String idVehicule = extraireId(vehicule.body(), "idVehicule");

        HttpResponse<String> vehiculeDuplicate = send("POST", "/vehicules",
                "{\"immatriculation\":\"TU-1234\",\"marque\":\"Renault\",\"modele\":\"Clio\","
                        + "\"categorie\":\"CITADINE\",\"tarifJournalier\":120.0,\"statut\":\"DISPONIBLE\"}");
        assertThat(vehiculeDuplicate.statusCode()).isEqualTo(409);

        HttpResponse<String> vehiculeRecherche = send("GET", "/vehicules/immatriculation/TU-1234", null);
        assertThat(vehiculeRecherche.statusCode()).isEqualTo(200);
        assertThat(vehiculeRecherche.body()).contains("Renault", "TU-1234");

        HttpResponse<String> vehiculeExiste = send("GET", "/vehicules/existe/TU-1234", null);
        assertThat(vehiculeExiste.statusCode()).isEqualTo(200);
        assertThat(vehiculeExiste.body()).isEqualTo("true");

        HttpResponse<String> vehiculesMarque = send("GET", "/vehicules/marque/Renault", null);
        assertThat(vehiculesMarque.statusCode()).isEqualTo(200);
        assertThat(vehiculesMarque.body()).contains("TU-1234");

        HttpResponse<String> vehiculesCount = send("GET", "/vehicules/statut/DISPONIBLE/count", null);
        assertThat(vehiculesCount.statusCode()).isEqualTo(200);
        assertThat(vehiculesCount.body()).isEqualTo("1");

        HttpResponse<String> maintenance = send("POST", "/maintenances",
                "{\"dateDebut\":\"2026-11-15\",\"dateFin\":null,\"description\":\"Revision\","
                        + "\"idVehicule\":" + idVehicule + "}");
        assertThat(maintenance.statusCode()).isEqualTo(201);
        assertThat(maintenance.body()).contains("Revision", "\"idVehicule\":" + idVehicule);

        HttpResponse<String> maintenancesVehicule = send("GET", "/maintenances/vehicule/" + idVehicule, null);
        assertThat(maintenancesVehicule.statusCode()).isEqualTo(200);
        assertThat(maintenancesVehicule.body()).contains("Revision");

        HttpResponse<String> maintenancesCount = send("GET",
                "/maintenances/vehicule/" + idVehicule + "/count", null);
        assertThat(maintenancesCount.statusCode()).isEqualTo(200);
        assertThat(maintenancesCount.body()).isEqualTo("1");

        HttpResponse<String> reservation = send("POST", "/reservations",
                "{\"dateDebut\":\"2026-11-20\",\"dateFin\":\"2026-11-25\",\"statut\":\"EN_ATTENTE\"}");
        assertThat(reservation.statusCode()).isEqualTo(201);
        String idReservation = extraireId(reservation.body(), "idReservation");

        HttpResponse<String> reservationAffectee = send("PATCH",
                "/reservations/" + idReservation + "/client/" + idClient, null);
        assertThat(reservationAffectee.statusCode()).isEqualTo(200);
        assertThat(reservationAffectee.body()).contains("\"idClient\":" + idClient);

        HttpResponse<String> reservationsClient = send("GET", "/reservations/client/" + idClient, null);
        assertThat(reservationsClient.statusCode()).isEqualTo(200);
        assertThat(reservationsClient.body()).contains("\"idReservation\":" + idReservation);

        HttpResponse<String> clientRecherche = send("GET",
                "/clients/recherche/email/ahmed.zneidi@example.com", null);
        assertThat(clientRecherche.statusCode()).isEqualTo(200);
        assertThat(clientRecherche.body()).contains("Ahmed");

        HttpResponse<String> agenceRecherche = send("GET", "/agences/recherche?q=Auto", null);
        assertThat(agenceRecherche.statusCode()).isEqualTo(200);
        assertThat(agenceRecherche.body()).contains("AutoLoc Tunis");

        HttpResponse<String> paiementParDefaut = send("GET", "/paiements/modes/defaut?montant=100", null);
        assertThat(paiementParDefaut.statusCode()).isEqualTo(200);
        assertThat(paiementParDefaut.body()).contains("CARTE", "carte");

        HttpResponse<String> paiementEspeces = send("GET", "/paiements/modes/especes?montant=100", null);
        assertThat(paiementEspeces.statusCode()).isEqualTo(200);
        assertThat(paiementEspeces.body()).contains("ESPECES", "esp");
    }

    @Test
    void primaryAndQualifierBeansAreResolved() {
        IMoyenPaiementService paiementParDefaut = applicationContext.getBean(IMoyenPaiementService.class);
        assertThat(paiementParDefaut).isInstanceOf(CartePaiementService.class);

        IMoyenPaiementService paiementEspeces = applicationContext.getBean(
                "especesPaiementService", IMoyenPaiementService.class);
        assertThat(paiementEspeces).isInstanceOf(EspecesPaiementService.class);

        assertThat(paiementModeService.payerParDefaut(BigDecimal.TEN).mode())
                .isEqualTo(ModePaiement.CARTE);
        assertThat(paiementModeService.payerEnEspeces(BigDecimal.TEN).mode())
                .isEqualTo(ModePaiement.ESPECES);
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
