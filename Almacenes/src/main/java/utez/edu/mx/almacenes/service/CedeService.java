package utez.edu.mx.almacenes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CedeRepository cedeRepository;

    public Cede crearCede(Cede cede){
        // Genera la clave solo si el ID no es nulo (después de guardar)
        Cede savedCede = cedeRepository.save(cede);
        String clave = "C" + savedCede.getId() + "-"
                + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + "-"
                + String.format("%04d", (int)(Math.random() * 10000));
        savedCede.setClave(clave);
        return cedeRepository.save(savedCede);
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
