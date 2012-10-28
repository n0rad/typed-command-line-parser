package net.awired.aclm.log;

import org.fusesource.jansi.Ansi;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LogbackColorLevelConverter extends ClassicConverter {

    public enum ColorLevel {
        TRACE(Level.TRACE, Ansi.Color.BLUE, "TRACE"), //
        DEBUG(Level.DEBUG, Ansi.Color.MAGENTA, "DEBUG"), //
        INFO(Level.INFO, Ansi.Color.GREEN, "INFO "), //
        WARN(Level.WARN, Ansi.Color.YELLOW, "WARN "), //
        ERROR(Level.ERROR, Ansi.Color.RED, "ERROR"), //

        ;

        private final Level  level;
        private final String sequence;

        private ColorLevel(Level level, Ansi.Color color, String out) {
            this.level = level;
            this.sequence = Ansi.ansi().fg(color).a(out).reset().toString();
        }

        public String getSequence() {
            return sequence;
        }

        public static ColorLevel valueOfLevel(Level level) {
            for (ColorLevel colorLevel : values()) {
                if (level == colorLevel.level) {
                    return colorLevel;
                }
            }
            return null;
        }

    }

    @Override
    public String convert(ILoggingEvent event) {
        ColorLevel valueOfLevel = ColorLevel.valueOfLevel(event.getLevel());
        if (valueOfLevel != null) {
            return valueOfLevel.getSequence();
        }
        return event.getLevel().toString();
    }

}
