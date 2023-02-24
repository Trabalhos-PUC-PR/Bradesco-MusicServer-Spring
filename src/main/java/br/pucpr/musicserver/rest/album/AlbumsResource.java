package br.pucpr.musicserver.rest.album;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/albums")
public class AlbumsResource {

    private AlbumsService service;

    public AlbumsResource(AlbumsService service) { this.service = service; }

    @PostMapping
    @Transactional
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name="AuthServer")
    public Album add(@Valid @RequestBody Album album){
        return service.add(album);
    }

    @GetMapping
    @Transactional
    @RolesAllowed({"USER", "ADMIN"})
    @SecurityRequirement(name="AuthServer")
    public List<Album> getAllAlbums(){ return service.getAllAlbums(); };

    @DeleteMapping("{id}")
    @Transactional
    @RolesAllowed({"ADMIN"})
    @SecurityRequirement(name="AuthServer")
    public void delete(@PathVariable("id") Long id) { service.delete(id); }

}
