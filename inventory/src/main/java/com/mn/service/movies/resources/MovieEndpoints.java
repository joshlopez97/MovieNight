package com.mn.service.movies.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.service.movies.core.RatingManager;
import com.mn.service.movies.logger.ServiceLogger;
import com.mn.service.movies.models.requests.RatingRequest;
import com.mn.service.movies.models.responses.*;
import com.mn.service.movies.core.IdmClient;
import com.mn.service.movies.core.MovieManager;
import com.mn.service.movies.models.requests.AddMovieRequest;
import com.mn.service.movies.models.responses.*;
import com.mn.service.movies.models.DetailedMovie;
import com.mn.service.movies.models.headers.RequestHeader;
import com.mn.service.movies.models.requests.MovieSearchRequest;
import com.mn.service.movies.validation.ValidationException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.mn.service.movies.validation.Validator.validateRequestHeader;

@Path("")
public class MovieEndpoints
{
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovies(
            @Context HttpHeaders headers,
            @QueryParam("title") String title,
            @QueryParam("genre") String genre,
            @QueryParam("year") Integer year,
            @QueryParam("director") String director,
            @QueryParam("hidden") Boolean hidden,
            @QueryParam("offset") Integer offset,
            @QueryParam("limit") Integer limit,
            @QueryParam("orderby") String sortby,
            @QueryParam("direction") String orderby,
            @QueryParam("detailed") Boolean detailed
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
            boolean auth = IdmClient.checkUserPrivilege(requestHeader.getEmail(), 4);

            MovieSearchRequest movieSearchRequest = new MovieSearchRequest(
                    title, genre, year, director, hidden, offset, limit,
                    sortby, orderby
            );
            ServiceLogger.LOGGER.info("Constructed MovieSearchRequest " + movieSearchRequest);
            detailed = detailed == null ? false : detailed;
            DetailedMovie[] movies = MovieManager.searchForMovies(movieSearchRequest, auth, detailed);
            if (movies.length == 0)
            {
                throw new ValidationException(211, "No movies found with search parameters.");
            }
            MovieSearchResponse response = new MovieSearchResponse(210, "Found movies with search parameters.", movies);
            String responseStr = mapper.writeValueAsString(response);
            ServiceLogger.LOGGER.info("Returning " + responseStr);
            return Response.status(Response.Status.OK).entity(responseStr).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("count")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response countMovies(
            @Context HttpHeaders headers,
            @QueryParam("title") String title,
            @QueryParam("genre") String genre,
            @QueryParam("year") Integer year,
            @QueryParam("director") String director,
            @QueryParam("hidden") Boolean hidden,
            @QueryParam("offset") Integer offset,
            @QueryParam("limit") Integer limit,
            @QueryParam("orderby") String sortby,
            @QueryParam("direction") String orderby
    )
            throws JsonProcessingException
    {

        ObjectMapper mapper = new ObjectMapper();
        try
        {
            MovieSearchRequest movieSearchRequest = new MovieSearchRequest(
                    title, genre, year, director, hidden, offset, limit,
                    sortby, orderby
            );
            ServiceLogger.LOGGER.info("Constructed MovieCountRequest " + movieSearchRequest);
            int movies = MovieManager.countMovies(movieSearchRequest);
            if (movies == 0)
            {
                throw new ValidationException(211, "No movies found with search parameters.");
            }
            MovieCountResponse response = new MovieCountResponse(210, "Found movies with search parameters.", movies);
            String responseStr = mapper.writeValueAsString(response);
            ServiceLogger.LOGGER.info("Returning " + responseStr);
            return Response.status(Response.Status.OK).entity(responseStr).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response movie(
            @Context HttpHeaders headers,
            @PathParam("id") String id
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
            boolean auth = true;

            DetailedMovie movie = MovieManager.getMovie(id);
            if (movie == null)
            {
                throw new ValidationException(211, "No movies found with search parameters.");
            }
            if (!auth)
            {
                if (movie.getHidden())
                {
                    throw new ValidationException(141, "User has insufficient privilege.");
                }
                movie.setHidden(null);
            }
            GetMovieResponse response = new GetMovieResponse(210, "Found movies with search parameters.", movie);
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
    public Response addMovie(
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
            AddMovieRequest request = mapper.readValue(jsonInput, AddMovieRequest.class);
            if (MovieManager.movieExistsWithTitle(request.getTitle()))
                throw new ValidationException(216, "Movie already exists.");
            AddMovieResponse response = MovieManager.addMovie(request);

            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("delete/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovie(
            @Context HttpHeaders headers,
            @PathParam("id") String id
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
            if (!MovieManager.movieExistsWithIDHiddenIncluded(id))
                throw new ValidationException(241, "Could not remove movie.");
            if (!MovieManager.deleteMovie(id))
                throw new ValidationException(242, "Movie has been already removed.");

            JsonResponse response = new JsonResponse(240, "Movie successfully removed.");
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovieRating(
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
            RatingRequest request = mapper.readValue(jsonInput, RatingRequest.class);
            if (!MovieManager.movieExistsWithID(request.getMovieId()))
                throw new ValidationException(211, "No movies found with search parameters.");
            if (!RatingManager.addRating(request.getRating(), request.getMovieId()))
                throw new ValidationException(251, "Could not update rating.");
            JsonResponse response = new JsonResponse(250, "Rating successfully updated.");

            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }
}
