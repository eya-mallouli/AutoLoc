package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.web.dto.ContratRequestDTO;
import tn.esprit.autoloc.web.dto.ContratResponseDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository repository;
    private final IReservationRepository reservationRepository;
    private final ContratMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ContratResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ContratResponseDTO findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    @Override
    public ContratResponseDTO create(ContratRequestDTO dto) {
        return mapper.toResponse(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public ContratResponseDTO update(Long id, ContratRequestDTO dto) {
        Contrat contrat = getOrThrow(id);
        mapper.updateEntity(contrat, dto);
        return mapper.toResponse(repository.save(contrat));
    }

    @Override
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    @Override
    public ContratResponseDTO affecterContratAReservation(LocalDate dateSignature, LocalDate dateDebut, LocalDate dateFin) {
        Contrat contrat = repository.findByDateSignature(dateSignature)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contrat introuvable avec la date de signature " + dateSignature));
        Reservation reservation = reservationRepository.findByDateDebutAndDateFin(dateDebut, dateFin)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reservation introuvable entre " + dateDebut + " et " + dateFin));
        contrat.setReservation(reservation);
        reservation.setContrat(contrat);
        return mapper.toResponse(repository.save(contrat));
    }

    private Contrat getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable avec l'id " + id));
    }
}