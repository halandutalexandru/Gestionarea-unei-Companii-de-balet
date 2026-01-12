package com.proiectbd.pilotlogin.service;

import com.proiectbd.pilotlogin.model.Bilet;
import com.proiectbd.pilotlogin.repository.BiletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BiletService {

    private static final Set<String> STATUS = Set.of("LIBER", "REZERVAT", "VANDUT");

    @Autowired
    private BiletRepository repo;

    public void validateAndNormalize(Bilet b) {
        if (b.getId_reprezentatie() <= 0) throw new IllegalArgumentException("Reprezentatia este obligatorie.");
        if (b.getNr_loc() <= 0) throw new IllegalArgumentException("Numarul locului trebuie sa fie > 0.");
        if (b.getPret() <= 0) throw new IllegalArgumentException("Pretul trebuie sa fie > 0.");

        if (b.getStatus() == null || b.getStatus().trim().isEmpty())
            throw new IllegalArgumentException("Statusul este obligatoriu.");

        String st = b.getStatus().trim().toUpperCase();
        if (!STATUS.contains(st)) throw new IllegalArgumentException("Status invalid (Liber/Rezervat/Vandut).");
        b.setStatus(st.equals("LIBER") ? "Liber" : st.equals("REZERVAT") ? "Rezervat" : "Vandut");

        Integer salaId = repo.getSalaIdByReprezentatie(b.getId_reprezentatie());
        if (salaId == null) throw new IllegalArgumentException("Reprezentatia selectata nu exista.");

        Integer cap = repo.getCapacitateSala(salaId);
        if (cap == null) throw new IllegalArgumentException("Sala reprezentatiei nu exista.");
        if (b.getNr_loc() > cap) throw new IllegalArgumentException("Loc invalid: sala are capacitate " + cap + ".");

        // setam id_sala automat, ca sa nu fie inconsistente
        b.setId_sala(salaId);
    }

    public void add(Bilet b) { validateAndNormalize(b); repo.insert(b); }
    public void edit(Bilet b) { validateAndNormalize(b); repo.update(b); }
}
