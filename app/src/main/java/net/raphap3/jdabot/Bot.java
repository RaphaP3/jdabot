package net.raphap3.jdabot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.raphap3.jdabot.database.SQLiteDataSource;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class Bot {

    private Bot() throws LoginException, SQLException {
        SQLiteDataSource.getConnection();

        JDABuilder.createDefault(
                Config.get("token")
        )
                .addEventListeners(new Listener())
                .setActivity(Activity.listening("pora jaum :v"))
                .build();

    }

    public static void main(String[] args) throws LoginException, SQLException {
        new Bot();
    }

}
