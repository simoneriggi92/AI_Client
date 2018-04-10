package exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ForbiddenException extends WebApplicationException{

    public ForbiddenException(String message) {
        super(Response.status(Response.Status.fromStatusCode(404)).entity(message)
                .type(MediaType.TEXT_PLAIN).build());
    }
}
