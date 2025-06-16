package utez.edu.mx.almacenes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utez.edu.mx.almacenes.model.Cede;
import utez.edu.mx.almacenes.repository.CedeRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CedeService {
    private final CedeRepository cedeRepository;

    public Cede crearCede(Cede cede){
        String clave = "C" + cede.getId() + "-" +
                LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "-" +
                String.format("%04d", (int)(Math.random() * 10000));
        cede.setClave(clave);
        return cedeRepository.save(cede);
    }

    public List<Cede> listarCedes() {
        return cedeRepository.findAll();
    }

    public Optional<Cede> buscarPorId(Long id) {
        return cedeRepository.findById(id);
    }

    public void eliminarCede(Long id) {
        cedeRepository.deletedById(id);
    }
}
