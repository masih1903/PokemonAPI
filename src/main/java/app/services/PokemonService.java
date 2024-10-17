package app.services;

import app.dtos.PokemonDTO;
import app.dtos.PokemonResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class PokemonService {

    private static final String POKEMON_API_URL = "https://pokeapi.co/api/v2/pokemon?offset=20&limit=1302";

    public static List<PokemonDTO> getAllPokemons() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(POKEMON_API_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = response.body();
        PokemonResponseDTO pokemonResponse = objectMapper.readValue(json, PokemonResponseDTO.class);

        List<PokemonDTO> pokemonList = new ArrayList<>(pokemonResponse.getPokemons());

        return pokemonList;

    }
}
