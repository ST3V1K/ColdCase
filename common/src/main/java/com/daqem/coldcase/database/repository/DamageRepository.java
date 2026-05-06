package com.daqem.coldcase.database.repository;

import com.daqem.coldcase.ColdCase;
import com.daqem.coldcase.database.Database;
import com.daqem.coldcase.model.DamageLog;
import com.daqem.coldcase.util.ItemStackSerializer;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DamageRepository extends Repository {

    private final Database database;

    public DamageRepository(Database database) {
        this.database = database;
    }

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS damages (
                    id text PRIMARY KEY NOT NULL,
                    time integer NOT NULL,
                    type varchar(255) NOT NULL,
                    attacker integer,
                    target integer NOT NULL,
                    attacker_x real,
                    attacker_y real,
                    attacker_z real,
                    attacker_pitch real,
                    attacker_yaw real,
                    target_x real NOT NULL,
                    target_y real NOT NULL,
                    target_z real NOT NULL,
                    target_pitch real NOT NULL,
                    target_yaw real NOT NULL,
                    level integer NOT NULL,
                    attacker_head text,
                    attacker_chest text,
                    attacker_legs text,
                    attacker_feet text,
                    attacker_main_hand text,
                    attacker_off_hand text,
                    target_head text,
                    target_chest text,
                    target_legs text,
                    target_feet text,
                    target_main_hand text,
                    target_off_hand text,
                    damage_amount real NOT NULL,
                    health_left real NOT NULL,
                    attacker_type text,
                    FOREIGN KEY(attacker) REFERENCES users(id),
                    FOREIGN KEY(target) REFERENCES users(id),
                    FOREIGN KEY(level) REFERENCES levels(id)
                );
                """;
        if (isMysql()) {
            sql = """
                    CREATE TABLE IF NOT EXISTS damages (
                        id varchar(36) PRIMARY KEY NOT NULL,
                        time bigint NOT NULL,
                        type varchar(255) NOT NULL,
                        attacker int,
                        target int NOT NULL,
                        attacker_x float,
                        attacker_y float,
                        attacker_z float,
                        attacker_pitch float,
                        attacker_yaw float,
                        target_x float NOT NULL,
                        target_y float NOT NULL,
                        target_z float NOT NULL,
                        target_pitch float NOT NULL,
                        target_yaw float NOT NULL,
                        level int NOT NULL,
                        attacker_head text,
                        attacker_chest text,
                        attacker_legs text,
                        attacker_feet text,
                        attacker_main_hand text,
                        attacker_off_hand text,
                        target_head text,
                        target_chest text,
                        target_legs text,
                        target_feet text,
                        target_main_hand text,
                        target_off_hand text,
                        damage_amount float NOT NULL,
                        health_left float NOT NULL,
                        attacker_type text,
                        FOREIGN KEY(attacker) REFERENCES users(id),
                        FOREIGN KEY(target) REFERENCES users(id),
                        FOREIGN KEY(level) REFERENCES levels(id)
                    )
                    ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4;
                    """;
        }
        database.createTable(sql);
    }

    public void createIndexes() {
        String sql = """
                CREATE INDEX IF NOT EXISTS coordinates ON damages (target_x, target_y, target_z);
                """;
        if (isMysql()) {
            sql = """
                    ALTER TABLE damages ADD INDEX coordinates (target_x, target_y, target_z);
                    """;
        }
        database.execute(sql, false);
    }

    public void insert(String id, long time, String type, String attackerUuid, String attackerType, String targetUuid, Float attackerX, Float attackerY, Float attackerZ, Float attackerPitch, Float attackerYaw, float targetX, float targetY, float targetZ, float targetPitch, float targetYaw, String levelName,
                       String attackerHead, String attackerChest, String attackerLegs, String attackerFeet, String attackerMainHand, String attackerOffHand,
                       String targetHead, String targetChest, String targetLegs, String targetFeet, String targetMainHand, String targetOffHand,
                       float damageAmount, float healthLeft) {
        String query = """
                INSERT OR IGNORE INTO damages(id, time, type, attacker, attacker_type, target, attacker_x, attacker_y, attacker_z, attacker_pitch, attacker_yaw, target_x, target_y, target_z, target_pitch, target_yaw, level,
                    attacker_head, attacker_chest, attacker_legs, attacker_feet, attacker_main_hand, attacker_off_hand,
                    target_head, target_chest, target_legs, target_feet, target_main_hand, target_off_hand,
                    damage_amount, health_left)
                VALUES(?, ?, ?, (
                    SELECT id FROM users WHERE uuid = ?
                ), ?, (
                    SELECT id FROM users WHERE uuid = ?
                ), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (
                    SELECT id FROM levels WHERE name = ?
                ), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;

        if (isMysql()) {
            query = """
                    INSERT IGNORE INTO damages(id, time, type, attacker, attacker_type, target, attacker_x, attacker_y, attacker_z, attacker_pitch, attacker_yaw, target_x, target_y, target_z, target_pitch, target_yaw, level,
                        attacker_head, attacker_chest, attacker_legs, attacker_feet, attacker_main_hand, attacker_off_hand,
                        target_head, target_chest, target_legs, target_feet, target_main_hand, target_off_hand,
                        damage_amount, health_left)
                    VALUES(?, ?, ?, (
                        SELECT id FROM users WHERE uuid = ?
                    ), ?, (
                        SELECT id FROM users WHERE uuid = ?
                    ), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (
                        SELECT id FROM levels WHERE name = ?
                    ), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                    """;
        }

        try {
            PreparedStatement preparedStatement = database.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setLong(2, time);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, attackerUuid);
            preparedStatement.setString(5, attackerType);
            preparedStatement.setString(6, targetUuid);

            if (attackerX != null) preparedStatement.setFloat(7, attackerX);
            else preparedStatement.setNull(7, Types.FLOAT);

            if (attackerY != null) preparedStatement.setFloat(8, attackerY);
            else preparedStatement.setNull(8, Types.FLOAT);

            if (attackerZ != null) preparedStatement.setFloat(9, attackerZ);
            else preparedStatement.setNull(9, Types.FLOAT);

            if (attackerPitch != null) preparedStatement.setFloat(10, attackerPitch);
            else preparedStatement.setNull(10, Types.FLOAT);

            if (attackerYaw != null) preparedStatement.setFloat(11, attackerYaw);
            else preparedStatement.setNull(11, Types.FLOAT);

            preparedStatement.setFloat(12, targetX);
            preparedStatement.setFloat(13, targetY);
            preparedStatement.setFloat(14, targetZ);
            preparedStatement.setFloat(15, targetPitch);
            preparedStatement.setFloat(16, targetYaw);
            preparedStatement.setString(17, levelName);
            preparedStatement.setString(18, attackerHead);
            preparedStatement.setString(19, attackerChest);
            preparedStatement.setString(20, attackerLegs);
            preparedStatement.setString(21, attackerFeet);
            preparedStatement.setString(22, attackerMainHand);
            preparedStatement.setString(23, attackerOffHand);
            preparedStatement.setString(24, targetHead);
            preparedStatement.setString(25, targetChest);
            preparedStatement.setString(26, targetLegs);
            preparedStatement.setString(27, targetFeet);
            preparedStatement.setString(28, targetMainHand);
            preparedStatement.setString(29, targetOffHand);
            preparedStatement.setFloat(30, damageAmount);
            preparedStatement.setFloat(31, healthLeft);

            database.queue.add(preparedStatement);
        } catch (SQLException exception) {
            ColdCase.LOGGER.error("Failed to insert damage into database", exception);
        }
    }

    @Nullable
    public List<DamageLog> getDamageHistory(HolderLookup.Provider provider, @Nullable UUID playerUuid, @Nullable Long startTime, @Nullable Long endTime) {
        StringBuilder queryBuilder = new StringBuilder("""
                SELECT d.*,
                       au.uuid as attacker_uuid, au.name as attacker_name,
                       tu.uuid as target_uuid, tu.name as target_name,
                       l.name as level_name
                FROM damages d
                LEFT JOIN users au ON d.attacker = au.id
                LEFT JOIN users tu ON d.target = tu.id
                LEFT JOIN levels l ON d.level = l.id
                """);

        List<Object> params = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        if (playerUuid != null) {
            conditions.add("(au.uuid = ? OR tu.uuid = ?)");
            params.add(playerUuid.toString());
            params.add(playerUuid.toString());
        }
        if (startTime != null) {
            conditions.add("d.time >= ?");
            params.add(startTime);
        }
        if (endTime != null) {
            conditions.add("d.time <= ?");
            params.add(endTime);
        }

        if (!conditions.isEmpty()) {
            queryBuilder.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        queryBuilder.append(" ORDER BY d.time DESC;");

        try {
            PreparedStatement preparedStatement = database.prepareStatement(queryBuilder.toString());
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            ResultSet rs = preparedStatement.executeQuery();
            List<DamageLog> damageLogs = new ArrayList<>();
            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                long time = rs.getLong("time");
                String type = rs.getString("type");

                UUID attackerUuid = rs.getString("attacker_uuid") != null ? UUID.fromString(rs.getString("attacker_uuid")) : null;
                String attackerName = rs.getString("attacker_name");
                String attackerType = rs.getString("attacker_type");

                UUID targetUuid = UUID.fromString(rs.getString("target_uuid"));
                String targetName = rs.getString("target_name");

                Float attackerX = getNullableFloat(rs, "attacker_x"); // rs.getObject("attacker_x", Float.class);
                Float attackerY = getNullableFloat(rs, "attacker_y"); // rs.getObject("attacker_y", Float.class);
                Float attackerZ = getNullableFloat(rs, "attacker_z"); // rs.getObject("attacker_z", Float.class);
                Vec3 attackerPos = (attackerX != null && attackerY != null && attackerZ != null) ? new Vec3(attackerX, attackerY, attackerZ) : null;

                Float attackerPitch = getNullableFloat(rs, "attacker_pitch"); // rs.getObject("attacker_pitch", Float.class);
                Float attackerYaw = getNullableFloat(rs, "attacker_yaw"); // rs.getObject("attacker_yaw", Float.class);

                float targetX = rs.getFloat("target_x");
                float targetY = rs.getFloat("target_y");
                float targetZ = rs.getFloat("target_z");
                Vec3 targetPos = new Vec3(targetX, targetY, targetZ);

                float targetPitch = rs.getFloat("target_pitch");
                float targetYaw = rs.getFloat("target_yaw");

                String levelName = rs.getString("level_name");

                Map<EquipmentSlot, ItemStack> attackerEquipment = new HashMap<>();
                attackerEquipment.put(EquipmentSlot.HEAD, ItemStackSerializer.deserialize(rs.getString("attacker_head"), provider));
                attackerEquipment.put(EquipmentSlot.CHEST, ItemStackSerializer.deserialize(rs.getString("attacker_chest"), provider));
                attackerEquipment.put(EquipmentSlot.LEGS, ItemStackSerializer.deserialize(rs.getString("attacker_legs"), provider));
                attackerEquipment.put(EquipmentSlot.FEET, ItemStackSerializer.deserialize(rs.getString("attacker_feet"), provider));
                attackerEquipment.put(EquipmentSlot.MAINHAND, ItemStackSerializer.deserialize(rs.getString("attacker_main_hand"), provider));
                attackerEquipment.put(EquipmentSlot.OFFHAND, ItemStackSerializer.deserialize(rs.getString("attacker_off_hand"), provider));

                Map<EquipmentSlot, ItemStack> targetEquipment = new HashMap<>();
                targetEquipment.put(EquipmentSlot.HEAD, ItemStackSerializer.deserialize(rs.getString("target_head"), provider));
                targetEquipment.put(EquipmentSlot.CHEST, ItemStackSerializer.deserialize(rs.getString("target_chest"), provider));
                targetEquipment.put(EquipmentSlot.LEGS, ItemStackSerializer.deserialize(rs.getString("target_legs"), provider));
                targetEquipment.put(EquipmentSlot.FEET, ItemStackSerializer.deserialize(rs.getString("target_feet"), provider));
                targetEquipment.put(EquipmentSlot.MAINHAND, ItemStackSerializer.deserialize(rs.getString("target_main_hand"), provider));
                targetEquipment.put(EquipmentSlot.OFFHAND, ItemStackSerializer.deserialize(rs.getString("target_off_hand"), provider));

                float damageAmount = rs.getFloat("damage_amount");
                float healthLeft = rs.getFloat("health_left");

                damageLogs.add(new DamageLog(
                        id, time, type, attackerUuid, attackerName, attackerType,
                        targetUuid, targetName,
                        attackerPos, attackerPitch != null ? attackerPitch : 0.0f, attackerYaw != null ? attackerYaw : 0.0f,
                        targetPos, targetPitch, targetYaw, levelName,
                        attackerEquipment, targetEquipment, damageAmount, healthLeft
                ));
            }
            return damageLogs;
        } catch (SQLException exception) {
            ColdCase.LOGGER.error("Failed to get damage logs from database", exception);
        }
        return null;
    }

    @Nullable
    private Float getNullableFloat(ResultSet rs, String column) throws SQLException {
        float value = rs.getFloat(column);
        return rs.wasNull() ? null : value;
    }
}