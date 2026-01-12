package com.proiectbd.pilotlogin.controller;

import com.proiectbd.pilotlogin.model.Utilizator;
import com.proiectbd.pilotlogin.repository.DashboardRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private DashboardRepository repo;

    private boolean canManage(HttpSession session) {
        Utilizator u = (Utilizator) session.getAttribute("user");
        if (u == null || u.getGrad() == null) return false;
        String g = u.getGrad().trim().toUpperCase();
        return g.equals("ADMIN") || g.equals("ANGAJAT");
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!canManage(session)) return "redirect:/home?err=Nu ai drepturi.";

        model.addAttribute("incasariTotal", repo.totalIncasari());
        model.addAttribute("vanduteTotal", repo.totalCountByStatus("Vandut"));
        model.addAttribute("rezervateTotal", repo.totalCountByStatus("Rezervat"));
        model.addAttribute("libereTotal", repo.totalCountByStatus("Liber"));

        model.addAttribute("top5", repo.topReprezentatiiByRevenue());
        model.addAttribute("ocupare", repo.ocuparePerReprezentatie());
        model.addAttribute("lunar", repo.incasariPeLuna());
        model.addAttribute("pesteMedie", repo.pesteMediaIncasarilor());
        model.addAttribute("sponsoriTip", repo.sponsoriPeTip());

        return "dashboard";
    }
}
