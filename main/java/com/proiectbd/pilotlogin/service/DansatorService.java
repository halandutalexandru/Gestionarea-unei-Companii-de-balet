package com.proiectbd.pilotlogin.service;

import com.proiectbd.pilotlogin.model.Dansator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DansatorService {
    private static final DateTimeFormatter BASIC = DateTimeFormatter.BASIC_ISO_DATE;

    public void validate(Dansator d) {
        if (d.getNume() == null || d.getNume().trim().isEmpty())
            throw new IllegalArgumentException("Numele este obligatoriu.");
        if (d.getPrenume() == null || d.getPrenume().trim().isEmpty())
            throw new IllegalArgumentException("Prenumele este obligatoriu.");

        if (d.getCnp() == null || !d.getCnp().matches("\\d{13}"))
            throw new IllegalArgumentException("CNP invalid (trebuie 13 cifre).");

        String ds = String.valueOf(d.getData_angajarii());
        if (ds.length() != 8) throw new IllegalArgumentException("Data angajarii trebuie YYYYMMDD.");
        try { LocalDate.parse(ds, BASIC); }
        catch (Exception e) { throw new IllegalArgumentException("Data angajarii nu e valida."); }

        if (d.getRang() == null || d.getRang().trim().isEmpty())
            throw new IllegalArgumentException("Rangul este obligatoriu.");
    }
}
