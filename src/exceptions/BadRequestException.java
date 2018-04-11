package exceptions;

public class BadRequestException extends Throwable{

    public BadRequestException(String message) {
        super(message);
//        super(Response.status(Response.Status.fromStatusCode(400)).entity(message)
//                .type(MediaType.TEXT_PLAIN).build());
    }
}
