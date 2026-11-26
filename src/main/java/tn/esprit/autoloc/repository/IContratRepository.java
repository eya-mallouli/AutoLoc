package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.StatutReservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IContratRepository extends JpaRepository<Contrat, Long> {

    Optional<Contrat> findByDateSignature(LocalDate dateSignature);

    @Query("""
            SELECT c FROM Contrat c
            JOIN FETCH c.reservation r
            WHERE c.valide = true
              AND r.statut NOT IN :statutsExclus
              AND r.dateFin BETWEEN :today AND :limitDate
            """)
    List<Contrat> findContratsArrivantAEcheance(
            @Param("today") LocalDate today,
            @Param("limitDate") LocalDate limitDate,
            @Param("statutsExclus") List<StatutReservation> statutsExclus);
}