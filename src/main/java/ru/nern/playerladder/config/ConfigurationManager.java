package ru.nern.playerladder.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import ru.nern.playerladder.PlayerLadder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.stream.Stream;

import static ru.nern.playerladder.PlayerLadder.CONFIG;

public class ConfigurationManager {

    private static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(PlayerLadder.MOD_ID).get().getMetadata().getVersion().getFriendlyString();
    private static final File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), PlayerLadder.MOD_ID+"_config.json");
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void loadConfig() {
        try {
            if (file.exists()) {
                StringBuilder contentBuilder = new StringBuilder();
                try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
                    stream.forEach(s -> contentBuilder.append(s).append("\n"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                CONFIG = gson.fromJson(contentBuilder.toString(), Config.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(CONFIG == null)
            CONFIG = new Config();
    }

    public static void saveConfig() {
        CONFIG.lastLoadedVersion = MOD_VERSION;
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(CONFIG));
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onInit() {
        if(!file.exists()) {
            saveConfig();
        }else{
            loadConfig();
            if(!Objects.equals(CONFIG.lastLoadedVersion, MOD_VERSION)) saveConfig();
        }
    }

    public static class Config {
        public String lastLoadedVersion = "";
        public Server server = new Server();
        public Client client = new Client();

        public static class Server {
            public ClickMode mode = ClickMode.RIDE;
            public int pickUpLimit = 16;
            public int stepUpLimit = 16;
            public boolean interactWithAnyLiving = false;
            public boolean rideExtension = true;
        }

        public static class Client {
            public boolean allowInteractions = true;
        }
    }

    public enum ClickMode {
        RIDE,
        PICK_UP,
        DO_NOTHING
    }
}
