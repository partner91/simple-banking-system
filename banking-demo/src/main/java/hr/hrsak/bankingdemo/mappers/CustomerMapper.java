package hr.hrsak.bankingdemo.mappers;

import hr.hrsak.bankingdemo.dto.CustomerDTO;
import hr.hrsak.bankingdemo.models.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO toDTO(Customer customer);

    @Mapping(target = "address", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "phoneNumber", ignore = true)
    Customer toEntity(CustomerDTO customerDTO);
}
