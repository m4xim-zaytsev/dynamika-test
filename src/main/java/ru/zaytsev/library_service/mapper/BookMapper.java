package ru.zaytsev.library_service.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.zaytsev.library_service.entity.Book;
import ru.zaytsev.library_service.web.request.BookRequest;

@Service
@RequiredArgsConstructor
public class BookMapper {

    private final ModelMapper modelMapper;

    public Book toEntity(BookRequest bookRequest) {
        return modelMapper.map(bookRequest, Book.class);
    }
}

