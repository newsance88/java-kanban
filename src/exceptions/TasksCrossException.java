package exceptions;

public class TasksCrossException extends IllegalArgumentException {

    public TasksCrossException(String message) {
        super(message);
    }
}
