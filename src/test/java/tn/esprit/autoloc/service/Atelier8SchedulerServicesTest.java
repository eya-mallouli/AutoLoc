package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import tn.esprit.autoloc.AutoLocApplication;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.ContratResponseDTO;
import tn.esprit.autoloc.web.dto.VehiculeResponseDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Atelier8SchedulerServicesTest {

    @Mock
    private IContratRepository contratRepository;
    @Mock
    private IReservationRepository reservationRepository;
    @Mock
    private IVehiculeRepository vehiculeRepository;

    private ContratSchedulerService contratSchedulerService;
    private VehiculeSchedulerService vehiculeSchedulerService;

    @BeforeEach
    void setUp() {
        contratSchedulerService = new ContratSchedulerService(contratRepository, new ContratMapper());
        vehiculeSchedulerService = new VehiculeSchedulerService(
                reservationRepository,
                vehiculeRepository,
                new VehiculeMapper()
        );
    }

    @Test
    void autoLocApplication_estAnnoteeEnableScheduling() {
        assertThat(AutoLocApplication.class.isAnnotationPresent(EnableScheduling.class)).isTrue();
    }

    @Test
    void contratSchedulerService_identifieContratsExpirantSous48Heures_etCronValide() throws NoSuchMethodException {
        Method scheduledMethod = ContratSchedulerService.class.getMethod("notifierContratsArrivantAEcheance");
        Scheduled scheduled = scheduledMethod.getAnnotation(Scheduled.class);
        assertThat(scheduled).isNotNull();
        assertThat(scheduled.cron()).isEqualTo("0 0 7 * * MON-FRI");

        LocalDate today = LocalDate.now();
        Reservation reservation = new Reservation();
        reservation.setIdReservation(10L);
        reservation.setDateFin(today.plusDays(1));
        reservation.setStatut(StatutReservation.CONFIRMEE);

        Contrat contrat = new Contrat();
        contrat.setIdContrat(5L);
        contrat.setDateSignature(today.minusDays(3));
        contrat.setMontantTotal(new BigDecimal("350.00"));
        contrat.setValide(true);
        contrat.setReservation(reservation);

        when(contratRepository.findContratsArrivantAEcheance(
                eq(today),
                eq(today.plusDays(2)),
                eq(List.of(StatutReservation.TERMINEE, StatutReservation.ANNULEE))))
                .thenReturn(List.of(contrat));

        List<ContratResponseDTO> result = contratSchedulerService.identifierContratsArrivantAEcheance();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).idContrat()).isEqualTo(5L);
        assertThat(result.get(0).valide()).isTrue();
    }

    @Test
    void vehiculeSchedulerService_clotureLocationEtLibereVehiculeLoue_etCronValide() throws NoSuchMethodException {
        Method scheduledMethod = VehiculeSchedulerService.class.getMethod("libererVehiculesEnFinDeLocation");
        Scheduled scheduled = scheduledMethod.getAnnotation(Scheduled.class);
        assertThat(scheduled).isNotNull();
        assertThat(scheduled.cron()).isEqualTo("0 0 1 * * *");

        LocalDate today = LocalDate.now();
        Vehicule vehiculeLoue = creerVehicule(1L, "111-TN-222", StatutVehicule.LOUE);
        Reservation reservationExpiree = new Reservation();
        reservationExpiree.setIdReservation(20L);
        reservationExpiree.setDateDebut(today.minusDays(4));
        reservationExpiree.setDateFin(today.minusDays(1));
        reservationExpiree.setStatut(StatutReservation.CONFIRMEE);
        reservationExpiree.setVehicule(vehiculeLoue);

        when(reservationRepository.findReservationsExpirees(
                eq(today),
                eq(List.of(StatutReservation.TERMINEE, StatutReservation.ANNULEE))))
                .thenReturn(List.of(reservationExpiree));
        when(reservationRepository.save(reservationExpiree)).thenReturn(reservationExpiree);
        when(vehiculeRepository.save(vehiculeLoue)).thenReturn(vehiculeLoue);

        List<VehiculeResponseDTO> result = vehiculeSchedulerService.cloturerLocationsTerminees();

        assertThat(reservationExpiree.getStatut()).isEqualTo(StatutReservation.TERMINEE);
        assertThat(vehiculeLoue.getStatut()).isEqualTo(StatutVehicule.DISPONIBLE);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).statut()).isEqualTo(StatutVehicule.DISPONIBLE);
    }

    @Test
    void vehiculeSchedulerService_neModifiePasVehiculeEnMaintenance() {
        LocalDate today = LocalDate.now();
        Vehicule vehiculeMaintenance = creerVehicule(2L, "333-TN-444", StatutVehicule.MAINTENANCE);
        Reservation reservation = new Reservation();
        reservation.setIdReservation(21L);
        reservation.setDateDebut(today.minusDays(3));
        reservation.setDateFin(today.minusDays(1));
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservation.setVehicule(vehiculeMaintenance);

        when(reservationRepository.findReservationsExpirees(
                eq(today),
                eq(List.of(StatutReservation.TERMINEE, StatutReservation.ANNULEE))))
                .thenReturn(List.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        List<VehiculeResponseDTO> result = vehiculeSchedulerService.cloturerLocationsTerminees();

        assertThat(reservation.getStatut()).isEqualTo(StatutReservation.TERMINEE);
        assertThat(vehiculeMaintenance.getStatut()).isEqualTo(StatutVehicule.MAINTENANCE);
        assertThat(result).isEmpty();
        verify(vehiculeRepository, never()).save(any());
    }

    private Vehicule creerVehicule(Long id, String immatriculation, StatutVehicule statut) {
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(id);
        vehicule.setImmatriculation(immatriculation);
        vehicule.setMarque("Renault");
        vehicule.setModele("Clio");
        vehicule.setCategorie(CategorieVehicule.values()[0]);
        vehicule.setTarifJournalier(new BigDecimal("100.00"));
        vehicule.setStatut(statut);
        return vehicule;
    }
}
