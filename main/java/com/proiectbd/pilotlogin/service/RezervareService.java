package com.proiectbd.pilotlogin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RezervareService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean rezerva(int idBilet, int idUtilizator) {
        // 1) schimba status doar daca e Liber (evita dubla rezervare)
        int updated = jdbcTemplate.update(
                "UPDATE Bilete SET status = 'Rezervat' WHERE id_bilet = ? AND RTRIM(status) = 'Liber'",
                idBilet
        );
        if (updated != 1) return false;

        // 2) salveaza rezervarea
        jdbcTemplate.update(
                "INSERT INTO Rezervari (id_bilet, id_utilizator) VALUES (?, ?)",
                idBilet, idUtilizator
        );
        return true;
    }
}
