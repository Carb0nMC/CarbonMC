package io.github.carbon.paper.jukebox.utils;

import com.xxmicloxx.NoteBlockAPI.event.SongNextEvent;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import io.github.carbon.carbonmc.plugin.PaperLoader;
import io.github.carbon.paper.jukebox.JukeBox;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;


public class JukeBoxRadio implements Listener {

    private final CustomSongPlayer songPlayer;

    public JukeBoxRadio(Playlist songs){
        Bukkit.getPluginManager().registerEvents(this, PaperLoader.getInstance());
        songPlayer = new CustomSongPlayer(songs);
        songPlayer.playNextSong();
        songPlayer.setRandom(true);
        songPlayer.setAutoDestroy(false);
        songPlayer.setRepeatMode(RepeatMode.ALL);
        songPlayer.setPlaying(true);
    }

    @EventHandler
    public void onSongNext(SongNextEvent e){
        if (e.getSongPlayer() == songPlayer) {
            for (UUID id : songPlayer.getPlayerUUIDs()){
                Player p = Bukkit.getPlayer(id);
                if (p != null) JukeBox.sendMessage(p, Lang.MUSIC_PLAYING + " " + JukeBox.getSongName(e.getSongPlayer().getSong()));
            }
        }
    }

    public Song getSong() {
        return songPlayer.getSong();
    }

    public void join(Player p){
        songPlayer.addPlayer(p);
        JukeBox.sendMessage(p, Lang.MUSIC_PLAYING + " " + JukeBox.getSongName(songPlayer.getSong()));
    }

    public void leave(Player p){
        songPlayer.removePlayer(p);
    }

    public boolean isListening(Player p) {
        return songPlayer.getPlayerUUIDs().contains(p.getUniqueId());
    }

    public void stop(){
        for (UUID id : songPlayer.getPlayerUUIDs()){
            Player p = Bukkit.getPlayer(id);
            if (p != null) JukeBox.sendMessage(p, Lang.MUSIC_STOPPED);
        }
        Player player = null;
        songPlayer.destroy();
    }

}
