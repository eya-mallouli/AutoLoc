package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository repository;
    private final ReservationMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    @Override
    public ReservationResponseDTO create(ReservationRequestDTO dto) {
        return mapper.toResponse(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public ReservationResponseDTO update(Long id, ReservationRequestDTO dto) {
        Reservation reservation = getOrThrow(id);
        mapper.updateEntity(reservation, dto);
        return mapper.toResponse(repository.save(reservation));
    }

    @Override
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Reservation getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation introuvable avec l'id " + id));
    }
}