package io.github.superjoy0502.daahsedb;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot {

    public static void main(String[] arguments) throws Exception {

        boolean isCanary = false;
        long startTime = System.nanoTime();
        String envVar;
        envVar = isCanary ? "DAAHSEDBCKey" : "DAAHSEDBKey";

        long guildId = 902691576105553961L;

        JDA api = JDABuilder.createDefault(System.getenv(envVar))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .build()
                .awaitReady();

        User bot = api.getSelfUser();
//        PartyMaker partyMaker = new PartyMaker(api, api.getGuildById(guildId));
        UpdateStatus.setVariables(isCanary, startTime, api, bot);
        UpdateStatus.updateStatusOnline();

        api.getPresence().setActivity(Activity.playing("in Ionia (Patch 220941)"));

        // region Rules
        /*EmbedBuilder esb = new EmbedBuilder();
        esb.setTitle(":pushpin: DAAHS Esports Discord Server Rules v0.2.1");
        esb.setColor(new Color(12, 60, 105));
        esb.setDescription("• The following rules define the rules of DAAHS Esports Discord Server.\n" +
                "• These rules must be followed strictly, or else penalties may apply.\n" +
                "• Please change your nickname to your real name.\n" +
                "• The maximum rating permitted in messages is **G**.");

        api.getTextChannelById(902691576105553964L).sendMessage(
                esb.build()
        ).queue();

        EmbedBuilder esb2 = new EmbedBuilder();
        esb2.setTitle("Discord Official Guideline", "https://discord.com/guidelines");
        esb2.setColor(new Color(12, 60, 105));
        esb2.setDescription("• The official guideline of Discord has to be followed in this server.");

        api.getTextChannelById(902691576105553964L).sendMessage(
                esb2.build()
        ).queue();

        EmbedBuilder esb3 = new EmbedBuilder();
        esb3.setTitle("Respect each others");
        esb3.setColor(new Color(12, 60, 105));
        esb3.setDescription("• In a community, every person should be able to **enjoy** the space.\n" +
                "• Discrimination towards others is prohibited.\n" +
                "• Mind your manners.\n" +
                "• Do not swear.");

        api.getTextChannelById(902691576105553964L).sendMessage(
                esb3.build()
        ).queue();

        EmbedBuilder esb4 = new EmbedBuilder();
        esb4.setTitle("This is an Esports server");
        esb4.setColor(new Color(12, 60, 105));
        esb4.setDescription("• Esports is not just about gaming, but also sportsmanship.\n" +
                "• Just like playing a fair sports game, you should treat people fair and honorable.\n" +
                "• Know what you should do and what you should not do.\n" +
                "• Do not perform immature acts such as spamming, mentioning everybody, etc.");

        api.getTextChannelById(902691576105553964L).sendMessage(
                esb4.build()
        ).queue();

        EmbedBuilder esbb = new EmbedBuilder();
        esbb.setTitle("DAAHS Esports Discord Bot");
        esbb.setColor(new Color(12, 60, 105));
        esbb.setDescription("• " + bot.getAsMention() + " is a bot made by DAAHS Esports leaders.\n" +
                "• This bot has many functions, and one of them is to verify newcomers.\n" +
                "• When you join the server, I will DM you to help you verify yourself as a DAAHS member.\n" +
                "• But, when I'm offline and cannot help you, please use " + api.getTextChannelById(903689843438153779L).getAsMention() + ".\n" +
                "• <@904288301110919168> is my replica, only used by the developers to test out new functions that will be added to me.");

        api.getTextChannelById(902691576105553964L).sendMessage(
                esbb.build()
        ).queue();

        EmbedBuilder esb5 = new EmbedBuilder();
        esb5.setTitle("Lastly");
        esb5.setColor(new Color(12, 60, 105));
        esb5.setDescription("• Have fun.");

        api.getTextChannelById(902691576105553964L).sendMessage(
                esb5.build()
        ).queue();*/
        // endregion


    }

}
