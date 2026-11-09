package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.web.dto.ClientRequestDTO;
import tn.esprit.autoloc.web.dto.ClientResponseDTO;
import tn.esprit.autoloc.web.mapper.ClientMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements IClientService {

    private final IClientRepository repository;
    private final ClientMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDTO> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDTO findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    @Override
    public ClientResponseDTO create(ClientRequestDTO dto) {
        return mapper.toResponse(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public ClientResponseDTO update(Long id, ClientRequestDTO dto) {
        Client client = getOrThrow(id);
        mapper.updateEntity(client, dto);
        return mapper.toResponse(repository.save(client));
    }

    @Override
    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Client getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'id " + id));
    }
}