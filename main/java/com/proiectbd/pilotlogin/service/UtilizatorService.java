package com.proiectbd.pilotlogin.service;

import com.proiectbd.pilotlogin.model.Utilizator;
import com.proiectbd.pilotlogin.repository.UtilizatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UtilizatorService {

    private static final Set<String> ROLES = Set.of("ADMIN", "ANGAJAT", "CLIENT", "ARTIST");

    @Autowired
    private UtilizatorRepository utilizatorRepository;

    public Utilizator login(String username, String parola) {
        return utilizatorRepository.findByUsernameAndPassword(username, parola);
    }

    public void addUser(String username, String parola, String grad, Integer id_dansator) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username obligatoriu.");
        }
        if (parola == null || parola.trim().isEmpty()) {
            throw new IllegalArgumentException("Parola obligatorie.");
        }

        String role = (grad == null) ? "" : grad.trim().toUpperCase();
        if (!ROLES.contains(role)) {
            throw new IllegalArgumentException("Rol invalid. Foloseste: ADMIN / ANGAJAT / CLIENT / ARTIST.");
        }

        if (utilizatorRepository.existsByUsername(username.trim())) {
            throw new IllegalArgumentException("Username deja existent.");
        }

        // daca e ARTIST, trebuie sa legam contul de un dansator (id_dansator)
        if ("ARTIST".equals(role)) {
            if (id_dansator == null) {
                throw new IllegalArgumentException("Pentru ARTIST trebuie completat id_dansator.");
            }
        } else {
            id_dansator = null; // pentru celelalte roluri, nu stocam id_dansator
        }

        utilizatorRepository.addUser(username.trim(), parola.trim(), role, id_dansator);
    }
}
