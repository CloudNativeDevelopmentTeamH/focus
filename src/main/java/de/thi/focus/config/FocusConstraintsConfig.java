package de.thi.focus.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "focus.constraints")
public interface FocusConstraintsConfig {

    Note note();
    Category category();

    interface Note {
        int maxLength();
    }

    interface Category {
        Name name();

        interface Name {
            int maxLength();
        }
    }
}
