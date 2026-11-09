package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.web.dto.MaintenanceRequestDTO;
import tn.esprit.autoloc.web.dto.MaintenanceResponseDTO;

@Component
public class MaintenanceMapper {

    public Maintenance toEntity(MaintenanceRequestDTO dto) {
        Maintenance maintenance = new Maintenance();
        updateEntity(maintenance, dto);
        return maintenance;
    }

    public void updateEntity(Maintenance maintenance, MaintenanceRequestDTO dto) {
        maintenance.setDateDebut(dto.dateDebut());
        maintenance.setDateFin(dto.dateFin());
        maintenance.setDescription(dto.description());
    }

    public MaintenanceResponseDTO toResponse(Maintenance maintenance) {
        return new MaintenanceResponseDTO(
                maintenance.getIdMaintenance(),
                maintenance.getDateDebut(),
                maintenance.getDateFin(),
                maintenance.getDescription());
    }
}