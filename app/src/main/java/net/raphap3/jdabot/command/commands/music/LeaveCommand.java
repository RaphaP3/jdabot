package net.raphap3.jdabot.command.commands.music;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.raphap3.jdabot.command.CommandContext;
import net.raphap3.jdabot.command.ICommand;
import net.raphap3.jdabot.lavaplayer.GuildMusicManager;
import net.raphap3.jdabot.lavaplayer.PlayerManager;

import java.util.Collections;
import java.util.List;

public class LeaveCommand implements ICommand {
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

        final Guild guild = ctx.getGuild();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        final AudioManager audioManager = guild.getAudioManager();

        musicManager.scheduler.repeating = false;
        musicManager.scheduler.queue.clear();
        musicManager.audioPlayer.stopTrack();

        audioManager.closeAudioConnection();

        channel.sendMessage("Eu saí da call").queue();
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Desconecta o bot da call\n" +
                "Aliases: `dc`";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("dc");
    }
}
