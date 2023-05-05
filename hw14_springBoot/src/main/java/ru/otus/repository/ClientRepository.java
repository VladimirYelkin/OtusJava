package ru.otus.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.crm.model.Client;

import java.util.Optional;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {

    List<Client> findAll();
}
