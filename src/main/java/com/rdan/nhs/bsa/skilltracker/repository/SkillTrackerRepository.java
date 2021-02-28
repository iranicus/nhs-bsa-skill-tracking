package com.rdan.nhs.bsa.skilltracker.repository;

import com.rdan.nhs.bsa.skilltracker.models.Person;
import com.rdan.nhs.bsa.skilltracker.models.Skill;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * A stand in for a memory database, methods simulate a DB transaction
 * personId and skillId fields added to simulate primary keys for Person and Skill
 *
 * @author richard D
 * @version 1.0, 28/02/21
 */
@Repository
public final class SkillTrackerRepository implements ISkillTrackerRepository {
    // == fields ==
    private static int personId = 1;
    private static int skillId = 1;

    private final List<Person> peopleList = new ArrayList<>();

    // == constructors ==
    public SkillTrackerRepository() {
    }

    // == public methods ==

    /**
     * Retrieve an immutable list of all known individuals
     *
     * @return      Immutable list of People
     */
    public List<Person> getPeople() {
        return Collections.unmodifiableList(peopleList);
    }

    /**
     * Attempts to retrieve a person by their ID
     *
     * @param personId      ID of the person to retrieve
     * @return              Person optional if found, empty optional if not
     */
    public Optional<Person> getPersonById(int personId) {
        return peopleList
                .stream()
                .filter(person -> person.getId() == personId)
                .findAny();
    }

    /**
     * Attempts to retrieve a skill via the person and skill ids, returns the skill
     * otherwise an empty optional if either person or skill are not found
     *
     * @param personId  Person id of the person with the skill
     * @param skillId   Skill id of the skill wishing to be retrieved
     * @return          Skill optional if found, Empty optional if not
     */
    public Optional<Skill> getSkillByPersonIdAndSkillId(int personId, int skillId) {
        Optional<Person> personOptional = getPersonById(personId);
        // test with a unknown person then with a unknown skill id (should return empty optional for both)
        return personOptional
                .flatMap(
                        person -> person.getSkills()
                                .stream()
                                .filter(skill -> skill.getId() == skillId)
                                .findAny()
                );
    }

    /**
     * Persist a new person and increment the tracked person ID
     *
     * @param toAdd     new Person to persist
     */
    public void addNewPerson(Person toAdd) {
        Person person = Person.builder()
                .id(personId).name(toAdd.getName()).skills(toAdd.getSkills())
                .build();

        peopleList.add(person);
        personId++;
    }

    /**
     * Persist a new skill if the person is found and increments the tracked skill id
     * otherwise false is returned
     *
     * @param personId  The id of the person
     * @param toAdd     The new skill to add
     * @return          true = Skill Added, false = person not found
     */
    public boolean addNewSkill(int personId, Skill toAdd) {
        // test with a unknown person
        Skill newSkill = Skill.builder()
                .id(skillId).name(toAdd.getName()).description(toAdd.getDescription())
                .level(toAdd.getLevel())
                .build();

        ListIterator<Person> personIterator = peopleList.listIterator();
        while (personIterator.hasNext()) {
            Person person = personIterator.next();
            if (person.getId() == personId) {
                Person updatedPerson = Person.builder()
                        .id(person.getId())
                        .name(person.getName())
                        .skills(person.getSkills())
                        .skill(newSkill)
                        .build();

                skillId++;
                personIterator.set(updatedPerson);

                return true;
            }
        }
        return false;
    }

    /**
     * Update a person
     *
     * @param toUpdate      The person to update
     * @return              true if updated, false if not (likely person not found)
     */
    public boolean updatePerson(Person toUpdate) {
        // need to test this with a known person and without one
        ListIterator<Person> personIterator = peopleList.listIterator();
        while (personIterator.hasNext()) {
            Person person = personIterator.next();
            if (person.getId() == toUpdate.getId()) {
                Person updatedPerson = Person.builder()
                        .id(person.getId())
                        .name(toUpdate.getName())
                        .skills(toUpdate.getSkills())
                        .build();
                personIterator.set(updatedPerson);
                return true;
            }
        }
        return false;
    }

    /**
     * Update the skill of a person
     *
     * @param personId  The ID of the person belonging to the skill
     * @param toUpdate  The updated skill
     * @return          Person optional if found, empty if not
     */
    public Optional<Person> updateSkill(int personId, Skill toUpdate) {
        // attempt to retrieve person
        Optional<Person> person = getPersonById(personId);

        if (person.isPresent()) {
            // person found, iterate their skills
            Person foundPerson = person.get();
            List<Skill> updatedSkillList = new ArrayList<>();

            // find the skill and update
            for (Skill skill : foundPerson.getSkills()) {
                if (skill.getId() == toUpdate.getId()) {
                    Skill updatedSkill = Skill.builder()
                            .id(skill.getId()).name(toUpdate.getName()).description(toUpdate.getDescription())
                            .level(toUpdate.getLevel())
                            .build();

                    // add the updated skill, overriding the original
                    updatedSkillList.add(updatedSkill);
                } else {
                    // non-matched original skill, keep this
                    updatedSkillList.add(skill);
                }
            }

            Person personUpdatedSkills = Person.builder()
                    .id(foundPerson.getId())
                    .name(foundPerson.getName())
                    .skills(updatedSkillList)
                    .build();

            // persist the person with the updated skill
            updatePerson(personUpdatedSkills);

            return Optional.of(personUpdatedSkills);
        }
        return person;
    }

    /**
     * Remove a person via their id
     *
     * @param personId      ID of the person to remove
     * @return              true if removed, false if not (person likely not found)
     */
    public boolean removePerson(int personId) {
        return peopleList.removeIf(person -> person.getId() == personId);
    }

    /**
     * Remove a skill via the person id and skill id
     *
     * @param personId      ID of the person
     * @param skillId       Skill id of the skill to remove
     * @return              true if removed, false if not (person not found or skill not found)
     */
    public boolean removeSkill(int personId, int skillId) {
        // attempt to retrieve person
        Optional<Person> person = getPersonById(personId);

        if (person.isPresent()) {
            // person found, iterate their skills
            Person foundPerson = person.get();
            List<Skill> updatedSkillList = new ArrayList<>();

            // find the skill and update
            for (Skill skill : foundPerson.getSkills()) {
                if (skill.getId() != skillId) {
                    updatedSkillList.add(skill);
                }
            }

            Person personUpdatedSkills = Person.builder()
                    .id(foundPerson.getId())
                    .name(foundPerson.getName())
                    .skills(updatedSkillList)
                    .build();

            // persist the person with the updated skill list (skill removed)
            updatePerson(personUpdatedSkills);
            return true;
        }
        return false;
    }
}
