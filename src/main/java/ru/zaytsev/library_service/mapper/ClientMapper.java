package ru.zaytsev.library_service.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.zaytsev.library_service.entity.Client;
import ru.zaytsev.library_service.web.request.ClientRequest;

@Service
@RequiredArgsConstructor
public class ClientMapper {

    private final ModelMapper modelMapper;

    public Client toEntity(ClientRequest clientRequest) {
        return modelMapper.map(clientRequest, Client.class);
    }

}
