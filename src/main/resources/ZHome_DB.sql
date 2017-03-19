CREATE TABLE ZHome_DB.ZHome_User
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email NVARCHAR(64) NOT NULL,
    password NVARCHAR(64) NOT NULL,
    createTime DATETIME NOT NULL,
    updateTime DATETIME NOT NULL
);

CREATE UNIQUE INDEX ZHome_User_Name_UIndex ON ZHome_DB.ZHome_User (email);

CREATE TABLE ZHome_DB.ZHome_Message
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creator NVARCHAR(64) NOT NULL,
    creatorIp NVARCHAR(64) NOT NULL,
    createTime DATETIME NOT NULL,
    text NVARCHAR(1280) NOT NULL,
    imageUrl NVARCHAR(64) DEFAULT NULL
);

CREATE TABLE ZHome_DB.ZHome_Comment
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creator NVARCHAR(64) NOT NULL,
    creatorIp NVARCHAR(64) NOT NULL,
    replyTo NVARCHAR(64),
    createTime DATETIME NOT NULL,
    text NVARCHAR(640) NOT NULL,
    messageId BIGINT NOT NULL
);

CREATE TABLE ZHome_DB.ZHome_Topic
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subject VARCHAR(64) NOT NULL,
    detail VARCHAR(2048) NOT NULL,
    author VARCHAR(32) NOT NULL,
    authorMobile VARCHAR(32) DEFAULT NULL ,
    authorQQ VARCHAR(32) DEFAULT NULL ,
    authorEmail VARCHAR(32) DEFAULT NULL ,
    authorWechat VARCHAR(32) DEFAULT NULL ,
    authorIp VARCHAR(32) NOT NULL,
    authorType INT NOT NULL,
    createTime DATETIME NOT NULL,
    updateTime DATETIME NOT NULL
);