package exceptions;

public class NotFoundException extends Throwable {

    public NotFoundException(String message) {
        super(message);
        //super(Response.status(Response.Status.fromStatusCode(404)).entity(message)
          //      .type(MediaType.TEXT_PLAIN).build());
    }
}
