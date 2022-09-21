package net.raphap3.jdabot.command;

import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    default List<String> getAliases() {
        return List.of(); // if Java Version =< 8 use Arrays.asList
    }
}
