package pt.dmms.arphenannouncements.repository;

import lombok.val;
import pt.dmms.arphenannouncements.ArphenAnnouncements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AnnouncementRepository {

    private final ArphenAnnouncements plugin;

    public AnnouncementRepository(ArphenAnnouncements plugin) {
        this.plugin = plugin;
        this.load();
    }

    private final List<List<String>> announcements = new ArrayList<>();

    public void update() {
        this.announcements.clear();
        this.load();
    }

    private void load() {
        val config = this.plugin.getConfig();
        for (String key : config.getConfigurationSection("announcements").getKeys(false)) {
            this.announcements.add(config.getStringList("announcements." + key));
        }
    }

    public List<String> get() {
        return announcements.get(ThreadLocalRandom.current().nextInt(announcements.size()));
    }

    public List<List<String>> getAnnouncements() {
        return announcements;
    }

}