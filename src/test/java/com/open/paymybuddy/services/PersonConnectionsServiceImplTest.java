package com.open.paymybuddy.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import com.open.paymybuddy.models.Person;
import com.open.paymybuddy.models.PersonConnection;
import com.open.paymybuddy.utils.NotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) // Tests change the collection, this is why we have to reset the collection
public class PersonConnectionsServiceImplTest {

    @Autowired
    PersonConnectionsService personConnectionsService;

    @Test
    public void getAllByOwnerIDTest() {
        PersonConnection conn1 = new PersonConnection("mike@gmail.com", new Person(), new Person());
        PersonConnection conn2 = new PersonConnection("carol@gmail.com", new Person(), new Person());
        List<PersonConnection> expected = List.of(conn1, conn2);
        List<PersonConnection> fromService = personConnectionsService.getAllByOwnerID(1);
        // Lazy fetching, this is why we do not compare Person (owner and friend) inside of a Connection,
        // we compare only emails.
        for (int i =0; i < expected.size(); i++) {
            assertEquals(expected.get(i).getEmail(), fromService.get(i).getEmail());
        }
    }

    @Test
    @WithUserDetails("james@gmail.com") // see https://docs.spring.io/spring-security/site/docs/4.2.x/reference/html/test-method.html#test-method-withuserdetails
    public void createTest() throws Exception {
        PersonConnection expected = new PersonConnection("mike@gmail.com", new Person(), new Person());
        PersonConnection fromService = personConnectionsService.create(1, "mike@gmail.com");
        assertEquals(expected.getEmail(), fromService.getEmail());
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void createWithExceptionTest() {
        Exception exception = assertThrows(Exception.class, () -> personConnectionsService.create(2, "mike@gmail.com"));
        assertTrue(exception.getMessage().contains("Data Integrity Exception."));

        exception = assertThrows(Exception.class, () -> personConnectionsService.create(1, "nonExistant@gmail.com"));
        assertTrue(exception.getMessage().contains("Entity with email nonExistant@gmail.com does not exist."));
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void deleteTest() throws NotFoundException {
        PersonConnection deleted = personConnectionsService.deleteByConnectionID(2);
        assertEquals(deleted.getEmail(), "carol@gmail.com");
        // After the deleteion there is going to be only one 
        // entry in the connection list of James
        assertEquals(1, personConnectionsService.getAllByOwnerID(1).size());
        
    }

    @Test
    @WithUserDetails("james@gmail.com")
    public void deleteWithExceptionTest() throws NotFoundException {
        Exception exception = assertThrows(NotFoundException.class, () -> personConnectionsService.deleteByConnectionID(4));
        assertTrue(exception.getMessage().contains("Entity with id 4 does not exist."));
    }
}
