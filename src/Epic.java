public class Epic extends Task{
    public Epic(String name, Status status) {
        super(name, status);
    }
    public Epic(String name, Status status, int id) {
        super(name, status);
        this.id = id;
    }
}
