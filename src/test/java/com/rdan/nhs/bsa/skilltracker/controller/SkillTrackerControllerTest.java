package com.rdan.nhs.bsa.skilltracker.controller;

import com.rdan.nhs.bsa.skilltracker.models.Person;
import com.rdan.nhs.bsa.skilltracker.models.Skill;
import com.rdan.nhs.bsa.skilltracker.models.enums.SkillLevel;
import com.rdan.nhs.bsa.skilltracker.repository.SkillTrackerRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@SpringBootTest
public class SkillTrackerControllerTest implements WithAssertions {
    // == mock data ==
    private static final int KNOWN_PERSON_ID = 1;
    private static final int KNOWN_SKILL_ID = 1;
    private static final int UNKNOWN_PERSON_ID = 99;
    private static final int UNKNOWN_SKILL_ID = 50;

    private static final List<Skill> EXAMPLE_SKILL_LIST = Collections.singletonList(
            Skill.builder()
                    .id(KNOWN_SKILL_ID).name("skill1").description("description1")
                    .level(SkillLevel.AWARENESS.name())
                    .build()
    );
    private static final Person KNOWN_PERSON = Person.builder()
            .id(KNOWN_PERSON_ID).name("richard").skills(EXAMPLE_SKILL_LIST)
            .build();

    // == dependencies ==
    @Mock
    private SkillTrackerRepository skillTrackerRepository;

    @InjectMocks
    private SkillTrackerController controllerUnderTest;

    @Test
    @DisplayName("home -> should return a response string of 'Home'")
    void whenHomeIsRequestedReturnHome() {
        assertThat(controllerUnderTest.home())
                .as("Expecting the string Home to be returned")
                .isEqualTo("Home");
    }

    @Test
    @DisplayName("getAll -> should return a list of all registered people and their skills")
    void whenGetAllIsRequestedReturnAllPeople() {
        given(skillTrackerRepository.getPeople()).willReturn(Collections.singletonList(KNOWN_PERSON));

        assertThat(controllerUnderTest.getAll())
                .as("Expecting a list of People to be returned")
                .isEqualTo(ResponseEntity.ok(Collections.singletonList(KNOWN_PERSON)));
    }

    @Test
    @DisplayName("retrievePersonById -> should return a 200 OK with the person object if the person is registered")
    void whenPersonIsRequestedWithKnownPersonIdReturn200Ok() {
        given(skillTrackerRepository.getPersonById(KNOWN_PERSON_ID)).willReturn(Optional.of(KNOWN_PERSON));

        assertThat(controllerUnderTest.retrievePersonById(KNOWN_PERSON_ID))
                .as("A 200 OK response should have been returned for the known person")
                .isEqualTo(ResponseEntity.ok(KNOWN_PERSON));
    }

    @Test
    @DisplayName("retrievePersonById -> should return 404 NOT FOUND if the person IS NOT registered")
    void whenPersonIsRequestedWithUnknownPersonIdReturn404NotFound() {
        given(skillTrackerRepository.getPersonById(UNKNOWN_PERSON_ID)).willReturn(Optional.empty());

        assertThat(controllerUnderTest.retrievePersonById(UNKNOWN_PERSON_ID))
                .as("A 404 NOT FOUND response should have been returned for the unknown person")
                .isEqualTo(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Test
    @DisplayName("retrieveSkillByPersonIdAndSkillId -> should return a 200 OK response with the Skill object")
    void whenSkillIsRequestedWithKnownPersonAndSkillIdReturn200OK() {}

    @Test
    @DisplayName("retrieveSkillByPersonIdAndSkillId -> should return a 404 NOT FOUND if the person is not found")
    void whenSkillIsRequestedWithUnknownPersonIdReturn404NotFound() {}

    @Test
    @DisplayName("addNewPerson -> should return 200 OK when a new person is registered")
    void whenNewPersonRegisteredReturnHttpStatusCreated() {}

    @Test
    @DisplayName("addNewSkill -> should return 200 OK when a new skill is added for a known person")
    void whenNewSkillIsAddedForKnownPersonReturn200Ok() {}

    @Test
    @DisplayName("addNewSkill -> should return 404 NOT FOUND if the person is not found")
    void whenNewSkillIsAddedForUnknownPersonReturn404NotFound() {}

    @Test
    @DisplayName("updatePerson -> should return 200 OK when the known person has been updated")
    void whenAKnownPersonHasBeenUpdatedReturn200Ok() {}

    @Test
    @DisplayName("updatePerson -> should return 404 NOT FOUND if the person to be updated was not found")
    void whenAUnknownPersonHasBeenRequestedForAnUpdateReturn404NotFound() {}

    @Test
    @DisplayName("updateSkill -> should return 200 OK when the skill of the known person has been updated")
    void whenAKnownPersonsSkillHasBeenUpdatedReturn200Ok() {}

    @Test
    @DisplayName("updateSkill -> should return 404 NOT FOUND if the person's skill to be updated was not found")
    void whenASkillUpdateForAnUnknownPersonHasBeenRequestedReturn404NotFound() {}

    @Test
    @DisplayName("deletePersonByPersonId -> should return 200 OK when the known person has been deleted")
    void whenAKnownPersonIsRemovedReturn200Ok() {}

    @Test
    @DisplayName("deletePersonByPersonId -> should return 404 NOT FOUND if the person to be removed was not found")
    void whenAUnknownPersonIsRequestedForRemovalReturn404NotFound() {}

    @Test
    @DisplayName("deleteSkillByPersonIdAndSkillId -> should return 200 OK when the known persons skill has been deleted")
    void whenAKnownPersonsSkillIsRemovedReturn200Ok() {}

    @Test
    @DisplayName("deleteSkillByPersonIdAndSkillId -> should return 404 NOT FOUND if the person was not found")
    void whenAUnknownPersonsSkillIsRequestedForRemovalReturn404NotFound() {}
}
