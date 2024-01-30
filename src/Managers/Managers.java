package Managers;

import DateTime.LocalDateTimeAdapter;
import DateTime.ZonedDateTimeAdapter;
import KVServer.KVServer;
import Managers.Files.FileBackedTasksManager;
import Managers.HistoryManager.HistoryManager;
import Managers.HistoryManager.InMemoryHistoryManager;
import Managers.Http.HttpTaskManager;
import Managers.TaskManager.TaskManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class Managers {
    public static TaskManager getDefault() {
        return new HttpTaskManager(KVServer.PORT);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
    }
}
