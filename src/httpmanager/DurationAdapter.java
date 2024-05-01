package httpmanager;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration value) throws IOException {
        if (value == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(value.getSeconds());
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        String dateStr = jsonReader.nextString();
        return dateStr != null ? Duration.ofMinutes(Long.parseLong(dateStr)) : null;
    }
}
