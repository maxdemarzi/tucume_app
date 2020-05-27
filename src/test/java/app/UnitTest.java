package app;

import com.fizzed.rocker.RockerModel;
import io.jooby.MockRouter;
import io.jooby.StatusCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {
  @Test
  public void welcome() {
    MockRouter router = new MockRouter(new App());
    router.get("/", rsp -> {
      RockerModel value = (RockerModel)rsp.value();
      assertNotNull(value);
      assertTrue(value.render().toString().contains("Tucume"));
      assertEquals(StatusCode.OK, rsp.getStatusCode());
    });
  }
}
