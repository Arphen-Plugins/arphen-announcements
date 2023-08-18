package pt.dmms.arphenannouncements.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import org.bukkit.command.CommandSender;
import pt.dmms.arphenannouncements.ArphenAnnouncements;

@CommandAlias("announcements|announcement|arphenannouncements|arphenannouncement")
@CommandPermission("arphenannouncements.admin")
public class AnnouncementCommand extends BaseCommand {

    private final ArphenAnnouncements plugin;

    public AnnouncementCommand(ArphenAnnouncements plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCommand(CommandSender sender) {
        plugin.reloadConfig(sender);
    }

}