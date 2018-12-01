package me.arboriginal.RulesCommands;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import clusterstorm.rules.RulesAPI;
import clusterstorm.rules.RulesConfirmedEvent;
import clusterstorm.rules.RulesRefusedEvent;

public class Main extends JavaPlugin implements Listener {
  protected FileConfiguration config;
  private Server              server;

  @Override
  public void onEnable() {
    server = getServer();

    reloadConfig();
    server.getPluginManager().registerEvents(this, this);
  }

  @Override
  public void reloadConfig() {
    super.reloadConfig();

    saveDefaultConfig();
    config = getConfig();
    config.options().copyDefaults(true);
    saveConfig();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (command.getName().equalsIgnoreCase("rulescmds-reload")) {
      reloadConfig();
      sendMessage(sender, "reload");

      return true;
    }

    return super.onCommand(sender, command, label, args);
  }

  @Override
  public void onDisable() {
    super.onDisable();

    HandlerList.unregisterAll((JavaPlugin) this);
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    if (player.hasPermission("exempt") || RulesAPI.isConfirmed(player.getName())) return;

    executeCommands("onJoin", player);
  }

  @EventHandler
  public void onRulesConfirmed(RulesConfirmedEvent event) {
    executeCommands("onAccept", event.getPlayer());
  }

  @EventHandler
  public void onRulesRefused(RulesRefusedEvent event) {
    executeCommands("onRefuse", event.getPlayer());
  }

  private void sendMessage(CommandSender sender, String key) {
    sender.sendMessage(prepareMessage(config.getString("messages." + key), sender.getName()));
  }

  private String prepareMessage(String message, String player) {
    return ChatColor.translateAlternateColorCodes('&',
        message.replace("{prefix}", config.getString("messages.prefix")).replace("{player}", player));
  }

  private void error(String key) {
    sendMessage(server.getConsoleSender(), key);
  }

  private void executeCommands(String event, Player player) {
    List<?> actions = config.getList("events." + event);

    if (actions == null) return;

    for (Object action : actions) {
      if (action instanceof LinkedHashMap) {
        Object  perm = ((LinkedHashMap<?, ?>) action).get("permission");
        boolean exec = (perm == null);

        if (!exec) {
          if (perm instanceof String) {
            exec = (player.hasPermission((String) perm)
                || (((String) perm).charAt(0) == '-') && !player.hasPermission(((String) perm).substring(1)));
          }
          else
            error("erPerm");
        }

        if (exec) {
          Object commands = ((LinkedHashMap<?, ?>) action).get("commands");

          if (commands != null) {
            boolean ok = (commands instanceof ArrayList);

            if (ok) {
              for (Object command : (ArrayList<?>) commands) {
                ok &= performCommand(command, player);
              }
            }

            if (!ok) error("erCmds");
          }
        }
      }
      else
        error("erAct");
    }
  }

  private boolean performCommand(Object command, Player player) {
    if (command instanceof String && !((String) command).isEmpty()) {
      command = ((String) command).replace("{player}", player.getName());

      server.dispatchCommand(server.getConsoleSender(), (String) command);

      return true;
    }

    return false;
  }
}
