package io.github.superjoy0502.daahsedb

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.InteractionHook
import net.dv8tion.jda.api.EmbedBuilder
import io.github.superjoy0502.daahsedb.PartyMakerListener
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import io.github.superjoy0502.daahsedb.PartyMaker
import net.dv8tion.jda.api.hooks.ListenerAdapter
import io.github.superjoy0502.daahsedb.UpdateStatus
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.components.Button
import java.time.Instant

class PartyMaker(api: JDA, guild: Guild) {
    init {
        api.addEventListener(PartyMakerListener(this))
        val commands = guild.updateCommands()
        commands.addCommands(
            CommandData(
                "test",
                "A test command."
            )
                .addOptions(OptionData(OptionType.STRING, "testoption", "testDescription", true))
        )
        commands.addCommands(
            CommandData(
                "pm",
                "Party Maker. Starts the Party Maker process."
            )
                .addSubcommands(
                    SubcommandData(
                        "create",
                        "Create a Party Maker message."
                    )
                        .addOptions(
                            OptionData(
                                OptionType.STRING,
                                "game",
                                "Choose which game you want to make a party for.",
                                true
                            )
                                .addChoices(
                                    Command.Choice(
                                        "Mario Kart 8 Deluxe",
                                        "Mario Kart 8 Deluxe"
                                    ),
                                    Command.Choice(
                                        "Super Smash Bros",
                                        "Super Smash Bros"
                                    )
                                )
                        )
                )
        )
        commands.queue()
    }
    var partyMemberCount = 4
    var embedMessageID: Long = 0

    fun CreatePartyMaker(event: SlashCommandEvent, game: String?) {
        println("CreatePartyMaker")
        event.deferReply(true).queue()
        val hook = event.hook
        hook.setEphemeral(true)

        val eb = EmbedBuilder()
        val user = event.user
        eb.setAuthor(user.name, null, user.effectiveAvatarUrl)
        eb.setTitle(String.format("%s Party Maker", game))
        eb.setDescription("Complete Party Maker by following the steps!")
        eb.addField("Step 1", "Set the amount of people you want. (Including yourself)", false)
        eb.addField("Party Count", String.format("1 / %d", partyMemberCount), false)
        eb.setFooter("Party Maker v0.1.0")
        eb.setTimestamp(Instant.now())
        hook.sendMessageEmbeds(eb.build())
            .addActionRow(
                Button.secondary("s5", "-5"),
                Button.secondary("s1", "-1"),
                Button.secondary("a1", "+1"),
                Button.secondary("a5", "+5"),
                Button.success("setPartyCount", "Submit")
            )
            .queue{
                message: Message ->
                embedMessageID = message.idLong
            }
    }

    fun controlMemberCount(x: Int) {
        partyMemberCount += x
    }

    fun updateEmbed() {

        

    }

}

internal class PartyMakerListener(var partyMaker: PartyMaker) : ListenerAdapter() {
    override fun onSlashCommand(event: SlashCommandEvent) {
        if (event.guild == null) return
        when (event.name) {
            "test" -> {
                event.deferReply(true).queue()
                event.hook.editOriginal("Test command").queue()
                val option = event.getOption("testoption")!!.asString
                println(option)
                println(System.getenv("DAAHSEDBSecret"))
                if (option == System.getenv("DAAHSEDBSecret")) {
                    println("SHUTDOWN")
                    UpdateStatus.updateStatusOffline()
                }
            }
            "pm" -> when (event.subcommandName) {
                "create" -> partyMaker.CreatePartyMaker(
                    event, event.getOption("game")!!
                        .asString
                )
            }
            else -> event.reply("I can't handle that command right now :(").setEphemeral(true).queue()
        }
    }

    override fun onButtonClick(event: ButtonClickEvent) {
        when (event.componentId) {
            "s5" -> partyMaker.controlMemberCount(-5)
            "s1" -> partyMaker.controlMemberCount(-1)
            "a1" -> partyMaker.controlMemberCount(1)
            "a5" -> partyMaker.controlMemberCount(5)
        }
    }
}