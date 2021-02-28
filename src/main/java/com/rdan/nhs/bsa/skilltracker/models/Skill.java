package com.rdan.nhs.bsa.skilltracker.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@SuperBuilder(toBuilder = true)
@Getter
@Value
public class Skill extends BaseEntity {
    // == fields ==
    @NonNull String name;
    @NonNull String description;
    @NonNull String level;
}
