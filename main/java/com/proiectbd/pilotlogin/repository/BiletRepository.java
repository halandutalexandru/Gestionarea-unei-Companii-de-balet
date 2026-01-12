package com.proiectbd.pilotlogin.repository;

import com.proiectbd.pilotlogin.model.Bilet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BiletRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Bilet> findAll(Integer idReprezentatie) {

        String base = """
SELECT b.id_bilet, b.id_reprezentatie, b.id_sala, b.nr_loc, b.pret, RTRIM(b.status) AS status,
       r.data_reprezentatie, r.ora_inceput,
       RTRIM(r.tip_reprezentatie) AS tip_reprezentatie,
       r.tip_spectacol,
       s.nume_sala
FROM Bilete b
JOIN Reprezentatii r ON r.id_reprezentatie = b.id_reprezentatie
JOIN Sali s ON s.id_sala = r.id_sala
""";

        if (idReprezentatie != null) {
            String sql = base + """
WHERE b.id_reprezentatie = ?
ORDER BY b.nr_loc
""";
            return jdbcTemplate.query(sql, new Object[]{idReprezentatie}, (rs, rowNum) -> map(rs));
        }

        String sql = base + """
ORDER BY r.data_reprezentatie DESC, r.ora_inceput DESC, b.nr_loc
""";
        return jdbcTemplate.query(sql, (rs, rowNum) -> map(rs));
    }


    public Bilet findById(int id) {
        String sql = """
        SELECT b.id_bilet,
               b.id_reprezentatie,
               b.nr_loc,
               b.pret,
               RTRIM(b.status) AS status,
               r.data_reprezentatie,
               r.ora_inceput,
               RTRIM(r.tip_reprezentatie) AS tip_reprezentatie,
               RTRIM(r.tip_spectacol)     AS tip_spectacol,
               r.id_sala,
               s.nume_sala
        FROM Bilete b
        JOIN Reprezentatii r ON r.id_reprezentatie = b.id_reprezentatie
        JOIN Sali s ON s.id_sala = r.id_sala
        WHERE b.id_bilet = ?
    """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> map(rs));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    // luam id_sala si capacitate din reprezentatie (ca sa validam locul)
    public Integer getSalaIdByReprezentatie(int idReprezentatie) {
        return jdbcTemplate.queryForObject(
                "SELECT id_sala FROM Reprezentatii WHERE id_reprezentatie = ?",
                Integer.class,
                idReprezentatie
        );
    }

    public Integer getCapacitateSala(int idSala) {
        return jdbcTemplate.queryForObject(
                "SELECT capacitate FROM Sali WHERE id_sala = ?",
                Integer.class,
                idSala
        );
    }

    public void insert(Bilet b) {
        String sql = """
            INSERT INTO Bilete (id_reprezentatie, nr_loc, pret, status, id_sala)
            VALUES (?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql,
                b.getId_reprezentatie(),
                b.getNr_loc(),
                b.getPret(),
                b.getStatus(),
                b.getId_sala()
        );
    }

    public void update(Bilet b) {
        String sql = """
            UPDATE Bilete
            SET id_reprezentatie = ?, nr_loc = ?, pret = ?, status = ?, id_sala = ?
            WHERE id_bilet = ?
        """;
        jdbcTemplate.update(sql,
                b.getId_reprezentatie(),
                b.getNr_loc(),
                b.getPret(),
                b.getStatus(),
                b.getId_sala(),
                b.getId_bilet()
        );
    }

    public void deleteById(int id) {
        jdbcTemplate.update("DELETE FROM Bilete WHERE id_bilet = ?", id);
    }

    private Bilet map(java.sql.ResultSet rs) throws java.sql.SQLException {
        Bilet b = new Bilet();
        b.setId_bilet(rs.getInt("id_bilet"));
        b.setId_reprezentatie(rs.getInt("id_reprezentatie"));
        b.setNr_loc(rs.getInt("nr_loc"));
        b.setPret(rs.getInt("pret"));
        b.setStatus(rs.getString("status"));
        b.setId_sala(rs.getInt("id_sala"));

        b.setTip_reprezentatie(rs.getString("tip_reprezentatie"));
        b.setData_reprezentatie(rs.getInt("data_reprezentatie"));
        b.setOra_inceput(rs.getInt("ora_inceput"));
        b.setNume_sala(rs.getString("nume_sala"));
        b.setTip_spectacol(rs.getString("tip_spectacol"));


        b.setTip_spectacol(rs.getString("tip_spectacol"));

        return b;
    }

}
