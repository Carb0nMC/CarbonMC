package io.github.carbon.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class DiscordBot {
    private JDA jda;
    private String token;
    private String prefix;

    public DiscordBot(String token, String prefix) {
        this.token = token;
        this.prefix = prefix;

        try{
            JDABuilder builder = JDABuilder.createDefault(token);

            builder.setActivity(Activity.playing("play.carbonmc.net"));
            builder.setStatus(OnlineStatus.ONLINE);
            this.jda = builder.build();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
