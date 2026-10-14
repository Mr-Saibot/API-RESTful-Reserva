package py.edu.snpp.reserva_cancha.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import py.edu.snpp.reserva_cancha.entity.Cancha;
import py.edu.snpp.reserva_cancha.entity.Cliente;
import py.edu.snpp.reserva_cancha.entity.EstadoReserva;
import py.edu.snpp.reserva_cancha.entity.Reserva;
import py.edu.snpp.reserva_cancha.repository.ReservaRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteService clienteService;
    private final CanchaService canchaService;

    @Transactional(readOnly = true)
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reserva no encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarPorCliente(Long clienteId) {
        clienteService.buscarPorId(clienteId);
        return reservaRepository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarPorCanchaYFecha(Long canchaId, LocalDate fecha) {
        canchaService.buscarPorId(canchaId);
        return reservaRepository.findByCanchaIdAndFecha(canchaId, fecha);
    }

    public Reserva registrar(Reserva reserva) {
        if (reserva.getCliente() == null || reserva.getCliente().getId() == null
                || reserva.getCancha() == null || reserva.getCancha().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Debe indicar el cliente y la cancha");
        }
        if (reserva.getFecha() == null || reserva.getHoraInicio() == null
                || reserva.getHoraFin() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Debe indicar fecha, hora de inicio y hora de fin");
        }
        if (!reserva.getHoraFin().isAfter(reserva.getHoraInicio())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La hora de fin debe ser posterior a la de inicio");
        }

        Cliente cliente = clienteService.buscarPorId(reserva.getCliente().getId());
        Cancha cancha = canchaService.buscarPorId(reserva.getCancha().getId());

        if (!cancha.isActiva()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "La cancha no se encuentra habilitada");
        }

        long solapadas = reservaRepository.countOverlapping(cancha.getId(), reserva.getFecha(),
                EstadoReserva.CANCELADA, reserva.getHoraInicio(), reserva.getHoraFin());
        if (solapadas > 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "La cancha ya está reservada en ese horario");
        }

        long minutos = Duration.between(reserva.getHoraInicio(), reserva.getHoraFin()).toMinutes();
        BigDecimal total = cancha.getPrecioHora()
                .multiply(BigDecimal.valueOf(minutos))
                .divide(BigDecimal.valueOf(60), 0, RoundingMode.HALF_UP);

        reserva.setId(null);
        reserva.setCliente(cliente);
        reserva.setCancha(cancha);
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setTotal(total);
        return reservaRepository.save(reserva);
    }

    public Reserva confirmar(Long id) {
        Reserva reserva = buscarPorId(id);
        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Solo se pueden confirmar reservas pendientes");
        }
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        return reservaRepository.save(reserva);
    }

    public Reserva cancelar(Long id) {
        Reserva reserva = buscarPorId(id);
        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "La reserva ya se encuentra cancelada");
        }
        reserva.setEstado(EstadoReserva.CANCELADA);
        return reservaRepository.save(reserva);
    }
}