# Notes sur la couche Repository

## Choix des interfaces

| Interface | Étend | Justification |
|---|---|---|
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, findAll renvoie une List, saveAndFlush disponible. |
| IAgenceRepository | JpaRepository<Agence, Long> | CRUD complet, tri, pagination, listes. |
| IEmployeRepository | JpaRepository<Employe, Long> | Idem. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | Idem. |
| IEquipementRepository | JpaRepository<Equipement, Long> | Idem. |
| IClientRepository | JpaRepository<Client, Long> | Idem. |
| IReservationRepository | JpaRepository<Reservation, Long> | Idem. |
| IPaiementRepository | JpaRepository<Paiement, Long> | Lecture des paiements ; création et suppression via le Contrat (composition). |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | Idem. |

## Anomalies SonarQube for IDE

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| Import inutilisé `java.util.Date` dans Vehicule (ligne 7) | java:S1128 : un import non utilisé alourdit le code | Import supprimé |
| Variable locale `x` inutilisée dans Vehicule.test() (ligne 59) | java:S1481 : une variable jamais lue est du code mort | Variable et méthode supprimées |
| Affectation inutile `int x = 5` (ligne 59) | java:S1854 : une valeur affectée mais jamais utilisée est inutile | Méthode `test()` supprimée |
| Utilisation de `java.util.Date` | java:S2143 : préférer l'API `java.time` à l'ancienne `Date` | Import supprimé (le projet utilise `LocalDate`) |

Ces anomalies ont été introduites volontairement dans `Vehicule.java` pour vérifier le fonctionnement de l'outil, puis corrigées.