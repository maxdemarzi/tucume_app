package app;

import com.fizzed.rocker.runtime.RockerRuntime;
import io.jooby.Jooby;
import io.jooby.rocker.RockerModule;

import java.nio.file.Path;
import java.nio.file.Paths;

public class App extends Jooby {

  {
    install(new RockerModule());
    RockerRuntime.getInstance().setReloading(true);

    Path assets = Paths.get("assets");
    assets("/assets/?*", assets);


    mvc(new HomeController());
  }

  public static void main(final String[] args) {
    runApp(args, App::new);
  }

}
