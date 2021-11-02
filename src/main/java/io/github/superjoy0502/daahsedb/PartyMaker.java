package io.github.superjoy0502.daahsedb;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Calendar;

public class PartyMaker {

    public PartyMaker(JDA api, Guild guild) {

        api.addEventListener(new PartyMakerListener(this));

        CommandListUpdateAction commands = guild.updateCommands();

        commands.addCommands(
                new CommandData("test",
                        "A test command. Sends current time to the user who triggered the command.")
        );

        commands.addCommands(
                new CommandData("pm",
                        "Party Maker. Starts the Party Maker process.")
                        .addSubcommands(
                                new SubcommandData("create",
                                        "Create a Party Maker message.")
                                        .addOptions(
                                                new OptionData(OptionType.STRING,
                                                        "game",
                                                        "Choose which game you want to make a party for.",
                                                        true)
                                                        .addChoices(
                                                                new Command.Choice("League of Legends", "LoL"),
                                                                new Command.Choice("Mario Kart 8", "MK8"),
                                                                new Command.Choice("Rocket League", "RL"),
                                                                new Command.Choice("Super Smash Bros", "SSS")
                                                        )
                                        )
                        )
        );

        commands.queue();

    }

    public void CreatePartyMaker(SlashCommandEvent event, String game) {

        event.deferReply().queue();

        EmbedBuilder eb = new EmbedBuilder();
        User user = event.getUser();
        eb.setAuthor(user.getName(), null, user.getEffectiveAvatarUrl());
        eb.setTitle(String.format("%s Party Maker", game));
        eb.setDescription("Complete Party Maker by following the steps!");


    }

}

class PartyMakerListener extends ListenerAdapter {

    PartyMaker partyMaker;

    PartyMakerListener(PartyMaker partyMaker) {

        this.partyMaker = partyMaker;

    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        if (event.getGuild() == null) return;

        switch (event.getName()) {

            case "test":
                event.deferReply().queue();
                event.getHook().editOriginal(Instant.now().toString()).queue();
                break;

            case "pm":
                switch (event.getSubcommandName()) {

                    case "create":
                        partyMaker.CreatePartyMaker(event, event.getOption("game").getAsString());
                        break;

                }
                break;

            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();

        }

    }

}
