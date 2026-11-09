package tn.esprit.autoloc.service;

import tn.esprit.autoloc.web.dto.EmployeRequestDTO;
import tn.esprit.autoloc.web.dto.EmployeResponseDTO;

import java.util.List;

public interface IEmployeService {

    List<EmployeResponseDTO> findAll();

    EmployeResponseDTO findById(Long id);

    EmployeResponseDTO create(EmployeRequestDTO dto);

    EmployeResponseDTO update(Long id, EmployeRequestDTO dto);

    void delete(Long id);
}