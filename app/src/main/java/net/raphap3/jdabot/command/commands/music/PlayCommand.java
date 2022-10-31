package net.raphap3.jdabot.command.commands.music;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import net.raphap3.jdabot.command.CommandContext;
import net.raphap3.jdabot.command.ICommand;
import net.raphap3.jdabot.lavaplayer.PlayerManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class PlayCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if (args.isEmpty()) {
            channel.sendMessage("A usagem correta seria `!!play <link>`").queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState voiceState = selfMember.getVoiceState();
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        String link = String.join(" ", args);

        if (!voiceState.inVoiceChannel()) {
            if (!selfMember.hasPermission(memberChannel, Permission.VOICE_CONNECT)) {
                channel.sendMessage("Eu não tenho permissão de entrar na sua call :(").queue();
                return;
            }

            audioManager.openAudioConnection(memberChannel);

            if (!isUrl(link)) {
                channel.sendMessage("Por favor mande um link").queue();
            }

            PlayerManager.getInstance()
                    .loadAndPlay(channel, link);
            return;
        }


        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("Você precisa estar em uma call para este comando funcionar").queue();
            return;
        }

        if (!memberChannel.equals(voiceState.getChannel())) {
            channel.sendMessage("Você precisa estar na mesma call que eu pra isso funcionar").queue();
            return;
        }

        if (!isUrl(link)) {
            channel.sendMessage("Por favor mande um link").queue();
        }

        PlayerManager.getInstance()
                .loadAndPlay(channel, link);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return """
                Toca uma música
                Usagem: `!!play <link>`
                Aliases: `p`""";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("p");
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
