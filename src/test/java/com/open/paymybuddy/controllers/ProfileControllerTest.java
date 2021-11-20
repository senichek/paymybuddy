package com.open.paymybuddy.controllers;

import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.security.UserPrincipalDetailsService;
import com.open.paymybuddy.services.PersonService;
import com.open.paymybuddy.utils.SecurityUtil;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private UserPrincipalDetailsService userPrincipalDetailsService;

    @MockBean
    private SecurityUtil securityUtil;

    private static Person prs;

    @BeforeAll
    private static void setup() {
        prs = new Person();
        prs.setId(1);
        prs.setEmail("james@gmail.com");
    
    }

    @Test
    @WithMockUser(username = "james@gmail.com", password = "pass111")
    public void showProfileTest() throws Exception {
        when(securityUtil.getLoggedInUser()).thenReturn(prs);
        when(personService.findById(any(Integer.class))).thenReturn(prs);
        
        mockMvc.perform(get("/profile"))
        .andExpect(status().isOk())
        .andExpect(view().name("profile"))
        .andDo(print());
    }

    @Test
    @WithMockUser(username = "james@gmail.com", password = "pass111")
    public void updateUserTest() throws Exception {
        when(securityUtil.getLoggedInUser()).thenReturn(prs);
        when(personService.findById(any(Integer.class))).thenReturn(prs);
        when(personService.update(any(Person.class))).thenReturn(prs);
        
        mockMvc.perform(post("/profile"))
        .andExpect(status().isOk())
        .andExpect(view().name("profile"))
        .andDo(print());
    }

    @Test
    @WithMockUser(username = "james@gmail.com", password = "pass111")
    public void increaseBalanceTest() throws Exception {
        when(securityUtil.getLoggedInUser()).thenReturn(prs);
        when(personService.increaseBalance(any(Person.class))).thenReturn(prs);

        mockMvc.perform(post("/profile/balanceincr"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/profile"))
        .andDo(print());
    }

    @Test
    @WithMockUser(username = "james@gmail.com", password = "pass111")
    public void decreaseBalanceTest() throws Exception {
        when(securityUtil.getLoggedInUser()).thenReturn(prs);
        when(personService.decreaseBalance(any(Person.class))).thenReturn(prs);

        mockMvc.perform(post("/profile/balancedecr"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/profile"))
        .andDo(print());
    }
}
