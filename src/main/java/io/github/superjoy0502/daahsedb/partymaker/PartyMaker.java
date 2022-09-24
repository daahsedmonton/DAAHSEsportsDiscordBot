package io.github.superjoy0502.daahsedb.partymaker;

import io.github.superjoy0502.daahsedb.Game;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PartyMaker {

    public static ArrayList<PartyMaker> partyMakers = new ArrayList<>();
    public TextChannel channel;
    public VoiceChannel voiceChannel;
    public JDA api;
    public Guild guild;
    public UUID uuid;
    public Game game;
    public int count;
    public User user;
    public boolean isChooseGameFieldFilled = false;
    public boolean isMemberCountFieldFilled = false;
    String gameDisplay;
    Emoji emoji;
    long[] messageId = new long[1];

    public PartyMaker() {
        uuid = UUID.randomUUID();
        partyMakers.add(this);
    }

    public static PartyMaker FindInstanceByUUID(UUID uuid) {

        for (PartyMaker partyMaker : partyMakers) if (partyMaker.uuid.equals(uuid)) return partyMaker;

        return null;

    }

    public static PartyMaker FindInstanceByUser(User user) {

        for (PartyMaker partyMaker : partyMakers) if (partyMaker.user.equals(user)) return partyMaker;

        return null;

    }

    public void CreateForm(SlashCommandEvent event) {

        user = event.getUser();
        guild = event.getGuild();
        channel = guild.getTextChannelById(959019963325239346L);
        api = event.getJDA();

        event.getHook().sendMessage(user.getAsMention() + "**'s LFG(Looking For Group) Builder**")
                .addActionRow(SelectionMenu.create("choose-game:" + uuid)
                        .setPlaceholder("Choose a game...")
                        .addOptions(
                                SelectOption.of("League of Legends", Game.LEAGUE_OF_LEGENDS.name())
                                        .withEmoji(Emoji.fromMarkdown("<:LeagueOfLegends:1023093942851477504>")),
                                SelectOption.of("Mario Kart", Game.MARIO_KART.name())
                                        .withEmoji(Emoji.fromMarkdown("<:MarioKart:1023097171500871681>")),
                                SelectOption.of("Rocket League", Game.ROCKET_LEAGUE.name())
                                        .withEmoji(Emoji.fromMarkdown("<:RocketLeague:1023095284546404392>")),
                                SelectOption.of("Super Smash Bros", Game.SUPER_SMASH_BROS.name())
                                        .withEmoji(Emoji.fromMarkdown("<:SuperSmashBros:1023098615016722473>"))
                        ).build()
                ).addActionRow(SelectionMenu.create("member-count:" + uuid)
                        .setPlaceholder("Set max count...")
                        .addOptions(
                                SelectOption.of("3 Players", "3"),
                                SelectOption.of("4 Players", "4"),
                                SelectOption.of("5 Players", "5"),
                                SelectOption.of("6 Players", "6")
                        ).build()
                ).addActionRow(
                        Button.primary("submit:" + uuid, "Submit"),
                        Button.danger("destroy:" + uuid, "Cancel")
                ).queue();

    }

    public void CreateSession(VoiceChannel voiceChannel) {

        this.voiceChannel = voiceChannel;

        switch (game) {
            case LEAGUE_OF_LEGENDS:
                gameDisplay = "League of Legends";
                emoji = Emoji.fromMarkdown("<:LeagueOfLegends:1023093942851477504>");
                break;
            case MARIO_KART:
                gameDisplay = "Mario Kart";
                emoji = Emoji.fromMarkdown("<:MarioKart:1023097171500871681>");
                break;
            case ROCKET_LEAGUE:
                gameDisplay = "Rocket League";
                emoji = Emoji.fromMarkdown("<:RocketLeague:1023095284546404392>");
                break;
            case SUPER_SMASH_BROS:
                gameDisplay = "Super Smash Bros";
                emoji = Emoji.fromMarkdown("<:SuperSmashBros:1023098615016722473>");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + game);
        }

        channel.sendMessageEmbeds(new EmbedBuilder()
                .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                .setTitle(emoji.getAsMention() + " " + gameDisplay)
                .setColor(new Color(12, 60, 105))
                .addField(
                        "Made by",
                        user.getAsMention(),
                        true
                )
                .addField(
                        "Members",
                        voiceChannel.getMembers().size() + " / " + count,
                        true
                )
                .setFooter(api.getSelfUser().getName())
                .setTimestamp(Instant.now())
                .build()
        ).queue(message -> {
            messageId[0] = message.getIdLong();
            voiceChannel.createInvite().setMaxAge(1L, TimeUnit.DAYS).flatMap(invite -> message.editMessageComponents(ActionRow.of(Button.link(invite.getUrl(), "Join")))).queue();
        });

    }

    public void UpdateSession() {

        channel.retrieveMessageById(messageId[0])
                .flatMap(message -> message.editMessageEmbeds(new EmbedBuilder()
                        .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                        .setTitle(emoji.getAsMention() + " " + gameDisplay)
                        .setColor(new Color(12, 60, 105))
                        .addField(
                                "Made by",
                                user.getAsMention(),
                                true
                        )
                        .addField(
                                "Members",
                                voiceChannel.getMembers().size() + " / " + count,
                                true
                        )
                        .setFooter(api.getSelfUser().getName())
                        .setTimestamp(Instant.now())
                        .build())).queue();

        if (voiceChannel.getMembers().size() == 0) {

            channel.retrieveMessageById(messageId[0]).flatMap(Message::delete).queue();
            voiceChannel.delete().queue();
            PartyMaker.partyMakers.remove(this);

        }

    }

}