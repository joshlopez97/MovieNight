package com.mn.service.movies.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mn.service.movies.core.IdmClient;
import com.mn.service.movies.core.MovieManager;
import com.mn.service.movies.core.StarManager;
import com.mn.service.movies.models.requests.AddStarToMovieRequest;
import com.mn.service.movies.models.DetailedStar;
import com.mn.service.movies.models.headers.RequestHeader;
import com.mn.service.movies.models.requests.AddStarRequest;
import com.mn.service.movies.models.requests.StarInRequest;
import com.mn.service.movies.models.requests.StarSearchRequest;
import com.mn.service.movies.models.responses.GetStarResponse;
import com.mn.service.movies.models.responses.JsonResponse;
import com.mn.service.movies.models.responses.ResponseFactory;
import com.mn.service.movies.models.responses.StarSearchResponse;
import com.mn.service.movies.validation.ValidationException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.sql.SQLException;

import static com.mn.service.movies.validation.Validator.validateRequestHeader;

@Path("star")
public class StarEndpoints
{
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchStars(
            @Context HttpHeaders headers,
            @QueryParam("movieTitle") String movieTitle,
            @QueryParam("name") String name,
            @QueryParam("birthYear") Integer birthYear,
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
            RequestHeader requestHeader = new RequestHeader(
                    headers.getHeaderString("email"),
                    headers.getHeaderString("sessionID"),
                    headers.getHeaderString("transactionID")
            );
            validateRequestHeader(requestHeader);
            boolean auth = IdmClient.checkUserPrivilege(requestHeader.getEmail(), 4);

            StarSearchRequest request = new StarSearchRequest(
                    offset, limit, sortby, orderby, name, birthYear, movieTitle
            );
            DetailedStar[] stars = StarManager.searchForStars(request, auth);
            if (stars.length == 0)
            {
                throw new ValidationException(213, "No stars found with search parameters.");
            }
            StarSearchResponse response = new StarSearchResponse(212, "Found stars with search parameters.", stars);
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (ValidationException e)
        {
            StarSearchResponse response = new StarSearchResponse(e.getCaseNumber(), e.getMessage(), null);
            Response.Status statusCode = e.getCaseNumber() < 0 ? Response.Status.BAD_REQUEST
                    : Response.Status.OK;
            return Response.status(statusCode).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response star(
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
            boolean auth = IdmClient.checkUserPrivilege(requestHeader.getEmail(), 4);

            DetailedStar star = StarManager.getStar(id);
            if (star == null)
            {
                throw new ValidationException(213, "No stars found with search parameters.");
            }
            GetStarResponse response = new GetStarResponse(212, "Found stars with search parameters.", star);
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (ValidationException e)
        {
            GetStarResponse response = new GetStarResponse(e.getCaseNumber(), e.getMessage(), null);
            Response.Status statusCode = e.getCaseNumber() < 0 ? Response.Status.BAD_REQUEST
                    : Response.Status.OK;
            return Response.status(statusCode).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response starDetailed(
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
            boolean auth = IdmClient.checkUserPrivilege(requestHeader.getEmail(), 4);

            DetailedStar star = StarManager.getStar(id);
            if (star == null)
            {
                throw new ValidationException(213, "No stars found with search parameters.");
            }
            star.setMovies(StarManager.getMoviesForStar(id));
            GetStarResponse response = new GetStarResponse(212, "Found stars with search parameters.", star);
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (ValidationException e)
        {
            GetStarResponse response = new GetStarResponse(e.getCaseNumber(), e.getMessage(), null);
            Response.Status statusCode = e.getCaseNumber() < 0 ? Response.Status.BAD_REQUEST
                    : Response.Status.OK;
            return Response.status(statusCode).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStar(
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
            AddStarRequest request = mapper.readValue(jsonInput, AddStarRequest.class);
            if (StarManager.starExistsWithName(request.getName(), request.getBirthYear()))
                throw new ValidationException(222, "Star already exists.");
            StarManager.addStar(request.getName(), request.getBirthYear());
            JsonResponse response = new JsonResponse(220, "Star successfully added.");
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JsonResponse response = new JsonResponse(221, "Could not add star.");
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("addto")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarToMovie(
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
            AddStarToMovieRequest request = mapper.readValue(jsonInput, AddStarToMovieRequest.class);

            StarManager.addStarInMovie(request.getStarID(), request.getMovieID());
            JsonResponse response = new JsonResponse(220, "Star successfully added.");
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            JsonResponse response = new JsonResponse(221, "Could not add star.");
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

    @Path("starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarInMovie(
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
            StarInRequest request = mapper.readValue(jsonInput, StarInRequest.class);
            if (!MovieManager.movieExistsWithID(request.getMovieId()))
                throw new ValidationException(211, "No movies found with search parameters.");
            if (StarManager.starExistsInMovie(request.getStarId(), request.getMovieId()))
                throw new ValidationException(232, "Star already exists in movie.");
            StarManager.addStarInMovie(request.getStarId(), request.getMovieId());
            JsonResponse response = new JsonResponse(230, "Star successfully added to movie.");
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (SQLException e)
        {
            JsonResponse response = new JsonResponse(231, "Could not add star to movie.");
            return Response.status(Response.Status.OK).entity(mapper.writeValueAsString(response)).build();
        }
        catch (Exception e)
        {
            return ResponseFactory.generateResponse(e);
        }
    }

}
