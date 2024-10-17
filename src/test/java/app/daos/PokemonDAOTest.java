package app.daos;

import app.config.HibernateConfig;
import app.entities.Pokemon;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PokemonDAOTest {

    static EntityManagerFactory emf;
    static PokemonDAO pokemonDAO;
    static Pokemon p1, p2, p3;


    @BeforeAll
    static void setUpAll() {

        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryConfigTest();
        pokemonDAO = new PokemonDAO(emf);

    }

    @BeforeEach
    void setUp() {

        p1 = new Pokemon("Pikachu");
        p2 = new Pokemon("Charmander");
        p3 = new Pokemon("Squirtle");

        pokemonDAO.create(p1);
        pokemonDAO.create(p2);
        pokemonDAO.create(p3);

    }

    @AfterEach
    void tearDown() {

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Pokemon").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE pokemon_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getById() {

            Pokemon pokemon = pokemonDAO.getById(p1.getId());
            assertEquals(p1.getName(), pokemon.getName());
    }

    @Test
    void getAll() {

        var pokemons = pokemonDAO.getAll();
        assertEquals(3, pokemons.size());
    }

    @Test
    void create() {

        Pokemon p4 = new Pokemon("Bulbasaur");
        pokemonDAO.create(p4);
        assertEquals(4, p4.getId());
    }

    @Test
    void update() {

        p1.setName("Raichu");
        pokemonDAO.update(p1);
        assertEquals("Raichu", p1.getName());
    }

    @Test
    void delete() {

            pokemonDAO.delete(p1.getId());
            assertNull(pokemonDAO.getById(p1.getId()));
    }
}