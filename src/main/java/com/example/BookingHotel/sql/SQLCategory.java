package com.example.BookingHotel.sql;

public class SQLCategory {
    public static final String GET_LIST_CATEGORIES =
            "select \n" +
                    "\tDISTINCT ca.category_id as categoryId,\n" +
                    "    ca.created_at as createdAt,\n" +
                    "    ca.icon as icon,\n" +
                    "    ca.label as label,\n" +
                    "    ca.updated_at as updatedAt,\n" +
                    "    ba.code as code,\n" +
                    "    ba.icon_url as iconUrl,\n" +
                    "    ba.label as labelBadge\n" +
                    "from categories as ca\n" +
                    "left join badges_categories as bc on ca.category_id = bc.category_id\n" +
                    "left join badges as ba on ba.badge_id = bc.badge_id";
}
