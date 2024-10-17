package app.controllers;

import app.daos.PokemonDAO;
import app.dtos.PokemonDTO;
import app.entities.Message;
import app.entities.Pokemon;
import app.exceptions.ApiException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PokemonController implements IController {

    private final Logger log = LoggerFactory.getLogger(PokemonController.class);
    private final PokemonDAO pokemonDao;

    public PokemonController(PokemonDAO pokemonDao) {
        this.pokemonDao = pokemonDao;
    }


    @Override
    public void getById(Context ctx) {

            try {
                // == request ==
                Integer id = Integer.parseInt(ctx.pathParam("id"));

                // == querying ==
                Pokemon pokemon = pokemonDao.getById(id);

                if (pokemon == null) {
                    ctx.res().setStatus(404);
                    ctx.json(new Message(404, "Pokemon not found"), Message.class);
                    return;
                }

                // == response ==
                PokemonDTO pokemonDto = new PokemonDTO(pokemon);
                ctx.res().setStatus(200);
                ctx.json(pokemonDto, PokemonDTO.class);

            } catch (NumberFormatException e) {
                log.error("Invalid pokemon ID format: {}", e.getMessage());
                throw new ApiException(400, "Invalid pokemon ID format");
            } catch (Exception e) {
                log.error("500 {}", e.getMessage());
                throw new ApiException(500, e.getMessage());
            }
    }

    @Override
    public void getAll(Context ctx) {

        try {
            // == querying ==
            List<Pokemon> pokemons = pokemonDao.getAll();

            // == response ==
            List<PokemonDTO> pokemonDtos = PokemonDTO.toPokemonDtoList(pokemons);
            ctx.res().setStatus(200);
            ctx.json(pokemonDtos, PokemonDTO.class);
        } catch (Exception e) {
            log.error("500 {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }

    }

    @Override
    public void create(Context ctx) {

        try {
            // == request ==
            PokemonDTO pokemonDto = ctx.bodyAsClass(PokemonDTO.class);

            // == converting DTO to Entity ==
            Pokemon newPokemon = new Pokemon(pokemonDto);

            // == persisting the pokemon ==
            pokemonDao.create(newPokemon);

            // == response ==
            ctx.res().setStatus(201);
            ctx.result("Pokemon created");
        } catch (Exception e) {

            log.error("400 {}", e.getMessage());

            throw new ApiException(400, e.getMessage());
        }

    }

    @Override
    public void update(Context ctx) {

        try {
            // == request ==
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            PokemonDTO pokemonDto = ctx.bodyAsClass(PokemonDTO.class); // Request body as DTO

            // == querying for existing pokemon ==
            Pokemon existingPokemon = pokemonDao.getById(id);

            if (existingPokemon == null) {
                ctx.res().setStatus(404);
                ctx.json(new Message(404, "Pokemon not found"), Message.class);
                return;
            }

            // == updating fields of the existing pokemon ==
            existingPokemon.setName(pokemonDto.getName());
            // Update any other fields necessary

            // == updating in DB ==
            pokemonDao.update(existingPokemon);

            // == response ==
            ctx.res().setStatus(200);
            ctx.result("Pokemon updated");

        } catch (NumberFormatException e) {
            log.error("Invalid pokemon ID format: {}", e.getMessage());
            throw new ApiException(400, "Invalid pokemon ID format");
        } catch (Exception e) {
            log.error("500 {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }


    }

    @Override
    public void delete(Context ctx) {

        try {
            // == request ==
            Integer id = Integer.parseInt(ctx.pathParam("id"));

            // == deleting from DB ==
            pokemonDao.delete(id);

            // == response ==
            ctx.res().setStatus(200);
            ctx.result("Pokemon deleted");

        } catch (NumberFormatException e) {
            log.error("Invalid pokemon ID format: {}", e.getMessage());
            throw new ApiException(400, "Invalid pokemon ID format");
        } catch (Exception e) {
            log.error("500 {}", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }

    }
}
