package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.ReservationRequestDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository repository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IContratRepository contratRepository;
    private final ReservationMapper mapper;
    private final ContratMapper contratMapper;

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

    @Override
    public ReservationResponseDTO affecterReservationAClient(Long idReservation, String nom, String prenom) {
        Reservation reservation = getOrThrow(idReservation);
        Client client = clientRepository.findByNomAndPrenom(nom, prenom)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Client introuvable avec le nom " + nom + " et le prénom " + prenom));
        reservation.setClient(client);
        if (!client.getReservations().contains(reservation)) {
            client.getReservations().add(reservation);
        }
        return mapper.toResponse(repository.save(reservation));
    }

    @Override
    public ReservationResponseDTO creerReservationEtContratAssocie(ReservationWithContratRequestDTO requestDTO) {
        Client client = clientRepository.findById(requestDTO.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'id " + requestDTO.clientId()));
        Vehicule vehicule = vehiculeRepository.findById(requestDTO.vehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable avec l'id " + requestDTO.vehiculeId()));

        Reservation reservation = mapper.toEntity(requestDTO);
        reservation.setClient(client);
        reservation.setVehicule(vehicule);
        Reservation savedReservation = repository.save(reservation);

        Contrat contrat = contratMapper.toEntity(requestDTO);
        contrat.setReservation(savedReservation);
        savedReservation.setContrat(contrat);
        contratRepository.save(contrat);

        return mapper.toResponse(savedReservation);
    }

    private Reservation getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation introuvable avec l'id " + id));
    }
}