package io.github.carbon.paper.jukebox;

import com.tchristofferson.configupdater.ConfigUpdater;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.carbonmc.plugin.PaperLoader;
import io.github.carbon.paper.jukebox.utils.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.Collator;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class JukeBox implements Listener{
    public static int version = 19;
    private static JukeBox instance;
    private boolean disable = false;

    private static File playersFile;
    public static FileConfiguration players;
    public static File songsFolder;

    public static JukeBoxRadio radio = null;

    private static LinkedList<Song> songs;
    private static Map<String, Song> fileNames;
    private static Map<String, Song> internalNames;
    private static Playlist playlist;

    public static int maxPage;
    public static boolean jukeboxClick = false;
    public static boolean sendMessages = true;
    public static boolean async = false;
    public static boolean autoJoin = false;
    public static boolean radioEnabled = true;
    public static boolean radioOnJoin = false;
    public static boolean autoReload = true;
    public static boolean preventVanillaMusic = false;
    public static String songOnJoinName;
    public static Song songOnJoin;
    public static PlayerData defaultPlayer = null;
    public static List<String> worldsEnabled;
    public static boolean worlds;
    public static boolean particles;
    public static boolean actionBar;
    public static List<Material> songItems;
    public static String itemFormat;
    public static String itemFormatWithoutAuthor;
    public static String itemFormatAdmin;
    public static String itemFormatAdminWithoutAuthor;
    public static String songFormat;
    public static String songFormatWithoutAuthor;
    public static String descriptionFormat;
    public static String descriptionFormatWithoutAuthor;
    public static boolean savePlayerDatas = true;
    public static int fadeInDuration, fadeOutDuration;
    public static boolean useExtendedOctaveRange = false;

    public ItemStack jukeboxItem;

    private Database db;
    public JukeBoxDatas datas;

    private BukkitTask vanillaMusicTask = null;
    public Consumer<Player> stopVanillaMusic = null;

    public void onEnable(){
        instance = this;

        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) Placeholders.registerPlaceholders();
        CarbonMC.get().getLogger().info("NotblocjAPI version: " + PaperLoader.getInstance().getPlugin(NoteBlockAPI.class).getDescription().getVersion());

        PaperLoader.getInstance().saveDefaultConfig();
        try {
            ConfigUpdater.update(PaperLoader.getInstance(), "config.yml", new File(PaperLoader.getInstance().getDataFolder(), "config.yml"));
        }catch (IOException e) {
            CarbonMC.get().getLogger().log(Level.WARNING, "Failed to update the configuration file.", e);
        }

        initAll();
    }

    public void onDisable(){
        if (!disable) disableAll();
    }

    public void disableAll(){
        if (radio != null){
            radio.stop();
            radio = null;
        }
        if (datas != null) {
            if (savePlayerDatas && db == null) players.set("players", datas.getSerializedList());
            players.set("item", (jukeboxItem == null) ? null : jukeboxItem.serialize());
            try {
                players.save(playersFile);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (vanillaMusicTask != null) vanillaMusicTask.cancel();
        if (db != null) db.closeConnection();
        HandlerList.unregisterAll((JavaPlugin) PaperLoader.getInstance());
    }

    public void initAll(){
        PaperLoader.getInstance().reloadConfig();

        loadLang();
        if (disable) return;

        FileConfiguration config = PaperLoader.getInstance().getConfig();
        jukeboxClick = config.getBoolean("jukeboxClick");
        sendMessages = config.getBoolean("sendMessages");
        async = config.getBoolean("asyncLoading");
        autoJoin = config.getBoolean("forceJoinMusic");
        songOnJoinName = autoJoin ? config.getString("songOnJoin") : null;
        defaultPlayer = PlayerData.deserialize(config.getConfigurationSection("defaultPlayerOptions").getValues(false), null);
        particles = config.getBoolean("noteParticles") && version >= 9;
        actionBar = config.getBoolean("actionBar") && version >= 9;
        radioEnabled = config.getBoolean("radio");
        radioOnJoin = radioEnabled && config.getBoolean("radioOnJoin");
        autoReload = config.getBoolean("reloadOnJoin");
        preventVanillaMusic = config.getBoolean("preventVanillaMusic") && version >= 13;
        songItems = config.getStringList("songItems").stream().map(Material::matchMaterial).collect(Collectors.toList());
        songItems.removeIf(x -> x == null);
        if (songItems.isEmpty()) {
            String[] materials;
            if (version > 12) {
                materials = new String[] { /*"MUSIC_DISC_11", */"MUSIC_DISC_13", "MUSIC_DISC_BLOCKS", "MUSIC_DISC_CAT", "MUSIC_DISC_CHIRP", "MUSIC_DISC_FAR", "MUSIC_DISC_MALL", "MUSIC_DISC_MELLOHI", "MUSIC_DISC_STAL", "MUSIC_DISC_STRAD", "MUSIC_DISC_WAIT", "MUSIC_DISC_WARD" };
            }else materials = new String[] { "RECORD_10", /*"RECORD_11", */"RECORD_12", "RECORD_3", "RECORD_4", "RECORD_5", "RECORD_6", "RECORD_7", "RECORD_8", "RECORD_9", "GOLD_RECORD", "GREEN_RECORD" };
            songItems = Arrays.stream(materials).map(Material::valueOf).collect(Collectors.toList());
        }
        itemFormat = config.getString("itemFormat");
        itemFormatWithoutAuthor = config.getString("itemFormatWithoutAuthor");
        itemFormatAdmin = config.getString("itemFormatAdmin");
        itemFormatAdminWithoutAuthor = config.getString("itemFormatAdminWithoutAuthor");
        songFormat = config.getString("songFormat");
        songFormatWithoutAuthor = config.getString("songFormatWithoutAuthor");
        descriptionFormat = config.getString("descriptionFormat");
        descriptionFormatWithoutAuthor = config.getString("descriptionFormatWithoutAuthor");
        savePlayerDatas = config.getBoolean("savePlayerDatas");
        fadeInDuration = config.getInt("fadeInDuration");
        fadeOutDuration = config.getInt("fadeOutDuration");
        useExtendedOctaveRange = config.getBoolean("useExtendedOctaveRange");

        worldsEnabled = config.getStringList("enabledWorlds");
        worlds = !worldsEnabled.isEmpty();

        ConfigurationSection dbConfig = config.getConfigurationSection("database");
        if (dbConfig.getBoolean("enabled")) {
            db = new Database(dbConfig);
            if (db.openConnection()) {
                CarbonMC.get().getLogger().info("Connected to database.");
            }else {
                CarbonMC.get().getLogger().info("Failed to connect to database. Now using YAML system.");
                db = null;
            }
        }

        if (async){
            new BukkitRunnable() {
                @Override
                public void run() {
                    loadDatas();
                    finishEnabling();
                }
            }.runTaskAsynchronously(PaperLoader.getInstance());
        }else{
            loadDatas();
            finishEnabling();
        }

        if (preventVanillaMusic) {
            try {
                String nms = "net.minecraft.server";
                String cb = "org.bukkit.craftbukkit";
                Method getHandle = getVersionedClass(cb, "entity.CraftPlayer").getDeclaredMethod("getHandle");
                Field playerConnection = getVersionedClass(nms, "EntityPlayer").getDeclaredField("playerConnection");
                Method sendPacket = getVersionedClass(nms, "PlayerConnection").getDeclaredMethod(version < 18 ? "sendPacket" : "a", getVersionedClass(nms, "Packet"));
                Class<?> soundCategory = getVersionedClass(nms, "SoundCategory");
                Object packet = getVersionedClass(nms, "PacketPlayOutStopSound").getDeclaredConstructor(getVersionedClass(nms, "MinecraftKey"), soundCategory).newInstance(null, soundCategory.getDeclaredField("MUSIC").get(null));

                stopVanillaMusic = player -> {
                    try {
                        sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), packet);
                    }catch (ReflectiveOperationException e1) {
                        e1.printStackTrace();
                    }
                };
            }catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Class<?> getVersionedClass(String packageName, String className) throws ClassNotFoundException {
        return Class.forName(packageName + "." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + className);
    }

    private void finishEnabling(){
        PaperLoader.getInstance().getCommand("adminmusic").setExecutor(new CommandAdmin());

        Bukkit.getServer().getPluginManager().registerEvents(this, PaperLoader.getInstance());

        radioEnabled = radioEnabled && !songs.isEmpty();
        if (radioEnabled){
            radio = new JukeBoxRadio(playlist);
        }else radioOnJoin = false;

        for (Player p : Bukkit.getOnlinePlayers()) {
            datas.joins(p);
        }

        if (stopVanillaMusic != null) {
            vanillaMusicTask = Bukkit.getScheduler().runTaskTimerAsynchronously(PaperLoader.getInstance(), () -> {
                for (PlayerData pdata : datas.getDatas()) {
                    if (pdata.isPlaying() && pdata.getPlayer() != null) stopVanillaMusic.accept(pdata.getPlayer());
                }
            }, 20L, 100l); // every 5 seconds
        }
    }

    private void loadDatas(){
        /* --------------------------------------------- SONGS ------- */
        songs = new LinkedList<>();
        fileNames = new HashMap<>();
        internalNames = new HashMap<>();
        songsFolder = new File(PaperLoader.getInstance().getDataFolder(), "songs");
        if (!songsFolder.exists()) songsFolder.mkdirs();
        for (File file : songsFolder.listFiles()){
            if (file.getName().substring(file.getName().lastIndexOf(".") + 1).equals("nbs")){
                Song song = NBSDecoder.parse(file);
                if (song == null) continue;
                String n = getInternal(song);
                if (internalNames.containsKey(n)) {
                    CarbonMC.get().getLogger().warning("Song \"" + n + "\" is duplicated. Please delete one from the songs directory. File name: " + file.getName());
                    continue;
                }
                fileNames.put(file.getName(), song);
                internalNames.put(n, song);
                if (file.getName().equals(songOnJoinName)) songOnJoin = song;
            }
        }
        CarbonMC.get().getLogger().info(internalNames.size() + " songs loadeds. Sorting by name... ");
        List<String> names = new ArrayList<>(internalNames.keySet());
        Collections.sort(names, Collator.getInstance());
        for (String str : names){
            songs.add(internalNames.get(str));
        }

        setMaxPage();
        CarbonMC.get().getLogger().info("Songs sorted ! " + songs.size() + " songs. Number of pages : " + maxPage);
        if (!songs.isEmpty()) playlist = new Playlist(songs.toArray(new Song[0]));

        /* --------------------------------------------- PLAYERS ------- */
        try {
            playersFile = new File(PaperLoader.getInstance().getDataFolder(), "datas.yml");
            playersFile.createNewFile();
            players = YamlConfiguration.loadConfiguration(playersFile);
            if (players.get("item") != null) jukeboxItem = ItemStack.deserialize(players.getConfigurationSection("item").getValues(false));
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (db == null) {
            datas = new JukeBoxDatas(players.getMapList("players"), internalNames);
        }else {
            try {
                datas = new JukeBoxDatas(db);
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    void setMaxPage(){
        maxPage = (int) StrictMath.ceil(songs.size() * 1.0 / 45);
    }


    private YamlConfiguration loadLang() {
        String s = "en.yml";
        if (PaperLoader.getInstance().getConfig().getString("lang") != null) s = PaperLoader.getInstance().getConfig().getString("lang") + ".yml";
        File lang = new File(PaperLoader.getInstance().getDataFolder(), s);
        if (!lang.exists()) {
            try {
                PaperLoader.getInstance().getDataFolder().mkdir();
                lang.createNewFile();
                InputStream defConfigStream = PaperLoader.getInstance().getResource(s);
                if (defConfigStream != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8));
                    defConfig.save(lang);
                    Lang.loadFromConfig(lang, defConfig);
                    CarbonMC.get().getLogger().info("Created language file " + s);
                    return defConfig;
                }
            } catch(IOException e) {
                e.printStackTrace();
                CarbonMC.get().getLogger().severe("Couldn't create language file.");
                CarbonMC.get().getLogger().severe("This is a fatal error. Now disabling.");
                disable = true;
                PaperLoader.getInstance().setEnabled(false);
                return null;
            }
        }
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        try {
            Lang.saveFile(conf, lang);
        }catch (IOException | ReflectiveOperationException e) {
            CarbonMC.get().getLogger().warning("Failed to save lang.yml.");
            CarbonMC.get().getLogger().warning("Report this stack trace to SkytAsul on SpigotMC.");
            e.printStackTrace();
        }
        Lang.loadFromConfig(lang, conf);
        CarbonMC.get().getLogger().info("Loaded language file " + s);
        return conf;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        datas.joins(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        datas.quits(e.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if (e.getItem() == null) return;
        if (jukeboxItem != null && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)){
            if (e.getItem().equals(jukeboxItem)){
                CommandMusic.open(e.getPlayer());
                e.setCancelled(true);
                return;
            }
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && jukeboxClick){
            if (e.getClickedBlock().getType() == Material.JUKEBOX){
                String disc = e.getItem().getType().name();
                if (disc.contains("RECORD") || disc.contains("MUSIC_DISC_")) {
                    CommandMusic.open(e.getPlayer());
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        if (!worlds) return;
        if (e.getFrom().getWorld() == e.getTo().getWorld()) return;
        if (worldsEnabled.contains(e.getTo().getWorld().getName())) return;
        PlayerData pdata = datas.getDatas(e.getPlayer());
        if (pdata == null) return;
        if (pdata.songPlayer != null) pdata.stopPlaying(true);
        if (pdata.getPlaylistType() == Playlists.RADIO) pdata.setPlaylist(Playlists.PLAYLIST, false);
    }


    public static JukeBox getInstance(){
        return PaperLoader.getInstance().getJukeBox();
    }

    public static boolean canSaveDatas(Player p) {
        return savePlayerDatas && p.hasPermission("music.save-datas");
    }

    private static Random random = new Random();
    public static Song randomSong() {
        if (songs.isEmpty()) return null;
        if (songs.size() == 1) return songs.get(0);
        return songs.get(random.nextInt(songs.size() - 1));
    }

    public static Playlist getPlaylist(){
        return playlist;
    }

    public static List<Song> getSongs(){
        return songs;
    }

    public static Song getSongByFile(String fileName){
        return fileNames.get(fileName);
    }

    public static Song getSongByInternalName(String internalName) {
        return internalNames.get(internalName);
    }

    public static String getInternal(Song s) {
        if (s.getTitle() == null || s.getTitle().isEmpty()) return s.getPath().getName();
        return s.getTitle();
    }

    public static String getItemName(Song s, Player p) {
        boolean admin = p.hasPermission("music.adminItem");
        return format(admin ? itemFormatAdmin : itemFormat, admin ? itemFormatAdminWithoutAuthor : itemFormatWithoutAuthor, s);
    }

    public static String getSongName(Song song) {
        return format(songFormat, songFormatWithoutAuthor, song);
    }

    private static String removeFileExtension(String path) {
        int dot = path.lastIndexOf('.');
        if(dot == -1) return path;
        return path.substring(0, dot);
    }

    public static String format(String base, String noAuthorBase, Song song) {
        String author = song.getAuthor();
        String format = (author == null || author.isEmpty()) ? noAuthorBase : base.replace("{AUTHOR}", author);
        return format
                .replace("{NAME}", song.getTitle().isEmpty() ? removeFileExtension(song.getPath().getName()) : song.getTitle())
                .replace("{ID}", String.valueOf(songs.indexOf(song)))
                .replace("{DESCRIPTION}", song.getDescription());
    }

    public static boolean sendMessage(Player p, String msg){
        if (JukeBox.sendMessages){
            if (JukeBox.actionBar){
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
            }else {
                p.spigot().sendMessage(TextComponent.fromLegacyText(msg));
            }
            return true;
        }
        return false;
    }
}
