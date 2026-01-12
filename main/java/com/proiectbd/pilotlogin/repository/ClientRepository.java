package com.proiectbd.pilotlogin.repository;

import com.proiectbd.pilotlogin.model.Bilet;
import com.proiectbd.pilotlogin.model.Reprezentatie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Reprezentatie> spectacole() {
        String sql = """
        SELECT r.id_reprezentatie,
               RTRIM(r.tip_reprezentatie) AS tip_reprezentatie,
               r.tip_spectacol,
               r.data_reprezentatie,
               r.ora_inceput,
               r.id_sala
        FROM Reprezentatii r
        WHERE RTRIM(r.tip_reprezentatie) = 'Spectacol'
        ORDER BY r.data_reprezentatie DESC, r.ora_inceput DESC
    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Reprezentatie r = new Reprezentatie();
            r.setId_reprezentatie(rs.getInt("id_reprezentatie"));
            r.setTip_reprezentatie(rs.getString("tip_reprezentatie")); // Spectacol
            r.setTip_spectacol(rs.getString("tip_spectacol"));         // Balet Clasic etc.
            r.setData_reprezentatie(rs.getInt("data_reprezentatie"));
            r.setOra_inceput(rs.getInt("ora_inceput"));
            r.setId_sala(rs.getInt("id_sala"));
            return r;
        });
    }


    public List<Bilet> bileteByReprezentatie(int idReprezentatie) {
        String sql = """
        SELECT id_bilet, id_reprezentatie, nr_loc, pret,
               RTRIM(status) AS status, id_sala
        FROM Bilete
        WHERE id_reprezentatie = ?
        ORDER BY nr_loc
    """;

        return jdbcTemplate.query(sql, new Object[]{idReprezentatie}, (rs, rowNum) -> {
            Bilet b = new Bilet();
            b.setId_bilet(rs.getInt("id_bilet"));
            b.setId_reprezentatie(rs.getInt("id_reprezentatie"));
            b.setNr_loc(rs.getInt("nr_loc"));
            b.setPret(rs.getInt("pret"));
            b.setStatus(rs.getString("status"));
            b.setId_sala(rs.getInt("id_sala"));
            return b;
        });
    }

}
