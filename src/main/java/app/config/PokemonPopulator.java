package app.config;

import app.daos.PokemonDAO;
import app.dtos.PokemonDTO;
import app.entities.Pokemon;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class PokemonPopulator {

    private static EntityManagerFactory emf;
    private static PokemonDAO pokemonDao;

    public PokemonPopulator(EntityManagerFactory emf, PokemonDAO pokemonDao){
        PokemonPopulator.emf = emf;
        PokemonPopulator.pokemonDao = pokemonDao;

    }

    public List<PokemonDTO> populatePokemons(){

        if(emf == null){

            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create country objects
            Pokemon p1 = new Pokemon("Bulbusaur");

            Pokemon p2 = new Pokemon("Charmander");

            Pokemon p3 = new Pokemon("Squirtle");

            // Persisting countries to the database
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);

            em.getTransaction().commit();

            return new ArrayList<>(List.of(new PokemonDTO(p1), new PokemonDTO(p2), new PokemonDTO(p3)));
        }
    }

    public void cleanUpPokemons() {
        if (emf == null) {
            throw new IllegalStateException("EntityManagerFactory is not initialized");
        }

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Pokemon").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE pokemon_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
