package co.edu.iudigital.helpmeiud.service.impl;

import co.edu.iudigital.helpmeiud.dto.CasoDTO;
import co.edu.iudigital.helpmeiud.exceptions.BadRequestException;
import co.edu.iudigital.helpmeiud.exceptions.ErrorDto;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.model.Caso;
import co.edu.iudigital.helpmeiud.model.Delito;
import co.edu.iudigital.helpmeiud.model.Usuario;
import co.edu.iudigital.helpmeiud.repository.ICasoRepository;
import co.edu.iudigital.helpmeiud.repository.IDelitoRepository;
import co.edu.iudigital.helpmeiud.repository.IUsuarioRepository;
import co.edu.iudigital.helpmeiud.service.iface.ICasoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CasoServiceImpl
        implements ICasoService {

    @Autowired
    private ICasoRepository casoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IDelitoRepository delitoRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CasoDTO> consultarTodos() throws RestException{
        log.info("consultando todos los casos{}");
        List<Caso> casos = casoRepository.findAll();
        if(casos.isEmpty()){
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message("Casos No encontrados")
                            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        // imperativa
        /*List<CasoDTO> casosDTO = new ArrayList<>();
        for(Caso caso: casos) {
            CasoDTO casoDTO =
                    CasoDTO.builder()
                            .id(caso.getId())
                            .descripcion(caso.getDescripcion())
                            .altitud(caso.getAltitud())
                            .latitud(caso.getLatitud())
                            .longitud(caso.getLongitud())
                            .esVisible(caso.getEsVisible())
                            .fechaHora(caso.getFechaHora())
                            .rmiUrl(caso.getRmiUrl())
                            .urlMap(caso.getUrlMap())
                            .usuarioId(caso.getUsuario().getId())
                            .delitoId(caso.getDelito().getId())
                            .build();
            casosDTO.add(casoDTO);
        }*/
        // programacion funcional: Lambdas Java
        return casos.stream().map(caso ->
            CasoDTO.builder()
                    .id(caso.getId())
                    .descripcion(caso.getDescripcion())
                    .altitud(caso.getAltitud())
                    .latitud(caso.getLatitud())
                    .longitud(caso.getLongitud())
                    .esVisible(caso.getEsVisible())
                    .fechaHora(caso.getFechaHora())
                    .rmiUrl(caso.getRmiUrl())
                    .urlMap(caso.getUrlMap())
                    .usuarioId(caso.getUsuario().getId())
                    .delitoId(caso.getDelito().getId())
                    .build()
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CasoDTO consultarPorId(Long id) throws RestException{
        Optional<Caso> casoOptional = casoRepository.findById(id);
        if(casoOptional.isPresent()){
            Caso caso = casoOptional.get();
            return CasoDTO.builder()
                    .id(caso.getId())
                    .descripcion(caso.getDescripcion())
                    .altitud(caso.getAltitud())
                    .latitud(caso.getLatitud())
                    .longitud(caso.getLongitud())
                    .esVisible(caso.getEsVisible())
                    .fechaHora(caso.getFechaHora())
                    .rmiUrl(caso.getRmiUrl())
                    .urlMap(caso.getUrlMap())
                    .usuarioId(caso.getUsuario().getId())
                    .delitoId(caso.getDelito().getId())
                    .build();
        }
        log.warn("No existe usuario {}", id);
        throw new BadRequestException(
                ErrorDto.builder()
                        .status(HttpStatus.NOT_FOUND.value())
                        .message("Delito No Encontrado")
                        .error(HttpStatus.NO_CONTENT.getReasonPhrase())
                        .date(LocalDateTime.now())
                        .build()
        );
    }

    @Transactional
    @Override
    public CasoDTO crear(CasoDTO casoDTO) throws RestException {
        Optional<Usuario> usuario = usuarioRepository
                .findById(casoDTO.getUsuarioId());
        Optional<Delito> delito = delitoRepository
                .findById(casoDTO.getDelitoId());
        if(!usuario.isPresent() || !delito.isPresent()){
            log.error("No existe usuario {}", casoDTO.getUsuarioId());
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("No existe usuario o delito")
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        Caso caso = new Caso();
        caso.setFechaHora(casoDTO.getFechaHora());
        caso.setLatitud(casoDTO.getLatitud());
        caso.setLongitud(casoDTO.getLongitud());
        caso.setAltitud(casoDTO.getAltitud());
        caso.setDescripcion(casoDTO.getDescripcion());
        caso.setEsVisible(true);
        caso.setUrlMap(casoDTO.getUrlMap());
        caso.setRmiUrl(casoDTO.getRmiUrl());
        caso.setUsuario(usuario.get());
        caso.setDelito(delito.get());
        caso = casoRepository.save(caso);
        return CasoDTO.builder()
                .id(caso.getId())
                .fechaHora(caso.getFechaHora())
                .latitud(caso.getLatitud())
                .longitud(caso.getLongitud())
                .altitud(caso.getAltitud())
                .descripcion(caso.getDescripcion())
                .esVisible(caso.getEsVisible())
                .urlMap(caso.getUrlMap())
                .rmiUrl(caso.getRmiUrl())
                .usuarioId(caso.getUsuario().getId())
                .delitoId(caso.getDelito().getId())
                .build();
    }

    @Transactional
    @Override
    public Boolean visible(Boolean visible, Long id)  throws RestException {
        Optional<Caso> casoFounded = casoRepository.findById(id);
        if(!casoFounded.isPresent())
        {
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message("Caso no encontrado")
                            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        return casoRepository.setVisible(visible, id);
    }



    @Override
    public CasoDTO update(Long id, CasoDTO casoDTO) throws BadRequestException {
        Optional<Usuario> usuario = usuarioRepository.findById(casoDTO.getUsuarioId());
        Optional<Delito> delito = delitoRepository.findById(casoDTO.getDelitoId());

        if(!usuario.isPresent() || !delito.isPresent()){
            log.error("No existe usuario {}", casoDTO.getUsuarioId());
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message("No existe usuario o delito")
                            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        Optional<Caso> casoFounded = casoRepository.findById(id);
        if(!casoFounded.isPresent()){
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message("No existe caso")
                            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        Caso casoUpdated = casoFounded.get();

        casoUpdated.setId(casoDTO.getId());
        casoUpdated.setFechaHora(casoDTO.getFechaHora());
        casoUpdated.setLatitud(casoDTO.getLatitud());
        casoUpdated.setLongitud(casoDTO.getLongitud());
        casoUpdated.setAltitud(casoDTO.getAltitud());
        casoUpdated.setDescripcion(casoDTO.getDescripcion());
        casoUpdated.setEsVisible(casoDTO.getEsVisible());
        casoUpdated.setUrlMap(casoDTO.getUrlMap());
        casoUpdated.setRmiUrl(casoDTO.getRmiUrl());
        casoUpdated.setUsuario(usuario.get());
        casoUpdated.setDelito(delito.get());

        casoUpdated = casoRepository.save(casoUpdated);

        return CasoDTO.builder()
                .id(casoUpdated.getId())
                .fechaHora(casoUpdated.getFechaHora())
                .latitud(casoUpdated.getLatitud())
                .longitud(casoUpdated.getLongitud())
                .altitud(casoUpdated.getAltitud())
                .descripcion(casoUpdated.getDescripcion())
                .esVisible(casoUpdated.getEsVisible())
                .urlMap(casoUpdated.getUrlMap())
                .rmiUrl(casoUpdated.getRmiUrl())
                .usuarioId(casoUpdated.getUsuario().getId())
                .delitoId(casoUpdated.getDelito().getId())
                .build();
    }

    @Override
    public void deleteById(Long id) throws RestException {
        Optional<Caso> casoFounded = casoRepository.findById(id);
        if(!casoFounded.isPresent())
        {
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message("Caso no encontrado")
                            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        casoRepository.deleteById(id);
    }
}
