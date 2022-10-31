package net.raphap3.jdabot.command.commands.music;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
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

        if (ctx.getArgs().isEmpty()) {
            channel.sendMessage("A usagem correta seria `!!play <link>`").queue();
            return;
        }

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

        String link = String.join(" ", ctx.getArgs());

        if (!isUrl(link)) {
            link = "ytsearch:" + link;
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

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
