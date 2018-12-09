# --- !Ups
CREATE TABLE `tasks` (
    `id` VARCHAR(255) PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `is_finished` BOOLEAN NOT NULL,
    `user_id` VARCHAR(255) NOT NULL,
    `deadline` DATETIME,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`)
);

# --- !Downs
DROP TABLE `tasks`;
