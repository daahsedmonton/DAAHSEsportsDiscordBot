package io.github.superjoy0502.daahsedb;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public class PartyMaker {

    public PartyMaker(Guild guild) {

        CommandListUpdateAction commands = guild.updateCommands();

        commands.addCommands(
                new CommandData("test",
                        "A test command. Sends current time to the user who triggered the command.")
        );

        commands.queue();

    }

}

class PartyMakerListener extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {

        if (event.getGuild() == null) return;

        switch (event.getName()) {

            case "test":
                event.deferReply().queue();
                event.getHook().editOriginal(Instant.now().toString()).queue();

        }

    }

}
