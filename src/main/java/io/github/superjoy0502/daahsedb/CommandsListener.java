package io.github.superjoy0502.daahsedb;

import io.github.superjoy0502.daahsedb.partymaker.PartyMaker;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

class CommandsListener extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        if (event.getName().equals("echo")) {

            String content = String.valueOf(event.getOption("message"));
            int times = event.getOption("times") != null ? Integer.parseInt(String.valueOf(event.getOption("times"))) : 1;

            StringBuilder message = new StringBuilder();
            for (int i = 0; i < times; i++) message.append("\n").append(content);
            event.reply(message.toString()).setEphemeral(true).queue();

        } else if (event.getName().equals("lfg")) {

            if (event.getSubcommandName().equals("create")) {

                if (PartyMaker.FindInstanceByUser(event.getUser()) != null) {
                    event.reply("You already have an existing LFG session!").setEphemeral(true).queue();
                    return;
                }

                PartyMaker partyMaker = new PartyMaker();

                event.deferReply(true).queue();

                partyMaker.CreateForm(event);

            }

        }

    }

}
