package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Contrat;

import java.time.LocalDate;
import java.util.Optional;

public interface IContratRepository extends JpaRepository<Contrat, Long> {

    Optional<Contrat> findByDateSignature(LocalDate dateSignature);
}