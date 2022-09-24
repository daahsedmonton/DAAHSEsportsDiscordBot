package io.github.superjoy0502.daahsedb;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UpdateStatus extends ListenerAdapter {

    static boolean isCanary;
    static long startTime;
    static JDA api;
    static User bot;
    static long msgId;
    static String offlineMsg;

    static void setVariables(boolean isCanary, long startTime, JDA api, User bot) {

        UpdateStatus.isCanary = isCanary;
        UpdateStatus.startTime = startTime;
        UpdateStatus.api = api;
        UpdateStatus.bot = bot;

        msgId = isCanary ? 905290567846592562L : 905697791773401088L;
        offlineMsg = isCanary ? "haha canary bot go brrrrr" :
                "Offline";

    }

    static void updateStatusOnline() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {

            api.getTextChannelById(905275922075234376L).retrieveMessageById(msgId).queue(
                    message -> {

                        long endTime = System.nanoTime();
                        long totalTime = endTime - startTime;
                        int hours = Math.toIntExact(TimeUnit.NANOSECONDS.toHours(totalTime));
                        int minutes = Math.toIntExact(TimeUnit.NANOSECONDS.toMinutes(totalTime) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.NANOSECONDS.toHours(totalTime)));

                        EmbedBuilder eb = new EmbedBuilder()
                                .setAuthor(bot.getName(), null, bot.getEffectiveAvatarUrl())
                                .setTitle(":green_circle: Online")
                                .setColor(!isCanary ? new Color(12, 60, 105) : new Color(252, 164, 28))
                                .addField(
                                        "Uptime",
                                        String.format(
                                                "%s hours %s minutes",
                                                hours,
                                                minutes
                                        ),
                                        true
                                )
                                .addField(
                                        "Memory Used",
                                        String.format(
                                                "%s / %s",
                                                ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576) + "MB",
                                                (Runtime.getRuntime().maxMemory() / 1048576) + "MB"
                                        ),
                                        true
                                )
                                .setFooter(bot.getName())
                                .setTimestamp(Instant.now());

                        message.editMessage(
                                " "
                        ).flatMap(message1 -> message1.editMessageEmbeds(eb.build())).queue();

                    }
            );

        };
        executorService.scheduleWithFixedDelay(task, 0, 1, TimeUnit.MINUTES);
    }

    static void updateStatusOffline() {

        api.getTextChannelById(905275922075234376L).retrieveMessageById(msgId).queue(
                message -> {

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(bot.getName(), null, bot.getEffectiveAvatarUrl());
                    eb.setTitle(":red_circle: Offline");
                    eb.setDescription(offlineMsg);
                    eb.setColor(!isCanary ? new Color(12, 60, 105) : new Color(252, 164, 28));
                    eb.setFooter(bot.getName());
                    eb.setTimestamp(Instant.now());

                    message.editMessage(
                            " "
                    ).queue();

                    message.editMessageEmbeds(
                            eb.build()
                    ).queue();

                    api.shutdown();
                    try {
                        TimeUnit.SECONDS.sleep(10);
                        Runtime.getRuntime().exit(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
        );

    }

}
