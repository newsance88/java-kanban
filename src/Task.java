public class Task {
    public int id;
    protected String name;
    protected Status status;

    public Task(String name, Status status) {
        this.name = name;
        this.status = status;
    }
    public Task(String name, Status status, int id) {
        this.name = name;
        this.status = status;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
