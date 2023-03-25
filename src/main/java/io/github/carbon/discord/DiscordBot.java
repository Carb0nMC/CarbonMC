package io.github.carbon.discord;

import io.github.carbon.carbonmc.command.CommandManager;
import io.github.carbon.discord.command.DCommandManager;
import io.github.carbon.discord.event.DEventlistener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class DiscordBot {
    private static DiscordBot instance;
    private JDA jda;
    private String prefix;
    private DCommandManager commandManager;

    public DiscordBot(String token, String prefix) {
        instance = this;
        this.prefix = prefix;
        this.commandManager = new DCommandManager();

        try{
            JDABuilder builder = JDABuilder.createDefault(token);

            for (GatewayIntent intent : GatewayIntent.values()) {
                builder.enableIntents(intent);
            }

            builder.addEventListeners(new DEventlistener());
            builder.setActivity(Activity.playing("play.carbonmc.net"));
            builder.setStatus(OnlineStatus.ONLINE);
            this.jda = builder.build();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public DCommandManager getCommandManager() {
        return commandManager;
    }

    public static DiscordBot getInstance(){
        return instance;
    }
}
