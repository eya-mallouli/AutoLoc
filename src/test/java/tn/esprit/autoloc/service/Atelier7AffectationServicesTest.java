package tn.esprit.autoloc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;
import tn.esprit.autoloc.repository.IMaintenanceRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.web.dto.ContratResponseDTO;
import tn.esprit.autoloc.web.dto.ReservationResponseDTO;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDTO;
import tn.esprit.autoloc.web.dto.VehiculeResponseDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;
import tn.esprit.autoloc.web.mapper.ReservationMapper;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Atelier7AffectationServicesTest {

    @Mock
    private IReservationRepository reservationRepository;
    @Mock
    private IClientRepository clientRepository;
    @Mock
    private IVehiculeRepository vehiculeRepository;
    @Mock
    private IContratRepository contratRepository;
    @Mock
    private IEquipementRepository equipementRepository;
    @Mock
    private IMaintenanceRepository maintenanceRepository;

    private ReservationServiceImpl reservationService;
    private VehiculeServiceImpl vehiculeService;
    private ContratServiceImpl contratService;

    @BeforeEach
    void setUp() {
        ReservationMapper reservationMapper = new ReservationMapper();
        ContratMapper contratMapper = new ContratMapper();
        VehiculeMapper vehiculeMapper = new VehiculeMapper();

        reservationService = new ReservationServiceImpl(
                reservationRepository,
                clientRepository,
                vehiculeRepository,
                contratRepository,
                reservationMapper,
                contratMapper
        );
        vehiculeService = new VehiculeServiceImpl(
                vehiculeRepository,
                equipementRepository,
                maintenanceRepository,
                vehiculeMapper
        );
        contratService = new ContratServiceImpl(
                contratRepository,
                reservationRepository,
                contratMapper
        );
    }

    @Test
    void affecterReservationAClient_associeClientEtRetourneDto() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation(10L);
        reservation.setDateDebut(LocalDate.of(2026, 6, 1));
        reservation.setDateFin(LocalDate.of(2026, 6, 5));
        reservation.setStatut(StatutReservation.EN_ATTENTE);

        Client client = new Client();
        client.setIdClient(3L);
        client.setNom("Ben Ali");
        client.setPrenom("Sami");
        client.setReservations(new ArrayList<>());

        when(reservationRepository.findById(10L)).thenReturn(Optional.of(reservation));
        when(clientRepository.findByNomAndPrenom("Ben Ali", "Sami")).thenReturn(Optional.of(client));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        ReservationResponseDTO result = reservationService.affecterReservationAClient(10L, "Ben Ali", "Sami");

        assertThat(result.idReservation()).isEqualTo(10L);
        assertThat(reservation.getClient()).isSameAs(client);
        assertThat(client.getReservations()).containsExactly(reservation);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void affecterReservationAClient_clientIntrouvable_lanceResourceNotFound() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation(10L);
        when(reservationRepository.findById(10L)).thenReturn(Optional.of(reservation));
        when(clientRepository.findByNomAndPrenom("Inconnu", "Test")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.affecterReservationAClient(10L, "Inconnu", "Test"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void affecterVehiculeAEquipement_associeSansDoublonEtRetourneDto() {
        Vehicule vehicule = creerVehicule(1L, "123-TN-456");
        Equipement equipement = new Equipement();
        equipement.setIdEquipement(2L);
        equipement.setNom("GPS");
        equipement.setVehicules(new ArrayList<>());

        when(vehiculeRepository.findById(1L)).thenReturn(Optional.of(vehicule));
        when(equipementRepository.findById(2L)).thenReturn(Optional.of(equipement));
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);

        VehiculeResponseDTO result = vehiculeService.affecterVehiculeAEquipement(1L, 2L);

        assertThat(result.idVehicule()).isEqualTo(1L);
        assertThat(vehicule.getEquipements()).containsExactly(equipement);
        assertThat(equipement.getVehicules()).containsExactly(vehicule);
    }

    @Test
    void affecterMaintenanceAvehicule_associeMaintenanceEtRetourneDto() {
        Vehicule vehicule = creerVehicule(4L, "999-TN-111");
        Maintenance maintenance = new Maintenance();
        maintenance.setIdMaintenance(7L);
        maintenance.setDescription("Vidange complète");

        when(maintenanceRepository.findByDescription("Vidange complète")).thenReturn(Optional.of(maintenance));
        when(vehiculeRepository.findByImmatriculation("999-TN-111")).thenReturn(Optional.of(vehicule));
        when(maintenanceRepository.save(maintenance)).thenReturn(maintenance);
        when(vehiculeRepository.save(vehicule)).thenReturn(vehicule);

        VehiculeResponseDTO result = vehiculeService.affecterMaintenanceAvehicule("Vidange complète", "999-TN-111");

        assertThat(result.immatriculation()).isEqualTo("999-TN-111");
        assertThat(maintenance.getVehicule()).isSameAs(vehicule);
        assertThat(vehicule.getMaintenances()).containsExactly(maintenance);
    }

    @Test
    void affecterContratAReservation_associeContratEtRetourneDto() {
        LocalDate dateSignature = LocalDate.of(2026, 7, 1);
        LocalDate dateDebut = LocalDate.of(2026, 7, 2);
        LocalDate dateFin = LocalDate.of(2026, 7, 6);

        Contrat contrat = new Contrat();
        contrat.setIdContrat(8L);
        contrat.setDateSignature(dateSignature);
        contrat.setMontantTotal(new BigDecimal("450.00"));
        contrat.setValide(true);

        Reservation reservation = new Reservation();
        reservation.setIdReservation(15L);
        reservation.setDateDebut(dateDebut);
        reservation.setDateFin(dateFin);

        when(contratRepository.findByDateSignature(dateSignature)).thenReturn(Optional.of(contrat));
        when(reservationRepository.findByDateDebutAndDateFin(dateDebut, dateFin)).thenReturn(Optional.of(reservation));
        when(contratRepository.save(contrat)).thenReturn(contrat);

        ContratResponseDTO result = contratService.affecterContratAReservation(dateSignature, dateDebut, dateFin);

        assertThat(result.idContrat()).isEqualTo(8L);
        assertThat(contrat.getReservation()).isSameAs(reservation);
        assertThat(reservation.getContrat()).isSameAs(contrat);
    }

    @Test
    void creerReservationEtContratAssocie_creeLesDeuxEntitesEtRetourneDto() {
        Client client = new Client();
        client.setIdClient(1L);
        Vehicule vehicule = creerVehicule(2L, "555-TN-777");

        ReservationWithContratRequestDTO requestDTO = new ReservationWithContratRequestDTO(
                LocalDate.of(2026, 8, 10),
                LocalDate.of(2026, 8, 15),
                StatutReservation.CONFIRMEE,
                LocalDate.of(2026, 8, 9),
                new BigDecimal("600.00"),
                true,
                1L,
                2L
        );

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(vehiculeRepository.findById(2L)).thenReturn(Optional.of(vehicule));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(inv -> {
            Reservation r = inv.getArgument(0);
            r.setIdReservation(20L);
            return r;
        });
        when(contratRepository.save(any(Contrat.class))).thenAnswer(inv -> {
            Contrat c = inv.getArgument(0);
            c.setIdContrat(30L);
            return c;
        });

        ReservationResponseDTO result = reservationService.creerReservationEtContratAssocie(requestDTO);

        assertThat(result.idReservation()).isEqualTo(20L);
        assertThat(result.statut()).isEqualTo(StatutReservation.CONFIRMEE);
        verify(reservationRepository).save(any(Reservation.class));
        verify(contratRepository).save(any(Contrat.class));
    }

    private Vehicule creerVehicule(Long id, String immatriculation) {
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(id);
        vehicule.setImmatriculation(immatriculation);
        vehicule.setMarque("Peugeot");
        vehicule.setModele("208");
        vehicule.setCategorie(CategorieVehicule.values()[0]);
        vehicule.setTarifJournalier(new BigDecimal("120.00"));
        vehicule.setStatut(StatutVehicule.DISPONIBLE);
        vehicule.setEquipements(new ArrayList<>());
        vehicule.setMaintenances(new ArrayList<>());
        return vehicule;
    }
}
