package DateTime;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ZonedDateTimeAdapter extends TypeAdapter<ZonedDateTime> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm", Locale.ENGLISH);
    private final static ZoneId zoneId = ZoneId.of("Europe/Moscow");
    @Override
    public void write(JsonWriter out, ZonedDateTime value) throws IOException {
        if (value != null){
            out.value(value.format(DATE_TIME_FORMATTER));
        }else {
            out.value("null");
        }

    }

    @Override
    public ZonedDateTime read(final JsonReader in) throws IOException {
        return LocalDateTime.parse(in.nextString(), DATE_TIME_FORMATTER).atZone(zoneId);
    }
}
