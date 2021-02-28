package com.rdan.nhs.bsa.skilltracker.controller;

import com.rdan.nhs.bsa.skilltracker.models.Person;
import com.rdan.nhs.bsa.skilltracker.models.Skill;

import com.rdan.nhs.bsa.skilltracker.repository.ISkillTrackerRepository;
import com.rdan.nhs.bsa.skilltracker.repository.SkillTrackerRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public final class SkillTrackerController {
    // == fields ==
    private final ISkillTrackerRepository skillTrackerRepository;

    // == constructors ==
    public SkillTrackerController(SkillTrackerRepository skillTrackerRepository) {
        this.skillTrackerRepository = skillTrackerRepository;
    }

    // == handler methods ==

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "Home";
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<Person>> getAll() {
        return ResponseEntity.ok(this.skillTrackerRepository.getPeople());
    }

    @RequestMapping(value = "/person/{personId}", method = RequestMethod.GET)
    public ResponseEntity<Person> retrievePersonById(@PathVariable int personId) {
        Optional<Person> personOptional = this.skillTrackerRepository.getPersonById(personId);
        return personOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @RequestMapping(value = "/skill/{personId}/{skillId}", method = RequestMethod.GET)
    public ResponseEntity<Skill> retrieveSkillByPersonIdAndSkillId(@PathVariable int personId, @PathVariable int skillId) {
        Optional<Skill> skillOptional = this.skillTrackerRepository.getSkillByPersonIdAndSkillId(personId, skillId);
        return skillOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @RequestMapping(value = "/person", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> addNewPerson(@RequestBody Person newPerson) {
        this.skillTrackerRepository.addNewPerson(newPerson);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/skill/{personId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> addNewSkill(@PathVariable int personId, @RequestBody Skill newSkill) {
        boolean skillAdded = this.skillTrackerRepository.addNewSkill(personId, newSkill);
        if (skillAdded) {
            return ResponseEntity.ok(HttpStatus.CREATED);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @RequestMapping(value = "/person", method = RequestMethod.PUT)
    public ResponseEntity<HttpStatus> updatePerson(@RequestBody Person updatedPerson) {
        boolean personUpdated = this.skillTrackerRepository.updatePerson(updatedPerson);
        return returnOkOrNotFound(personUpdated);
    }

    @RequestMapping(value = "/skill/{personId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> updateSkill(@PathVariable int personId, @RequestBody Skill updatedSkill) {
        Optional<Person> personOptional = this.skillTrackerRepository.updateSkill(personId, updatedSkill);
        return personOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @RequestMapping(value = "/person/{personId}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deletePersonByPersonId(@PathVariable int personId) {
        boolean personRemoved = this.skillTrackerRepository.removePerson(personId);
        return returnOkOrNotFound(personRemoved);
    }

    @RequestMapping(value = "/skill/{personId}/{skillId}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deleteSkillByPersonIdAndSkillId(@PathVariable int personId, @PathVariable int skillId) {
        boolean skillRemoved = this.skillTrackerRepository.removeSkill(personId, skillId);
        return returnOkOrNotFound(skillRemoved);
    }

    /**
     * Helper method to return the appropriate response depending on the boolean
     *
     * @param input     boolean
     * @return          true = 200 OK, false = 404 NOT FOUND
     */
    private ResponseEntity<HttpStatus> returnOkOrNotFound(boolean input) {
        if (input) {
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
