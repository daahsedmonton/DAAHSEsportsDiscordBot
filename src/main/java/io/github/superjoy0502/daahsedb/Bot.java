package io.github.superjoy0502.daahsedb;

import io.github.superjoy0502.daahsedb.verification.Verification;
import io.github.superjoy0502.daahsedb.verification.VerificationListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot {

    public static void main(String[] arguments) throws Exception {

        boolean isCanary = false;

        VerificationListener verificationListener = new VerificationListener(isCanary);
        Verification verification = verificationListener.verification;
        long guildId = 902691576105553961L;

        JDA api = JDABuilder.createDefault(System.getenv("DAAHSEDBKey"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(verificationListener)
                .addEventListeners(verification)
                .build()
                .awaitReady();

        api.getPresence().setActivity(Activity.watching("you (version 0.1.2a)"));

//        PartyMaker partyMaker = new PartyMaker(api, api.getGuildById(guildId));

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {

            System.out.println("Currently used memory: "
                    + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576) + "MB");

        };
        executorService.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);

    }

}
