package com.example.BookingHotel.sql;

public class SQLInventory {

    public static  final String  AVAILABLE_ROOM_CHECK =
            "select i.room_inventory_id as roomInventoryId,\n" +
                    "\t   i.available_room_date as availableRoomDate,\n" +
                    "       i.booked_room as bookedRoom,\n" +
                    "       i.last_updated as lastUpdated,\n" +
                    "       i.price_multiplier as priceMultiplier,\n" +
                    "       i.stock as stock,\n" +
                    "       i.version as version,\n" +
                    "       i.room_id as roomId\n" +
                    "       from room_inventory as i\n" +
                    "       where i.room_id = :roomId and i.available_room_date >=:checkinDate and  i.available_room_date <=:checkoutDate";
}
