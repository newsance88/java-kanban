package httpmanager;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Epic;

import java.io.IOException;

public class EpicAdapter extends TypeAdapter<Epic> {
    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id").value(epic.getId());
        jsonWriter.name("name").value(epic.getName());
        jsonWriter.name("status").value(epic.getStatus().toString());
        jsonWriter.name("description").value(epic.getDescription());
        // Здесь продолжаем сериализацию остальных полей по аналогии
        jsonWriter.endObject();
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
