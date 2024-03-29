package net.raphap3.jdabot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import java.util.Timer;

public class Bot {

    private Bot() throws LoginException {
        EventWaiter waiter = new EventWaiter();
        Timer timer = new Timer();

        JDABuilder.createDefault(
                Config.get("token"),
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_VOICE_STATES
        )
                .disableCache(EnumSet.of(
                        CacheFlag.CLIENT_STATUS,
                        CacheFlag.ACTIVITY,
                        CacheFlag.EMOTE
                ))
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(new Listener(waiter, timer), waiter)
                .setActivity(Activity.listening("!!help"))
                .build();
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}