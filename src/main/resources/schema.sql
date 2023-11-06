-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema csye62252
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema csye62252
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `csye6225sw` DEFAULT CHARACTER SET utf8 ;
USE `csye6225sw` ;


-- -----------------------------------------------------
-- Table `mydb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `csye6225sw`.`users` (
                                                    `id` INT NOT NULL AUTO_INCREMENT,
                                                    `first_name` VARCHAR(45) NOT NULL,
    `last_name` VARCHAR(45) NOT NULL,
    `email` VARCHAR(225) NOT NULL,
    `password` CHAR(68) NOT NULL,
    `account_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    `account_updated` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `role` VARCHAR(45) NULL DEFAULT 'user',
    `enabled` TINYINT NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `csye62252`.`assignment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `csye6225sw`.`assignment` (
                                                         `id` VARCHAR(225) NOT NULL,
                                                         `name` VARCHAR(45) NOT NULL,
    `points` CHAR(25) NOT NULL,
    `num_of_attemps` CHAR(25) NOT NULL,
    `deadline` DATETIME NOT NULL,
    `assignment_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    `assignment_updated` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `users_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_assignment_users_idx` (`users_id` ASC) VISIBLE,
    UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
