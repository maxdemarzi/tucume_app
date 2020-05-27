package app;

import com.fizzed.rocker.runtime.RockerRuntime;
import io.jooby.AssetHandler;
import io.jooby.AssetSource;
import io.jooby.Jooby;
import io.jooby.rocker.RockerModule;
import io.jooby.whoops.WhoopsModule;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class App extends Jooby {

  {
    // Debug friendly error messages
    install(new WhoopsModule());

    install(new RockerModule());
    RockerRuntime.getInstance().setReloading(true);

    Path assets = Paths.get("assets");
    AssetSource www = AssetSource.create(assets);
    assets("/assets/?*", new AssetHandler(www)
            .setMaxAge(Duration.ofDays(365)));

    assets("/robots.txt", assets.resolve("robots.txt"));

    mvc(new FooterController());
    mvc(new HomeController());
  }

  public static void main(final String[] args) {
    runApp(args, App::new);
  }

}
