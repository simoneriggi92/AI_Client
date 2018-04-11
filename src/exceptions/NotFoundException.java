package exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NotFoundException extends Throwable {

    public NotFoundException(String message) {
        //super(Response.status(Response.Status.fromStatusCode(404)).entity(message)
          //      .type(MediaType.TEXT_PLAIN).build());
    }
}
