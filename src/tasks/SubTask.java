package tasks;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, Status status, int epicId) {
        super(name, status);
        this.epicId = epicId;
    }

    public SubTask(String name, Status status, int epicId, int id) {
        super(name, status);
        this.epicId = epicId;
        this.id = id;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }


    @Override
    public String toString() {
        return "Sub{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }
}
