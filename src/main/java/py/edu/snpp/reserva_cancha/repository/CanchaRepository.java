package py.edu.snpp.reserva_cancha.repository;



import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import py.edu.snpp.reserva_cancha.entity.Cancha;
import py.edu.snpp.reserva_cancha.entity.TipoCancha;

public interface CanchaRepository extends JpaRepository<Cancha, Long> {

    List<Cancha> findByTipo(TipoCancha tipo);
}
