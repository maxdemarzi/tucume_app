package app;

import app.models.User;
import app.neo4j.Neo4jQueries;
import io.jooby.Context;
import io.jooby.StatusCode;
import io.jooby.annotations.FormParam;
import io.jooby.annotations.GET;
import io.jooby.annotations.POST;
import io.jooby.annotations.Path;
import io.jooby.exception.StatusCodeException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;

@Path("/")
public class HomeController {

  @GET
  public Object index() {
    return views.index.template();
  }

  @GET
  @Path("/signin")
  public Object signin() {
    return views.signin.template();
  }

  @GET
  @Path("/register")
  public Object register() {
    return views.register.template();
  }

  @POST
  @Path("/register")
  public Object registration(Context context, @FormParam String username, @FormParam String password, @FormParam String name, @FormParam String email) {
    password = BCrypt.hashpw(password, BCrypt.gensalt());
    HashMap<String, Object> parameters = new HashMap<>();
    parameters.put("username", username);
    parameters.put("password", password);
    parameters.put("name", name);
    parameters.put("email", email);
    User user = Neo4jQueries.usersCreate(parameters);
    if (user != null) {
      return context.sendRedirect("/signin");
    } else {
      throw new StatusCodeException(StatusCode.BAD_REQUEST);
    }
  }

}
