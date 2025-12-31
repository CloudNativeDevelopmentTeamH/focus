package de.thi.focus.usecases.ports;

import java.time.Instant;

/**
 * Provides the current time.
 * Abstracted to make use cases deterministic and testable.
 */
public interface Clock {

    Instant now();
}
