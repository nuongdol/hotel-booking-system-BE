package com.example.BookingHotel.sql;

public class SQLRoom {
    public static final String SEARCH_LIST_ROOM =
            "SELECT\n" +
                    "    c.city_id AS cityId,\n" +
                    "    c.name AS cityName,\n" +
                    "\n" +
                    "    h.hotel_id AS hotelId,\n" +
                    "    h.name_hotel AS nameHotel,\n" +
                    "    h.address_hotel AS addressHotel,\n" +
                    "    h.image_hotel AS imageHotel,\n" +
                    "    h.rate_hotel AS rateHotel,\n" +
                    "    h.location AS location,\n" +
                    "    h.ward AS ward,\n" +
                    "    h.district AS district,\n" +
                    "    h.latitude AS latitude,\n" +
                    "    h.longitude AS longitude,\n" +
                    "\n" +
                    "    b.label AS badgeLabel,\n" +
                    "\n" +
                    "    r.id AS roomId,\n" +
                    "    r.room_type AS roomType,\n" +
                    "\n" +
                    "    rrp.original_price AS originalPrice,\n" +
                    "    rrp.price AS price,\n" +
                    "    rrp.is_tax_included AS isTaxIncluded,\n" +
                    "\n" +
                    "    p.type AS policyType,\n" +
                    "    p.description AS policyDescription,\n" +
                    "\n" +
                    "    promo.promo_id AS promoId,\n" +
                    "    promo.label AS promoLabel,\n" +
                    "    promo.discount_value AS discountValue,\n" +
                    "\n" +
                    "    v.voucher_code AS voucherCode,\n" +
                    "    v.discount_amount AS discountAmount,\n" +
                    "    v.used_voucher_count AS usedVoucherCount,\n" +
                    "    v.usage_limit AS usageLimit,\n" +
                    "\n" +
                    "    -- Số phòng còn lại\n" +
                    "    MIN(i.stock - i.booked_room) AS availableRooms\n" +
                    "\n" +
                    "FROM city AS c\n" +
                    "\n" +
                    "left JOIN hotel AS h\n" +
                    "    ON h.id_city = c.city_id\n" +
                    "\n" +
                    "left JOIN room AS r\n" +
                    "    ON r.hotel_id = h.hotel_id\n" +
                    "\n" +
                    "LEFT JOIN room_rate_plans AS rrp\n" +
                    "    ON rrp.room_id = r.id\n" +
                    "\n" +
                    "LEFT JOIN badges_hotel AS bh\n" +
                    "    ON bh.hotel_id = h.hotel_id\n" +
                    "\n" +
                    "LEFT JOIN badges AS b\n" +
                    "    ON b.badge_id = bh.badge_id\n" +
                    "\n" +
                    "LEFT JOIN policies AS p\n" +
                    "    ON p.room_rate_plans_id = rrp.rate_plan_id\n" +
                    "\n" +
                    "LEFT JOIN promotions AS promo\n" +
                    "    ON promo.room_rate_plan_id = rrp.rate_plan_id\n" +
                    "\n" +
                    "LEFT JOIN vouchers AS v\n" +
                    "    ON v.promotion_id = promo.promo_id\n" +
                    "\n" +
                    "JOIN room_inventory AS i\n" +
                    "    ON i.room_id = r.id\n" +
                    "\n" +
                    "WHERE\n" +
                    "    (:city IS NULL OR c.name = :city)\n" +
                    "\n" +
                    "    AND (:adults IS NULL OR r.max_adults >= :adults)\n" +
                    "\n" +
                    "    AND (:children IS NULL OR r.max_children >= :children)\n" +
                    "\n" +
                    "    AND (\n" +
                    "        :checkInDate IS NULL\n" +
                    "        OR :checkOutDate IS NULL\n" +
                    "        OR (\n" +
                    "            i.available_room_date >= :checkInDate\n" +
                    "            AND i.available_room_date < :checkOutDate\n" +
                    "        )\n" +
                    "    )\n" +
                    "\n" +
                    "GROUP BY\n" +
                    "    c.city_id,\n" +
                    "    c.name,\n" +
                    "\n" +
                    "    h.hotel_id,\n" +
                    "    h.name_hotel,\n" +
                    "    h.address_hotel,\n" +
                    "    h.image_hotel,\n" +
                    "    h.rate_hotel,\n" +
                    "    h.location,\n" +
                    "    h.ward,\n" +
                    "    h.district,\n" +
                    "    h.latitude,\n" +
                    "    h.longitude,\n" +
                    "\n" +
                    "    b.badge_id,\n" +
                    "    b.label,\n" +
                    "\n" +
                    "    r.id,\n" +
                    "    r.room_type,\n" +
                    "\n" +
                    "    rrp.rate_plan_id,\n" +
                    "    rrp.original_price,\n" +
                    "    rrp.price,\n" +
                    "    rrp.is_tax_included,\n" +
                    "\n" +
                    "    p.policy_id,\n" +
                    "    p.type,\n" +
                    "    p.description,\n" +
                    "\n" +
                    "    promo.promo_id,\n" +
                    "    promo.label,\n" +
                    "    promo.discount_value,\n" +
                    "\n" +
                    "    v.voucher_id,\n" +
                    "    v.voucher_code,\n" +
                    "    v.discount_amount,\n" +
                    "    v.used_voucher_count,\n" +
                    "    v.usage_limit\n" +
                    "\n" +
                    "HAVING\n" +
                    "    COUNT(DISTINCT i.available_room_date) = DATEDIFF(:checkOutDate, :checkInDate)\n" +
                    "\n" +
                    "    AND MIN(i.stock - i.booked_room) >= :rooms\n" +
                    "\n" +
                    "ORDER BY h.hotel_id";
}
