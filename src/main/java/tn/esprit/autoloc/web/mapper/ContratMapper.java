package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.web.dto.ContratRequestDTO;
import tn.esprit.autoloc.web.dto.ContratResponseDTO;

@Component
public class ContratMapper {

    public Contrat toEntity(ContratRequestDTO dto) {
        Contrat contrat = new Contrat();
        updateEntity(contrat, dto);
        return contrat;
    }

    public void updateEntity(Contrat contrat, ContratRequestDTO dto) {
        contrat.setDateSignature(dto.dateSignature());
        contrat.setMontantTotal(dto.montantTotal());
        contrat.setValide(dto.valide());
    }

    public ContratResponseDTO toResponse(Contrat contrat) {
        return new ContratResponseDTO(
                contrat.getIdContrat(),
                contrat.getDateSignature(),
                contrat.getMontantTotal(),
                contrat.isValide());
    }
}