package py.edu.snpp.reserva_cancha.repository;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import py.edu.snpp.reserva_cancha.entity.EstadoReserva;
import py.edu.snpp.reserva_cancha.entity.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByClienteId(Long clienteId);

    List<Reserva> findByCanchaIdAndFecha(Long canchaId, LocalDate fecha);

    boolean existsByClienteId(Long clienteId);

    boolean existsByCanchaId(Long canchaId);

    @Query("SELECT COUNT(r) FROM Reserva r "
            + "WHERE r.cancha.id = :canchaId AND r.fecha = :fecha "
            + "AND r.estado <> :estado "
            + "AND r.horaInicio < :horaFin AND r.horaFin > :horaInicio")
    long countOverlapping(@Param("canchaId") Long canchaId,
                          @Param("fecha") LocalDate fecha,
                          @Param("estado") EstadoReserva estado,
                          @Param("horaInicio") LocalTime horaInicio,
                          @Param("horaFin") LocalTime horaFin);
}
