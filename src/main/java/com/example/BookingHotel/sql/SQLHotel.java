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
                    "GROUP_CONCAT(DISTINCT t.name ORDER BY t.name SEPARATOR ',') as tagName,\n" +
                    "MIN(r.room_price) as minPrice,\n" +
                    "COUNT( DISTINCT h.hotel_id) as hotelsCount\n" +
                    "FROM city as c\n" +
                    "LEFT JOIN hotel as h on h.id_city = c.city_id\n" +
                    "LEFT JOIN room as r on r.hotel_id = h.hotel_id\n" +
                    "LEFT JOIN city_tag as ct on ct.city_id = c.city_id\n" +
                    "LEFT JOIN tag as t on t.tag_id = ct.tag_id\n" +
                    "WHERE c.city_id in :cityIds\n" +
                    "GROUP BY c.city_id, c.name";

    public static final String GET_CHEAPEST_ROOM_IN_CITY = "WITH cheapest_rate AS (\n" +
            "    SELECT \n" +
            "        r.hotel_id,\n" +
            "        r.id AS room_id,\n" +
            "        r.room_type,\n" +
            "        rrp.rate_plan_id,\n" +
            "        rrp.original_price,\n" +
            "        rrp.price,\n" +
            "        rrp.is_tax_included,\n" +
            "        ROW_NUMBER() OVER (\n" +
            "            PARTITION BY r.hotel_id\n" +
            "            ORDER BY rrp.price ASC\n" +
            "        ) AS rn\n" +
            "    FROM room AS r\n" +
            "    JOIN room_rate_plans AS rrp ON rrp.room_id = r.id\n" +
            ")\n" +
            "SELECT \n" +
            "    c.city_id       AS cityId,\n" +
            "    c.name          AS cityName,\n" +
            "    h.hotel_id      AS hotelId,\n" +
            "    h.name_hotel    AS nameHotel,\n" +
            "    h.address_hotel AS addressHotel,\n" +
            "    h.image_hotel   AS imageHotel,\n" +
            "    h.rate_hotel    AS rateHotel,\n" +
            "    h.location      AS location,\n" +
            "    h.ward          AS ward,\n" +
            "    h.district      AS district,\n" +
            "    h.latitude      AS latitude,\n" +
            "    h.longitude     AS longitude,\n" +
            "    b.label         AS badgeLabel,\n" +
            "    cr.room_type    AS roomType,\n" +
            "    cr.original_price AS originalPrice,\n" +
            "    cr.price        AS price,\n" +
            "    cr.is_tax_included AS isTaxIncluded,\n" +
            "    p.type          AS policyType,\n" +
            "    p.description   AS policyDescription,\n" +
            "    promo.label     AS promoLabel,\n" +
            "    promo.discount_value AS discountValue\n" +
            "FROM city AS c\n" +
            "JOIN hotel AS h ON h.id_city = c.city_id\n" +
            "JOIN cheapest_rate AS cr ON cr.hotel_id = h.hotel_id AND cr.rn = 1\n" +
            "LEFT JOIN badges_hotel AS bh ON bh.hotel_id = h.hotel_id\n" +
            "LEFT JOIN badges AS b ON b.badge_id = bh.badge_id\n" +
            "LEFT JOIN policies AS p ON p.room_rate_plans_id = cr.rate_plan_id\n" +
            "LEFT JOIN promotions AS promo ON promo.room_rate_plan_id = cr.rate_plan_id\n" +
            "WHERE c.city_id = :cityId";

    public static final String GET_POPULAR_ROOM_IN_A_CITY = "";
}
