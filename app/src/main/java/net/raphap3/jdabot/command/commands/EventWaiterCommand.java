package net.raphap3.jdabot.command.commands;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.raphap3.jdabot.command.CommandContext;
import net.raphap3.jdabot.command.ICommand;

import java.util.concurrent.TimeUnit;

public class EventWaiterCommand implements ICommand {
    private static final String EMOTE = "👌";
    private final EventWaiter waiter;

    public EventWaiterCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final String argsText = ctx.getArgsText();

        if (argsText.isEmpty()) {
            channel.sendMessage("Faltando argumentos").queue();
            return;
        }

        channel.sendMessage("Reaja com ")
                .append(EMOTE)
                .appendFormat(" para %s", argsText)
                .queue((message) -> {
                    message.addReaction(EMOTE).queue();

                    this.waiter.waitForEvent(
                            GuildMessageReactionAddEvent.class,
                            (e) -> e.getMessageIdLong() == message.getIdLong() && !e.getUser().isBot(),
                            (e) -> channel.sendMessageFormat("%#s foi o primeiro a reagir!", e.getUser()).queue(),
                            10L, TimeUnit.SECONDS,
                            () -> channel.sendMessage("Você esperou por tempo demais").queue()
                    );
                });
    }

    @Override
    public String getName() {
        return "eventwaiter";
    }

    @Override
    public String getHelp() {
        return "Cria uma mensagem que anuncia o primeiro a reagir";
    }
}
