package com.course;

import com.course.api.CharactersApi;
import com.course.model.CharacterModel;
import com.course.model.InlineResponse2004;
import com.course.model.InlineResponse2005;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

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
}
