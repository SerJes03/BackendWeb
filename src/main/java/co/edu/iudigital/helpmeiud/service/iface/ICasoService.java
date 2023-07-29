package co.edu.iudigital.helpmeiud.service.iface;

import co.edu.iudigital.helpmeiud.dto.CasoDTO;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.model.Caso;

import java.util.List;
import java.util.Optional;

public interface ICasoService {

    List<CasoDTO> consultarTodos() throws RestException;

    CasoDTO crear(CasoDTO casoDTO) throws RestException;

    Boolean visible(Boolean visible, Long id) throws RestException;

    CasoDTO consultarPorId(Long id) throws RestException;

    CasoDTO update(Long id, CasoDTO casoDTO) throws RestException;

    void deleteById(Long id) throws RestException;
}
