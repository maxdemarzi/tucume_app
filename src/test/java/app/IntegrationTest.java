package app;

import io.jooby.JoobyTest;
import io.jooby.StatusCode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@JoobyTest(App.class)
public class IntegrationTest {

  static OkHttpClient client = new OkHttpClient();

  @Test
  public void shouldGetHomePage() throws IOException {
    Request req = new Request.Builder()
            .url("http://localhost:8911")
            .build();

    try (Response rsp = client.newCall(req).execute()) {
      assertNotNull(rsp.body());
      assertTrue(rsp.body().string().contains("Tucume"));
      assertEquals(StatusCode.OK.value(), rsp.code());
    }
  }

  @Test
  public void shouldSayAbout() throws IOException {
    Request req = new Request.Builder()
        .url("http://localhost:8911/about")
        .build();

    try (Response rsp = client.newCall(req).execute()) {
      assertNotNull(rsp.body());
      assertTrue(rsp.body().string().contains("About"));
      assertEquals(StatusCode.OK.value(), rsp.code());
    }
  }
}
