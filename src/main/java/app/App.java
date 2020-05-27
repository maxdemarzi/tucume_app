package app;

import io.jooby.Jooby;
import io.jooby.OpenAPIModule;

public class App extends Jooby {

  {

    mvc(new Controller());
  }

  public static void main(final String[] args) {
    runApp(args, App::new);
  }

}
