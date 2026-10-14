package py.edu.snpp.reserva_cancha.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import py.edu.snpp.reserva_cancha.entity.Cancha;
import py.edu.snpp.reserva_cancha.entity.TipoCancha;
import py.edu.snpp.reserva_cancha.service.CanchaService;

@RestController
@RequestMapping("/api/v1/canchas")
@RequiredArgsConstructor
@Tag(name = "Canchas")
public class CanchaController {

    private final CanchaService canchaService;

    @GetMapping
    public List<Cancha> listar(@RequestParam(required = false) TipoCancha tipo) {
        if (tipo != null) {
            return canchaService.listarPorTipo(tipo);
        }
        return canchaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Cancha buscarPorId(@PathVariable Long id) {
        return canchaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cancha registrar(@RequestBody Cancha cancha) {
        return canchaService.registrar(cancha);
    }

    @PutMapping("/{id}")
    public Cancha actualizar(@PathVariable Long id, @RequestBody Cancha cancha) {
        return canchaService.actualizar(id, cancha);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        canchaService.eliminar(id);
    }
}