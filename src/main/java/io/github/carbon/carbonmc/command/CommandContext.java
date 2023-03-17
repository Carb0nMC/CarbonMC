package io.github.carbon.carbonmc.command;

public class CommandContext {
    private final ICommandSender ICommandSender;
    private final String[] args;
    private final String label;

    public CommandContext(ICommandSender ICommandSender, String[] args, String label) {
        this.ICommandSender = ICommandSender;
        this.args = args;
        this.label = label;
    }

    public ICommandSender getCommandSender() {
        return ICommandSender;
    }

    public String[] getArgs() {
        return args;
    }

    public String getLabel() {
        return label;
    }
}
