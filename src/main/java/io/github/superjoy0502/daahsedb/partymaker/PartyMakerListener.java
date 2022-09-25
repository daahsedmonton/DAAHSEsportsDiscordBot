package io.github.superjoy0502.daahsedb.partymaker;

import io.github.superjoy0502.daahsedb.Game;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PartyMakerListener extends ListenerAdapter {

    @Override
    public void onSelectionMenu(@NotNull SelectionMenuEvent event) {

        event.deferEdit().queue();

        String componentId = event.getComponentId();
        String[] splitComponentId = componentId.split(":");
        String id = splitComponentId[0];
        UUID uuid = UUID.fromString(splitComponentId[1]);
        PartyMaker partyMaker = PartyMaker.FindInstanceByUUID(uuid);

        if (id.equals("choose-game")) {
            partyMaker.game = Game.valueOf(event.getValues().get(0));
            partyMaker.isChooseGameFieldFilled = true;
        } else if (id.equals("member-count")) {
            partyMaker.count = Integer.parseInt(event.getValues().get(0));
            partyMaker.isMemberCountFieldFilled = true;
        }

    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {

        String componentId = event.getComponentId();
        String[] splitComponentId = componentId.split(":");
        String id = splitComponentId[0];
        UUID uuid = UUID.fromString(splitComponentId[1]);
        PartyMaker partyMaker = PartyMaker.FindInstanceByUUID(uuid);

        if (id.equals("submit")) {

            if (partyMaker.isChooseGameFieldFilled && partyMaker.isMemberCountFieldFilled) {

                event.deferEdit().queue();

                partyMaker.CreateSession(event);

            } else {

                event.deferReply(true).queue();

                event.getHook().sendMessage("Fill in all of the forms!").queue();

            }

        } else if (id.equals("destroy")) {

            event.deferEdit().queue();

            event.getHook().editOriginal("This LFG session has been canceled.").queue();
            event.getHook().editOriginalComponents().queue();
            PartyMaker.partyMakers.remove(partyMaker);

        }

    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        String channelName = event.getChannelJoined().getName();
        String[] channelNameSplit = channelName.split(":");
        if (!channelNameSplit[0].equals("LFG")) return;
        UUID uuid = UUID.fromString(channelNameSplit[1]);
        PartyMaker partyMaker = PartyMaker.FindInstanceByUUID(uuid);
        partyMaker.UpdateSession();
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        String channelName = event.getChannelLeft().getName();
        String[] channelNameSplit = channelName.split(":");
        if (!channelNameSplit[0].equals("LFG")) return;
        UUID uuid = UUID.fromString(channelNameSplit[1]);
        PartyMaker partyMaker = PartyMaker.FindInstanceByUUID(uuid);
        partyMaker.UpdateSession();
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        String channelName = event.getChannelLeft().getName();
        String[] channelNameSplit = channelName.split(":");
        if (!channelNameSplit[0].equals("LFG")) return;
        UUID uuid = UUID.fromString(channelNameSplit[1]);
        PartyMaker partyMaker = PartyMaker.FindInstanceByUUID(uuid);
        partyMaker.UpdateSession();
    }

}