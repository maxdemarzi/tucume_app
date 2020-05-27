package app;

import io.jooby.annotations.GET;
import io.jooby.annotations.Path;

public class FooterController {

    @GET
    @Path("/about")
    public Object about() {
        return views.footer.about.template();
    }

    @GET
    @Path("/cookies")
    public Object cookies() {
        return views.footer.cookies.template();
    }

    @GET
    @Path("/privacy")
    public Object privacy() {
        return views.footer.privacy.template();
    }

    @GET
    @Path("/terms")
    public Object terms() {
        return views.footer.terms.template();
    }

}
