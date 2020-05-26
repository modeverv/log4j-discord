package asia.daemon.lovesaemi.discordappender;

import java.io.Serializable;
import java.net.URL;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.StringLayout;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;

@Plugin(name = "Discord", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class DiscordAppender extends AbstractAppender {

    private final DiscordManager manager;

    private DiscordAppender(final String name, final Filter filter, final StringLayout layout, final boolean ignoreExceptions, final DiscordManager manager) {
        super(name, filter, layout, ignoreExceptions, new Property[]{});
        this.manager = manager;
    }

    @Override
    public void append(final LogEvent event) {
        String message = (String) getLayout().toSerializable(event);
        manager.sendMessage(message);
    }

    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder<B extends Builder<B>>
            extends AbstractAppender.Builder<B>
            implements org.apache.logging.log4j.core.util.Builder<DiscordAppender> {

        @PluginBuilderAttribute
        @Required
        private URL webhook;

        public B setWebhook(final URL webhook) {
            this.webhook = webhook;
            return asBuilder();
        }

        @Override
        public DiscordAppender build() {
            Layout<? extends Serializable> layout = getOrCreateLayout();
            if (!(layout instanceof StringLayout)) {
                throw new IllegalArgumentException("Layout must be a StringLayout");
            }
            DiscordManager manager = DiscordManager.getManager(getName(), getConfiguration().getLoggerContext(), webhook);
            return new DiscordAppender(getName(), getFilter(), (StringLayout) layout, isIgnoreExceptions(), manager);
        }
    }
}
