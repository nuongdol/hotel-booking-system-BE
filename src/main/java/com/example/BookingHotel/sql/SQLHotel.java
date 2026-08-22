package com.example.BookingHotel.sql;

public class SQLHotel {
    public static final String GET_LIST_HOTEL =
            "select distinct\n" +
                    "hotel.hotel_id as hotelId,\n" +
                    "hotel.address_hotel as addressHotel,\n" +
                    "hotel.image_hotel as imageHotel,\n" +
                    "hotel.name_hotel as nameHotel,\n" +
                    "hotel.rate_hotel as rateHotel,\n" +
                    "city.city_name as cityName\n" +
                    "from hotel \n" +
                    "join city on hotel.city_id = city.city_id";
    public static final String GET_DETAIL_HOTEL =
            "select hotel.hotel_id as hotelId,\n" +
                    "hotel.image_hotel as imageHotel,\n" +
                    "hotel.name_hotel as nameHotel,\n" +
                    "city.city_name as cityName,\n" +
                    "room.id as id,\n" +
                    "room.photo as photo,\n" +
                    "room.room_price as roomPrice,\n" +
                    "room.room_type as roomType\n" +
                    "from hotel join room on room.hotel_id = hotel.hotel_id\n" +
                    "join city on city.hotel_id = hotel.hotel_id\n" +
                    "where hotel.hotel_id = :hotelId";

    public static final String GET_COUNT_HOTEL_MIN_PRICES =
            "select c.city_id as cityId,\n" +
                    "c.name as name,\n" +
                    "c.image_url as imageUrl,\n" +
                    "MIN(r.room_price) as minPrice,\n" +
                    "COUNT( DISTINCT h.hotel_id) as hotelsCount\n" +
                    "FROM city as c\n" +
                    "LEFT JOIN hotel as h on h.id_city = c.city_id\n" +
                    "LEFT JOIN room as r on r.hotel_id = h.hotel_id\n" +
                    "WHERE c.city_id in :cityIds\n" +
                    "GROUP BY c.city_id, c.name";
}
