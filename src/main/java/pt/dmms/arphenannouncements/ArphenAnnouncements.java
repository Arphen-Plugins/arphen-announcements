package pt.dmms.arphenannouncements;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import pt.dmms.arphenannouncements.command.AnnouncementCommand;
import pt.dmms.arphenannouncements.repository.AnnouncementRepository;
import pt.dmms.arphenannouncements.task.AnnouncementTask;
import pt.dmms.arphenheart.factory.ConfigFactory;
import pt.dmms.arphenheart.util.SoundsUtil;
import pt.dmms.arphenheart.util.TitleUtil;
import pt.dmms.arphenheart.util.message.ColorUtil;
import pt.dmms.arphenheart.util.message.MessageUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ArphenAnnouncements extends JavaPlugin {

    private ConfigFactory langConfigFactory, soundsConfigFactory, titleConfigFactory;
    private MessageUtil messageUtil;
    private SoundsUtil soundsUtil;
    private TitleUtil titleUtil;
    private AnnouncementRepository announcementRepository;
    private BukkitTask task;

    @Override
    public void onEnable() {
        loadConfig();
        loadUtil();
        loadRepository();
        loadCommand();
        loadTask();
    }

    private final List<ConfigFactory> configFactories = new ArrayList<>();

    private void loadConfig() {
        saveDefaultConfig();
        configFactories.addAll(
                List.of(
                        langConfigFactory = new ConfigFactory(this, "lang.yml"),
                        soundsConfigFactory = new ConfigFactory(this, "sounds.yml"),
                        titleConfigFactory = new ConfigFactory(this, "title.yml")
                )
        );
        //Update the info.yml every run
        ConfigFactory.delete(this, "info.yml");
        new ConfigFactory(this, "info.yml");
    }

    private void loadUtil() {
        soundsUtil = new SoundsUtil(soundsConfigFactory.getConfig());
        titleUtil = new TitleUtil(titleConfigFactory.getConfig());
        messageUtil = new MessageUtil(langConfigFactory.getConfig(), soundsUtil, titleUtil);
    }

    private void loadCommand() {
        command(new AnnouncementCommand(this));
    }

    private PaperCommandManager paperCommandManager;


    private void command(BaseCommand... command) {
        paperCommandManager = new PaperCommandManager(this);
        for (BaseCommand baseCommand : command) {
            paperCommandManager.registerCommand(baseCommand);
        }
    }

    private void loadRepository() {
        announcementRepository = new AnnouncementRepository(this);
    }

    public void reloadConfig(CommandSender sender) {
        reloadConfig();
        for (ConfigFactory configFactory : configFactories) {
            configFactory.reload();
        }
        loadUtil();
        loadTask();
        announcementRepository.update();
        sender.sendMessage(ColorUtil.apply("<green>Config reloaded!"));
    }

    public void loadTask() {
        if (task != null && !task.isCancelled()) task.cancel();
        val time = getConfig().getInt("announcement-timer") * 20L;
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(this,
                new AnnouncementTask(this), time, time);
    }

}