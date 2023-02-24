package br.pucpr.musicserver.rest.album;

import br.pucpr.musicserver.lib.exception.BadRequestException;
import br.pucpr.musicserver.lib.exception.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AlbumsService {

    private AlbumsRepository repository;

    public AlbumsService(AlbumsRepository repository) { this.repository = repository; }

    public Album add(Album album){
        if(album==null) {
            throw new BadRequestException("Album cannot be null");
        }
        if(album.getId()!=null) {
            throw new BadRequestException("Album cannot have a preset id");
        }
        return repository.save(album);
    }

    public List<Album> getAllAlbums(){
        return repository.findAll(Sort.by(Sort.Order.asc("name")));
    }

    public void delete(Long id){
        if( !repository.existsById(id)){
            throw new NotFoundException(id);
        }
        repository.deleteById(id);
    }

}
