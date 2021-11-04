package io.github.superjoy0502.daahsedb;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class PartyMaker {

    public PartyMaker(JDA api, Guild guild) {

        api.addEventListener(new PartyMakerListener(this));

        CommandListUpdateAction commands = guild.updateCommands();

        commands.addCommands(
                new CommandData("test",
                        "A test command. Sends current time to the user who triggered the command.")
                        .addOptions(new OptionData(OptionType.STRING, "testoption", "testDescription", true))
        );

        /*commands.addCommands(
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
//                                                                new Command.Choice("League of Legends", "LoL"),
                                                                new Command.Choice("Mario Kart 8", "MK8"),
//                                                                new Command.Choice("Rocket League", "RL"),
                                                                new Command.Choice("Super Smash Bros", "SSS")
                                                        )
                                        )
                        )
        );*/

        commands.queue();

    }

    public void CreatePartyMaker(SlashCommandEvent event, String game) {

        System.out.println("CreatePartyMaker");

        event.deferReply(true).queue();
        InteractionHook hook = event.getHook();
        hook.setEphemeral(true);

        int total;
        switch (game) {
            case "MK8":
                total = 8;
                break;
            case "SSS":
                total = 8;
                break;
            default:
                total = 3;
                break;
        }

        EmbedBuilder eb = new EmbedBuilder();
        User user = event.getUser();
        eb.setAuthor(user.getName(), null, user.getEffectiveAvatarUrl());
        eb.setTitle(String.format("%s Party Maker", game));
        eb.setDescription("Complete Party Maker by following the steps!");
        eb.addField("Step 1", "Set the amount of people you want. (Including yourself)", false);
        eb.addField("Party Count", String.format("1 / %d", total), false);
        eb.setFooter("Party Maker v0.1.0");
        eb.setTimestamp(Instant.now());

        hook.sendMessageEmbeds(eb.build())
                .addActionRow(
                        Button.secondary("s5", "-5"),
                        Button.secondary("s1", "-1"),
                        Button.secondary("a1", "+1"),
                        Button.secondary("a5", "+5"),
                        Button.success("setPartyCount", "Submit")
                )
                .queue();

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
                event.deferReply(true).queue();
                String option = event.getOption("testoption").getAsString();
                if (option.equals(System.getenv("DAAHSEDBSecret"))) {
                    System.out.println("shutdown");
                    event.getHook().editOriginal("Bot is now shutting down.").queue();
                    UpdateStatus.updateStatusOffline();
                } else event.getHook().editOriginal(option).queue();
                break;

            /*case "pm":
                switch (event.getSubcommandName()) {

                    case "create":
                        partyMaker.CreatePartyMaker(event, event.getOption("game").getAsString());
                        break;

                }
                break;*/

            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();

        }

    }

}
