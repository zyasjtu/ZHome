CREATE TABLE zhome_db.ZHome_User
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email NVARCHAR(64) NOT NULL,
    password NVARCHAR(64) NOT NULL,
    createTime DATETIME NOT NULL,
    updateTime DATETIME NOT NULL
);

CREATE UNIQUE INDEX zhome_user_name_uindex ON zhome_db.zhome_user (email);

CREATE TABLE zhome_db.ZHome_Message
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creator NVARCHAR(64) NOT NULL,
    creatorIp NVARCHAR(64) NOT NULL,
    createTime DATETIME NOT NULL,
    text NVARCHAR(1280) NOT NULL,
    imageUrl NVARCHAR(64) DEFAULT NULL
);

CREATE TABLE zhome_db.ZHome_Comment
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creator NVARCHAR(64) NOT NULL,
    creatorIp NVARCHAR(64) NOT NULL,
    replyTo NVARCHAR(64),
    createTime DATETIME NOT NULL,
    text NVARCHAR(640) NOT NULL,
    messageId BIGINT NOT NULL
);