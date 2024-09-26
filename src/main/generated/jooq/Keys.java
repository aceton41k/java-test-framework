/*
 * This file is generated by jOOQ.
 */
package jooq;


import jooq.tables.Comments;
import jooq.tables.Posts;
import jooq.tables.Roles;
import jooq.tables.Tasks;
import jooq.tables.Users;
import jooq.tables.records.CommentsRecord;
import jooq.tables.records.PostsRecord;
import jooq.tables.records.RolesRecord;
import jooq.tables.records.TasksRecord;
import jooq.tables.records.UsersRecord;

import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<CommentsRecord> COMMENTS_PKEY = Internal.createUniqueKey(Comments.COMMENTS, DSL.name("comments_pkey"), new TableField[] { Comments.COMMENTS.ID }, true);
    public static final UniqueKey<PostsRecord> POSTS_PK = Internal.createUniqueKey(Posts.POSTS, DSL.name("posts_pk"), new TableField[] { Posts.POSTS.ID }, true);
    public static final UniqueKey<RolesRecord> ROLES_PKEY = Internal.createUniqueKey(Roles.ROLES, DSL.name("roles_pkey"), new TableField[] { Roles.ROLES.ID }, true);
    public static final UniqueKey<RolesRecord> UKOFX66KERUAPI6VYQPV6F2OR37 = Internal.createUniqueKey(Roles.ROLES, DSL.name("ukofx66keruapi6vyqpv6f2or37"), new TableField[] { Roles.ROLES.NAME }, true);
    public static final UniqueKey<TasksRecord> TASKS_PKEY = Internal.createUniqueKey(Tasks.TASKS, DSL.name("tasks_pkey"), new TableField[] { Tasks.TASKS.ID }, true);
    public static final UniqueKey<UsersRecord> UK6DOTKOTT2KJSP8VW4D0M25FB7 = Internal.createUniqueKey(Users.USERS, DSL.name("uk6dotkott2kjsp8vw4d0m25fb7"), new TableField[] { Users.USERS.EMAIL }, true);
    public static final UniqueKey<UsersRecord> USERS_PKEY = Internal.createUniqueKey(Users.USERS, DSL.name("users_pkey"), new TableField[] { Users.USERS.ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<CommentsRecord, PostsRecord> COMMENTS__FKH4C7LVSC298WHOYD4W9TA25CR = Internal.createForeignKey(Comments.COMMENTS, DSL.name("fkh4c7lvsc298whoyd4w9ta25cr"), new TableField[] { Comments.COMMENTS.POST_ID }, Keys.POSTS_PK, new TableField[] { Posts.POSTS.ID }, true);
}
