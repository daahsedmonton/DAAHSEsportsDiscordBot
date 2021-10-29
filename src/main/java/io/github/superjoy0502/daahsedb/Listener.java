package io.github.superjoy0502.daahsedb;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Listener extends ListenerAdapter {

    final Long channelId = 903477388992725004L;
    TextChannel verificationChannel;
    public Verification verification = new Verification();

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        System.out.println("Bot Ready!");

        verificationChannel = event.getJDA().getTextChannelById(channelId);
        verificationChannel.sendMessage("Bot is up!").queue();

    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        Member member = event.getMember();



//        verification.sendUserRequestEmbed(verificationChannel, member);

    }

}
