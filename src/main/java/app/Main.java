package app;

import app.config.AppConfig;

public class Main {
    public static void main(String[] args){

        AppConfig.startServer();

        // All of this code below is for fetching data from an API in the internet and saving it to our own database
        // Just remember the imports if you want to use the code below!

        /* EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        PokemonDAO pokemonDao = new PokemonDAO(emf);

        List<PokemonDTO> pokemons = PokemonService.getAllPokemons();

        pokemonDao.savePokemonsToDB(pokemons); */
    }
}