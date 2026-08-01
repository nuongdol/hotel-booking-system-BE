package com.example.BookingHotel.mapper;


import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.request.BookingRequest;
import com.example.BookingHotel.response.BookingResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingRequest toBookingDto(BookedRoom bookedRoom);
    BookingResponse toBookingResponse(BookedRoom bookedRoom);
}
