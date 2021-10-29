package io.github.superjoy0502.daahsedb;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot {

    public static void main(String[] arguments) throws Exception {

        Listener listener = new Listener();
        Verification verification = listener.verification;

        JDA api = JDABuilder.createDefault(System.getenv("DAAHSEDBKey"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(listener)
                .addEventListeners(verification)
                .build()
                .awaitReady();

    }

}
