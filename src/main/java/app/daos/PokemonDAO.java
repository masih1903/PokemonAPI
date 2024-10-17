package app.daos;

import app.dtos.PokemonDTO;
import app.entities.Pokemon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class PokemonDAO implements IDAO<Pokemon> {

    private final EntityManagerFactory emf;

    public PokemonDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }


    @Override
    public Pokemon getById(Integer id) {

        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Pokemon.class, id);
        }
    }

    @Override
    public List<Pokemon> getAll() {

        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT p FROM Pokemon p", Pokemon.class).getResultList();
        }
    }

    @Override
    public void create(Pokemon pokemon) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(pokemon);
            em.getTransaction().commit();
        }
    }

    @Override
    public void update(Pokemon pokemon) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(pokemon);
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(Integer id) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Pokemon pokemon = em.find(Pokemon.class, id);
            em.remove(pokemon);
            em.getTransaction().commit();
        }
    }

    public void savePokemonsToDB(List<PokemonDTO> pokemonDTOList) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            for(PokemonDTO pokemonDto : pokemonDTOList){

                Pokemon pokemon = new Pokemon(pokemonDto);
                em.persist(pokemon);
            }

            em.getTransaction().commit();
        }
    }
}
