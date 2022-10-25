package net.raphap3.jdabot.command.commands;

import net.dv8tion.jda.api.entities.TextChannel;
import net.raphap3.jdabot.CommandManager;
import net.raphap3.jdabot.Config;
import net.raphap3.jdabot.command.CommandContext;
import net.raphap3.jdabot.command.ICommand;

import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();

            builder.append("Lista de comandos\n");

            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (it) -> builder.append('`').append(Config.get("prefix")).append(it).append("`\n")
            );

            channel.sendMessage(builder.toString()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            channel.sendMessage("Nada encontrado para " +
                    search).queue();
            return;
        }

        channel.sendMessage(command.getHelp()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Mostra a lista de comandos do bot\n" +
                "Usagem: `!!help [comando]`";
    }
}
