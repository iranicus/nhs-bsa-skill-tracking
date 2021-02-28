package com.rdan.nhs.bsa.skilltracker.models;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@SuperBuilder(toBuilder = true)
@Getter
public class BaseEntity {
    // == fields ==
    private final int id;
}
