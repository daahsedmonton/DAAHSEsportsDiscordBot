package io.github.superjoy0502.daahsedb;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.util.Map;

public class Listener extends ListenerAdapter {

    boolean isCanary;

    final Long channelId = 903477388992725004L;
    final Long verifiedRole = 903098761167929394L;
    final Long helpTeacher = 59744566466580480L;
    TextChannel verificationChannel;
    JDA api;

    public Listener(boolean isCanary) {

        this.isCanary = isCanary;

    }

    public Verification verification = new Verification(isCanary);

    // Canary OFF
    @Override
    public void onReady(@NotNull ReadyEvent event) {

        if (isCanary) return;

        System.out.println("Bot Ready!");

        api = event.getJDA();

        verificationChannel = event.getJDA().getTextChannelById(channelId);
        verificationChannel.sendMessage("Bot is up!").queue();

    }

    // Canary OFF
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        if (isCanary) return;

        verification.sendUserDirectWelcomeMessage(event.getUser(), api);

    }

    // Canary OFF
    @Override
    public void onGuildMessageReactionAdd(@NotNull GuildMessageReactionAddEvent event) {

        if (isCanary) return;

        if (event.getUser().isBot()) return;

        long eventMessageId = event.getMessageIdLong();
        String reactionCodepoints = event.getReactionEmote().getAsCodepoints();

        if (verification.verifyMessageIdMap.containsValue(eventMessageId)) {

            if (verification.verifyMessageCheckedMap.get(eventMessageId)) return;

            User user = event.getUser();
            long verificationUserId = getKey(verification.verifyMessageIdMap, eventMessageId);

            if (reactionCodepoints.equalsIgnoreCase(verification.checkmark)) {

                verificationChannel.retrieveMessageById(eventMessageId).queue(
                        message -> {

                            EmbedBuilder eb = new EmbedBuilder();

                            User verificationUser = verification.userMap.get(verificationUserId);

                            eb.setAuthor(verificationUser.getName(), null, verificationUser.getEffectiveAvatarUrl());
                            eb.setTitle(":white_check_mark: Accepted verification request", null);
                            eb.setDescription("This verification request has been confirmed by: " + user.getAsMention());
                            eb.setColor(new Color(12, 60, 105));

                            eb.addField("User", verificationUser.getAsMention(), false);
                            eb.addField("Name", verification.nameMap.get(verificationUserId), true);
                            eb.addField("Grade", verification.gradeMap.get(verificationUserId), true);
                            eb.addField("EPSB ID", verification.epsbIdMap.get(verificationUserId), true);

                            eb.setFooter("Automated message for user verification");
                            eb.setTimestamp(Instant.now());

                            message.editMessage(eb.build()).queue();

                            verificationUser.openPrivateChannel()
                                    .queue(channel -> {

                                        channel.sendMessage("Thank you for waiting.\n" +
                                                "You are verified to use the DAAHS Esports Discord Server.\n" +
                                                "Have fun! :smile: ").queue();

                                    });

                            event.getGuild().addRoleToMember(verificationUserId, api.getRoleById(verifiedRole)).queue();

                        }
                );

            } else if (reactionCodepoints.equalsIgnoreCase(verification.noentry)) {

                verificationChannel.retrieveMessageById(eventMessageId).queue(
                        message -> {

                            EmbedBuilder eb = new EmbedBuilder();

                            User verificationUser = verification.userMap.get(verificationUserId);

                            eb.setAuthor(verificationUser.getName(), null, verificationUser.getEffectiveAvatarUrl());
                            eb.setTitle(":no_entry: Declined verification request", null);
                            eb.setDescription("This verification request has been declined by: " + user.getAsMention());
                            eb.setColor(new Color(12, 60, 105));

                            eb.addField("User", verificationUser.getAsMention(), false);
                            eb.addField("Name", verification.nameMap.get(verificationUserId), true);
                            eb.addField("Grade", verification.gradeMap.get(verificationUserId), true);
                            eb.addField("EPSB ID", verification.epsbIdMap.get(verificationUserId), true);

                            eb.setFooter("Automated message for user verification");
                            eb.setTimestamp(Instant.now());

                            message.editMessage(eb.build()).queue();

                            verificationUser.openPrivateChannel()
                                    .queue(channel -> {

                                        channel.sendMessage("Thank you for waiting.\n" +
                                                "Your verification request has been declined.\n" +
                                                "You can rejoin the server and apply again.\n" +
                                                "If you have any objections to this decision, " +
                                                "please talk in the \"#help\" channel in the server").queue();

                                    });

                        }
                );

            }

            verificationChannel.retrieveMessageById(eventMessageId).queue(
                    message -> message.clearReactions().queue()
            );
            verification.verifyMessageCheckedMap.put(eventMessageId, true);

        }

    }

    public <K, V> K getKey(Map<K, V> map, V value) {

        for (Map.Entry<K, V> entry : map.entrySet()) {

            if (entry.getValue().equals(value)) return entry.getKey();

        }

        return null;

    }

}
