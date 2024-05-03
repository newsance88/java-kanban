package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import httpmanager.DurationAdapter;
import httpmanager.EpicAdapter;
import httpmanager.TimeAdapter;
import tasks.Epic;

import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {
    private Managers() {

    }

    public static Gson createGsonWithAdapters() {
        return new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(LocalDateTime.class, new TimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
