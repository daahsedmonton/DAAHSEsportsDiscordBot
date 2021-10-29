package io.github.superjoy0502.daahsedb;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;

public class Verification extends ListenerAdapter {

    public void sendUserDirectMessage(Member member) {

        User user = member.getUser();

        user.openPrivateChannel()
                .flatMap(channel -> channel.sendMessage(
                        "Hello, " + user.getAsMention() + "!\n"
                        + "Welcome to Dr. Anne Anderson High School Esports Server!\n"
                        + "To get you started, we have to verify your identity first.\n"
                        + "Please understand that these procedures are being carried out for the student safety.\n"
                        + "Thank you for your cooperation in advance.\n"
                        + "Should we continue?"
                ))
                .queue(message -> message.addReaction("U+2705").queue());

    }

    @Override
    public void onPrivateMessageReactionAdd(@NotNull PrivateMessageReactionAddEvent event) {
        super.onPrivateMessageReactionAdd(event);
    }

    public void sendUserRequestEmbed(TextChannel channel, Member member) {

        EmbedBuilder eb = new EmbedBuilder();

        eb.setAuthor(member.getNickname(), null, member.getUser().getEffectiveAvatarUrl());
        eb.setTitle("New verification request", null);
        eb.setDescription("Click :white_check_mark: to verify, :no_entry: to decline user.");
        eb.setColor(new Color(12,60,105));

        eb.addField("User", member.getAsMention(), true);
        eb.addField("Name", "{name}", true);
        eb.addField("Grade", "{grade}", true);

        eb.setFooter("Automated message for user verification");
        eb.setTimestamp(Instant.now());

        channel.sendMessage(eb.build()).queue(message -> {
            message.addReaction("U+2705").queue();
            message.addReaction("U+26D4").queue();
        });

    }

}
