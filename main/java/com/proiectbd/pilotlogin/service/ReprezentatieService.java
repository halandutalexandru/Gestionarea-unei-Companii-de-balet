package com.proiectbd.pilotlogin.service;

import com.proiectbd.pilotlogin.model.Reprezentatie;
import com.proiectbd.pilotlogin.repository.ReprezentatieRepository;
import com.proiectbd.pilotlogin.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class ReprezentatieService {

    private static final Set<String> TIPURI = Set.of("SPECTACOL", "REPETITIE");
    private static final DateTimeFormatter BASIC = DateTimeFormatter.BASIC_ISO_DATE;

    @Autowired
    private ReprezentatieRepository repo;

    @Autowired
    private SalaRepository salaRepo;

    public void validate(Reprezentatie r) {
        if (r.getTip_reprezentatie() == null || r.getTip_reprezentatie().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipul este obligatoriu (Spectacol/Repetitie).");
        }
        String tip = r.getTip_reprezentatie().trim().toUpperCase();
        if (!TIPURI.contains(tip)) {
            throw new IllegalArgumentException("Tip invalid. Alege Spectacol sau Repetitie.");
        }
        r.setTip_reprezentatie(tip.equals("SPECTACOL") ? "Spectacol" : "Repetitie");

        // data: YYYYMMDD
        String ds = String.valueOf(r.getData_reprezentatie());
        if (ds.length() != 8) throw new IllegalArgumentException("Data trebuie in format YYYYMMDD (ex: 20250312).");
        try { LocalDate.parse(ds, BASIC); }
        catch (Exception e) { throw new IllegalArgumentException("Data nu este valida (YYYYMMDD)."); }

        // ore: HHMM
        if (!isValidHHMM(r.getOra_inceput()) || !isValidHHMM(r.getOra_sfarsit())) {
            throw new IllegalArgumentException("Orele trebuie in format HHMM (ex: 1800).");
        }
        if (r.getOra_inceput() >= r.getOra_sfarsit()) {
            throw new IllegalArgumentException("Ora de inceput trebuie sa fie mai mica decat ora de sfarsit.");
        }

        if (!salaRepo.existsById(r.getId_sala())) {
            throw new IllegalArgumentException("Sala selectata nu exista.");
        }
    }

    private boolean isValidHHMM(int hhmm) {
        if (hhmm < 0 || hhmm > 2359) return false;
        int hh = hhmm / 100;
        int mm = hhmm % 100;
        return hh >= 0 && hh <= 23 && mm >= 0 && mm <= 59;
    }

    public void add(Reprezentatie r) { validate(r); repo.insert(r); }
    public void edit(Reprezentatie r) { validate(r); repo.update(r); }
}
