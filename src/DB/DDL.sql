# 刪除現有表格（如果存在）
# DROP TABLE IF EXISTS Employee;
# DROP TABLE IF EXISTS SeatingChart;


# SeatingChart 樓層座位表

CREATE TABLE SeatingChart (
                              FLOOR_SEAT_SEQ INT AUTO_INCREMENT PRIMARY KEY COMMENT '座位序號（主鍵）',
                              FLOOR_NO INT NOT NULL COMMENT '樓層編號',
                              SEAT_NO VARCHAR(10) NOT NULL COMMENT '座位編號',
                              CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
                              UPDATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
                              UNIQUE KEY UK_FLOOR_SEAT (FLOOR_NO, SEAT_NO)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='樓層座位表';

CREATE INDEX IDX_FLOOR_NO ON SeatingChart(FLOOR_NO);

# Employee 員工資料表
CREATE TABLE Employee (
                          EMP_ID VARCHAR(5) PRIMARY KEY COMMENT '員工編號（固定5碼）',
                          NAME VARCHAR(100) NOT NULL COMMENT '員工姓名',
                          EMAIL VARCHAR(100) NOT NULL COMMENT '員工電子郵件',
                          FLOOR_SEAT_SEQ INT NULL COMMENT '座位資訊（外鍵）',
                          CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '建立時間',
                          UPDATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
                          CONSTRAINT FK_EMPLOYEE_SEAT FOREIGN KEY (FLOOR_SEAT_SEQ)
                              REFERENCES SeatingChart(FLOOR_SEAT_SEQ)
                              ON DELETE SET NULL
                              ON UPDATE CASCADE,
                          CONSTRAINT CHK_EMP_ID_LENGTH CHECK (CHAR_LENGTH(EMP_ID) = 5),
                          CONSTRAINT UK_EMAIL UNIQUE (EMAIL),
                          CONSTRAINT UK_SEAT UNIQUE (FLOOR_SEAT_SEQ)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='員工資料表';

CREATE INDEX IDX_FLOOR_SEAT_SEQ ON Employee(FLOOR_SEAT_SEQ);
