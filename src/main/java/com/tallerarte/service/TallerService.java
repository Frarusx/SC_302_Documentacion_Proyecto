package com.tallerarte.service;

import com.tallerarte.domain.Taller;
import com.tallerarte.repository.TallerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TallerService {

    private final TallerRepository tallerRepository;

    // Catalogo para el cliente. Si idCategoria es null devuelve todos los activos.
    @Transactional(readOnly = true)
    public List<Taller> getCatalogo(Integer idCategoria) {
        if (idCategoria == null) {
            return tallerRepository.findByActivoTrueOrderByFechaHoraAsc();
        }
        return tallerRepository.findByActivoTrueAndCategoriaIdCategoriaOrderByFechaHoraAsc(idCategoria);
    }

    @Transactional(readOnly = true)
    public List<Taller> getTalleresDeInstructor(Integer idInstructor) {
        return tallerRepository.findByInstructorIdUsuarioOrderByFechaHoraAsc(idInstructor);
    }

    @Transactional(readOnly = true)
    public Taller getTaller(Integer id) {
        return tallerRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Taller taller) {
        tallerRepository.save(taller);
    }

    @Transactional
    public void delete(Integer id) {
        tallerRepository.deleteById(id);
    }
}
