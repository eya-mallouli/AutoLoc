package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.VehiculeResponseDTO;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiculeSchedulerService {

    private static final List<StatutReservation> STATUTS_EXCLUS =
            List.of(StatutReservation.TERMINEE, StatutReservation.ANNULEE);

    private final IReservationRepository reservationRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;

    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void libererVehiculesEnFinDeLocation() {
        List<VehiculeResponseDTO> vehiculesLiberes = cloturerLocationsTerminees();
        log.info("{} véhicule(s) remis à DISPONIBLE suite à la clôture des locations terminées.", vehiculesLiberes.size());
    }

    @Transactional
    public List<VehiculeResponseDTO> cloturerLocationsTerminees() {
        LocalDate today = LocalDate.now();
        List<Reservation> reservationsExpirees =
                reservationRepository.findReservationsExpirees(today, STATUTS_EXCLUS);

        List<VehiculeResponseDTO> vehiculesMisAJour = new ArrayList<>();
        for (Reservation reservation : reservationsExpirees) {
            reservation.setStatut(StatutReservation.TERMINEE);
            reservationRepository.save(reservation);

            Vehicule vehicule = reservation.getVehicule();
            if (vehicule.getStatut() == StatutVehicule.LOUE) {
                vehicule.setStatut(StatutVehicule.DISPONIBLE);
                vehiculesMisAJour.add(vehiculeMapper.toResponse(vehiculeRepository.save(vehicule)));
            }

            log.info("Réservation #{} clôturée (TERMINEE), véhicule #{} statut={}.",
                    reservation.getIdReservation(), vehicule.getIdVehicule(), vehicule.getStatut());
        }

        return vehiculesMisAJour;
    }
}
