package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;

@Component
public class ReservationMapper {

    public Reservation toEntity(ReservationRequestDTO dto) {
        Reservation reservation = new Reservation();
        updateEntity(reservation, dto);
        return reservation;
    }

    public void updateEntity(Reservation reservation, ReservationRequestDTO dto) {
        reservation.setDateDebut(dto.dateDebut());
        reservation.setDateFin(dto.dateFin());
        reservation.setStatut(dto.statut());
    }

    public ReservationResponseDTO toResponse(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.getIdReservation(),
                reservation.getDateDebut(),
                reservation.getDateFin(),
                reservation.getStatut());
    }
}