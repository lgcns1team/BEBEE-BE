-- V4: Rename help_category.name to help_type and update unique constraint

-- 1. DROP EXISTING UNIQUE CONSTRAINT
ALTER TABLE `help_category` DROP INDEX `UQ_HELP_CATEGORY_NAME`;

-- 2. RENAME COLUMN
ALTER TABLE `help_category` CHANGE COLUMN `name` `help_type` VARCHAR(30) NOT NULL;

-- 3. CREATE NEW UNIQUE CONSTRAINT
ALTER TABLE `help_category` ADD CONSTRAINT `UQ_HELP_CATEGORY_TYPE` UNIQUE (`help_type`);
