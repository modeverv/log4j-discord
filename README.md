# Discord Appender for Log4j ![](https://travis-ci.org/modeverv/log4j-discord.svg?branch=master)

This is a simple webhook-based Discord appender for Log4j 2.x.
To use this appender in your code, first get a webhook URL from Discord.
Then, add this to your project's dependencies:

```
allprojects {
   repositories {
        ...
        maven { url 'https://jitpack.io' }
   }
}
dependencies {
        implementation 'com.github.modeverv:log4j-discord:0.0.1'
}
```

To use this plugin, add the following to your `log4j2.xml` configuration (or translate to your preferred format):

```
<Configuration>
    <Appenders>
        <Discord name="discord" webhook="https://discord...">
            <MarkerFilter onMatch="ACCEPT" onMismatch="DENY" marker="DISCORD"/>
            <PatternLayout pattern="%m"/>
        </Discord>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="discord"/>
        </Root>
    </Loggers>
</Configuration>
```

Using the MarkerFilter configuration like this allows you to send a log message to Discord from any class.
By using the `DISCORD` marker, this allows the configuration to filter all messages with that marker and make sure to send them to the DiscordAppender.
As long as you don't override the additivity property of the relevant loggers, then these log messages will also be logged to the console or file or whatever you have configured normally.
Read more about markers [here](https://logging.apache.org/log4j/2.x/manual/markers.html).

## Configuration

The following are all the configuration attributes or elements supported:

* name: name of the appender, used when referencing via `<AppenderRef ref="name"/>`.
* filter: an optional filter to use; a MarkerFilter is recommended here as described above.
* ignoreExceptions: whether or not to let exceptions from the appender to be swallowed or propagated.
  This is true by default and should usually only be set to false when using a [FailoverAppender](https://logging.apache.org/log4j/2.x/manual/appenders.html#FailoverAppender) or similar.
* layout: a string layout to use to format log events into a Slack message.
  Only `StringLayout` layouts are supported since sending arbitrary binary data to Slack doesn't make sense.
  If no layout is specified, the default pattern layout is used.
* webhook: URL to the Slack webhook to send messages to.

## Test
set environment variable and run test.
```
DISCORD_WEBHOOK="https://...." ./gradlew test
```

## Contributing

Submit pull requests.

## Thanks
[log4j-slack](https://github.com/jvz/log4j-slack)  