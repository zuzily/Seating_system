DELIMITER $$

# SP1.查詢所有座位資訊（含員工資訊）
DROP PROCEDURE IF EXISTS SP_GET_ALL_SEATS$$
CREATE PROCEDURE SP_GET_ALL_SEATS()
BEGIN
    SELECT
        sc.FLOOR_SEAT_SEQ,
        sc.FLOOR_NO,
        sc.SEAT_NO,
        e.EMP_ID,
        e.NAME,
        e.EMAIL
    FROM SeatingChart sc
    LEFT JOIN Employee e ON sc.FLOOR_SEAT_SEQ = e.FLOOR_SEAT_SEQ
    ORDER BY sc.FLOOR_NO, sc.SEAT_NO;
END$$

# SP2: 依樓層查詢座位資訊
DROP PROCEDURE IF EXISTS SP_GET_SEATS_BY_FLOOR$$
CREATE PROCEDURE SP_GET_SEATS_BY_FLOOR(
    IN p_floor_no INT
)
BEGIN
    SELECT
        sc.FLOOR_SEAT_SEQ,
        sc.FLOOR_NO,
        sc.SEAT_NO,
        e.EMP_ID,
        e.NAME,
        e.EMAIL
    FROM SeatingChart sc
    LEFT JOIN Employee e ON sc.FLOOR_SEAT_SEQ = e.FLOOR_SEAT_SEQ
    WHERE sc.FLOOR_NO = p_floor_no
    ORDER BY sc.SEAT_NO;
END$$

# SP3: 查詢所有員工資訊
DROP PROCEDURE IF EXISTS SP_GET_ALL_EMPLOYEES$$
CREATE PROCEDURE SP_GET_ALL_EMPLOYEES()
BEGIN
    SELECT
        e.EMP_ID,
        e.NAME,
        e.EMAIL,
        e.FLOOR_SEAT_SEQ,
        sc.FLOOR_NO,
        sc.SEAT_NO
    FROM Employee e
    LEFT JOIN SeatingChart sc ON e.FLOOR_SEAT_SEQ = sc.FLOOR_SEAT_SEQ
    ORDER BY e.EMP_ID;
END$$

# SP4: 查詢未分配座位的員工
DROP PROCEDURE IF EXISTS SP_GET_UNASSIGNED_EMPLOYEES$$
CREATE PROCEDURE SP_GET_UNASSIGNED_EMPLOYEES()
BEGIN
    SELECT
        EMP_ID,
        NAME,
        EMAIL
    FROM Employee
    WHERE FLOOR_SEAT_SEQ IS NULL
    ORDER BY EMP_ID;
END$$

# SP5: 分配座位給員工（含交易處理）
DROP PROCEDURE IF EXISTS SP_ASSIGN_SEAT$$
CREATE PROCEDURE SP_ASSIGN_SEAT(
    IN p_emp_id VARCHAR(5),
    IN p_floor_seat_seq INT,
    OUT p_result INT,
    OUT p_message VARCHAR(200)
)
BEGIN
    DECLARE v_current_seat INT;
    DECLARE v_seat_occupied INT;
    DECLARE v_employee_exists INT;
    DECLARE v_seat_exists INT;
    DECLARE v_error_msg TEXT;

#     錯誤處理
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            GET DIAGNOSTICS CONDITION 1 v_error_msg = MESSAGE_TEXT;
            ROLLBACK;
            SET p_result = -1;
            SET p_message = CONCAT('系統錯誤，交易已中止: ', IFNULL(v_error_msg, '未知錯誤'));
        END;

    START TRANSACTION;

#     1. 檢查員工是否存在
    SELECT COUNT(*) INTO v_employee_exists
    FROM Employee
    WHERE EMP_ID = p_emp_id;

    IF v_employee_exists = 0 THEN
        SET p_result = 0;
        SET p_message = '員工編號不存在';
        ROLLBACK;
    ELSE
#         2. 檢查座位是否存在
        SELECT COUNT(*) INTO v_seat_exists
        FROM SeatingChart
        WHERE FLOOR_SEAT_SEQ = p_floor_seat_seq;

        IF v_seat_exists = 0 THEN
            SET p_result = 0;
            SET p_message = '座位不存在';
            ROLLBACK;
        ELSE
#             3. 檢查座位是否已被佔用
            SELECT COUNT(*) INTO v_seat_occupied
            FROM Employee
            WHERE FLOOR_SEAT_SEQ = p_floor_seat_seq;

            IF v_seat_occupied > 0 THEN
                SET p_result = 0;
                SET p_message = '座位已被其他員工佔用';
                ROLLBACK;
            ELSE
#                 4. 取得員工目前的座位
                SELECT FLOOR_SEAT_SEQ INTO v_current_seat
                FROM Employee
                WHERE EMP_ID = p_emp_id;

#                 5. 更新員工座位
                UPDATE Employee
                SET FLOOR_SEAT_SEQ = p_floor_seat_seq,
                    UPDATED_DATE = CURRENT_TIMESTAMP
                WHERE EMP_ID = p_emp_id;

                COMMIT;
                SET p_result = 1;
                SET p_message = '座位分配成功';
            END IF;
        END IF;
    END IF;
END$$

# SP6: 清除員工座位（含交易處理）
DROP PROCEDURE IF EXISTS SP_CLEAR_SEAT$$
CREATE PROCEDURE SP_CLEAR_SEAT(
    IN p_emp_id VARCHAR(5),
    OUT p_result INT,
    OUT p_message VARCHAR(200)
)
BEGIN
    DECLARE v_employee_exists INT;
    DECLARE v_has_seat INT;
    DECLARE v_error_msg TEXT;

#     錯誤處理
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            GET DIAGNOSTICS CONDITION 1 v_error_msg = MESSAGE_TEXT;
            ROLLBACK;
            SET p_result = -1;
            SET p_message = CONCAT('系統錯誤，交易已中止: ', IFNULL(v_error_msg, '未知錯誤'));
        END;

    START TRANSACTION;

#     1. 檢查員工是否存在
    SELECT COUNT(*) INTO v_employee_exists
    FROM Employee
    WHERE EMP_ID = p_emp_id;

    IF v_employee_exists = 0 THEN
        SET p_result = 0;
        SET p_message = '員工編號不存在';
        ROLLBACK;
    ELSE
#         2. 檢查員工是否有座位
        SELECT COUNT(*) INTO v_has_seat
        FROM Employee
        WHERE EMP_ID = p_emp_id AND FLOOR_SEAT_SEQ IS NOT NULL;

        IF v_has_seat = 0 THEN
            SET p_result = 0;
            SET p_message = '該員工目前無座位';
            ROLLBACK;
        ELSE
#             3. 清除員工座位
            UPDATE Employee
            SET FLOOR_SEAT_SEQ = NULL,
                UPDATED_DATE = CURRENT_TIMESTAMP
            WHERE EMP_ID = p_emp_id;

            COMMIT;
            SET p_result = 1;
            SET p_message = '座位清除成功';
        END IF;
    END IF;
END$$

# SP7: 批次更新座位（含交易處理）
DROP PROCEDURE IF EXISTS SP_BATCH_UPDATE_SEATS$$
CREATE PROCEDURE SP_BATCH_UPDATE_SEATS(
    IN p_updates JSON,
    OUT p_result INT,
    OUT p_message VARCHAR(500)
)
BEGIN
    DECLARE v_idx INT DEFAULT 0;
    DECLARE v_length INT;
    DECLARE v_emp_id VARCHAR(5);
    DECLARE v_floor_seat_seq INT;
    DECLARE v_action VARCHAR(10);
    DECLARE v_error_msg VARCHAR(500) DEFAULT '';

# 錯誤處理
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
    ROLLBACK;
    SET p_result = -1;
    SET p_message = CONCAT('系統錯誤，交易已中止: ', v_error_msg);
    END;

    START TRANSACTION;

# 取得 JSON 陣列長度
    SET v_length = JSON_LENGTH(p_updates);
#     逐筆處理更新
        WHILE v_idx < v_length DO
            SET v_emp_id = JSON_UNQUOTE(JSON_EXTRACT(p_updates, CONCAT('$[', v_idx, '].empId')));
            SET v_floor_seat_seq = JSON_EXTRACT(p_updates, CONCAT('$[', v_idx, '].floorSeatSeq'));
            SET v_action = JSON_UNQUOTE(JSON_EXTRACT(p_updates, CONCAT('$[', v_idx, '].action')));
        IF v_action = 'assign' THEN
#             分配座位
            UPDATE Employee
            SET FLOOR_SEAT_SEQ = v_floor_seat_seq,
                UPDATED_DATE = CURRENT_TIMESTAMP
            WHERE EMP_ID = v_emp_id;
        ELSEIF v_action = 'clear' THEN
#             清除座位
            UPDATE Employee
            SET FLOOR_SEAT_SEQ = NULL,
                UPDATED_DATE = CURRENT_TIMESTAMP
            WHERE EMP_ID = v_emp_id;
        END IF;
        SET v_idx = v_idx + 1;
    END WHILE;

    COMMIT;
    SET p_result = 1;
    SET p_message = CONCAT('成功處理 ', v_length, ' 筆座位更新');
END$$

# SP8: 查詢座位統計資訊
DROP PROCEDURE IF EXISTS SP_GET_SEAT_STATISTICS$$
CREATE PROCEDURE SP_GET_SEAT_STATISTICS()
BEGIN
    SELECT
        sc.FLOOR_NO                                                            AS floor_no,
        COUNT(*)                                                               AS total_seats,
        SUM(IF(e.FLOOR_SEAT_SEQ IS NOT NULL, 1, 0))                            AS occupied_seats,
        SUM(IF(e.FLOOR_SEAT_SEQ IS NULL, 1, 0))                                AS available_seats,
        ROUND(SUM(IF(e.FLOOR_SEAT_SEQ IS NOT NULL, 1, 0)) / COUNT(*) * 100, 2) AS occupancy_rate
    FROM SeatingChart sc LEFT JOIN Employee e ON sc.FLOOR_SEAT_SEQ = e.FLOOR_SEAT_SEQ
    GROUP BY sc.FLOOR_NO
    ORDER BY sc.FLOOR_NO;
END$$

DELIMITER ;