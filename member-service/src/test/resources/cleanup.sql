-- Clean up test data
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM member WHERE email = 'test@example.com';
SET FOREIGN_KEY_CHECKS = 1;

