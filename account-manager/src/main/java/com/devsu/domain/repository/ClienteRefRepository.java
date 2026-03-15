package com.devsu.domain.repository;

import com.devsu.domain.entity.ClienteRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRefRepository extends JpaRepository<ClienteRef, Long>{

}
