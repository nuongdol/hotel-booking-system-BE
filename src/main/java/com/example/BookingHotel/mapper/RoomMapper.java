package com.example.BookingHotel.mapper;

import com.example.BookingHotel.model.Room;
import com.example.BookingHotel.response.RoomResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.sql.Blob;
import java.util.Base64;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "photo", source = "photo", qualifiedByName = "blobToString")
    RoomResponse toRoomResponse(Room room);
    //convert blob -> string(Base64)
    @Named("blobToString")
    default String convertBlobToString(Blob blob){
        if (blob == null) {
            return null;
        }
        try {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            return null;
        }
    }
}
