package com.open.paymybuddy.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.models.PersonConnection;
import com.open.paymybuddy.services.MoneyTransactionService;
import com.open.paymybuddy.services.PersonConnectionsService;
import com.open.paymybuddy.services.PersonService;
import com.open.paymybuddy.utils.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    MoneyTransactionService moneyTransactionService;

    @Autowired
    PersonConnectionsService personConnectionsService;

    @Autowired
    PersonService personService;

    // TODO выкинуть эксепшн если логинится несуществующий пользователь
    // или неверный пароль, иначе возникает проблема на странице с ошибками.
    // TODO перенести Transactional в репозитории
    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @PostMapping(value = "/home")
    public String homePage() {
        // return "home";
        return "redirect:/transfer";
    }

    @GetMapping(value = "/error")
    public String showError() {
        return "error";
    }

    @GetMapping(value = "/home")
    public String showHome() {
        return "home";
    }

    @GetMapping(value = "/registration")
    public String showRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute Person person, Model model) throws Exception {
        Person prs = personService.create(person);
        // If a user with this email exists we return null.
        if (prs == null) {
            model.addAttribute("userExists", "Account exists. You can log in");
            return "registration";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping(value = "/transfer")
    public String listTransactionsAndConnections(Model model, @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) throws Exception {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);

        List<MoneyTransaction> transactions = moneyTransactionService
                .getAllForLoggedIn(SecurityUtil.getLoggedInUser().getEmail());

        Page<MoneyTransaction> transactionPage = moneyTransactionService
                .findPaginated(PageRequest.of(currentPage - 1, pageSize), transactions);

        model.addAttribute("transactionPage", transactionPage);

        int totalPages = transactionPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        List<PersonConnection> connections = personConnectionsService
                .getAllByOwnerID(SecurityUtil.getLoggedInUser().getId());
        model.addAttribute("connections", connections);

        return "transfer";
    }

    @PostMapping("/transfer")
    public String moneyTransactionSubmit(@ModelAttribute MoneyTransaction mTransaction) throws Exception {
        moneyTransactionService.create(SecurityUtil.getLoggedInUser().getId(), mTransaction.getReceiverEmail(),
                mTransaction.getAmount(), mTransaction.getDescription());
        return "redirect:/transfer";
    }
}
