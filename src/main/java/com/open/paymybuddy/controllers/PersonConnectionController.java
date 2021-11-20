package com.open.paymybuddy.controllers;

import java.util.List;
import com.open.paymybuddy.models.PersonConnection;
import com.open.paymybuddy.services.PersonConnectionsService;
import com.open.paymybuddy.utils.NotFoundException;
import com.open.paymybuddy.utils.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PersonConnectionController {

    @Autowired
    private PersonConnectionsService personConnectionsService;

    @Autowired
    private SecurityUtil securityUtil;

    @GetMapping(value = "/connections")
    public String showConnections(Model model) {
        List<PersonConnection> connections = personConnectionsService.getAllByOwnerID(securityUtil.getLoggedInUser().getId());
        model.addAttribute("connections", connections);
        return "connections";
    }

    @GetMapping(value = "/connections/{id}")
    public String deleteConnection(@PathVariable Integer id, Model model) throws NotFoundException {
        personConnectionsService.deleteByConnectionID(id);
        List<PersonConnection> connections = personConnectionsService.getAllByOwnerID(securityUtil.getLoggedInUser().getId());
        model.addAttribute("connections", connections);
        return "redirect:/connections";
    } 

    @PostMapping("/connection")
    public String connectionSubmit(@ModelAttribute PersonConnection pConnection) throws Exception {
        personConnectionsService.create(securityUtil.getLoggedInUser().getId(), pConnection.getEmail());
        return "redirect:/transfer";
    }
}
