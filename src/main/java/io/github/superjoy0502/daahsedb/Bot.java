package io.github.superjoy0502.daahsedb;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot {

    public static void main(String[] arguments) throws Exception {

        Listener listener = new Listener();
        Verification verification = listener.verification;
        long guildId = 902691576105553961L;

        JDA api = JDABuilder.createDefault(System.getenv("DAAHSEDBCKey"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(listener)
                .addEventListeners(verification)
                .build()
                .awaitReady();

        api.getPresence().setActivity(Activity.playing("with Joy (Canary)"));

        PartyMaker partyMaker = new PartyMaker(api, api.getGuildById(guildId));

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {

            System.out.println("Currently used memory: "
                    + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576) + "MB");

        };
        executorService.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);

    }

}
