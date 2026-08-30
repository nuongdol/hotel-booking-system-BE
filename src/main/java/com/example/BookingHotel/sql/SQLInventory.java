package com.example.BookingHotel.sql;

public class SQLInventory {

    public static final String  AVAILABLE_ROOM_CHECK =
            "select ri.room_inventory_id as roomInventoryId,\n" +
                    "       ri.booked_room as bookedRoom,\n" +
                    "       ri.last_updated as lastUpdated,\n" +
                    "       ri.price_multiplier as priceMultiplier,\n" +
                    "       ri.stock as stock,\n" +
                    "       ri.version as version,\n" +
                    "       ri.room_id as roomId\n" +
                    "       from room_inventory as ri\n" +
                    "       where ri.room_id = :roomId " +
                    "and ri.available_room_date >=:checkinDate " +
                    "and  ri.available_room_date <=:checkoutDate " +
                    "limit 1 ";
}
