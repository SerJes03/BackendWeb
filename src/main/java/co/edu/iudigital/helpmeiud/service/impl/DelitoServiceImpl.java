package co.edu.iudigital.helpmeiud.service.impl;

import co.edu.iudigital.helpmeiud.dto.request.DelitoDTORequest;
import co.edu.iudigital.helpmeiud.dto.response.DelitoDTO;
import co.edu.iudigital.helpmeiud.exceptions.BadRequestException;
import co.edu.iudigital.helpmeiud.exceptions.ErrorDto;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.model.Delito;
import co.edu.iudigital.helpmeiud.model.Usuario;
import co.edu.iudigital.helpmeiud.repository.IDelitoRepository;
import co.edu.iudigital.helpmeiud.repository.IUsuarioRepository;
import co.edu.iudigital.helpmeiud.service.iface.IDelitoService;
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
public class DelitoServiceImpl implements IDelitoService {
    private IDelitoRepository delitoRepository;
    private IUsuarioRepository usuarioRepository;

    @Autowired // Inyección de Dependencias por constructor
    public DelitoServiceImpl(IDelitoRepository delitoRepository,
                             IUsuarioRepository usuarioRepository){
        this.delitoRepository = delitoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DelitoDTO> consultarTodos() throws RestException{
        List<Delito> delitos = delitoRepository.findAll();
        if(delitos.isEmpty())
        {
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message("Delitos No encontrados")
                            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        return delitos.stream()
                .map(delito ->
                    DelitoDTO.builder()
                            .id(delito.getId())
                            .nombre(delito.getNombre())
                            .descripcion(delito.getDescripcion())
                            .usuarioId(delito.getUsuario().getId())
                            .usuario(delito.getUsuario().getNombre())
                            .build()
                ).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DelitoDTO consultarPorId(Long id) throws RestException{

        Optional<Delito> delitoFounded = delitoRepository.findById(id);
        if (!delitoFounded.isPresent()) {
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message("Delito No Encontrado")
                            .error(HttpStatus.NO_CONTENT.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        return DelitoDTO.builder()
                .id(delitoFounded.get().getId())
                .nombre(delitoFounded.get().getNombre())
                .descripcion(delitoFounded.get().getDescripcion())
                .usuarioId(delitoFounded.get().getUsuario().getId())
                .usuario(delitoFounded.get().getUsuario().getNombre())
                .build();
    }

    @Transactional
    @Override
    public DelitoDTO guardarDelito(DelitoDTORequest delitoDTORequest) throws RestException {
        Delito delito = new Delito();
        delito.setNombre(delitoDTORequest.getNombre());
        delito.setDescripcion(delitoDTORequest.getDescripcion());
        Optional<Usuario> usuarioOptional = usuarioRepository
                .findById(delitoDTORequest.getUsuarioId());
        if(!usuarioOptional.isPresent()){
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("No existe usuario")
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }
        delito.setUsuario(usuarioOptional.get());
        delito = delitoRepository.save(delito);
        return DelitoDTO.builder()
                .id(delito.getId())
                .nombre(delito.getNombre())
                .descripcion(delito.getDescripcion())
                .build();
    }


    @Override
    public DelitoDTO ActualizarDelito(DelitoDTORequest delitoDTORequest, Long id) throws RestException {

        Optional<Usuario> usuario = usuarioRepository.findById(delitoDTORequest.getUsuarioId());
        if(!usuario.isPresent()){
            log.error("No existe usuario {}", delitoDTORequest.getUsuarioId());
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("No existe usuario")
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        Optional<Delito> delitoFounded = delitoRepository.findById(id);
        if(!delitoFounded.isPresent()){
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("No existe delito")
                            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        Delito delitoUpdated = delitoFounded.get();

        delitoUpdated.setNombre(delitoDTORequest.getNombre());
        delitoUpdated.setDescripcion(delitoDTORequest.getDescripcion());
        delitoUpdated.setUsuario(usuario.get());

        delitoUpdated = delitoRepository.save(delitoUpdated);

        return DelitoDTO.builder()
                .id(delitoUpdated.getId())
                .nombre(delitoUpdated.getNombre())
                .descripcion(delitoUpdated.getDescripcion())
                .build();
    }

    @Override
    public void borrarDelitoPorId(Long id) throws RestException{
        Optional<Delito> delitoFounded = delitoRepository.findById(id);
        if(!delitoFounded.isPresent())
        {
            throw new BadRequestException(
                    ErrorDto.builder()
                            .status(HttpStatus.NOT_FOUND.value())
                            .message("Delito no encontrado")
                            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                            .date(LocalDateTime.now())
                            .build()
            );
        }

        delitoRepository.deleteById(id);
    }
}
