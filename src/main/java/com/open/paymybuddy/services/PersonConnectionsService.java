package com.open.paymybuddy.services;

import java.util.List;

import com.open.paymybuddy.models.PersonConnection;

public interface PersonConnectionsService {
    List<PersonConnection> getAllByOwnerID(Integer id);
}
