package com.mn.service.movies.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.service.movies.models.requests.AddGenreRequest;
import com.mn.service.movies.core.GenreManager;
import com.mn.service.movies.core.IdmClient;
import com.mn.service.movies.models.Genre;
import com.mn.service.movies.models.headers.RequestHeader;
import com.mn.service.movies.models.responses.GetGenresResponse;
import com.mn.service.movies.models.responses.JsonResponse;
import com.mn.service.movies.models.responses.ResponseFactory;
import com.mn.service.movies.models.responses.*;
import com.mn.service.movies.validation.ValidationException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.mn.service.movies.validation.Validator.validateRequestHeader;

@Path("genre")
public class GenreEndpoints
{
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresInMovies(
            @Context HttpHeaders headers,
            @PathParam("id") String movieId
    )
            throws JsonProcessingException
    {

        ObjectMapper mapper = new ObjectMapper();
        try
        {
            RequestHeader requestHeader = new RequestHeader(
                    headers.getHeaderString("email"),
                    headers.getHeaderString("sessionID"),
                    headers.getHeaderString("transactionID")
            );
            validateRequestHeader(requestHeader);
            boolean auth = IdmClient.checkUserPrivilege(requestHeader.getEmail(), 3);
            if (!auth)
                throw new ValidationException(141, "User has insufficient privilege.");

            Genre[] genres = GenreManager.getGenresForMovie(movieId);
            if (genres.length == 0)
            {
                throw new ValidationException(211, "No movies found with search parameters.");
            }
            GetGenresResponse response = new GetGenresResponse(219, "Genres successfully retrieved.", genres);
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGenres(
            @Context HttpHeaders headers
    )
            throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            RequestHeader requestHeader = new RequestHeader(
                    headers.getHeaderString("email"),
                    headers.getHeaderString("sessionID"),
                    headers.getHeaderString("transactionID")
            );
            validateRequestHeader(requestHeader);

            Genre[] genres = GenreManager.getAllGenres();
            if (genres.length == 0)
            {
                throw new ValidationException(211, "No movies found with search parameters.");
            }
            GetGenresResponse response = new GetGenresResponse(219, "Genres successfully retrieved.", genres);
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenre(
            @Context HttpHeaders headers,
            String jsonInput
    )
            throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            RequestHeader requestHeader = new RequestHeader(
                    headers.getHeaderString("email"),
                    headers.getHeaderString("sessionID"),
                    headers.getHeaderString("transactionID")
            );
            validateRequestHeader(requestHeader);
            boolean auth = IdmClient.checkUserPrivilege(requestHeader.getEmail(), 3);
            if (!auth)
                throw new ValidationException(141, "User has insufficient privilege.");
            AddGenreRequest request = mapper.readValue(jsonInput, AddGenreRequest.class);
            if (GenreManager.genreExists(request.getName()))
                throw new ValidationException(218, "Genre could not be added.");
            GenreManager.addGenre(request.getName());
            JsonResponse response = new JsonResponse(217, "Genre successfully added.");
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }
}
