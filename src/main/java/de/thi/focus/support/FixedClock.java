package de.thi.focus.support;

import de.thi.focus.usecases.ports.outbound.system.Clock;

import java.time.Instant;

public final class FixedClock implements Clock {

    private final Instant fixedNow;

    public FixedClock(Instant fixedNow) {
        this.fixedNow = fixedNow;
    }

    @Override
    public Instant now() {
        return fixedNow;
    }
}
