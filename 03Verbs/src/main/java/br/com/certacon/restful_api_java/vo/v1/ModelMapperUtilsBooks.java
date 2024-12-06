package br.com.certacon.restful_api_java.vo.v1;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class ModelMapperUtilsBooks {
    private static ModelMapper modelMapper = new ModelMapper();

    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source.stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    static {
        // Define custom property mappings if needed
        modelMapper.addMappings(new PropertyMap<BookVO, BookVO>() {
            protected void configure() {
                map(source.getId(), destination.getId()); // Map key to key (if needed)
            }
        });
    }
    public static <D> D map(Object source, Class<D> destinationType) {
        return modelMapper.map(source, destinationType);
    }
}
