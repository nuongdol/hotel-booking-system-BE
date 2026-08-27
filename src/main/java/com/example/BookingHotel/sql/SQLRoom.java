package com.example.BookingHotel.sql;

public class SQLRoom {
    public static final String SEARCH_LIST_ROOM =
            "select c.city_id as cityId,\n" +
                    "c.name as cityName,\n" +
                    "h.hotel_id as hotelId,\n" +
                    "h.name_hotel as nameHotel,\n" +
                    "h.address_hotel as addressHotel,\n" +
                    "h.image_hotel as imageHotel,\n" +
                    "h.rate_hotel as rateHotel,\n" +
                    "h.location as location,\n" +
                    "h.ward as ward,\n" +
                    "h.district as district,\n" +
                    "h.latitude as latitude, \n" +
                    "h.longitude as longitude, \n" +
                    "b.label as badgeLabel,\n" +
                    "r.id as roomId,\n" +
                    "r.room_type as roomType, \n" +
                    "rrp.original_price as originalPrice, \n" +
                    "rrp.price as price, \n" +
                    "rrp.is_tax_included as isTaxIncluded,\n" +
                    "p.type as policyType,\n" +
                    "p.description as policyDescription,\n" +
                    "promo.promo_id as promoId,\n" +
                    "promo.label as promoLabel,\n" +
                    "promo.discount_value as discountValue\n" +
                    "v.voucher_code as voucherCode\n" +
                    "v.discount_amount as discountAmount\n" +
                    "v.used_voucher_count as usedVoucherCount\n" +
                    "v.usage_limit as usageLimit\n" +
                    "FROM city AS c\n" +
                    "JOIN hotel AS h ON h.id_city = c.city_id\n" +
                    "JOIN room AS r ON r.hotel_id = h.hotel_id\n" +
                    "LEFT JOIN room_rate_plans AS rrp ON rrp.room_id = r.id\n" +
                    "LEFT JOIN badges_hotel AS bh ON bh.hotel_id = h.hotel_id\n" +
                    "LEFT JOIN badges AS b ON b.badge_id = bh.badge_id\n" +
                    "LEFT JOIN policies AS p ON p.room_rate_plans_id = rrp.rate_plan_id\n" +
                    "LEFT JOIN promotions AS promo ON promo.room_rate_plan_id = rrp.rate_plan_id\n" +
                    "LEFT JOIN vouchers AS v ON v.promotion_id = promo.promo_id\n" +
                    "WHERE c.name = :city and r.max_adults >= :adults and r.max_children = :children and r.room_id = :roomId\n" +
                    "and r.id not in (\n" +
                    "select br.room_id from booked_room as br\n" +
                    "where br.status = 'CONFIRMED'\n" +
                    "    and br.check_out_date > :checkInDate\n" +
                    "    and br.check_in_date < :checkOutDate \n" +
                    ")";
}
