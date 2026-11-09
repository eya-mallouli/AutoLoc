package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.web.dto.EmployeRequestDTO;
import tn.esprit.autoloc.web.dto.EmployeResponseDTO;

@Component
public class EmployeMapper {

    public Employe toEntity(EmployeRequestDTO dto) {
        Employe employe = new Employe();
        updateEntity(employe, dto);
        return employe;
    }

    public void updateEntity(Employe employe, EmployeRequestDTO dto) {
        employe.setNom(dto.nom());
        employe.setPrenom(dto.prenom());
        employe.setRole(dto.role());
    }

    public EmployeResponseDTO toResponse(Employe employe) {
        return new EmployeResponseDTO(
                employe.getIdEmploye(),
                employe.getNom(),
                employe.getPrenom(),
                employe.getRole());
    }
}