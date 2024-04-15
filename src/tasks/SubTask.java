package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;


    public SubTask(String name, Status status, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, status, description,duration,startTime);
        this.epicId = epicId;
    }
    public SubTask(String name, Status status, String description, int epicId,int id, Duration duration, LocalDateTime startTime) {
        super(name, status, description,id,duration,startTime);
        this.epicId = epicId;
    }

    public SubTask(String name, Status status, String description, int epicId, int id) {
        super(name, status, description,id);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
