package exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BadRequestException extends WebApplicationException{

    public BadRequestException(String message) {
        super(Response.status(Response.Status.fromStatusCode(400)).entity(message)
                .type(MediaType.TEXT_PLAIN).build());
    }
}
