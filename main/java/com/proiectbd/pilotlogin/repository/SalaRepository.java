package com.proiectbd.pilotlogin.repository;

import com.proiectbd.pilotlogin.model.Sala;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SalaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Sala> findAll() {
        String sql = "SELECT id_sala, nume_sala FROM Sali ORDER BY nume_sala";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Sala s = new Sala();
            s.setId_sala(rs.getInt("id_sala"));
            s.setNume_sala(rs.getString("nume_sala"));
            return s;
        });
    }

    public boolean existsById(int idSala) {
        Integer cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) FROM Sali WHERE id_sala = ?",
                Integer.class,
                idSala
        );
        return cnt != null && cnt > 0;
    }
}
