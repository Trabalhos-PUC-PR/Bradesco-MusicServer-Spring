package br.pucpr.musicserver.rest.album;

import br.pucpr.musicserver.rest.artists.Artist;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity @AllArgsConstructor @NoArgsConstructor
public class Album {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Integer releaseYear;

    @NotEmpty
    @ManyToMany
    @Column(name="creators")
    @JoinTable(
            name="ArtistsAlbums",
            joinColumns = @JoinColumn(name="artist_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name="album_id", referencedColumnName = "id")
    )
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Artist> creators;


}
