package com.rdan.nhs.bsa.skilltracker.repository;

import com.rdan.nhs.bsa.skilltracker.models.Person;
import com.rdan.nhs.bsa.skilltracker.models.Skill;

import java.util.List;
import java.util.Optional;

public interface ISkillTrackerRepository {
    List<Person> getPeople();
    Optional<Person> getPersonById(int personId);
    Optional<Skill> getSkillByPersonIdAndSkillId(int personId, int skillId);
    void addNewPerson(Person toAdd);
    boolean addNewSkill(int personId, Skill toAdd);
    boolean updatePerson(Person toUpdate);
    Optional<Person> updateSkill(int personId, Skill toUpdate);
    boolean removePerson(int personId);
    boolean removeSkill(int personId, int skillId);
}
