package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private PokemonRoute pokemonRoutes = new PokemonRoute();

    public EndpointGroup getApiRoutes() {
        return () ->
        {
            path("/pokemons", pokemonRoutes.getPokemonRoutes());

        };
    }
}
