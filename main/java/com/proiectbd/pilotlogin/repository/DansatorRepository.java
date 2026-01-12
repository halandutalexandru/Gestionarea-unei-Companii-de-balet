package com.proiectbd.pilotlogin.repository;

import com.proiectbd.pilotlogin.model.Dansator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DansatorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Dansator> findAll() {
        String sql = """
            SELECT id_dansator, nume, prenume, cnp, data_angajarii, rang
            FROM Dansatori
            ORDER BY nume, prenume
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Dansator d = new Dansator();
            d.setId_dansator(rs.getInt("id_dansator"));
            d.setNume(rs.getString("nume"));
            d.setPrenume(rs.getString("prenume"));
            d.setCnp(rs.getString("cnp"));
            d.setData_angajarii(rs.getInt("data_angajarii"));
            d.setRang(rs.getString("rang"));
            return d;
        });
    }

    public Dansator findById(int id) {
        String sql = """
            SELECT id_dansator, nume, prenume, cnp, data_angajarii, rang
            FROM Dansatori
            WHERE id_dansator = ?
        """;
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Dansator d = new Dansator();
                d.setId_dansator(rs.getInt("id_dansator"));
                d.setNume(rs.getString("nume"));
                d.setPrenume(rs.getString("prenume"));
                d.setCnp(rs.getString("cnp"));
                d.setData_angajarii(rs.getInt("data_angajarii"));
                d.setRang(rs.getString("rang"));
                return d;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void insert(Dansator d) {
        String sql = """
            INSERT INTO Dansatori (nume, prenume, cnp, data_angajarii, rang)
            VALUES (?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql, d.getNume(), d.getPrenume(), d.getCnp(), d.getData_angajarii(), d.getRang());
    }

    public void update(Dansator d) {
        String sql = """
            UPDATE Dansatori
            SET nume = ?, prenume = ?, cnp = ?, data_angajarii = ?, rang = ?
            WHERE id_dansator = ?
        """;
        jdbcTemplate.update(sql, d.getNume(), d.getPrenume(), d.getCnp(), d.getData_angajarii(), d.getRang(), d.getId_dansator());
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM Dansatori WHERE id_dansator = ?", id);
    }
}
