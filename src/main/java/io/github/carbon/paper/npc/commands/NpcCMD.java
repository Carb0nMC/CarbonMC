package io.github.carbon.paper.npc.commands;

import io.github.carbon.paper.npc.Npc;
import io.github.carbon.paper.npc.NpcLoader;
import io.github.carbon.paper.npc.utils.SkinFetcher;
import io.github.carbon.paper.npc.utils.UUIDFetcher;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class NpcCMD implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 1){
            return Stream.of("help", "version", "create", "remove", "skin", "movehere", "displayName", "equipment", "playerCommand", "serverCommand", "showInTab", "glowing", "glowingColor", "list", "turnToPlayer")
                    .filter(input -> input.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        } else if(args.length == 2 && !args[0].equalsIgnoreCase("create")){
            return NpcLoader.getInstance().getNpcManager().getAllNpcs()
                    .stream()
                    .map(Npc::getName)
                    .filter(input -> input.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        } else if(args.length == 3 && args[0].equalsIgnoreCase("equipment")){
            return Arrays.stream(EquipmentSlot.values())
                    .map(EquipmentSlot::getName)
                    .filter(input -> input.toLowerCase().startsWith(args[2].toLowerCase()))
                    .toList();
        } else if(args.length == 3 && (args[0].equalsIgnoreCase("showInTab") || args[0].equalsIgnoreCase("glowing") || args[0].equalsIgnoreCase("turnToPlayer"))){
            return Stream.of("true", "false")
                    .filter(input -> input.toLowerCase().startsWith(args[2].toLowerCase()))
                    .toList();
        } else if(args.length == 3 && args[0].equalsIgnoreCase("glowingcolor")){
            return Arrays.stream(ChatFormatting.values())
                    .map(ChatFormatting::getName)
                    .filter(input -> input.toLowerCase().startsWith(args[2].toLowerCase()))
                    .toList();
        }

        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player p)){
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Only players can execute this command</red>"));
            return false;
        }

        if(args.length >= 1 && args[0].equalsIgnoreCase("help")){
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<green><b>NPC Commands::"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc version <dark_gray>- <white>Überprüft die aktuelle MC Version für NMS (nur für devs)"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc create (name) <dark_gray>- <white>Erstellt einen NPC an der aktuellen position"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc remove (name) <dark_gray>- <white>Entfernt einen NPC"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc list <dark_gray>- <white>Listet alle NPCs auf"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc skin (name) (skin) <dark_gray>- <white>Setzt den Skin für einen NPC"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc movehere (name) <dark_gray>- <white>Teleportiert einen NPC zu dir"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc displayName (name) (displayName ...) <dark_gray>- <white>Setzt den Displaynamen für einen NPC"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc equipment (name) (slot) <dark_gray>- <white>Setzt das Equipment für einen NPC zu dem Item in deiner Hand"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc playerCommand (name) (command ...) <dark_gray>- <white>Der Befehl wird ausgeführt, wenn jemand mit dem NPC interagiert (Player)"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc serverCommand (name) (command ...) <dark_gray>- <white>Der Befehl wird ausgeführt, wenn jemand mit dem NPC interagiert (Console)"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc showInTab (name) (true|false) <dark_gray>- <white>Ob der NPC in der Tablist angezeigt wird oder nicht"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc glowing (name) (true|false) <dark_gray>- <white>Ob der NPC leuchtet oder nicht"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc glowingColor (name) (color) <dark_gray>- <white>Die Farbe des leuchtenden NPCs"));
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<dark_green> - <green>/npc turnToPlayer (name) (true|false) <dark_gray>- <white>Ob der NPC sich zum Spieler dreht oder nicht"));

            return true;
        }

        if(args.length >= 1 && args[0].equalsIgnoreCase("version")){
            p.sendMessage(MiniMessage.miniMessage().deserialize("<color:#54f790><i>Checking version, please wait...</i></color>"));
            return true;
        } else if(args.length >= 1 && args[0].equalsIgnoreCase("list")){
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<green><b>All NPCs:</b></green>"));

            Collection<Npc> allNpcs = NpcLoader.getInstance().getNpcManager().getAllNpcs();

            if(allNpcs.isEmpty()){
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>There are no NPCs. Use '/npc create' to create one</yellow>"));
            } else {
                final DecimalFormat df = new DecimalFormat("#########.##");
                for (Npc npc : allNpcs) {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<green><hover:show_text:'<gray><i>Click to teleport'><click:run_command:'{tp_cmd}'>- {name} ({x}/{y}/{z})</click></hover></green>"
                            .replace("{name}", npc.getName())
                            .replace("{x}", df.format(npc.getLocation().x()))
                            .replace("{y}", df.format(npc.getLocation().y()))
                            .replace("{z}", df.format(npc.getLocation().z()))
                            .replace("{tp_cmd}", "/tp " + npc.getLocation().x() + " " + npc.getLocation().y() + " " + npc.getLocation().z())
                    ));
                }
            }

            return true;
        }

        if (args.length < 2){
            sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
            return false;
        }

        String subcommand = args[0];
        String name = args[1];

        switch (subcommand.toLowerCase()){
            case "create" -> {
                if(NpcLoader.getInstance().getNpcManager().getNpc(name) != null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>An npc with that name already exists</red>"));
                    return false;
                }

                Npc npc = new Npc(name, p.getLocation());
                npc.create();
                npc.spawnForAll();

                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Created new npc</green>"));
            }

            case "remove" -> {
                Npc npc = NpcLoader.getInstance().getNpcManager().getNpc(name);
                if(npc == null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Could not find npc</red>"));
                    return false;
                }

                npc.removeForAll();
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Removed npc</green>"));
            }

            case "movehere" -> {
                Npc npc = NpcLoader.getInstance().getNpcManager().getNpc(name);
                if(npc == null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Could not find npc</red>"));
                    return false;
                }

                npc.moveForAll(p.getLocation());
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Moved npc to your location</green>"));
            }

            case "skin" -> {
                if(args.length < 3){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                    return false;
                }

                String skinName = args[2];

                Npc npc = NpcLoader.getInstance().getNpcManager().getNpc(name);
                if(npc == null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Could not find npc</red>"));
                    return false;
                }

                SkinFetcher skinFetcher = new SkinFetcher(UUIDFetcher.getUUID(skinName).toString());
                npc.updateSkin(skinFetcher);
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Updated skin of npc</green>"));
            }

            case "displayname" -> {
                if(args.length < 3){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                    return false;
                }

                Npc npc = NpcLoader.getInstance().getNpcManager().getNpc(name);
                if(npc == null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Could not find npc</red>"));
                    return false;
                }

                String displayName = "";
                for (int i = 2; i < args.length; i++) {
                    displayName += args[i] + " ";
                }
                displayName = displayName.substring(0, displayName.length() - 1);

                npc.updateDisplayName(displayName);
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Updated display name of npc</green>"));
            }

            case "equipment" -> {
                if(args.length < 3){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                    return false;
                }

                Npc npc = NpcLoader.getInstance().getNpcManager().getNpc(name);
                if(npc == null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Could not find npc</red>"));
                    return false;
                }

                String slot = args[2];

                EquipmentSlot equipmentSlot = null;
                try {
                    equipmentSlot = EquipmentSlot.byName(slot);
                } catch (IllegalArgumentException e){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid equipment slot</red>"));
                    return false;
                }

                ItemStack item = p.getInventory().getItemInMainHand();

                npc.addEquipment(equipmentSlot, CraftItemStack.asNMSCopy(item));
                npc.removeForAll();
                npc.create();
                npc.spawnForAll();
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Updated equipment of npc</green>"));
            }

            case "servercommand" -> {
                if(args.length < 3){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                    return false;
                }

                Npc npc = NpcLoader.getInstance().getNpcManager().getNpc(name);
                if(npc == null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Could not find npc</red>"));
                    return false;
                }

                String cmd = "";
                for (int i = 2; i < args.length; i++) {
                    cmd += args[i] + " ";
                }
                cmd = cmd.substring(0, cmd.length() - 1);

                npc.setServerCommand(cmd);

                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Updated (server) command to be executed</green>"));
            }

            case "playercommand" -> {
                if(args.length < 3){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                    return false;
                }

                Npc npc = NpcLoader.getInstance().getNpcManager().getNpc(name);
                if(npc == null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Could not find npc</red>"));
                    return false;
                }

                String cmd = "";
                for (int i = 2; i < args.length; i++) {
                    cmd += args[i] + " ";
                }
                cmd = cmd.substring(0, cmd.length() - 1);

                npc.setPlayerCommand(cmd);

                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Updated (player) command to be executed</green>"));
            }

            case "showintab" -> {
                if(args.length < 3){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                    return false;
                }

                Npc npc = NpcLoader.getInstance().getNpcManager().getNpc(name);
                if(npc == null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Could not find npc</red>"));
                    return false;
                }

                boolean showInTab;
                switch (args[2].toLowerCase()) {
                    case "true" -> showInTab = true;
                    case "false" -> showInTab = false;
                    default -> {
                        sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Invalid argument (use 'true' or 'false')</red>"));
                        return false;
                    }
                }

                if(showInTab == npc.isShowInTab()){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Nothing has changed</red>"));
                    return false;
                }

                npc.updateShowInTab(showInTab);

                if(showInTab){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>NPC will now be shown in tab</green>"));
                } else {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>NPC will no longer be shown in tab</green>"));
                }
            }

            case "glowing" -> {
                if(args.length < 3){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                    return false;
                }

                Npc npc = NpcLoader.getInstance().getNpcManager().getNpc(name);
                if(npc == null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Could not find npc</red>"));
                    return false;
                }

                boolean glowing;
                try{
                    glowing = Boolean.parseBoolean(args[2]);
                }catch (Exception e){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                    return false;
                }

                npc.updateGlowing(glowing);

                if(glowing){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>NPC will now glow</green>"));
                } else {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>NPC will no glow</green>"));
                }
            }

            case "glowingcolor" -> {
                if(args.length < 3){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                    return false;
                }

                Npc npc = NpcLoader.getInstance().getNpcManager().getNpc(name);
                if(npc == null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Could not find npc</red>"));
                    return false;
                }

                ChatFormatting color = ChatFormatting.getByName(args[2]);
                if(color == null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                    return false;
                }

                npc.updateGlowingColor(color);

                sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>Updated glowing color to '" + color.getName() + "'</green>"));
            }

            case "turntoplayer" -> {
                if(args.length < 3){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                    return false;
                }

                Npc npc = NpcLoader.getInstance().getNpcManager().getNpc(name);
                if(npc == null){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Could not find npc</red>"));
                    return false;
                }

                boolean turnToPlayer;
                try{
                    turnToPlayer = Boolean.parseBoolean(args[2]);
                }catch (Exception e){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                    return false;
                }

                npc.setTurnToPlayer(turnToPlayer);

                if(turnToPlayer){
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>NPC will turn to the players</green>"));
                } else {
                    sender.sendMessage(MiniMessage.miniMessage().deserialize("<green>NPC will no longer turn to the players</green>"));

                    npc.moveForAll(npc.getLocation()); // move to default pos
                }
            }

            default -> {
                sender.sendMessage(MiniMessage.miniMessage().deserialize("<red>Wrong usage: /npc help</red>"));
                return false;
            }
        }

        return false;
    }
}
