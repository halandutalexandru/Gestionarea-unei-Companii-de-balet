package com.proiectbd.pilotlogin.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RezervariRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> findMy(int idUtilizator) {
        String sql = """
            SELECT r.id_rezervare, r.data_rezervare,
                   b.nr_loc, b.pret,
                   rep.data_reprezentatie, rep.ora_inceput, RTRIM(rep.tip_reprezentatie) AS tip_reprezentatie,
                   s.nume_sala
            FROM Rezervari r
            JOIN Bilete b ON b.id_bilet = r.id_bilet
            JOIN Reprezentatii rep ON rep.id_reprezentatie = b.id_reprezentatie
            JOIN Sali s ON s.id_sala = rep.id_sala
            WHERE r.id_utilizator = ?
            ORDER BY r.data_rezervare DESC
        """;
        return jdbcTemplate.queryForList(sql, idUtilizator);
    }
}
