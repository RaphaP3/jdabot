package net.raphap3.jdabot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.raphap3.jdabot.database.DatabaseManager;
import net.raphap3.jdabot.lavaplayer.GuildMusicManager;
import net.raphap3.jdabot.lavaplayer.PlayerManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager;
    private final Timer timer;

    public Listener(EventWaiter waiter, Timer timer) {
        manager = new CommandManager(waiter);
        this.timer = timer;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} esta pronto.", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        final long guildId = event.getGuild().getIdLong();
        String prefix = EstaMerdaAquiBixo.PREFIXES.computeIfAbsent(guildId, DatabaseManager.INSTANCE::getPrefix);
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "shutdown")
                && user.getId().equals(Config.get("owner_id"))) {
            LOGGER.info("Desligando");
            timer.cancel();
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());


            return;
        }

        if (raw.startsWith(prefix)) {
            manager.handle(event, prefix);
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        VoiceChannel channel = event.getChannelLeft();
        Guild guild = channel.getGuild();
        AudioManager audioManager = guild.getAudioManager();
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

        timer.schedule(new DisconnectTask(channel, audioManager, musicManager), 5000);
    }

    private static class DisconnectTask extends TimerTask {
        private final VoiceChannel channel;
        private final AudioManager audioManager;
        private final GuildMusicManager musicManager;

        public DisconnectTask(VoiceChannel channel, AudioManager audioManager, GuildMusicManager musicManager) {
            this.channel = channel;
            this.audioManager = audioManager;
            this.musicManager = musicManager;
        }

        @Override
        public void run() {
            if (channel.getMembers().stream().allMatch(member -> member.getUser().isBot())) {
                musicManager.scheduler.repeating = false;
                musicManager.scheduler.queue.clear();
                musicManager.audioPlayer.stopTrack();

                audioManager.closeAudioConnection();
            }
        }
    }
}
