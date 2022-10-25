package net.raphap3.jdabot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Bot {

    private Bot() throws LoginException {
        JDABuilder.createDefault(
                Config.get("token")
        )
                .addEventListeners(new Listener())
                .setActivity(Activity.listening("pora jaum :v"))
                .build();

    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }

}
