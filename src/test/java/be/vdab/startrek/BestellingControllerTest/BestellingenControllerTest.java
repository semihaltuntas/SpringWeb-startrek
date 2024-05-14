package be.vdab.startrek.BestellingControllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}