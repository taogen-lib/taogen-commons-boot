/*
 user
 */
insert into user (id, name, age, dept_id, hobby_ids, area_id)
values (1, 'Jone', 18, 1, '1,2,3', 1);
insert into user (id, name, age, dept_id, hobby_ids, area_id)
values (2, 'Jack', 20, 2, '2,3,4', 2);
insert into user (id, name, age, dept_id, hobby_ids, area_id)
values (3, 'Tom', 28, 3, '3,4,5', 3);
insert into user (id, name, age, dept_id, hobby_ids, area_id)
values (4, 'Jerry', 32, 4, '4,5,6', 4);

/*
 department
 */
insert into department (id, name)
values (1, 'Technology');
insert into department (id, name)
values (2, 'Sales');
insert into department (id, name)
values (3, 'Finance');
insert into department (id, name)
values (4, 'Operation');
insert into department (id, name)
values (5, 'HR');
insert into department (id, name)
values (6, 'Marketing');

/*
 hobby
 */
insert into hobby (id, name)
values (1, 'Soccer');
insert into hobby (id, name)
values (2, 'Basketball');
insert into hobby (id, name)
values (3, 'Swimming');
insert into hobby (id, name)
values (4, 'Reading');
insert into hobby (id, name)
values (5, 'Traveling');
insert into hobby (id, name)
values (6, 'Coding');

/*
 area
 */
insert into area (id, name, code, parent_id)
values (1, 'China', 'CN', 0);
insert into area (id, name, code, parent_id)
values (2, 'Beijing', 'BJ', 1);
insert into area (id, name, code, parent_id)
values (3, 'Shanghai', 'SH', 1);
insert into area (id, name, code, parent_id)
values (4, 'Haidian', 'HD', 2);
insert into area (id, name, code, parent_id)
values (5, 'Chaoyang', 'CY', 2);
insert into area (id, name, code, parent_id)
values (6, 'Pudong', 'PD', 3);
insert into area (id, name, code, parent_id)
values (7, 'Xuhui', 'XH', 3);

/*
 role
 */
insert into role (id, name)
values (1, 'Admin');
insert into role (id, name)
values (2, 'User');
insert into role (id, name)
values (3, 'Guest');

insert into user_role (user_id, role_id)
values (1, 1);
insert into user_role (user_id, role_id)
values (1, 2);
insert into user_role (user_id, role_id)
values (2, 2);
insert into user_role (user_id, role_id)
values (3, 1);
