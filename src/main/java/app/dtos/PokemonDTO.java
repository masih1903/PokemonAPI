package app.dtos;

import app.entities.Pokemon;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class PokemonDTO {

    @JsonProperty("name")
    private String name;

    public PokemonDTO(Pokemon pokemon) {
        this.name = pokemon.getName();
    }

    // Converts from a list of Pokemon entities to a list of PokemonDTOs
    public static List<PokemonDTO> toPokemonDtoList(List<Pokemon> pokemons) {
        return pokemons.stream().map(PokemonDTO::new).toList();
    }

}
