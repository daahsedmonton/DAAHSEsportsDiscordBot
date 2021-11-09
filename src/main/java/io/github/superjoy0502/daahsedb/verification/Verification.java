package io.github.superjoy0502.daahsedb.verification;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.util.HashMap;

public class Verification extends ListenerAdapter {

    public final String checkmark = "U+2705";
    public final String noentry = "U+26D4";
    public final String anticlockwise = "U+1F504";
    public final long channelId = 903477388992725004L;
    public final long teacherRole = 902691576105553962L;
    public final long gameLeaderRole = 902692806479446066L;
    public final long eventCoordinatorRole = 902693703699795978L;
    public HashMap<Long, User> userMap = new HashMap<>();
    public HashMap<Long, Long> welcomeMessageIdMap = new HashMap<>();
    public HashMap<Long, Long> confirmMessageIdMap = new HashMap<>();
    public HashMap<Long, Long> verifyMessageIdMap = new HashMap<>();
    public HashMap<Long, Boolean> verificationAgreedMap = new HashMap<>();
    public HashMap<Long, Boolean> confirmedMap = new HashMap<>();
    public HashMap<Long, String> responseContentMap = new HashMap<>();
    public HashMap<Long, String> nameMap = new HashMap<>();
    public HashMap<Long, String> gradeMap = new HashMap<>();
    public HashMap<Long, String> epsbIdMap = new HashMap<>();
    public HashMap<Long, Boolean> verifyMessageCheckedMap = new HashMap<>();
    public HashMap<Long, Integer> verificationProgressMap = new HashMap<>();
    boolean isCanary;
    JDA api;

    public Verification(boolean isCanary) {

        this.isCanary = isCanary;

    }

    public void sendUserDirectWelcomeMessage(User user, JDA api) {

        long id = user.getIdLong();

        userMap.put(id, user);
        verificationProgressMap.put(id, 0);

        this.api = api;

        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(
                        "Hello, " + user.getAsMention() + "!\n"
                                + "Welcome to the Dr. Anne Anderson High School Esports Server!\n"
                                + "To get you started, we have to verify your identity first.\n"
                                + "Please understand that these procedures are being carried out for the student safety.\n"
                                + "Thank you for your cooperation in advance.\n"
                                + "Shall we continue?"
                ))
                .queue(message -> {

                    message.addReaction(checkmark).queue();
                    welcomeMessageIdMap.put(id, message.getIdLong());
                    verificationAgreedMap.put(id, false);

                });

    }

    public void verificationProcess(long id) {

        User user = userMap.get(id);

        switch (verificationProgressMap.get(id)) {

            case 0: {

                user.openPrivateChannel()
                        .queue(channel -> {

                            channel.sendMessage("Great!");
                            verificationProgressMap.put(id, 1);
                            verificationProcess(id);

                        });

                break;

            }

            case 1: {

                user.openPrivateChannel()
                        .queue(channel -> {

                            channel.sendMessage("Please enter your full name.\n(ex: Steve Jobs)\n" +
                                    "(You can redo this process if you have entered inaccurate information at the end!)").queue();

                        });

                api.addEventListener(new GetUserInput(this, user, isCanary, api));

                verificationProgressMap.put(id, 2);
                break;

            }

            case 2: {

                nameMap.put(id, responseContentMap.get(id));

                user.openPrivateChannel()
                        .queue(channel -> {

                            channel.sendMessage("Please enter your grade.\n(ex: 11)").queue();

                            api.addEventListener(new GetUserInput(this, user, isCanary, api));

                        });

                verificationProgressMap.put(id, 3);
                break;

            }

            case 3: {

                gradeMap.put(id, responseContentMap.get(id));

                user.openPrivateChannel()
                        .queue(channel -> {

                            channel.sendMessage("Please enter your EPSB ID.\n(ex: s.jobs11)").queue();

                            api.addEventListener(new GetUserInput(this, user, isCanary, api));

                        });

                verificationProgressMap.put(id, 4);
                break;

            }

            case 4: {

                epsbIdMap.put(id, responseContentMap.get(id));

                user.openPrivateChannel()
                        .queue(channel -> {

                            channel.sendMessage("Please check if the following information is correct:").queue();

                            EmbedBuilder eb = new EmbedBuilder();

                            eb.setAuthor(user.getName(), null, user.getEffectiveAvatarUrl());
                            eb.setTitle(user.getName(), null);
                            eb.setDescription("Click :white_check_mark: to confirm, :arrows_counterclockwise: to re-enter your information.");
                            eb.setColor(new Color(12, 60, 105));

                            eb.addField("Name", nameMap.get(id), true);
                            eb.addField("Grade", gradeMap.get(id), true);
                            eb.addField("EPSB ID", epsbIdMap.get(id), true);

                            eb.setFooter("Automated message for user verification");
                            eb.setTimestamp(Instant.now());

                            channel.sendMessage(eb.build()).queue(message -> {

                                message.addReaction(checkmark).queue();
                                message.addReaction(anticlockwise).queue();
                                confirmMessageIdMap.put(id, message.getIdLong());
                                confirmedMap.put(id, false);

                            });

                        });

                verificationProgressMap.put(id, 0);
                break;

            }

            default:
                throw new IllegalStateException("Unexpected value: " + verificationProgressMap.get(id));

        }

    }

    // Canary OFF
    @Override
    public void onPrivateMessageReactionAdd(@NotNull PrivateMessageReactionAddEvent event) {

        if (isCanary) return;

        if (event.getUser().isBot()) return;

        long eventMessageId = event.getMessageIdLong();
        String reactionCodepoints = event.getReactionEmote().getAsCodepoints();

        long id = event.getUser().getIdLong();

        if (eventMessageId == welcomeMessageIdMap.get(id) && verificationAgreedMap.get(id) == false) {

            if (reactionCodepoints.equals(checkmark)) {

                verificationAgreedMap.put(id, true);
                verificationProcess(id);

            }

        } else if (eventMessageId == confirmMessageIdMap.get(id) && confirmedMap.get(id) == false) {

            if (reactionCodepoints.equalsIgnoreCase(checkmark)) {

                sendUserRequestEmbed(id);
                confirmedMap.put(id, true);
                User user = event.getUser();
                user.openPrivateChannel()
                        .queue(channel -> {

                            channel.sendMessage("Thank you for your cooperation.\n" +
                                    "Verification may take some time.\n" +
                                    "Please wait with patience.").queue();

                        });

            } else if (reactionCodepoints.equalsIgnoreCase(anticlockwise)) {

                confirmMessageIdMap.put(id, null);
                verificationProgressMap.put(id, 1);
                verificationProcess(id);

            }

        }

    }

    public void sendUserRequestEmbed(long id) {

        EmbedBuilder eb = new EmbedBuilder();

        User user = userMap.get(id);

        eb.setAuthor(user.getName(), null, user.getEffectiveAvatarUrl());
        eb.setTitle("New verification request", null);
        eb.setDescription("Click :white_check_mark: to verify, :no_entry: to decline user.");
        eb.setColor(new Color(12, 60, 105));

        eb.addField("User", user.getAsMention(), false);
        eb.addField("Name", nameMap.get(id), true);
        eb.addField("Grade", gradeMap.get(id), true);
        eb.addField("EPSB ID", epsbIdMap.get(id), true);

        eb.setFooter("Automated message for user verification");
        eb.setTimestamp(Instant.now());

        api.getTextChannelById(channelId).sendMessage(
                api.getRoleById(teacherRole).getAsMention()
                        + api.getRoleById(gameLeaderRole).getAsMention()
                        + api.getRoleById(eventCoordinatorRole).getAsMention()
        ).queue();
        api.getTextChannelById(channelId).sendMessage(eb.build()).queue(message -> {

            message.addReaction(checkmark).queue();
            message.addReaction(noentry).queue();
            verifyMessageIdMap.put(id, message.getIdLong());
            verifyMessageCheckedMap.put(message.getIdLong(), false);

        });

    }

}

class GetUserInput extends ListenerAdapter {

    final long userId;
    boolean isCanary;
    Verification verification;
    JDA api;

    public GetUserInput(Verification verification, User user, boolean isCanary, JDA api) {

        this.verification = verification;
        this.userId = user.getIdLong();
        this.isCanary = isCanary;
        this.api = api;

    }

    // Canary OFF
    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {

        if (isCanary) return;

        if (event.getAuthor().isBot()) return;

        if (event.getAuthor().getIdLong() == userId) {

            verification.responseContentMap.put(userId, event.getMessage().getContentRaw());
            verification.verificationProcess(userId);

        }

        api.removeEventListener(this);

    }

}
