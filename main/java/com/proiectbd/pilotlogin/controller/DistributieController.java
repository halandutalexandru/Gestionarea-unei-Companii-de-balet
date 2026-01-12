package com.proiectbd.pilotlogin.controller;

import com.proiectbd.pilotlogin.model.Utilizator;
import com.proiectbd.pilotlogin.repository.DansatorRepository;
import com.proiectbd.pilotlogin.repository.DistributieRepository;
import com.proiectbd.pilotlogin.repository.ReprezentatieRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/distributie")
public class DistributieController {

    @Autowired private DistributieRepository repo;
    @Autowired private DansatorRepository dansatorRepo;
    @Autowired private ReprezentatieRepository repRepo;

    private boolean canManage(HttpSession session) {
        Utilizator u = (Utilizator) session.getAttribute("user");
        if (u == null || u.getGrad() == null) return false;
        String g = u.getGrad().trim().toUpperCase();
        return g.equals("ADMIN") || g.equals("ANGAJAT");
    }

    @GetMapping("/{idReprezentatie}")
    public String page(@PathVariable int idReprezentatie,
                       @RequestParam(value="msg", required=false) String msg,
                       @RequestParam(value="err", required=false) String err,
                       HttpSession session,
                       Model model) {
        if (!canManage(session)) return "redirect:/reprezentatii?err=Nu ai drepturi.";

        model.addAttribute("rep", repRepo.findById(idReprezentatie));
        model.addAttribute("rows", repo.findByReprezentatie(idReprezentatie));
        model.addAttribute("dansatori", dansatorRepo.findAll());
        model.addAttribute("msg", msg);
        model.addAttribute("err", err);
        return "distributie";
    }

    @PostMapping("/{idReprezentatie}/add")
    public String add(@PathVariable int idReprezentatie,
                      @RequestParam int id_dansator,
                      @RequestParam(required=false) String rol_in_reprezentatie,
                      HttpSession session) {
        if (!canManage(session)) return "redirect:/reprezentatii?err=Nu ai drepturi.";

        String rol = (rol_in_reprezentatie == null) ? null : rol_in_reprezentatie.trim();
        if (repo.exists(idReprezentatie, id_dansator)) {
            return "redirect:/distributie/" + idReprezentatie + "?err=Dansator deja adaugat.";
        }
        repo.add(idReprezentatie, id_dansator, rol);
        return "redirect:/distributie/" + idReprezentatie + "?msg=Adaugat.";
    }

    @PostMapping("/{idReprezentatie}/update")
    public String update(@PathVariable int idReprezentatie,
                         @RequestParam int id_dansator,
                         @RequestParam(required=false) String rol_in_reprezentatie,
                         HttpSession session) {
        if (!canManage(session)) return "redirect:/reprezentatii?err=Nu ai drepturi.";
        String rol = (rol_in_reprezentatie == null) ? null : rol_in_reprezentatie.trim();
        repo.updateRole(idReprezentatie, id_dansator, rol);
        return "redirect:/distributie/" + idReprezentatie + "?msg=Modificat.";
    }

    @PostMapping("/{idReprezentatie}/remove")
    public String remove(@PathVariable int idReprezentatie,
                         @RequestParam int id_dansator,
                         HttpSession session) {
        if (!canManage(session)) return "redirect:/reprezentatii?err=Nu ai drepturi.";
        repo.remove(idReprezentatie, id_dansator);
        return "redirect:/distributie/" + idReprezentatie + "?msg=Eliminat.";
    }
}
