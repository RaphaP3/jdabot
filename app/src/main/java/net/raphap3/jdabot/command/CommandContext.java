package net.raphap3.jdabot.command;

import me.duncte123.botcommons.commands.ICommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class CommandContext implements ICommandContext {
    private final GuildMessageReceivedEvent event;
    private final List<String> args;
    private final String argsText;

    public CommandContext(GuildMessageReceivedEvent event, List<String> args, String argsText) {
        this.event = event;
        this.args = args;
        this.argsText = argsText;
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    public List<String> getArgs() {
        return args;
    }

    public String getArgsText() {
        return argsText;
    }
}
