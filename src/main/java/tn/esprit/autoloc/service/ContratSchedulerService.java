package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.web.dto.ContratResponseDTO;
import tn.esprit.autoloc.web.mapper.ContratMapper;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContratSchedulerService {

    private static final List<StatutReservation> STATUTS_EXCLUS =
            List.of(StatutReservation.TERMINEE, StatutReservation.ANNULEE);

    private final IContratRepository contratRepository;
    private final ContratMapper contratMapper;

    @Scheduled(cron = "0 0 7 * * MON-FRI")
    @Transactional(readOnly = true)
    public void notifierContratsArrivantAEcheance() {
        List<ContratResponseDTO> contrats = identifierContratsArrivantAEcheance();
        log.info("{} contrat(s) arrivant à échéance sous 48 heures identifié(s).", contrats.size());
    }

    @Transactional(readOnly = true)
    public List<ContratResponseDTO> identifierContratsArrivantAEcheance() {
        LocalDate today = LocalDate.now();
        LocalDate limitDate = today.plusDays(2);

        List<Contrat> contrats = contratRepository.findContratsArrivantAEcheance(today, limitDate, STATUTS_EXCLUS);
        contrats.forEach(contrat -> log.warn(
                "Contrat #{} (réservation #{}, date de fin {}) arrive à échéance sous 48h.",
                contrat.getIdContrat(),
                contrat.getReservation().getIdReservation(),
                contrat.getReservation().getDateFin()));

        return contrats.stream().map(contratMapper::toResponse).toList();
    }
}
