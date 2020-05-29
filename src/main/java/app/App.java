package app;

import app.neo4j.Neo4jAuthenticator;
import app.neo4j.Neo4jExtension;
import com.fizzed.rocker.runtime.RockerRuntime;
import io.jooby.*;
import io.jooby.pac4j.Pac4jModule;
import io.jooby.rocker.RockerModule;
import io.jooby.whoops.WhoopsModule;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.http.client.indirect.FormClient;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class App extends Jooby {

  {
    // Debug friendly error messages
    install(new WhoopsModule());

    // Neo4j Driver
    install(new Neo4jExtension());

    // Compress output
    setServerOptions(new ServerOptions().setGzip(true));

    // Template
    install(new RockerModule());
    RockerRuntime.getInstance().setReloading(true);

    // Static Assets
    Path assets = Paths.get("assets");
    AssetSource www = AssetSource.create(assets);
    assets("/assets/?*", new AssetHandler(www)
            .setMaxAge(Duration.ofDays(365)));

    assets("/robots.txt", assets.resolve("robots.txt"));

    // Handle Errors
    error((ctx, cause, statusCode) -> {
      Router router = ctx.getRouter();
      router.getLog().error("found `{}` error", statusCode.value(), cause);

      String code = String.valueOf(statusCode.value());
      code = code.replaceAll("0", "&#x1f635;");


      ctx.render(views.error.template(code, cause.getMessage()));
    });

    // Cross Site Request Forgery
    //before(new CsrfHandler());

    mvc(new FooterController());
    mvc(new HomeController());

    install(new Pac4jModule().client("*", conf -> new FormClient("/signin", new Neo4jAuthenticator())) );
    get("/home", ctx -> {
      UserProfile user = ctx.getUser();
      return "Hello " + user.getId();
    });

    //FormClient formClient = new FormClient("http://localhost:8080/theForm.jsp", new SimpleTestUsernamePasswordAuthenticator(), new UsernameProfileCreator());
    //use(new Auth().form("*", Neo4jAuthenticator.class));
  }

  public static void main(final String[] args) {
    runApp(args, App::new);
  }

}
