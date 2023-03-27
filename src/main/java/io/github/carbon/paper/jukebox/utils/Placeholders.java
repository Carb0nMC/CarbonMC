package io.github.carbon.paper.jukebox.utils;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import io.github.carbon.carbonmc.CarbonMC;
import io.github.carbon.paper.jukebox.JukeBox;
import io.github.carbon.paper.jukebox.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.List;

public class Placeholders extends PlaceholderExpansion {

    public static void registerPlaceholders(){
        new Placeholders().register();
        CarbonMC.get().getLogger().info("Placeholders registered");
    }

    private Placeholders() {}

    @Override
    public String getAuthor() {
        return CarbonMC.AUTHOR;
    }

    @Override
    public String getIdentifier() {
        return "jukebox";
    }

    @Override
    public String getVersion() {
        return CarbonMC.VERSION;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public List<String> getPlaceholders() {
        return Arrays.asList("playeroptions_volume", "playeroptions_shuffle", "playeroptions_join", "playeroptions_particles", "playeroptions_loop", "active", "active_title", "active_author", "active_original_author", "active_description", "playlist");
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        PlayerData pdata = JukeBox.getInstance().datas.getDatas(p.getUniqueId());
        if (pdata == null) return "§c§lunknown player data";
        if (params.startsWith("playeroptions_")) {
            switch (params.substring(params.indexOf("_") + 1)) {
                case "volume":
                    return pdata.getVolume() + "%";
                case "shuffle":
                    return pdata.isShuffle() ? Lang.ENABLED : Lang.DISABLED;
                case "join":
                    return pdata.hasJoinMusic() ? Lang.ENABLED : Lang.DISABLED;
                case "particles":
                    return pdata.hasParticles() ? Lang.ENABLED : Lang.DISABLED;
                case "loop":
                    return pdata.isRepeatEnabled() ? Lang.ENABLED : Lang.DISABLED;
                default:
                    return "§c§lunknown option";
            }
        }else if (params.startsWith("active")) {
            Song song = pdata.getListeningTo();
            if (song == null) return Lang.NONE;
            if (params.equals("active_title")) return song.getTitle();
            if (params.equals("active_author")) return song.getAuthor();
            if (params.equals("active_original_author")) return song.getOriginalAuthor();
            if (params.equals("active_description")) return song.getDescription();
            return JukeBox.getSongName(song);
        }else if (params.equals("playlist")) {
            return pdata.getPlaylistType().name;
        }
        return null;
    }

}
