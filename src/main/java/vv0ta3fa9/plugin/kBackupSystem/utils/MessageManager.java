package vv0ta3fa9.plugin.kBackupSystem.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vv0ta3fa9.plugin.kBackupSystem.kBackupSystem;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MessageManager {
    private final kBackupSystem plugin;
    private FileConfiguration messages;
    private String language;

    public MessageManager(kBackupSystem plugin) {
        this.plugin = plugin;
        this.language = plugin.getConfigManager().getString("language", "ru");
        loadMessages();
    }

    private void loadMessages() {
        String fileName = "messages_" + language + ".yml";
        File messagesFile = new File(plugin.getDataFolder(), fileName);

        if (!messagesFile.exists()) {
            plugin.saveResource(fileName, false);
        }

        messages = YamlConfiguration.loadConfiguration(messagesFile);

        InputStream defConfigStream = plugin.getResource(fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));
            messages.setDefaults(defConfig);
        }
    }
    public String getMessage(String key) {
        if (messages == null) {
            return key;
        }
        return messages.getString(key, key);
    }

    public String getMessage(String key, Object... replacements) {
        String message = getMessage(key);
        
        if (replacements.length % 2 != 0) {
            plugin.getLogger().warning("Invalid replacements for message key: " + key);
            return message;
        }

        for (int i = 0; i < replacements.length; i += 2) {
            String placeholder = replacements[i].toString();
            String value = replacements[i + 1].toString();
            message = message.replace(placeholder, value);
        }

        return message;
    }

    public void reload() {
        this.language = plugin.getConfigManager().getString("language", "ru");
        loadMessages();
    }
}
