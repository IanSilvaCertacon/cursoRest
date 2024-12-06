package br.com.certacon.restful_api_java.mapper;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class CertaMapper {
    private static ModelMapper mapper = new ModelMapper();

    public static <O, D> D parseObject (O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObject (List<O> origin, Class<D> destination) {
        List<D> destinationObjects = new ArrayList<D>();
        for (O o : origin) {
            destinationObjects.add(mapper.map(origin, destination));
        }
        return destinationObjects;
    }
}
