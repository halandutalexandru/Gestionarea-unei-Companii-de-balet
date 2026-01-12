package com.proiectbd.pilotlogin.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean usernameExists(String username) {
        Integer cnt = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) FROM Utilizatori WHERE username = ?",
                Integer.class,
                username
        );
        return cnt != null && cnt > 0;
    }

    public void registerClient(String username, String parola) {
        jdbcTemplate.update(
                "INSERT INTO Utilizatori (username, parola, grad) VALUES (?, ?, ?)",
                username, parola, "CLIENT"
        );
    }

}
