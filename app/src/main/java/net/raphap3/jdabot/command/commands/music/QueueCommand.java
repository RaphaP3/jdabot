package net.raphap3.jdabot.command.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.raphap3.jdabot.command.CommandContext;
import net.raphap3.jdabot.command.ICommand;
import net.raphap3.jdabot.lavaplayer.GuildMusicManager;
import net.raphap3.jdabot.lavaplayer.PlayerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueCommand implements ICommand {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        if (queue.isEmpty()) {
            channel.sendMessage("A fila atual está vazia").queue();
            return;
        }

        final int trackCount = Math.min(queue.size(), 20);
        final List<AudioTrack> trackList = new ArrayList<>(queue);
        final MessageAction messageAction = channel.sendMessage("**Fila Atual:**\n");

        for (int i = 0; i < trackCount; i++) {
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();

            messageAction.append('#')
                    .append(String.valueOf(i + 1))
                    .append(" `")
                    .append(info.title)
                    .append(" - ")
                    .append(info.author)
                    .append("` [`")
                    .append(formatTime(track.getDuration()))
                    .append("`]\n");
        }

        if (trackList.size() > trackCount) {
            messageAction.append("E `")
                    .append(String.valueOf(trackList.size() - trackCount))
                    .append("` mais...");
        }

        messageAction.queue();
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return "Mostra a fila atual do bot\n" +
                "Aliases: `q`";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("q");
    }
}
