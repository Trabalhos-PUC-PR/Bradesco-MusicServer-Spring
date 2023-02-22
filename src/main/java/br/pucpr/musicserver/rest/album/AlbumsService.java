package br.pucpr.musicserver.rest.album;

import org.springframework.stereotype.Service;

@Service
public class AlbumsService {

    private AlbumsRepository repository;

    public AlbumsService(AlbumsRepository repository) { this.repository = repository; }

}
