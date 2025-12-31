package de.thi.focus.frameworksdrivers.time;

import de.thi.focus.usecases.ports.Clock;

import java.time.Instant;

public final class SystemClock implements Clock {

    @Override
    public Instant now() {
        return Instant.now();
    }
}
