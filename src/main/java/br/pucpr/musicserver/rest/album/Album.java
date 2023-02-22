package br.pucpr.musicserver.rest.album;

import br.pucpr.musicserver.rest.artists.Artist;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@Entity @AllArgsConstructor
public class Album {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Integer releaseYear;

    @NotEmpty
    @ElementCollection
    @CollectionTable(name="Artist", joinColumns = @JoinColumn(name="id"))
    @Column(name="creator")
    private Set<Artist> creators;


}
