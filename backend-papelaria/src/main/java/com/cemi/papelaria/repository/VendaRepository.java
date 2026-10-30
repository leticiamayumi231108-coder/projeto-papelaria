package com.cemi.papelaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cemi.papelaria.domain.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long>{

}
