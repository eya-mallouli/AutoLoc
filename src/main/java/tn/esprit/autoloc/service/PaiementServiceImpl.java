package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IPaiementRepository;
import tn.esprit.autoloc.web.dto.PaiementRequestDTO;
import tn.esprit.autoloc.web.dto.PaiementResponseDTO;
import tn.esprit.autoloc.web.mapper.PaiementMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaiementServiceImpl implements IPaiementService {

    private final IPaiementRepository repository;
    private final PaiementMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<PaiementResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PaiementResponseDTO findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    @Override
    public PaiementResponseDTO create(PaiementRequestDTO dto) {
        return mapper.toResponse(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public PaiementResponseDTO update(Long id, PaiementRequestDTO dto) {
        Paiement paiement = getOrThrow(id);
        mapper.updateEntity(paiement, dto);
        return mapper.toResponse(repository.save(paiement));
    }

    @Override
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Paiement getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement introuvable avec l'id " + id));
    }
}