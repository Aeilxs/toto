package com.course;

import com.course.api.CharactersApi;
import com.course.model.CharacterModel;
import com.course.model.InlineResponse2004;
import com.course.model.InlineResponse2005;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
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

    @Transactional
    @PostConstruct
    public void init() throws IOException {
        this.load();
    }

    @Transactional
    public void load() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CharacterModel> list = objectMapper.readerForListOf(CharacterModel.class).readValue(getClass().getClassLoader().getResourceAsStream("hp.json"));
        for (CharacterModel characterModel : list) {
            characterModel.getAttributes().persist();
            characterModel.persist();
        }
    }

    @GET
    @Path("/chardb")
    public List<CharacterModel> findAllDb() {
        return CharacterModel.findAll().list();
    }

    @GET
    @Path("/chardb/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CharacterModel findByIdDb(@PathParam("id") String id) {
        return CharacterModel.find("jsonId", id).firstResult();
    }

    @GET
    @Path("/chardb/houses/{house}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CharacterModel> findByHouseDb(@PathParam("house") String house) {
        return CharacterModel.find("#Character.getByHouse", house).list();
    }

    @GET
    @Path("/chardb/houses-count/{house}")
    @Produces(MediaType.APPLICATION_JSON)
    public long countByHouse(@PathParam("house") String house) {
        return CharacterModel.find("#Character.getByHouse", house).count();
    }


    @GET
    @Path("/chardb/species/{specie}")
    public List<CharacterModel> findBySpeciesLike(@PathParam("specie") String s) {
        return CharacterModel.find("#Character.getBySpecies", s).list();
    }
}
