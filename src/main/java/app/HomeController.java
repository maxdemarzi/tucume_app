package app;

import io.jooby.annotations.*;

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

}
