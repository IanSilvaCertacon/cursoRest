package br.com.certacon.restful_api_java.services;

import br.com.certacon.restful_api_java.controllers.PersonController;
import br.com.certacon.restful_api_java.exceptions.RequiredObjectIsNullException;
import br.com.certacon.restful_api_java.exceptions.ResourceNotFoundException;
import br.com.certacon.restful_api_java.mapper.PersonMapper;
import br.com.certacon.restful_api_java.model.Person;
import br.com.certacon.restful_api_java.repositories.PersonRepository;
import br.com.certacon.restful_api_java.vo.v1.ModelMapperUtils;
import br.com.certacon.restful_api_java.vo.v1.PersonVO;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {
    private final Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper mapper;

    public List<PersonVO> findAll() {
        logger.info("Finding all people");

        List<PersonVO> result = ModelMapperUtils.mapList(repository.findAll(), PersonVO.class);
        result.forEach(p -> {
            try {
                // Add self link for each person
                p.add(linkTo(methodOn(PersonController.class).findById(p.getId())).withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return result;
    }

    public PersonVO findById(Long id) throws Exception {
        logger.info("Finding one person");
        ModelMapper modelMapper = new ModelMapper();
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        var vo = modelMapper.map(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO person){

        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one person");
        ModelMapper modelMapper = new ModelMapper();
        var entity = modelMapper.map(person, Person.class);
        var vo = modelMapper.map(repository.save(entity), PersonVO.class);
        try {
            vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return vo;
    }

    @Transactional
    public PersonVO disablePerson(Long id) throws Exception {

        logger.info("Disabling one person!");

        repository.disablePerson(id);

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        ModelMapper modelMapper = new ModelMapper();
        var vo = modelMapper.map(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person) {
        logger.info("Creating one person");
        var entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        ModelMapper modelMapper = new ModelMapper();
        var vo = modelMapper.map(repository.save(entity), PersonVO.class);
        try {
            vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one person");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));
        repository.delete(entity);
    }
}