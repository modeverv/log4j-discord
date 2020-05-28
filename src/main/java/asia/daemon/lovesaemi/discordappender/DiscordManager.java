package asia.daemon.lovesaemi.discordappender;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public class DiscordManager extends AbstractManager {

    private static final SlackManagerFactory FACTORY = new SlackManagerFactory();

    private final URL webhook;
    private final JsonFactory factory = new JsonFactory();

    private DiscordManager(final LoggerContext loggerContext, final String name, final URL webhook) {
        super(loggerContext, name);
        this.webhook = webhook;
    }

    public void sendMessage(String message) {
        try {
            HttpURLConnection connection = (HttpURLConnection) webhook.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent","Mozilla/5.0 (Macintosh; U; Intel Mac OS X; ja-JP-mac; rv:1.8.1.6) Gecko/20070725 Firefox/2.0.0.6");
            connection.connect();
            message = message.substring(0, Math.min(message.length(), 2000));
            try (JsonGenerator generator = factory.createGenerator(connection.getOutputStream())) {
                generator.writeStartObject();
                generator.writeStringField("content", message);
                generator.writeEndObject();
            }
            connection.getResponseCode();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static DiscordManager getManager(final String name, final LoggerContext context, final URL webhook) {
        GoogleChatConfiguration config = new GoogleChatConfiguration();
        config.context = context;
        config.webhook = webhook;
        return getManager(name, FACTORY, config);
    }

    private static class GoogleChatConfiguration {
        private LoggerContext context;
        private URL webhook;
    }

    private static class SlackManagerFactory implements ManagerFactory<DiscordManager, GoogleChatConfiguration> {

        @Override
        public DiscordManager createManager(final String name, final GoogleChatConfiguration data) {
            return new DiscordManager(data.context, name, data.webhook);
        }
    }
}