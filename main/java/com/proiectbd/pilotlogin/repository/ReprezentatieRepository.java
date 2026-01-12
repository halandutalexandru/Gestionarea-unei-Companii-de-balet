package com.proiectbd.pilotlogin.repository;

import com.proiectbd.pilotlogin.model.Reprezentatie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReprezentatieRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Reprezentatie> findAllWithSala() {
        String sql = """
        SELECT r.id_reprezentatie,
               RTRIM(r.tip_reprezentatie) AS tip_reprezentatie,
               RTRIM(r.tip_spectacol)     AS tip_spectacol,
               r.data_reprezentatie, r.ora_inceput, r.ora_sfarsit,
               r.id_sala,
               s.nume_sala
        FROM Reprezentatii r
        JOIN Sali s ON s.id_sala = r.id_sala
        ORDER BY r.data_reprezentatie DESC, r.ora_inceput DESC
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Reprezentatie r = new Reprezentatie();
            r.setId_reprezentatie(rs.getInt("id_reprezentatie"));
            r.setTip_reprezentatie(rs.getString("tip_reprezentatie"));
            r.setTip_spectacol(rs.getString("tip_spectacol"));
            r.setData_reprezentatie(rs.getInt("data_reprezentatie"));
            r.setOra_inceput(rs.getInt("ora_inceput"));
            r.setOra_sfarsit(rs.getInt("ora_sfarsit"));
            r.setId_sala(rs.getInt("id_sala"));
            r.setNume_sala(rs.getString("nume_sala"));
            return r;
        });
    }


    public Reprezentatie findById(int id) {
        String sql = """
        SELECT id_reprezentatie,
               RTRIM(tip_reprezentatie) AS tip_reprezentatie,
               RTRIM(tip_spectacol)     AS tip_spectacol,
               data_reprezentatie, ora_inceput, ora_sfarsit, id_sala
        FROM Reprezentatii
        WHERE id_reprezentatie = ?
    """;
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Reprezentatie r = new Reprezentatie();
                r.setId_reprezentatie(rs.getInt("id_reprezentatie"));
                r.setTip_reprezentatie(rs.getString("tip_reprezentatie"));
                r.setTip_spectacol(rs.getString("tip_spectacol"));
                r.setData_reprezentatie(rs.getInt("data_reprezentatie"));
                r.setOra_inceput(rs.getInt("ora_inceput"));
                r.setOra_sfarsit(rs.getInt("ora_sfarsit"));
                r.setId_sala(rs.getInt("id_sala"));
                return r;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public void insert(Reprezentatie r) {
        String sql = """
        INSERT INTO Reprezentatii (tip_reprezentatie, tip_spectacol, data_reprezentatie, ora_inceput, ora_sfarsit, id_sala)
        VALUES (?, ?, ?, ?, ?, ?)
    """;
        jdbcTemplate.update(sql,
                r.getTip_reprezentatie(),
                r.getTip_spectacol(),
                r.getData_reprezentatie(),
                r.getOra_inceput(),
                r.getOra_sfarsit(),
                r.getId_sala()
        );
    }


    public void update(Reprezentatie r) {
        String sql = """
        UPDATE Reprezentatii
        SET tip_reprezentatie = ?, tip_spectacol = ?, data_reprezentatie = ?, ora_inceput = ?, ora_sfarsit = ?, id_sala = ?
        WHERE id_reprezentatie = ?
    """;
        jdbcTemplate.update(sql,
                r.getTip_reprezentatie(),
                r.getTip_spectacol(),
                r.getData_reprezentatie(),
                r.getOra_inceput(),
                r.getOra_sfarsit(),
                r.getId_sala(),
                r.getId_reprezentatie()
        );
    }


    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM Reprezentatii WHERE id_reprezentatie = ?", id);
    }

    public List<Reprezentatie> findSpectacoleForBilete() {
        String sql = """
        SELECT DISTINCT
               r.id_reprezentatie,
               RTRIM(r.tip_reprezentatie) AS tip_reprezentatie,
               RTRIM(r.tip_spectacol)     AS tip_spectacol,
               r.data_reprezentatie, r.ora_inceput, r.ora_sfarsit,
               r.id_sala,
               s.nume_sala
        FROM Reprezentatii r
        JOIN Sali s ON s.id_sala = r.id_sala
        WHERE RTRIM(r.tip_reprezentatie) = 'Spectacol'
          AND r.tip_spectacol IS NOT NULL
          AND LTRIM(RTRIM(r.tip_spectacol)) <> ''
        ORDER BY r.data_reprezentatie DESC, r.ora_inceput DESC
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Reprezentatie r = new Reprezentatie();
            r.setId_reprezentatie(rs.getInt("id_reprezentatie"));
            r.setTip_reprezentatie(rs.getString("tip_reprezentatie"));
            r.setTip_spectacol(rs.getString("tip_spectacol"));
            r.setData_reprezentatie(rs.getInt("data_reprezentatie"));
            r.setOra_inceput(rs.getInt("ora_inceput"));
            r.setOra_sfarsit(rs.getInt("ora_sfarsit"));
            r.setId_sala(rs.getInt("id_sala"));
            r.setNume_sala(rs.getString("nume_sala"));
            return r;
        });
    }

}
