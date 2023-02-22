package br.pucpr.musicserver.rest.album;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlbumsResource {

    private AlbumsService service;

    public AlbumsResource(AlbumsService service) { this.service = service; }

}
