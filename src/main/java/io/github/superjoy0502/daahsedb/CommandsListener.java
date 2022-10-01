package io.github.superjoy0502.daahsedb;

import io.github.superjoy0502.daahsedb.partymaker.PartyMaker;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

class CommandsListener extends ListenerAdapter {

    // Handle slash commands
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        if (event.getName().equals("echo")) { // If the command is "echo"

            // Get the message option
            String content = String.valueOf(event.getOption("message"));
            // Get the times option
            int times = event.getOption("times") != null ? Integer.parseInt(String.valueOf(event.getOption("times"))) : 1;

            // Reply to the command
            StringBuilder message = new StringBuilder();
            for (int i = 0; i < times; i++) message.append("\n").append(content);
            event.reply(message.toString()).setEphemeral(true).queue();

        } else if (event.getName().equals("lfg")) { // Check if the command is "lfg"

            // Check if the subcommand is "create"
            if (event.getSubcommandName().equals("create")) {

                if (PartyMaker.findInstanceByUser(event.getUser()) != null) {
                    // Check if the user already has a PartyMaker instance
                    event.reply("You already have an existing LFG session! Use `/lfg cancel` if you think this is an error.").setEphemeral(true).queue();
                    return;
                }

                OptionMapping title = event.getOption("title");
                PartyMaker partyMaker;

                if (title == null) {
                    partyMaker = new PartyMaker();
                } else {
                    partyMaker = new PartyMaker(title.getAsString());
                }

                event.deferReply(true).queue(); // Defer the reply

                partyMaker.createForm(event); // Create the form

            } else if (event.getSubcommandName().equals("cancel")) {

                PartyMaker partyMaker = PartyMaker.findInstanceByUser(event.getUser());

                if (partyMaker == null) {
                    event.reply("You don't have an existing LFG session!").setEphemeral(true).queue();
                    return;
                }

                event.deferReply(true).queue(); // Defer the reply

                partyMaker.cancel(event);

            }

        }

    }

}
