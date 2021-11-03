package io.github.superjoy0502.daahsedb;

import io.github.superjoy0502.daahsedb.verification.Verification;
import io.github.superjoy0502.daahsedb.verification.VerificationListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Bot {

    public static void main(String[] arguments) throws Exception {

        boolean isCanary = true;
        long startTime = System.nanoTime();

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

        api.getPresence().setActivity(Activity.watching("Youtube (version Canary)"));

        EmbedBuilder esb = new EmbedBuilder();
        esb.setTitle(":pushpin: DAAHS Esports Discord Server Rules v0.2.1");
        esb.setColor(new Color(12, 60, 105));
        esb.setDescription("• The following rules define the rules of DAAHS Esports Discord Server.\n" +
                "• These rules must be followed strictly, or else penalties may apply.\n" +
                "• Please change your nickname to your real name.\n" +
                "• The maximum rating permitted in messages is **G**.");

        api.getTextChannelById(902691576105553964L).sendMessage(
                esb.build()
        ).queue();

        EmbedBuilder esb2 = new EmbedBuilder();
        esb2.setTitle("Discord Official Guideline", "https://discord.com/guidelines");
        esb2.setColor(new Color(12, 60, 105));
        esb2.setDescription("• The official guideline of Discord has to be followed in this server.");

        api.getTextChannelById(902691576105553964L).sendMessage(
                esb2.build()
        ).queue();

        EmbedBuilder esb3 = new EmbedBuilder();
        esb3.setTitle("Respect each others");
        esb3.setColor(new Color(12, 60, 105));
        esb3.setDescription("• In a community, every person should be able to **enjoy** the space.\n" +
                "• Discrimination towards others is prohibited.\n" +
                "• Mind your manners.\n" +
                "• Do not swear.");

        api.getTextChannelById(902691576105553964L).sendMessage(
                esb3.build()
        ).queue();

        EmbedBuilder esb4 = new EmbedBuilder();
        esb4.setTitle("This is an Esports server");
        esb4.setColor(new Color(12, 60, 105));
        esb4.setDescription("• Esports is not just about gaming, but also sportsmanship.\n" +
                "• Just like playing a fair sports game, you should treat people fair and honorable.\n" +
                "• Know what you should do and what you should not do.\n" +
                "• Do not perform immature acts such as spamming, mentioning everybody, etc.");

        api.getTextChannelById(902691576105553964L).sendMessage(
                esb4.build()
        ).queue();

        User bot = api.getSelfUser();

        EmbedBuilder esbb = new EmbedBuilder();
        esbb.setTitle("DAAHS Esports Discord Bot");
        esbb.setColor(new Color(12, 60, 105));
        esbb.setDescription("• " + bot.getAsMention() + " is a bot made by DAAHS Esports leaders.\n" +
                "• This bot has many functions, and one of them is to verify newcomers.\n" +
                "• When you join the server, I will DM you to help you verify yourself as a DAAHS member.\n" +
                "• But, when I'm offline and cannot help you, please use " + api.getTextChannelById(903689843438153779L).getAsMention() + ".\n" +
                "• <@904288301110919168> is my replica, only used by the developers to test out new functions that will be added to me.");

        api.getTextChannelById(902691576105553964L).sendMessage(
                esbb.build()
        ).queue();

        EmbedBuilder esb5 = new EmbedBuilder();
        esb5.setTitle("Lastly");
        esb5.setColor(new Color(12, 60, 105));
        esb5.setDescription("• Have fun.");

        api.getTextChannelById(902691576105553964L).sendMessage(
                esb5.build()
        ).queue();


//        api.getTextChannelById(905275922075234376L).sendMessage(".").queue();

        PartyMaker partyMaker = new PartyMaker(api, api.getGuildById(guildId));


        updateStatusOnline(isCanary, startTime, api, bot);

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                updateStatusOffline(isCanary, startTime, api, bot);
            }

        });

    }

    private static void updateStatusOnline(boolean isCanary, long startTime, JDA api, User bot) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {

            api.getTextChannelById(905275922075234376L).retrieveMessageById(905290567846592562L).queue(
                    message -> {

                        long endTime   = System.nanoTime();
                        long totalTime = endTime - startTime;
                        int hours = Math.toIntExact(TimeUnit.NANOSECONDS.toHours(totalTime));
                        int minutes = Math.toIntExact(TimeUnit.NANOSECONDS.toMinutes(totalTime) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.NANOSECONDS.toHours(totalTime)));

                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(bot.getName(), null, bot.getEffectiveAvatarUrl());
                        eb.setTitle(":green_circle: Online");
                        eb.setColor(!isCanary ? new Color(12, 60, 105) : new Color(252, 164, 28));
                        eb.addField(
                                "Uptime",
                                String.format(
                                        "%s hours %s minutes",
                                        hours,
                                        minutes
                                ),
                                true
                        );
                        eb.addField(
                                "Used Memory",
                                String.format(
                                        "%s / %s",
                                        ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576) + "MB",
                                        (Runtime.getRuntime().maxMemory() / 1048576) + "MB"
                                ),
                                true
                        );
                        eb.setFooter(bot.getName());
                        eb.setTimestamp(Instant.now());

                        message.editMessage(
                                " "
                        ).queue();

                        message.editMessage(
                                eb.build()
                        ).queue();

                    }
            );

        };
        executorService.scheduleWithFixedDelay(task, 0, 1, TimeUnit.MINUTES);
    }

    private static void updateStatusOffline(boolean isCanary, long startTime, JDA api, User bot) {

        api.getTextChannelById(905275922075234376L).retrieveMessageById(905290567846592562L).queue(
                message -> {

                    long endTime   = System.nanoTime();
                    long totalTime = endTime - startTime;
                    int hours = Math.toIntExact(TimeUnit.NANOSECONDS.toHours(totalTime));
                    int minutes = Math.toIntExact(TimeUnit.NANOSECONDS.toMinutes(totalTime) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.NANOSECONDS.toHours(totalTime)));

                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(bot.getName(), null, bot.getEffectiveAvatarUrl());
                    eb.setTitle(":red_circle: Offline");
                    eb.setColor(!isCanary ? new Color(12, 60, 105) : new Color(252, 164, 28));
                    eb.addField(
                            "Last Uptime",
                            String.format(
                                    "%s hours %s minutes",
                                    hours,
                                    minutes
                            ),
                            true
                    );

                    LocalDateTime myDateObj = LocalDateTime.now();
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E., MMM. dd\nHH:mm:ss");
                    String formattedDate = myDateObj.format(myFormatObj);

                    eb.addField(
                            "Offline Since",
                            formattedDate.toString(),
                            true
                    );
                    eb.setFooter(bot.getName());
                    eb.setTimestamp(Instant.now());

                    message.editMessage(
                            " "
                    ).queue();

                    message.editMessage(
                            eb.build()
                    ).queue();

                }
        );

    }

}
