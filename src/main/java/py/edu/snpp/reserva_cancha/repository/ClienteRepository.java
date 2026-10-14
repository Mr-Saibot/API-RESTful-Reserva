package py.edu.snpp.reserva_cancha.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import py.edu.snpp.reserva_cancha.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCedula(String cedula);
}