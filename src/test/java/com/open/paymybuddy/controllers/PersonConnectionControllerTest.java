package com.open.paymybuddy.controllers;

import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.models.PersonConnection;
import com.open.paymybuddy.security.UserPrincipalDetailsService;
import com.open.paymybuddy.services.PersonConnectionsService;
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


@WebMvcTest(controllers = PersonConnectionController.class)
public class PersonConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonConnectionsService personConnectionsService;

    @MockBean
    private SecurityUtil securityUtil;

    @MockBean
    private UserPrincipalDetailsService userPrincipalDetailsService;

    private static Person prs;
    private static PersonConnection pConnection;

    @BeforeAll
    private static void setup() {
        prs = new Person();
        prs.setId(1);
        prs.setEmail("james@gmail.com");

        pConnection = new PersonConnection("carol@gmail.com", null, null);
    }

    @Test
    @WithMockUser(username = "james@gmail.com", password = "pass111")
    public void showConnectionsTest() throws Exception {
        when(securityUtil.getLoggedInUser()).thenReturn(prs);

        mockMvc.perform(get("/connections"))
        .andExpect(status().isOk())
        .andExpect(view().name("connections"))
        .andDo(print());
    }

    @Test
    @WithMockUser(username = "james@gmail.com", password = "pass111")
    public void deleteConnectionTest() throws Exception {
        when(securityUtil.getLoggedInUser()).thenReturn(prs);
        when(personConnectionsService.deleteByConnectionID(any(Integer.class)))
        .thenReturn(pConnection);

        mockMvc.perform(get("/connections/1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/connections"))
        .andDo(print());
    }

    @Test
    @WithMockUser(username = "james@gmail.com", password = "pass111")
    public void connectionSubmitTest() throws Exception {
        when(securityUtil.getLoggedInUser()).thenReturn(prs);
        when(personConnectionsService.create(any(Integer.class), any(String.class))).thenReturn(pConnection);

        mockMvc.perform(post("/connection"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/transfer"))
        .andDo(print());
    }
}
