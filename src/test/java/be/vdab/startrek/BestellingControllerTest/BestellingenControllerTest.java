package be.vdab.startrek.BestellingControllerTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Sql({"/werknemers.sql", "/bestellingen.sql"})
@AutoConfigureMockMvc
class BestellingenControllerTest {
    private final String WERKNEMERS_TABLE = "werknemers";
    private final String BESTELLINGEN_TABLE = "bestellingen";
    private final static Path TEST_RESOURCE = Path.of("src/test/resources");
    private final MockMvc mockMvc;
    private final JdbcClient jdbcClient;

    public BestellingenControllerTest(MockMvc mockMvc, JdbcClient jdbcClient) {
        this.mockMvc = mockMvc;
        this.jdbcClient = jdbcClient;
    }

    long idVanTest1() {
        var sql = """
                select id from werknemers where voornaam='test1';
                """;
        return jdbcClient.sql(sql).query(Long.class).single();
    }

    @Test
    void findBestellinByWerknemerId() throws Exception {
        var id = idVanTest1();
        //  System.out.println(id);
        var aantalbestellingen = JdbcTestUtils.countRowsInTableWhere(
                jdbcClient, BESTELLINGEN_TABLE, "werknemerId =" + id);
        //  System.out.println(aantalbestellingen);
        mockMvc.perform(get("/werknemers/{id}/bestellingen", id))
                .andExpectAll(status().isOk(),
                        jsonPath("length()").value(aantalbestellingen));
    }

    @Test
    void bestelMaaktEenBestellingEnVermindertHetWerknemerBudget() throws Exception {
        var jsonData = Files.readString(TEST_RESOURCE.resolve("correcteBestelling.json"));
        var id = idVanTest1();
        mockMvc.perform(post("/werknemers/{id}/bestellingen", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpectAll(status().isOk());
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, BESTELLINGEN_TABLE,
                "omschrijving = 'test4' and bedrag = 10 and werknemerId =" + id)).isOne();
        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, WERKNEMERS_TABLE,
                "budget = 990 and id=" + id)).isOne();
    }

    @ParameterizedTest
    @ValueSource(strings = {"bestellingMetLegeOmschijving.json", "bestellingNegatieveBedrag.json",
            "bestellingZonderBedrag.json", "bestellingZonderOmschrijving.json"})
    void bestellingMetVerkeerdeDataMislukt(String bestandsnaam) throws Exception {
        var jsonData = Files.readString(TEST_RESOURCE.resolve(bestandsnaam));
        var id = idVanTest1();
        mockMvc.perform(post("/werknemers/{id}/bestellingen", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpectAll(status().isBadRequest());
    }

    @Test
    void bestellingMetTeGrootBedrag() throws Exception {
        var jsonData = Files.readString(TEST_RESOURCE.resolve("bestellingMetTeGrootBedrag.json"));
        var id = idVanTest1();
        mockMvc.perform(post("/werknemers/{id}/bestellingen", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpectAll(status().isConflict());
    }

    @Test
    void bestelVoorOnbestaandeWerknemerMislukt() throws Exception {
        var jsonData = Files.readString(TEST_RESOURCE.resolve("correcteBestelling.json"));
        mockMvc.perform(post("/werknemers/{id}/bestellingen", Long.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonData))
                .andExpectAll(status().isNotFound());
    }
}