package be.vdab.startrek.werknemers;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class WerknemerRepository {
    private final JdbcClient jdbcClient;

    public WerknemerRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Werknemer> toonAlleWerknemers() {
        var sql = """
                select id,voornaam,familienaam,budget
                from werknemers
                order by voornaam
                """;
        return jdbcClient.sql(sql)
                .query(Werknemer.class)
                .list();
    }

    public Optional<Werknemer> findByIdEnLock(long id) {
        var sql = """
                 select id,voornaam,familienaam,budget
                 from werknemers
                 where id = ?
                 for update 
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(Werknemer.class)
                .optional();
    }
    public void updateBudget(long id, BigDecimal budget){
        var sql = """
                update werknemers
                set budget = ?
                where id = ?
                """;
        if (jdbcClient.sql(sql).params(budget,id).update()== 0){
            throw new WerknemerNietGevondenException(id);
        }
    }

}
