package br.com.certacon.restful_api_java.services;

import br.com.certacon.restful_api_java.controllers.BookController;
import br.com.certacon.restful_api_java.controllers.PersonController;
import br.com.certacon.restful_api_java.exceptions.RequiredObjectIsNullException;
import br.com.certacon.restful_api_java.exceptions.ResourceNotFoundException;
import br.com.certacon.restful_api_java.mapper.PersonMapper;
import br.com.certacon.restful_api_java.model.Book;
import br.com.certacon.restful_api_java.repositories.BookRepository;
import br.com.certacon.restful_api_java.vo.v1.BookVO;
import br.com.certacon.restful_api_java.vo.v1.ModelMapperUtilsBooks;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {
    private final Logger logger = Logger.getLogger(BookServices.class.getName());

    @Autowired
    BookRepository repository;

    @Autowired
    PersonMapper mapper;

    public List<BookVO> findAll() {
        logger.info("Finding all books");

        List<BookVO> result = ModelMapperUtilsBooks.mapList(repository.findAll(), BookVO.class);
        result.forEach(p -> {
            try {
                // Add self link for each person
                p.add(linkTo(methodOn(BookController.class).findById(p.getId())).withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return result;
    }

    public BookVO findById(Long id) throws Exception {
        logger.info("Finding one book");
        ModelMapper modelMapper = new ModelMapper();
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        var vo = modelMapper.map(entity, BookVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO book){

        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one book");
        ModelMapper modelMapper = new ModelMapper();
        var entity = modelMapper.map(book, Book.class);
        var vo = modelMapper.map(repository.save(entity), BookVO.class);
        try {
            vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return vo;
    }

//    public PersonVO createV2(PersonVOV2 person) {
//        logger.info("Creating one person");
//        ModelMapper modelMapper = new ModelMapper();
//        var entity = modelMapper.map(person, Person.class);
//        var vo = modelMapper.map(repository.save(entity), PersonVOV2.class);
//        return vo;
//    }
//
//    public PersonVOV2 createV2(PersonVOV2 person) {
//        logger.info("Creating a new version 2 person");
//        var entity = mapper.convertVoToEntity(person);
//        var vo = mapper.convertEntityToVo(repository.save(entity));
//        return vo;
//    }

    public BookVO update(BookVO book) {
        logger.info("Creating one book");
        var entity = repository.findById(book.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));

        entity.setAuthor(book.getAuthor());
        entity.setTitle(book.getTitle());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());

        ModelMapper modelMapper = new ModelMapper();
        var vo = modelMapper.map(repository.save(entity), BookVO.class);
        try {
            vo.add(linkTo(methodOn(BookController.class).findById(vo.getId())).withSelfRel());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one book");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this Id"));
        repository.delete(entity);
    }
}