package de.thi.focus.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "focus.defaults")
public interface FocusDefaultsConfig {
    String categoryColor();
}
