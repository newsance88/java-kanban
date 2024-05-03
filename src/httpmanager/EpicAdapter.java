package httpmanager;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Epic;
import tasks.Status;

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
        //Здесь добавить на чтение не забыть, тест не робит

        Epic epic = new Epic("", Status.NEW, "");

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            switch (name) {
                case "id":
                    epic.setId(jsonReader.nextInt());
                    break;
                case "name":
                    epic.setName(jsonReader.nextString());
                    break;
                case "status":
                    epic.setStatus(Status.valueOf(jsonReader.nextString()));
                    break;
                case "description":
                    epic.setDescription(jsonReader.nextString());
                    break;
                case "subTaskId":
                    jsonReader.skipValue();
                    break;
                default:
                    jsonReader.skipValue();
                    break;
            }
        }
        jsonReader.endObject();

        return epic;
    }
}
