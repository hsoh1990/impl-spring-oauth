-- create table oauth_client_details
-- (
--   client_id               VARCHAR(256) PRIMARY KEY,
--   resource_ids            VARCHAR(256),
--   client_secret           VARCHAR(256),
--   scope                   VARCHAR(256),
--   authorized_grant_types  VARCHAR(256),
--   web_server_redirect_uri VARCHAR(256),
--   authorities             VARCHAR(256),
--   access_token_validity   INTEGER,
--   refresh_token_validity  INTEGER,
--   additional_information  VARCHAR(4096),
--   autoapprove             VARCHAR(256)
-- );
--

-- -----------------------------------------------------
-- Schema impl_oauth
-- -----------------------------------------------------

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `impl_oauth` DEFAULT CHARACTER SET utf8;
USE `impl_oauth`;

-- -----------------------------------------------------
-- Table `impl_oauth`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `impl_oauth`.`account`
(
  `idx`         BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `account_id`  VARCHAR(255) NOT NULL,
  `company`     VARCHAR(255) NULL     DEFAULT NULL,
  `email`       VARCHAR(255) NOT NULL,
  `join_date`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `name`        VARCHAR(255) NOT NULL,
  `password`    VARCHAR(255) NOT NULL,
  `modify_date` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idx`),
  UNIQUE INDEX `UK_4lde57f579n315au55ua4gqxk` (`account_id` ASC),
  UNIQUE INDEX `UK_q0uja26qgu1atulenwup9rxyr` (`email` ASC)
)
ENGINE = MyISAM
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `impl_oauth`.`account_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `impl_oauth`.`account_roles`
(
  `account_idx` BIGINT(20)   NOT NULL,
  `roles`       VARCHAR(255) NULL DEFAULT NULL,
  INDEX         `fk_account_roles_account_idx` (`account_idx` ASC)
)
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `impl_oauth`.`oauth_client_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `impl_oauth`.`oauth_client_details`
(
  `client_id`               VARCHAR(255)  NOT NULL,
  `access_token_validity`   INT(11)       NULL DEFAULT NULL,
  `additional_information`  VARCHAR(4096) NULL DEFAULT NULL,
  `authorized_grant_types`  VARCHAR(255)  NULL DEFAULT NULL,
  `authorities`             VARCHAR(255)  NULL DEFAULT NULL,
  `autoapprove`             VARCHAR(255)  NULL DEFAULT NULL,
  `client_secret`           VARCHAR(255)  NULL DEFAULT NULL,
  `refresh_token_validity`  INT(11)       NULL DEFAULT NULL,
  `resource_ids`            VARCHAR(255)  NULL DEFAULT NULL,
  `scope`                   VARCHAR(255)  NULL DEFAULT NULL,
  `web_server_redirect_uri` VARCHAR(255)  NULL DEFAULT NULL,
  `name`                    VARCHAR(45)   NULL DEFAULT NULL,
  `account_idx`             BIGINT(20)    NOT NULL,
  PRIMARY KEY (`client_id`),
  INDEX                     `fk_oauth_client_details_account1_idx` (`account_idx` ASC)
)
ENGINE = MyISAM
DEFAULT CHARACTER SET = utf8;

-- -----------------------------------------------------
-- Table `impl_oauth`.`oauth_client_token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `impl_oauth`.`oauth_client_token`
(
  `token_id`          VARCHAR(256) NULL DEFAULT NULL,
  `token`             LONGBLOB     NULL DEFAULT NULL,
  `authentication_id` VARCHAR(256) NOT NULL,
  `user_name`         VARCHAR(256) NULL DEFAULT NULL,
  `client_id`         VARCHAR(256) NULL DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
