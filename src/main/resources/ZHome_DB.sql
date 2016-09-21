CREATE TABLE zhome_db.ZHome_User
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name NVARCHAR(64) NOT NULL,
    password NVARCHAR(64) NOT NULL
);
CREATE UNIQUE INDEX zhome_user_name_uindex ON zhome_db.zhome_user (name);

CREATE TABLE zhome_db.ZHome_Message
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creator NVARCHAR(64) NOT NULL,
    createIp NVARCHAR(64) NOT NULL,
    createTime DATETIME NOT NULL,
    updateTime DATETIME NOT NULL,
    text NVARCHAR(1280) NOT NULL,
    imageUrl NVARCHAR(64) DEFAULT NULL
);