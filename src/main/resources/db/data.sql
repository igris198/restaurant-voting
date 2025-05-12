DELETE FROM user_role;
DELETE FROM voting;
DELETE FROM users;
DELETE FROM meal;
DELETE FROM menu;
DELETE FROM restaurant;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '$2a$10$C8XHUDql9pRRWZGCrdPouufaY8KrpjfC5dQuu573iorYhDUN501Km'),      /* 100000 pass: password */
       ('Admin', 'admin@gmail.com', '$2a$10$3AC7nMQnJuXjPAssQy2frOvS/rkzVESLxOjJ5wiE8JTjyMtXCvwe6'),    /* 100001 pass: admin */
       ('Guest', 'guest@gmail.com', '$2a$10$noLk0msQB6fmeJCEGTu9yun.Sr8heXFa5nTSkm84uHd3xtdEwK2B2');    /* 100002 pass: guest */

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('USER', 100001),
       ('ADMIN', 100001);

INSERT INTO restaurant (name)
VALUES ('First resto'),     /* 100003 */
       ('Second resto'),    /* 100004 */
       ('Третий ресторан'); /* 100005 */

INSERT INTO menu (restaurant_id, menu_date)
VALUES (100003, DATEADD('DAY', -1, CURRENT_DATE)),      /* 100006 */
       (100004, DATEADD('DAY', -1, CURRENT_DATE)),      /* 100007 */
       (100003, CURRENT_DATE),                          /* 100008 */
       (100004, CURRENT_DATE),                          /* 100009 */
       (100005, CURRENT_DATE);                          /* 100010 */

INSERT INTO meal (menu_id, name, price)
VALUES (100006, 'Завтрак', 500.01),                     /* 100011 */
       (100006, 'Обед', 1000.02),                       /* 100012 */
       (100007, 'Ужин', 500.03),                        /* 100013 */
       (100008, 'Еда на граничное значение', 100.00),   /* 100014 */
       (100008, 'Завтрак', 500),                        /* 100015 */
       (100009, 'Обед', 1000.10),                       /* 100016 */
       (100009, 'Ужин', 510.21);                        /* 100017 */

INSERT INTO voting (user_id, restaurant_id, voting_date)
VALUES (100001, 100003, CURRENT_DATE),                      /* 100018 */
       (100000, 100003, DATEADD('DAY', -1, CURRENT_DATE));   /* 100019 */