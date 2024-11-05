package com.course;

import com.course.api.CharactersApi;
import com.course.model.CharacterModel;
import com.course.model.InlineResponse2004;
import com.course.model.InlineResponse2005;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.IOException;
import java.util.List;

@Path("/")
public class ExampleResource {
    @RestClient
    CharactersApi charactersApi;

    @GET
    @Path("characters")
    @Produces(MediaType.APPLICATION_JSON)
    public InlineResponse2004 findAll(
            @QueryParam("page[size]") @DefaultValue("50") Integer pageSize,
            @QueryParam("page[number]") @DefaultValue("1") Integer pageNumber,
            @QueryParam("sort") List<String> sort,
            @QueryParam("filter") String filter)
    {
        return charactersApi.getCharacters(pageSize, pageNumber, sort, filter);
    }

    @GET
    @Path("characters/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public InlineResponse2005 findById(@PathParam("id") String id) {
        return charactersApi.getCharacter(id);
    }

    @GET
    @Path("/load")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String findById() throws IOException {
        this.load();
        return "data loaded";

    }

    private void load() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CharacterModel> list = objectMapper.readerForListOf(CharacterModel.class).readValue(getClass().getClassLoader().getResourceAsStream("hp.json"));
        for (CharacterModel characterModel : list) {
            characterModel.getAttributes().persist();
            characterModel.persist();
        }
    }

    @GET
    public List<CharacterModel> findAll() {
        return CharacterModel.findAll().list();
    }

    //GET on character/{id}
    @GET
    @Path("/chardb/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CharacterModel> findById(Long id) {
        return CharacterModel.findById(id);
    }
//    //GET on houses/{house}
//    public List<CharacterModel> findByHouse(Houses house) {
//    }
//
//    //GET on houses-count/{house}
//    public int countByHouse(Houses house) {
//    }
//    //GET on species/{specie}
//    public List<CharacterModel> findBySpeciesLike(String specie) {
//        //Use a namedQuery for this query
//    }
}
