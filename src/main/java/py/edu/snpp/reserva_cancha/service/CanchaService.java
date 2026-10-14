package py.edu.snpp.reserva_cancha.service;



import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import py.edu.snpp.reserva_cancha.entity.Cancha;
import py.edu.snpp.reserva_cancha.entity.TipoCancha;
import py.edu.snpp.reserva_cancha.repository.CanchaRepository;
import py.edu.snpp.reserva_cancha.repository.ReservaRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CanchaService {

    private final CanchaRepository canchaRepository;
    private final ReservaRepository reservaRepository;

    @Transactional(readOnly = true)
    public List<Cancha> listarTodas() {
        return canchaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Cancha> listarPorTipo(TipoCancha tipo) {
        return canchaRepository.findByTipo(tipo);
    }

    @Transactional(readOnly = true)
    public Cancha buscarPorId(Long id) {
        return canchaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cancha no encontrada: " + id));
    }

    public Cancha registrar(Cancha cancha) {
        validar(cancha);
        cancha.setId(null);
        return canchaRepository.save(cancha);
    }

    public Cancha actualizar(Long id, Cancha datos) {
        validar(datos);
        Cancha existente = buscarPorId(id);
        existente.setNombre(datos.getNombre());
        existente.setTipo(datos.getTipo());
        existente.setPrecioHora(datos.getPrecioHora());
        existente.setActiva(datos.isActiva());
        return canchaRepository.save(existente);
    }

    public void eliminar(Long id) {
        Cancha cancha = buscarPorId(id);
        if (reservaRepository.existsByCanchaId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "La cancha tiene reservas registradas");
        }
        canchaRepository.delete(cancha);
    }

    private void validar(Cancha cancha) {
        if (cancha.getNombre() == null || cancha.getNombre().isBlank()
                || cancha.getTipo() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nombre y tipo son obligatorios");
        }
        if (cancha.getPrecioHora() == null
                || cancha.getPrecioHora().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El precio por hora debe ser mayor a cero");
        }
    }
}