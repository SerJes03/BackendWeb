package co.edu.iudigital.helpmeiud.controller;

import co.edu.iudigital.helpmeiud.dto.CasoDTO;
import co.edu.iudigital.helpmeiud.exceptions.RestException;
import co.edu.iudigital.helpmeiud.model.Caso;
import co.edu.iudigital.helpmeiud.service.iface.ICasoService;
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
@RequestMapping("/casos")
@Api(value = "/casos", tags = {"Casos"})
@SwaggerDefinition(tags = {
        @Tag(name = "Casos", description = "Gestion API Casos")
})
public class CasoController {

    @Autowired
    private ICasoService casoService;

    @ApiOperation(value = "Obtiene todos casos",
            responseContainer = "List",
            produces = "application/json",
            httpMethod = "GET")
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<List<CasoDTO>> index() throws RestException {
        return ResponseEntity
                .ok()
                .body(
                        casoService.consultarTodos()
                );
    }


    @ApiOperation(
            value = "Obtener caso por Id",
            responseContainer = "caso",
            httpMethod = "GET")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @GetMapping("/caso/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<CasoDTO> getById(@PathVariable Long id) throws RestException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        casoService.consultarPorId(id)
                );
    }


    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @ApiOperation(value = "Crea un caso",
            response = Caso.class,
            responseContainer = "Caso",
            produces = "application/json",
            httpMethod = "POST")
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<CasoDTO> create(
            @RequestBody @Valid CasoDTO casoDTO
    ) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        casoService.crear(casoDTO)
                );
    }

    @ApiOperation(value = "Actualiza un caso",
            response = Caso.class,
            responseContainer = "Caso",
            produces = "application/json",
            httpMethod = "PUT")
    @Secured("ROLE_ADMIN")
    @PutMapping("/update/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<CasoDTO> update(@RequestBody @Valid CasoDTO casoDTO, @PathVariable Long id) throws RestException{
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        casoService.update(id, casoDTO)
                );
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/visible/{visible}/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Boolean setVisble(@PathVariable Boolean visible, @PathVariable Long id) throws RestException {
        return casoService.visible(visible, id);
    }


    @ApiOperation(value = "Elimina un caso",
            httpMethod = "DELETE")
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void delete(@PathVariable Long id) throws RestException{
        casoService.deleteById(id);
    }
}
