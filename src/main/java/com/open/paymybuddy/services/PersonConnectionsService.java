package com.open.paymybuddy.services;

import java.util.List;
import com.open.paymybuddy.models.PersonConnection;
import com.open.paymybuddy.utils.NotFoundException;

public interface PersonConnectionsService {
    List<PersonConnection> getAllByOwnerID(Integer id);
    PersonConnection create(Integer ownerID, String friendsEmail) throws Exception;
    PersonConnection deleteByConnectionID(Integer id) throws NotFoundException;
}
