package bounswegroup1.resource;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import bounswegroup1.db.CommentDAO;
import bounswegroup1.db.RecipeDAO;
import bounswegroup1.db.MenuDAO;
import bounswegroup1.db.UserDAO;
import bounswegroup1.db.RatingDAO;
import bounswegroup1.model.AccessToken;
import bounswegroup1.model.Comment;
import bounswegroup1.model.Recipe;
import bounswegroup1.model.Menu;
import bounswegroup1.model.User;
import bounswegroup1.model.Rating;

import org.joda.time.DateTime;

import io.dropwizard.auth.Auth;


@Path("/rate")
@Produces(MediaType.APPLICATION_JSON)
public class RatingResource {
    private final RatingDAO ratingDAO;
    private final UserDAO userDAO;


    public RatingResource(RatingDAO ratingDAO, UserDAO userDAO) {
        super();
        this.ratingDAO = ratingDAO;
        this.userDAO = userDAO;
    }

    @POST
    public Rating addRating(@Auth AccessToken accessToken, Rating rating) {
        // if id is present, edit, otherwise add.
        if(rating.getRating() > 5) rating.setRating(5.0f);
        else if(rating.getRating() < 0) rating.setRating(0.0f);
        
        rating.setUserId(accessToken.getUserId());
        ratingDAO.deleteRating(rating);
        rating.setCreatedAt(new DateTime());
        ratingDAO.addRating(rating);
        return rating;
    }

    @GET
    @Path("/menu/{id}")
    public Rating getAvgRatingForMenu(@PathParam("id") Long id){
        return ratingDAO.getAverageRatingForParent(id, "menu");
    }

    @GET
    @Path("/recipe/{id}")
    public Rating getAvgRatingForRecipe(@PathParam("id") Long id){
        return ratingDAO.getAverageRatingForParent(id, "recipe");
    }

    @GET
    @Path("/user/{id}")
    public Rating getAvgRatingForUser(@PathParam("id") Long id){
        return ratingDAO.getAverageRatingForParent(id, "user");
    }

    @GET
    @Path("/menu/{id}/{raterId}")
    public Rating getRatingByUserForMenu(@PathParam("id") Long id, @PathParam("raterId") Long raterId){
        return ratingDAO.getRatingByUserForMenu(id, raterId);
    } 

    @GET
    @Path("/recipe/{id}/{raterId}")
    public Rating getRatingByUserForRecipe(@PathParam("id") Long id, @PathParam("raterId") Long raterId){
        return ratingDAO.getRatingByUserForRecipe(id, raterId);
    } 

    @GET
    @Path("/user/{id}/{raterId}")
    public Rating getRatingByUserForUser(@PathParam("id") Long id, @PathParam("raterId") Long raterId){
        return ratingDAO.getRatingByUserForUser(id, raterId);
    } 


}