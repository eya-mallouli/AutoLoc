package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.web.dto.PaiementRequestDTO;
import tn.esprit.autoloc.web.dto.PaiementResponseDTO;

@Component
public class PaiementMapper {

    public Paiement toEntity(PaiementRequestDTO dto) {
        Paiement paiement = new Paiement();
        updateEntity(paiement, dto);
        return paiement;
    }

    public void updateEntity(Paiement paiement, PaiementRequestDTO dto) {
        paiement.setMontant(dto.montant());
        paiement.setDatePaiement(dto.datePaiement());
        paiement.setModePaiement(dto.modePaiement());
    }

    public PaiementResponseDTO toResponse(Paiement paiement) {
        return new PaiementResponseDTO(
                paiement.getIdPaiement(),
                paiement.getMontant(),
                paiement.getDatePaiement(),
                paiement.getModePaiement());
    }
}