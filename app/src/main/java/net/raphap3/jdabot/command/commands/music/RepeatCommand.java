package net.raphap3.jdabot.command.commands.music;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.raphap3.jdabot.command.CommandContext;
import net.raphap3.jdabot.command.ICommand;
import net.raphap3.jdabot.lavaplayer.GuildMusicManager;
import net.raphap3.jdabot.lavaplayer.PlayerManager;

import java.util.Collections;
import java.util.List;

public class RepeatCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState voiceState = selfMember.getVoiceState();

        if (!voiceState.inVoiceChannel()) {
            channel.sendMessage("Eu preciso estar em uma call para isto funcionar").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("Você precisa estar em uma call para este comando funcionar").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(voiceState.getChannel())) {
            channel.sendMessage("Você precisa estar na mesma call que eu pra isso funcionar").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final boolean newRepeating = !musicManager.scheduler.repeating;

        musicManager.scheduler.repeating = newRepeating;

        channel.sendMessageFormat("O player foi setado para **%s**", newRepeating ? "repetindo" : "não repetindo").queue();
    }

    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getHelp() {
        return "Coloca a música atual em loop\n" +
                "Aliases: `loop`";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("loop");
    }
}
