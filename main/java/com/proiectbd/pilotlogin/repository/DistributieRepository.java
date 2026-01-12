package com.proiectbd.pilotlogin.repository;

import com.proiectbd.pilotlogin.model.DistributieItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DistributieRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DistributieItem> findByReprezentatie(int idReprezentatie) {
        String sql = """
            SELECT rd.id_reprezentatie, rd.id_dansator,
                   d.nume, d.prenume,
                   rd.rol_in_reprezentatie
            FROM Reprezentatie_Dansator rd
            JOIN Dansatori d ON d.id_dansator = rd.id_dansator
            WHERE rd.id_reprezentatie = ?
            ORDER BY d.nume, d.prenume
        """;
        return jdbcTemplate.query(sql, new Object[]{idReprezentatie}, (rs, rowNum) -> {
            DistributieItem it = new DistributieItem();
            it.setId_reprezentatie(rs.getInt("id_reprezentatie"));
            it.setId_dansator(rs.getInt("id_dansator"));
            it.setNume(rs.getString("nume"));
            it.setPrenume(rs.getString("prenume"));
            it.setRol_in_reprezentatie(rs.getString("rol_in_reprezentatie"));
            return it;
        });
    }

    public void add(int idReprezentatie, int idDansator, String rol) {
        jdbcTemplate.update(
                "INSERT INTO Reprezentatie_Dansator (id_reprezentatie, id_dansator, rol_in_reprezentatie) VALUES (?,?,?)",
                idReprezentatie, idDansator, rol
        );
    }

    public void updateRole(int idReprezentatie, int idDansator, String rol) {
        jdbcTemplate.update(
                "UPDATE Reprezentatie_Dansator SET rol_in_reprezentatie = ? WHERE id_reprezentatie = ? AND id_dansator = ?",
                rol, idReprezentatie, idDansator
        );
    }

    public void remove(int idReprezentatie, int idDansator) {
        jdbcTemplate.update(
                "DELETE FROM Reprezentatie_Dansator WHERE id_reprezentatie = ? AND id_dansator = ?",
                idReprezentatie, idDansator
        );
    }

    public boolean exists(int idReprezentatie, int idDansator) {
        Integer cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) FROM Reprezentatie_Dansator WHERE id_reprezentatie = ? AND id_dansator = ?",
                Integer.class,
                idReprezentatie, idDansator
        );
        return cnt != null && cnt > 0;
    }
}
