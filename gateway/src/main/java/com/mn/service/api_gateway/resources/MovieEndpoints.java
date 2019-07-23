package com.mn.service.api_gateway.resources;

import com.mn.service.api_gateway.GatewayService;
import com.mn.service.api_gateway.models.movies.requests.*;
import com.mn.service.api_gateway.utility.ResponseFactory;
import com.mn.service.api_gateway.validation.ModelValidationException;
import com.mn.service.api_gateway.validation.ModelValidator;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

@Path("movies")
public class MovieEndpoints {
    @Path("search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchMovieRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo)
    {
        try {
            return ResponseFactory.createMovieResponse(headers, uriInfo, null, GatewayService.getMovieConfigs().getEPMovieSearch(), "GET");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("count")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response countMovieRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo)
    {
        try {
            return ResponseFactory.createMovieResponse(headers, uriInfo, null, GatewayService.getMovieConfigs().getEPMovieCount(), "GET");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("get/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo)
    {
        try {
            return ResponseFactory.createMovieResponse(headers, uriInfo, null, GatewayService.getMovieConfigs().getEPMovieGet(), "GET");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovieRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, AddMovieRequest.class);
            return ResponseFactory.createMovieResponse(headers, uriInfo, jsonText, GatewayService.getMovieConfigs().getEPMovieAdd(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, AddMovieRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("delete/{movieid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo)
    {
        try {
            return ResponseFactory.createMovieResponse(headers, uriInfo, null, GatewayService.getMovieConfigs().getEPMovieDelete(), "DELETE");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("genre")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo)
    {
        try {
            return ResponseFactory.createMovieResponse(headers, uriInfo, null, GatewayService.getMovieConfigs().getEPGenreGet(), "GET");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("genre/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addGenreRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, AddGenreRequest.class);
            return ResponseFactory.createMovieResponse(headers, uriInfo, jsonText, GatewayService.getMovieConfigs().getEPGenreAdd(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, AddGenreRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("genre/{movieid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGenresForMovieRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo)
    {
        try
        {
            return ResponseFactory.createMovieResponse(headers, uriInfo, null, GatewayService.getMovieConfigs().getEPGenreMovie(), "GET");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("star/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response starSearchRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo)
    {
        try {
            return ResponseFactory.createMovieResponse(headers, uriInfo, null, GatewayService.getMovieConfigs().getEPStarSearch(), "GET");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("star/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStarRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo)
    {
        try {
            return ResponseFactory.createMovieResponse(headers, uriInfo, null, GatewayService.getMovieConfigs().getEPStarGet(), "GET");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("star/get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDetailedStarRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo)
    {
        try {
            return ResponseFactory.createMovieResponse(headers, uriInfo, null, GatewayService.getMovieConfigs().getEPStarDetailedGet(), "GET");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("star/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, AddStarRequest.class);
            return ResponseFactory.createMovieResponse(headers, uriInfo, jsonText, GatewayService.getMovieConfigs().getEPStarAdd(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, AddStarRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("star/starsin")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStarToMovieRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, StarInRequest.class);
            return ResponseFactory.createMovieResponse(headers, uriInfo, jsonText, GatewayService.getMovieConfigs().getEPStarIn(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, StarInRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Path("rating")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRatingRequest(@Context HttpHeaders headers, @Context UriInfo uriInfo, String jsonText)
    {
        try {
            ModelValidator.verifyModel(jsonText, RatingRequest.class);
            return ResponseFactory.createMovieResponse(headers, uriInfo, jsonText, GatewayService.getMovieConfigs().getEPRating(), "POST");
        }
        catch (ModelValidationException e)
        {
            return ModelValidator.returnInvalidRequest(e, RatingRequest.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
