package io.github.superjoy0502.daahsedb.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class Commands {

    // Create new commands
    public Commands(JDA api, Guild guild) {

        // Register commands
        guild.updateCommands().addCommands(
                new CommandData("lfg", "LFG(Looking For Group) Commands") // LFG Commands
                        .addSubcommands(
                                new SubcommandData("create", "Create a LFG session.")
                                        .addOption(OptionType.STRING, "title", "The title of the LFG session.", false),
                                new SubcommandData("cancel", "Cancel a LFG session.")
                        )
        ).complete();

    }

}