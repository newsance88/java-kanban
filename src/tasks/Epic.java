package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTaskId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, Status status, String description) {
        super(name, status, description, null, null);
    }

    public Epic(String name, Status status, String description, int id, Duration duration, LocalDateTime startTime) {
        super(name, status, description, id, duration, startTime);
    }

    public Epic(String name, Status status, String description, int id) {
        super(name, status, description, id);
    }

    public ArrayList<Integer> getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(ArrayList<Integer> subTaskId) {
        this.subTaskId = subTaskId;
    }

    public void clearSubTaskId() {
        subTaskId.clear();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", description='" + description + '\'' +
                '}';
    }
}
