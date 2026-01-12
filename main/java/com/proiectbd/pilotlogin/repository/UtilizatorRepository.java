package com.proiectbd.pilotlogin.repository;

import com.proiectbd.pilotlogin.model.Utilizator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class UtilizatorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Utilizator findByUsernameAndPassword(String username, String parola) {
        String sql = "SELECT * FROM Utilizatori WHERE username = ? AND parola = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username, parola}, (rs, rowNum) -> {
                Utilizator u = new Utilizator();
                u.setId_utilizator(rs.getInt("id_utilizator"));
                u.setUsername(rs.getString("username"));
                u.setParola(rs.getString("parola"));
                u.setGrad(rs.getString("grad"));
                u.setId_dansator(rs.getObject("id_dansator") != null ? rs.getInt("id_dansator") : null);
                return u;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void addUser(String username, String parola, String grad, Integer id_dansator) {
        String sql = "INSERT INTO Utilizatori (username, parola, grad, id_dansator) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, username, parola, grad, id_dansator);
    }
    public boolean existsByUsername(String username) {
        Integer cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) FROM Utilizatori WHERE username = ?",
                Integer.class,
                username
        );
        return cnt != null && cnt > 0;
    }

}
