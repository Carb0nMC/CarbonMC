package io.github.carbon.discord.command;

import io.github.carbon.discord.DiscordBot;
import io.github.carbon.discord.command.impl.ClearCommand;
import io.github.carbon.discord.command.impl.PingCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DCommandManager {
    private ArrayList<DiscordCommand> commands;

    public DCommandManager(){
        this.commands = new ArrayList<>();

        addCommand(new PingCommand());
        addCommand(new ClearCommand());
    }

    public void addCommand(DiscordCommand command){
        for(DiscordCommand cmd : commands){
            if(cmd.getName().equalsIgnoreCase(command.getName())){
                throw new IllegalStateException("Command with name " + command.getName() + " already exists!");
            }
        }

        commands.add(command);
    }

    public void handle(MessageReceivedEvent event){
        String message = event.getMessage().getContentRaw();
        String prefix = DiscordBot.getInstance().getPrefix();

        boolean isCommand = message.startsWith(prefix);
        if(!isCommand) return;

        String[] args = message.substring(prefix.length()).split(" ");
        String commandName = args[0];
        DiscordCommand command = getByName(commandName);

        if(command == null){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.RED);
            builder.setTitle("Command nicht gefunden");
            builder.setDescription("Der Command wurde leider nicht gefunden, versuche " + prefix + "help für mehr Hilfe.");

            event.getMessage().delete().queue();
            event.getChannel().sendMessageEmbeds(builder.build()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
            return;
        }

        DCommandContext context = new DCommandContext(event);
        command.execute(context);
    }

    public DiscordCommand getByName(String name){
        for(DiscordCommand command : commands){
            if(command.getName().equalsIgnoreCase(name)){
                return command;
            }
        }
        return null;
    }


}
