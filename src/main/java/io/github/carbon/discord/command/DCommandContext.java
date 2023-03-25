package io.github.carbon.discord.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class DCommandContext {
    private MessageReceivedEvent event;

    public DCommandContext(MessageReceivedEvent event){
        this.event = event;
    }

    public MessageReceivedEvent getEvent(){
        return event;
    }

    public void reply(String message){
        event.getChannel().sendMessage(message).queue();
    }

    public void reply(String message, boolean delete){
        if(delete){
            event.getChannel().sendMessage(message).queue(message1 -> message1.delete().queueAfter(5, TimeUnit.SECONDS));
        } else {
            event.getChannel().sendMessage(message).queue();
        }
    }

    public void reply(String message, boolean delete, int time){
        if(delete){
            event.getChannel().sendMessage(message).queue(message1 -> message1.delete().queueAfter(time, TimeUnit.SECONDS));
        } else {
            event.getChannel().sendMessage(message).queue();
        }
    }

    public void reply(String message, boolean delete, int time, TimeUnit unit){
        if(delete){
            event.getChannel().sendMessage(message).queue(message1 -> message1.delete().queueAfter(time, unit));
        } else {
            event.getChannel().sendMessage(message).queue();
        }
    }

    public MessageChannelUnion getChannel(){
        return event.getChannel();
    }

    public Message getMessage(){
        return event.getMessage();
    }

    public String getPrefix(){
        return event.getMessage().getContentRaw().split(" ")[0];
    }

    public String[] getArgs(){
        //Get the args without the prefix
        String[] args = event.getMessage().getContentRaw().split(" ");
        String[] argsWithoutPrefix = Arrays.copyOfRange(args, 1, args.length);
        return argsWithoutPrefix;
    }

    public void replyEmbed(String title, String description){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(description);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }

    public void replyEmbed(String title, String description, boolean delete){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(description);
        if(delete){
            event.getChannel().sendMessageEmbeds(builder.build()).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
        } else {
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
        }
    }

    public void replyEmbed(String title, String description, boolean delete, int time){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(description);
        if(delete){
            event.getChannel().sendMessageEmbeds(builder.build()).queue(message -> message.delete().queueAfter(time, TimeUnit.SECONDS));
        } else {
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
        }
    }

    public void replyEmbed(String title, String description, boolean delete, int time, TimeUnit unit){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(description);
        if(delete){
            event.getChannel().sendMessageEmbeds(builder.build()).queue(message -> message.delete().queueAfter(time, unit));
        } else {
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
        }
    }

    public void replyEmbed(String title, String description, Color color){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(description);
        builder.setColor(color);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }

    public void replyEmbed(String title, String description, Color color, boolean delete){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(description);
        builder.setColor(color);
        if(delete){
            event.getChannel().sendMessageEmbeds(builder.build()).queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
        } else {
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
        }
    }

    public void replyEmbed(String title, String description, Color color, boolean delete, int time){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(description);
        builder.setColor(color);
        if(delete){
            event.getChannel().sendMessageEmbeds(builder.build()).queue(message -> message.delete().queueAfter(time, TimeUnit.SECONDS));
        } else {
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
        }
    }

public void replyEmbed(String title, String description, Color color, boolean delete, int time, TimeUnit unit){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setDescription(description);
        builder.setColor(color);
        if(delete){
            event.getChannel().sendMessageEmbeds(builder.build()).queue(message -> message.delete().queueAfter(time, unit));
        } else {
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
        }
    }
}