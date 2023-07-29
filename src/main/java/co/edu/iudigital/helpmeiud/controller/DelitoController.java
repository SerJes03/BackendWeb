package co.edu.iudigital.helpmeiud.controller;


import co.edu.iudigital.helpmeiud.dto.request.DelitoDTORequest;
import co.edu.iudigital.helpmeiud.dto.response.DelitoDTO;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.service.iface.IDelitoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/delitos")
@Api(value = "/delitos", tags = {"Delitos"})
@SwaggerDefinition(tags = {
        @Tag(name = "Delitos", description = "Gestion API Delitos")
})
public class DelitoController {

    @Autowired
    IDelitoService delitoService;

    @ApiOperation(value = "Obtiene todos delitos",
            responseContainer = "List",
            produces = "application/json",
            httpMethod = "GET")
    @GetMapping
    @Secured("ROLE_ADMIN")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<DelitoDTO>> index() throws RestException {
        return ResponseEntity
                .ok()
                .body(
                        delitoService.consultarTodos()
                );
    }

    @ApiOperation(
            value = "Obtener delito por Id",
            responseContainer = "delito",
            httpMethod = "GET")
    @Secured("ROLE_ADMIN")
    @GetMapping("/delito/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<DelitoDTO> getById(@PathVariable Long id) throws RestException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        delitoService.consultarPorId(id)
                );
    }

    @ApiOperation(value = "Guardar un Delito",
            response = DelitoDTO.class,
            responseContainer = "DelitoDTO",
            produces = "application/json",
            httpMethod = "POST")
    @Secured("ROLE_ADMIN")
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<DelitoDTO> create(
            @RequestBody @Valid DelitoDTORequest delitoDTORequest
    ) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                   delitoService.guardarDelito(delitoDTORequest)
                );
    }


    @Secured("ROLE_ADMIN")
    @PutMapping("/update/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<DelitoDTO> update(@RequestBody @Valid DelitoDTORequest delitoDTORequest, @PathVariable Long id) throws RestException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        delitoService.ActualizarDelito(delitoDTORequest, id)
                );
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void delete(@PathVariable Long id) throws RestException{
        delitoService.borrarDelitoPorId(id);
    }
}
