package vv0ta3fa9.plugin.kBackupSystem;

import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import vv0ta3fa9.plugin.kBackupSystem.main.CommandManager;
import vv0ta3fa9.plugin.kBackupSystem.utils.BackupManager;
import vv0ta3fa9.plugin.kBackupSystem.utils.Color.Colorizer;
import vv0ta3fa9.plugin.kBackupSystem.utils.Color.impl.LegacyColorizer;
import vv0ta3fa9.plugin.kBackupSystem.utils.ConfigManager;
import vv0ta3fa9.plugin.kBackupSystem.utils.MessageManager;

@Getter
public class kBackupSystem extends JavaPlugin {
    private Colorizer colorizer;
    protected ConfigManager configManager;
    protected MessageManager messageManager;
    protected CommandManager commandsManager;
    protected KBSManager kbsManager;
    protected BackupManager backupManager;
    public boolean reload = false;

    PluginManager pluginManager;


    protected void startTask() {
        long intervalTicks = getConfigManager().time() * 60L * 20L;

        getServer().getScheduler().runTaskTimerAsynchronously(
                this,
                () -> getBackupManager().createBackupsAllWorlds(),
                intervalTicks,
                intervalTicks
        );

        getLogger().info(getMessageManager().getMessage("backup.task.started", 
                "{time}", String.valueOf(getConfigManager().time())));
    }

    protected void loadingConfiguration() {
        try {
            colorizer = new LegacyColorizer();
            configManager = new ConfigManager(this);
            messageManager = new MessageManager(this);
            backupManager = new BackupManager(this);
            commandsManager = new CommandManager(this);
        } catch (Exception e) {
            getLogger().severe("ОШИБКА ЗАГРУЗКИ КОНФИГУРАЦИИ! Выключение плагина...");
            e.printStackTrace();
            throw e;
        }
    }
    protected void registerCommands() {
        if (getCommand("kbackupsystem") != null) getCommand("kbackupsystem").setExecutor(commandsManager);
        else getLogger().severe(getMessageManager().getMessage("plugin.command.not_found"));
    }





}
