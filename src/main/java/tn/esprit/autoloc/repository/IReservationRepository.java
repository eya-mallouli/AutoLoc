package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByDateDebutAndDateFin(LocalDate dateDebut, LocalDate dateFin);

    @Query("""
            SELECT r FROM Reservation r
            JOIN FETCH r.vehicule v
            WHERE r.dateFin < :today
              AND r.statut NOT IN :statutsExclus
            """)
    List<Reservation> findReservationsExpirees(
            @Param("today") LocalDate today,
            @Param("statutsExclus") List<StatutReservation> statutsExclus);
}