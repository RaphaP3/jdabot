package net.raphap3.jdabot.command.commands;

import net.dv8tion.jda.api.JDA;
import net.raphap3.jdabot.command.CommandContext;
import net.raphap3.jdabot.command.ICommand;

public class PingCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                        .sendMessageFormat("REST Ping: `%sms`\nWS Ping: `%sms`", ping, jda.getGatewayPing()).queue()
        );
    }

    @Override
    public String getName() {
        return "ping";
    }
}
