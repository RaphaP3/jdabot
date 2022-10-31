package net.raphap3.jdabot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.raphap3.jdabot.command.CommandContext;
import net.raphap3.jdabot.command.ICommand;
import net.raphap3.jdabot.command.commands.EventWaiterCommand;
import net.raphap3.jdabot.command.commands.HelpCommand;
import net.raphap3.jdabot.command.commands.PingCommand;
import net.raphap3.jdabot.command.commands.admin.SetPrefixCommand;
import net.raphap3.jdabot.command.commands.music.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager(EventWaiter waiter) {
        addCommand(new PingCommand());
        addCommand(new HelpCommand(this));
        addCommand(new EventWaiterCommand(waiter));

        addCommand(new SetPrefixCommand());

        addCommand(new JoinCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new SkipCommand());
        addCommand(new QueueCommand());
        addCommand(new RepeatCommand());
        addCommand(new LeaveCommand());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound) {
            throw new IllegalArgumentException("Um comando com este nome ja existe");
        }

        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }

        return null;
    }

    void handle(GuildMessageReceivedEvent event, String prefix) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);
            String argsText = String.join(" ", args);

            CommandContext ctx = new CommandContext(event, args, argsText);

            cmd.handle(ctx);
        }
    }

}
