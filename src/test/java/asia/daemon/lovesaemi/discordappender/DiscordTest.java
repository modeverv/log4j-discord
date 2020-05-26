package asia.daemon.lovesaemi.discordappender;

import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("ALL")
public class DiscordTest {
    public static final String DISCORD_WEBHOOK = "DISCORD_WEBHOOK";

    @Before
    public void setup() {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        builder.setPackages("asia.daemon.lovesaemi");
        //String webhookUrl = System.getProperty(GOOGLECHAT_WEBHOOK, System.getenv(GOOGLECHAT_WEBHOOK));
        String webhookUrl = System.getenv(DISCORD_WEBHOOK);
        System.out.println(webhookUrl);
        assertNotNull(DISCORD_WEBHOOK + " MUST NOT be null", webhookUrl);
        AppenderComponentBuilder appenderComponentBuilder = builder.newAppender("discord", "Discord");
        appenderComponentBuilder.addAttribute("webhook", webhookUrl);
        appenderComponentBuilder.add(builder.newFilter(
                "MarkerFilter",
                Filter.Result.ACCEPT,
                Filter.Result.DENY).addAttribute("marker", "DISCORD"));
        appenderComponentBuilder.add(builder.newLayout("PatternLayout").
                addAttribute("pattern", "[%d{yyyy-MM-dd HH:mm:ss.SSS}], %-5p, %t, %c, %m%n"));
        builder.add(appenderComponentBuilder);
        builder.add(builder.newRootLogger(Level.INFO).add(builder.newAppenderRef("discord")));
        System.out.println(builder.toXmlConfiguration());
        Configurator.initialize(builder.build());
    }


    @Test
    public void sendInfoWithoutStack() throws InterruptedException {
        Marker GOOGLECHAT = MarkerManager.getMarker("DISCORD");
        LogManager.getLogger(getClass()).warn(GOOGLECHAT,"Test warning message");
        Thread.sleep(2000);
    }
}