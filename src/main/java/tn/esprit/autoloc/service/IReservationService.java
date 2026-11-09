package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;

import java.util.List;

public interface IReservationService {

    List<ReservationResponseDTO> findAll();

    ReservationResponseDTO findById(Long id);

    ReservationResponseDTO create(ReservationRequestDTO dto);

    ReservationResponseDTO update(Long id, ReservationRequestDTO dto);

    void delete(Long id);
}