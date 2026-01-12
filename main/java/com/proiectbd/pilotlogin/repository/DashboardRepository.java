package com.proiectbd.pilotlogin.repository;

import com.proiectbd.pilotlogin.model.MonthRevenue;
import com.proiectbd.pilotlogin.model.RepStat;
import com.proiectbd.pilotlogin.model.SponsorSum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DashboardRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int totalIncasari() {
        String sql = """
            SELECT COALESCE(SUM(CASE WHEN RTRIM(status) = 'Vandut' THEN pret ELSE 0 END), 0) AS inc
            FROM Bilete
        """;
        Integer x = jdbcTemplate.queryForObject(sql, Integer.class);
        return x == null ? 0 : x;
    }

    public int totalCountByStatus(String status) {
        String sql = "SELECT COALESCE(SUM(CASE WHEN RTRIM(status)=? THEN 1 ELSE 0 END),0) FROM Bilete";
        Integer x = jdbcTemplate.queryForObject(sql, Integer.class, status);
        return x == null ? 0 : x;
    }

    // TOP 5 reprezentatii dupa incasari (JOIN + GROUP BY)
    public List<RepStat> topReprezentatiiByRevenue() {
        String sql = """
            SELECT TOP 5
                r.id_reprezentatie,
                RTRIM(r.tip_reprezentatie) AS tip_reprezentatie,
                r.data_reprezentatie,
                r.ora_inceput,
                s.nume_sala,
                s.capacitate,
                SUM(CASE WHEN RTRIM(b.status)='Vandut' THEN 1 ELSE 0 END) AS vandute,
                SUM(CASE WHEN RTRIM(b.status)='Rezervat' THEN 1 ELSE 0 END) AS rezervate,
                SUM(CASE WHEN RTRIM(b.status)='Liber' THEN 1 ELSE 0 END) AS libere,
                SUM(CASE WHEN RTRIM(b.status)='Vandut' THEN b.pret ELSE 0 END) AS incasari
            FROM Reprezentatii r
            JOIN Sali s ON s.id_sala = r.id_sala
            LEFT JOIN Bilete b ON b.id_reprezentatie = r.id_reprezentatie
            GROUP BY r.id_reprezentatie, r.tip_reprezentatie, r.data_reprezentatie, r.ora_inceput, s.nume_sala, s.capacitate
            ORDER BY incasari DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RepStat x = new RepStat();
            x.setId_reprezentatie(rs.getInt("id_reprezentatie"));
            x.setTip_reprezentatie(rs.getString("tip_reprezentatie"));
            x.setData_reprezentatie(rs.getInt("data_reprezentatie"));
            x.setOra_inceput(rs.getInt("ora_inceput"));
            x.setNume_sala(rs.getString("nume_sala"));
            x.setCapacitate(rs.getInt("capacitate"));
            x.setVandute(rs.getInt("vandute"));
            x.setRezervate(rs.getInt("rezervate"));
            x.setLibere(rs.getInt("libere"));
            x.setIncasari(rs.getInt("incasari"));
            return x;
        });
    }

    // Toate reprezentatiile + ocupare (JOIN + GROUP BY)
    public List<RepStat> ocuparePerReprezentatie() {
        String sql = """
            SELECT
                r.id_reprezentatie,
                RTRIM(r.tip_reprezentatie) AS tip_reprezentatie,
                r.data_reprezentatie,
                r.ora_inceput,
                s.nume_sala,
                s.capacitate,
                SUM(CASE WHEN RTRIM(b.status)='Vandut' THEN 1 ELSE 0 END) AS vandute,
                SUM(CASE WHEN RTRIM(b.status)='Rezervat' THEN 1 ELSE 0 END) AS rezervate,
                SUM(CASE WHEN RTRIM(b.status)='Liber' THEN 1 ELSE 0 END) AS libere,
                SUM(CASE WHEN RTRIM(b.status)='Vandut' THEN b.pret ELSE 0 END) AS incasari
            FROM Reprezentatii r
            JOIN Sali s ON s.id_sala = r.id_sala
            LEFT JOIN Bilete b ON b.id_reprezentatie = r.id_reprezentatie
            GROUP BY r.id_reprezentatie, r.tip_reprezentatie, r.data_reprezentatie, r.ora_inceput, s.nume_sala, s.capacitate
            ORDER BY r.data_reprezentatie DESC, r.ora_inceput DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RepStat x = new RepStat();
            x.setId_reprezentatie(rs.getInt("id_reprezentatie"));
            x.setTip_reprezentatie(rs.getString("tip_reprezentatie"));
            x.setData_reprezentatie(rs.getInt("data_reprezentatie"));
            x.setOra_inceput(rs.getInt("ora_inceput"));
            x.setNume_sala(rs.getString("nume_sala"));
            x.setCapacitate(rs.getInt("capacitate"));
            x.setVandute(rs.getInt("vandute"));
            x.setRezervate(rs.getInt("rezervate"));
            x.setLibere(rs.getInt("libere"));
            x.setIncasari(rs.getInt("incasari"));
            return x;
        });
    }

    // Incasari pe luna (GROUP BY pe YYYYMM din YYYYMMDD)
    public List<MonthRevenue> incasariPeLuna() {
        String sql = """
            SELECT
                (r.data_reprezentatie / 100) AS yyyymm,
                SUM(CASE WHEN RTRIM(b.status)='Vandut' THEN b.pret ELSE 0 END) AS incasari
            FROM Reprezentatii r
            LEFT JOIN Bilete b ON b.id_reprezentatie = r.id_reprezentatie
            GROUP BY (r.data_reprezentatie / 100)
            ORDER BY yyyymm DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            MonthRevenue m = new MonthRevenue();
            m.setYyyymm(rs.getInt("yyyymm"));
            m.setIncasari(rs.getInt("incasari"));
            return m;
        });
    }

    // SUBINTEROGARE: reprezentatii cu incasari peste media incasarilor (subquery)
    public List<RepStat> pesteMediaIncasarilor() {
        String sql = """
            WITH rep_inc AS (
                SELECT r.id_reprezentatie,
                       RTRIM(r.tip_reprezentatie) AS tip_reprezentatie,
                       r.data_reprezentatie,
                       r.ora_inceput,
                       s.nume_sala,
                       s.capacitate,
                       SUM(CASE WHEN RTRIM(b.status)='Vandut' THEN b.pret ELSE 0 END) AS incasari,
                       SUM(CASE WHEN RTRIM(b.status)='Vandut' THEN 1 ELSE 0 END) AS vandute
                FROM Reprezentatii r
                JOIN Sali s ON s.id_sala = r.id_sala
                LEFT JOIN Bilete b ON b.id_reprezentatie = r.id_reprezentatie
                GROUP BY r.id_reprezentatie, r.tip_reprezentatie, r.data_reprezentatie, r.ora_inceput, s.nume_sala, s.capacitate
            )
            SELECT *
            FROM rep_inc
            WHERE incasari > (SELECT AVG(CAST(incasari AS FLOAT)) FROM rep_inc)
            ORDER BY incasari DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            RepStat x = new RepStat();
            x.setId_reprezentatie(rs.getInt("id_reprezentatie"));
            x.setTip_reprezentatie(rs.getString("tip_reprezentatie"));
            x.setData_reprezentatie(rs.getInt("data_reprezentatie"));
            x.setOra_inceput(rs.getInt("ora_inceput"));
            x.setNume_sala(rs.getString("nume_sala"));
            x.setCapacitate(rs.getInt("capacitate"));
            x.setIncasari(rs.getInt("incasari"));
            x.setVandute(rs.getInt("vandute"));
            return x;
        });
    }

    // Sponsori pe tip
    public List<SponsorSum> sponsoriPeTip() {
        String sql = """
            SELECT RTRIM(tip_sponsor) AS tip_sponsor, SUM(suma_donata) AS total
            FROM Sponsori
            GROUP BY RTRIM(tip_sponsor)
            ORDER BY total DESC
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            SponsorSum s = new SponsorSum();
            s.setTip_sponsor(rs.getString("tip_sponsor"));
            s.setTotal(rs.getInt("total"));
            return s;
        });
    }
}
