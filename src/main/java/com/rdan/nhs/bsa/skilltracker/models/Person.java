package com.rdan.nhs.bsa.skilltracker.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@SuperBuilder(toBuilder = true)
@Getter
@Value
public class Person extends BaseEntity {
    // == fields ==
    @NonNull String name;
    @Singular
    @NonNull List<Skill> skills; // initialised as a unmodifiable AL by Lombok
}
