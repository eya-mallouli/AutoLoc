package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeRequestDTO;
import tn.esprit.autoloc.web.dto.VehiculeResponseDTO;

@Component
public class VehiculeMapper {

    public Vehicule toEntity(VehiculeRequestDTO dto) {
        Vehicule vehicule = new Vehicule();
        updateEntity(vehicule, dto);
        return vehicule;
    }

    public void updateEntity(Vehicule vehicule, VehiculeRequestDTO dto) {
        vehicule.setImmatriculation(dto.immatriculation());
        vehicule.setMarque(dto.marque());
        vehicule.setModele(dto.modele());
        vehicule.setCategorie(dto.categorie());
        vehicule.setTarifJournalier(dto.tarifJournalier());
        vehicule.setStatut(dto.statut());
    }

    public VehiculeResponseDTO toResponse(Vehicule vehicule) {
        return new VehiculeResponseDTO(
                vehicule.getIdVehicule(),
                vehicule.getImmatriculation(),
                vehicule.getMarque(),
                vehicule.getModele(),
                vehicule.getCategorie(),
                vehicule.getTarifJournalier(),
                vehicule.getStatut());
    }
}