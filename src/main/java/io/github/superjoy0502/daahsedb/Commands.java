package io.github.superjoy0502.daahsedb;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class Commands {

    public Commands(JDA api, Guild guild) {

        guild.updateCommands().addCommands(
                new CommandData("echo", "Repeats messages back to you.")
                        .addOption(OptionType.STRING, "message", "The message to repeat.", true)
                        .addOption(OptionType.INTEGER, "times", "The number of times to repeat the message."),
                new CommandData("lfg", "LFG(Looking For Group) Commands")
                        .addSubcommands(
                                new SubcommandData("create", "Create a LFG session.")
                        )
        ).complete();

    }

}