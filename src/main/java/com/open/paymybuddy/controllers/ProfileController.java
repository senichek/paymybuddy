package com.open.paymybuddy.controllers;

import java.math.BigDecimal;
import java.util.List;

import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.models.PersonConnection;
import com.open.paymybuddy.services.PersonConnectionsService;
import com.open.paymybuddy.services.PersonService;
import com.open.paymybuddy.utils.NotFoundException;
import com.open.paymybuddy.utils.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    @Autowired
    PersonService personService;
    
    @GetMapping(value = "/profile")
    public String showProfile(Model model) throws NotFoundException {
        Person currentUser = personService.findById(SecurityUtil.getLoggedInUser().getId());
        model.addAttribute("currentUser", currentUser);
        return "profile";
    }

    @PostMapping(value = "/profile")
    public String updateUser(@ModelAttribute Person person, Model model) throws Exception {
        model.addAttribute("currentUser", personService.update(person));
        return "profile";
    }

    @PostMapping(value = "/profile/balanceincr")
    public String increaseBalance(@ModelAttribute Person person, Model model) throws Exception {
        model.addAttribute("currentUser", personService.increaseBalance(person));
        return "redirect:/profile";
    }

    @PostMapping(value = "/profile/balancedecr")
    public String decreaseBalance(@ModelAttribute Person person, Model model) throws Exception {
        model.addAttribute("currentUser", personService.decreaseBalance(person));
        return "redirect:/profile";
    }
}
