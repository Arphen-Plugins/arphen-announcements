package pt.dmms.arphenannouncements.task;

import org.bukkit.Bukkit;
import pt.dmms.arphenannouncements.ArphenAnnouncements;
import pt.dmms.arphenannouncements.repository.AnnouncementRepository;
import pt.dmms.arphenheart.util.message.ColorUtil;

public class AnnouncementTask implements Runnable {

    private final AnnouncementRepository repository;

    public AnnouncementTask(ArphenAnnouncements plugin) {
        this.repository = plugin.getAnnouncementRepository();
    }

    @Override
    public void run() {
        for (String s : repository.get()) {
            Bukkit.broadcast(ColorUtil.apply(s));
        }
    }

}
