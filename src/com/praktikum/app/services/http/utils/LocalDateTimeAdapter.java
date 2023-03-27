package com.praktikum.app.services.http.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm";

    public LocalDateTimeAdapter() {
    }

    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(localDateTime.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
        }
    }

    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        String strFromJson = jsonReader.nextString();
        if (Objects.equals(strFromJson, "null")) {
            return null;
        }
        return LocalDateTime.parse(strFromJson, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }
}
