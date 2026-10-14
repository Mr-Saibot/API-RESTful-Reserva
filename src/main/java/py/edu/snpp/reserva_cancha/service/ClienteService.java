package py.edu.snpp.reserva_cancha.service;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import py.edu.snpp.reserva_cancha.entity.Cliente;
import py.edu.snpp.reserva_cancha.repository.ClienteRepository;
import py.edu.snpp.reserva_cancha.repository.ReservaRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ReservaRepository reservaRepository;

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado: " + id));
    }

    public Cliente registrar(Cliente cliente) {
        validar(cliente);
        if (clienteRepository.existsByCedula(cliente.getCedula())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un cliente con esa cédula");
        }
        cliente.setId(null);
        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(Long id, Cliente datos) {
        validar(datos);
        Cliente existente = buscarPorId(id);
        boolean cambioCedula = !existente.getCedula().equals(datos.getCedula());
        if (cambioCedula && clienteRepository.existsByCedula(datos.getCedula())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Ya existe un cliente con esa cédula");
        }
        existente.setNombre(datos.getNombre());
        existente.setApellido(datos.getApellido());
        existente.setCedula(datos.getCedula());
        existente.setTelefono(datos.getTelefono());
        existente.setEmail(datos.getEmail());
        return clienteRepository.save(existente);
    }

    public void eliminar(Long id) {
        Cliente cliente = buscarPorId(id);
        if (reservaRepository.existsByClienteId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "El cliente tiene reservas registradas");
        }
        clienteRepository.delete(cliente);
    }

    private void validar(Cliente cliente) {
        if (esVacio(cliente.getNombre()) || esVacio(cliente.getApellido())
                || esVacio(cliente.getCedula())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nombre, apellido y cédula son obligatorios");
        }
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
