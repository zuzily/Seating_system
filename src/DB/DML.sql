# 1. 清空現有資料
# DELETE FROM Employee;
# DELETE FROM SeatingChart;


# 2. 插入樓層座位資料

# 1樓座位（4個座位）
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO)
VALUES(1, '座位1'),(1, '座位2'),(1, '座位3'),(1, '座位4');

# 2樓座位（4個座位）
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO)
VALUES(2, '座位1'),(2, '座位2'),(2, '座位3'),(2, '座位4');

# 3樓座位（4個座位）
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO)
VALUES(3, '座位1'),(3, '座位2'),(3, '座位3'),(3, '座位4');

# 4樓座位（4個座位）
INSERT INTO SeatingChart (FLOOR_NO, SEAT_NO)
VALUES(4, '座位1'),(4, '座位2'),(4, '座位3'),(4, '座位4');


# 3. 插入員工資料

# 已分配座位的員工
INSERT INTO Employee (EMP_ID, NAME, EMAIL, FLOOR_SEAT_SEQ)
VALUES
   ('12006', '蘋果', 'apple@esunbank.com', 3),      -- 1樓座位3
   ('16142', '香蕉', 'banana@esunbank.com', 7),     -- 2樓座位3
   ('13040', '草莓', 'strawberry@esunbank.com', 9), -- 3樓座位1
   ('17081', '西瓜', 'watermelon@esunbank.com', 10),-- 3樓座位2
   ('11221', '芒果', 'mango@esunbank.com', 12),     -- 3樓座位4
   ('16722', '鳳梨', 'pineapple@esunbank.com', 15); -- 4樓座位3

# 未分配座位的員工
INSERT INTO Employee (EMP_ID, NAME, EMAIL, FLOOR_SEAT_SEQ)
VALUES
   ('10001', '葡萄', 'grape@esunbank.com', NULL),
   ('10002', '櫻桃', 'cherry@esunbank.com', NULL),
   ('10003', '水蜜桃', 'peach@esunbank.com', NULL),
   ('10004', '奇異果', 'kiwi@esunbank.com', NULL),
   ('10005', '柳橙', 'orange@esunbank.com', NULL),
   ('10006', '檸檬', 'lemon@esunbank.com', NULL),
   ('10007', '火龍果', 'dragonfruit@esunbank.com', NULL),
   ('10008', '藍莓', 'blueberry@esunbank.com', NULL);

# 4. 驗證資料
# 查詢所有座位資訊
SELECT
    sc.FLOOR_SEAT_SEQ,
    sc.FLOOR_NO,
    sc.SEAT_NO,
    e.EMP_ID,
    e.NAME
FROM SeatingChart sc LEFT JOIN Employee e ON sc.FLOOR_SEAT_SEQ = e.FLOOR_SEAT_SEQ
ORDER BY sc.FLOOR_NO, sc.SEAT_NO;

# 查詢所有員工資訊
SELECT
    EMP_ID,
    NAME,
    EMAIL,
    FLOOR_SEAT_SEQ
FROM Employee
ORDER BY EMP_ID;

# 5. 資料統計
# 總座位數
SELECT COUNT(*) AS total_seats FROM SeatingChart;

# 已佔用座位數
SELECT COUNT(*) AS occupied_seats FROM SeatingChart sc INNER JOIN Employee e ON sc.FLOOR_SEAT_SEQ = e.FLOOR_SEAT_SEQ;

# 空位數
SELECT COUNT(*) AS available_seats FROM SeatingChart sc LEFT JOIN Employee e ON sc.FLOOR_SEAT_SEQ = e.FLOOR_SEAT_SEQ
WHERE e.FLOOR_SEAT_SEQ IS NULL;

# 各樓層座位使用情況
SELECT
    sc.FLOOR_NO,
    COUNT(*) AS total_seats,
    SUM(CASE WHEN e.FLOOR_SEAT_SEQ IS NOT NULL THEN 1 ELSE 0 END) AS occupied,
    SUM(CASE WHEN e.FLOOR_SEAT_SEQ IS NULL THEN 1 ELSE 0 END) AS available
FROM SeatingChart sc
         LEFT JOIN Employee e ON sc.FLOOR_SEAT_SEQ = e.FLOOR_SEAT_SEQ
GROUP BY sc.FLOOR_NO
ORDER BY sc.FLOOR_NO;