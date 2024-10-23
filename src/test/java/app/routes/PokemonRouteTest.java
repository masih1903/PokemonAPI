package app.routes;

import app.config.AppConfig;
import app.config.HibernateConfig;
import app.config.PokemonPopulator;
import app.daos.PokemonDAO;
import app.dtos.PokemonDTO;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class PokemonRouteTest {

    private static EntityManagerFactory emf;
    private static String BASE_URL = "http://localhost:7070/api";
    private static PokemonDAO pokemonDao;
    private static PokemonPopulator populator;
    private static PokemonDTO p1, p2, p3;
    private static List<PokemonDTO> pokemons;

    @BeforeAll
    static void init() {

        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryConfigTest();
        pokemonDao = new PokemonDAO(emf);  // Initialize the DAO after emf is set
        populator = new PokemonPopulator(emf, pokemonDao);  // Initialize Populator after emf and DAO are set
        AppConfig.startServer();
    }

    @BeforeEach
    void setUp() {

        pokemons = populator.populatePokemons();
        p1 = pokemons.get(0);
        p2 = pokemons.get(1);
        p3 = pokemons.get(2);
    }

    @AfterEach
    void tearDown() {

        populator.cleanUpPokemons();
    }

    @Test
    void getPokemonRoutes() {

        // Test the GET /api/pokemons route
        List<PokemonDTO> fetchedPokemons =
                given()
                        .when()
                        .get(BASE_URL + "/pokemons")
                        .then()
                        .extract()
                        .body()
                        .jsonPath()
                        .getList(".", PokemonDTO.class);
        assertEquals(pokemons, fetchedPokemons);
    }
}