package app.routes;

import app.config.HibernateConfig;
import app.controllers.PokemonController;
import app.daos.PokemonDAO;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;


public class PokemonRoute {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final PokemonDAO pokemonDao = new PokemonDAO(emf);
    private final PokemonController pokemonController = new PokemonController(pokemonDao);

    public EndpointGroup getPokemonRoutes() {
        return () ->
        {
            path("/auth", () -> {

                get("/{id}", pokemonController::getById, Role.ANYONE);
                get("/", pokemonController::getAll, Role.ANYONE);
                post("/", pokemonController::create, Role.ADMIN);
                put("/{id}", pokemonController::update, Role.ADMIN);
                delete("/{id}", pokemonController::delete, Role.ADMIN);
            });
        };
    }
}
