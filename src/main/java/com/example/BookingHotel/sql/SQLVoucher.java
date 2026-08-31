package com.example.BookingHotel.sql;


public class SQLVoucher {
    public static final String GET_VOUCHERS =
            "SELECT\n" +
                    "    v.voucher_id AS voucherId,\n" +
                    "    v.voucher_code AS voucherCode,\n" +
                    "    v.discount_amount AS discountAmount,\n" +
                    "    v.description AS description,\n" +
                    "    v.used_voucher_count AS usedVoucherCount,\n" +
                    "    v.usage_limit AS usageLimit,\n" +
                    "    v.created_at AS createdAt,\n" +
                    "    v.updated_at AS updatedAt,\n" +
                    "\n" +
                    "    b.badge_id AS badgeId,\n" +
                    "    b.code AS code,\n" +
                    "    b.label AS label\n" +
                    "\n" +
                    "FROM vouchers AS v\n" +
                    "\n" +
                    "JOIN badges_voucher AS bv\n" +
                    "    ON v.voucher_id = bv.voucher_id\n" +
                    "\n" +
                    "JOIN badges AS b\n" +
                    "    ON b.badge_id = bv.badge_id\n" +
                    "\n" +
                    "WHERE v.ended_at >= CURRENT_TIMESTAMP()\n" +
                    "  AND v.status = 1\n" +
                    "\n" +
                    "ORDER BY v.updated_at DESC";
}
