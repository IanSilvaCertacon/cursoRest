package br.com.certacon.restful_api_java.services;

import br.com.certacon.restful_api_java.model.Person;
import br.com.certacon.restful_api_java.vo.v1.PersonVO;

import java.util.ArrayList;
import java.util.List;

public class MockPerson {

    public Person mockEntity() {
        return mockEntity(0); // Default mock entity with index 0
    }

    public PersonVO mockVO() {
        return mockVO(0); // Default mock PersonVO with index 0
    }

    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i)); // Create a list of mock entities
        }
        return persons;
    }

    public List<PersonVO> mockVOList() {
        List<PersonVO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i)); // Create a list of mock PersonVOs
        }
        return persons;
    }

    // Mock Person entity with dynamic index for testing purposes
    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setAddress("Addres Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender((number % 2) == 0 ? "Male" : "Female");
        person.setId(number.longValue()); // Set ID based on number
        person.setLastName("Last Name Test" + number);
        return person;
    }

    // Mock PersonVO with dynamic index for testing purposes
    public PersonVO mockVO(Integer number) {
        PersonVO person = new PersonVO();
        person.setAddress("Addres Test" + number);
        person.setFirstName("First Name Test" + number);
        person.setGender((number % 2) == 0 ? "Male" : "Female");
        person.setId(number.longValue()); // Set ID based on number
        person.setLastName("Last Name Test" + number);
        return person;
    }
}
