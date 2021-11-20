package com.open.paymybuddy.controllers;

import com.open.paymybuddy.configs.SecurityConfig;
import com.open.paymybuddy.models.MoneyTransaction;
import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.security.UserPrincipalDetailsService;
import com.open.paymybuddy.services.MoneyTransactionService;
import com.open.paymybuddy.services.PersonConnectionsService;
import com.open.paymybuddy.services.PersonService;
import com.open.paymybuddy.utils.GlobalExceptionHandler;
import com.open.paymybuddy.utils.SecurityUtil;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(controllers = MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private PersonConnectionsService personConnectionsService;

    @MockBean
    private MoneyTransactionService moneyTransactionService;

    @MockBean
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @MockBean
    private GlobalExceptionHandler globalExceptionHandler;

    @MockBean
    private SecurityUtil securityUtil;
    
    @Test
    public void loginTest() throws Exception {
        mockMvc.perform(get("/login"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andDo(print());
    }

    @Test
    public void loginErrorTest() throws Exception {
        mockMvc.perform(get("/login?error=true"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"))
        .andDo(print());
    }

    @Test
    @WithMockUser(username = "james@gmail.com", password = "pass111")
    public void homePageTest() throws Exception {
        mockMvc.perform(post("/home"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/transfer"))
        .andDo(print());
    }

    @Test
    public void showErrorTest() throws Exception {
        mockMvc.perform(get("/error"))
        .andExpect(status().isOk())
        .andExpect(view().name("error"))
        .andDo(print());
    }

    @Test
    @WithMockUser(username = "james@gmail.com", password = "pass111")
    public void homeTest() throws Exception {
        mockMvc.perform(get("/home"))
        .andExpect(status().isOk())
        .andExpect(view().name("home"))
        .andDo(print());
    }

    @Test
    public void showRegistrationTest() throws Exception {
        mockMvc.perform(get("/registration"))
        .andExpect(status().isOk())
        .andExpect(view().name("registration"))
        .andDo(print());
    }

    @Test
    public void showRegistrationRedirectTest() throws Exception {
        when(personService.create(any(Person.class))).thenReturn(new Person());
        mockMvc.perform(post("/registration"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/login"))
        .andDo(print());
    }

    @Test
    public void showRegistrationExistingUser() throws Exception {
        when(personService.create(any(Person.class))).thenReturn(null);
        mockMvc.perform(post("/registration"))
        .andExpect(status().isOk())
        .andExpect(view().name("registration"))
        .andDo(print());
    }

    @Test
    @WithMockUser(username = "james@gmail.com", password = "pass111")
    public void listTransactionsAndConnectionsTest() throws Exception {
        Person sender = new Person();
        sender.setId(1);
        sender.setEmail("james@gmail.com");

        Person receiver = new Person();
        receiver.setId(3);
        receiver.setEmail("carol@gmail.com");

        MoneyTransaction mTrans = new MoneyTransaction("Description", new BigDecimal(50), sender, 
        receiver, "james@gmail.com", "carol@gmail.com", new BigDecimal(2.5), LocalDateTime.now());

        when(securityUtil.getLoggedInUser()).thenReturn(sender);
        when(moneyTransactionService.getAllForLoggedIn(any(String.class))).thenReturn(List.of(mTrans));
    
        Page<MoneyTransaction> pageList = new PageImpl<>(List.of(mTrans), PageRequest.of(1, 3), 10);
        when(moneyTransactionService.findPaginated(any(PageRequest.class), ArgumentMatchers.<MoneyTransaction>anyList())).thenReturn(pageList);
        
        mockMvc.perform(get("/transfer?size=3&page=1"))
        .andExpect(status().isOk())
        .andExpect(view().name("transfer"))
        .andDo(print());
    }

    @Test
    @WithMockUser(username = "james@gmail.com", password = "pass111")
    public void moneyTransactionSubmitTest() throws Exception {
        Person sender = new Person();
        sender.setId(1);
        sender.setEmail("james@gmail.com");

        Person receiver = new Person();
        receiver.setId(3);
        receiver.setEmail("carol@gmail.com");

        when(securityUtil.getLoggedInUser()).thenReturn(sender);

        MoneyTransaction mTrans = new MoneyTransaction("Description", new BigDecimal(50), sender, 
        receiver, "james@gmail.com", "carol@gmail.com", new BigDecimal(2.5), LocalDateTime.now());

        when(moneyTransactionService.create(any(Integer.class), any(String.class), any(BigDecimal.class), 
                                                any(String.class))).thenReturn(mTrans);
        
        mockMvc.perform(post("/transfer"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/transfer"))
        .andDo(print());
    }
}
