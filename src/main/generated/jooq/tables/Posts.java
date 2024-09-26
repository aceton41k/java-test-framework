/*
 * This file is generated by jOOQ.
 */
package jooq.tables;


import java.time.LocalDateTime;
import java.util.Collection;

import jooq.Keys;
import jooq.Public;
import jooq.tables.Comments.CommentsPath;
import jooq.tables.records.PostsRecord;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Posts extends TableImpl<PostsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.posts</code>
     */
    public static final Posts POSTS = new Posts();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PostsRecord> getRecordType() {
        return PostsRecord.class;
    }

    /**
     * The column <code>public.posts.id</code>.
     */
    public final TableField<PostsRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.posts.title</code>.
     */
    public final TableField<PostsRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.posts.message</code>.
     */
    public final TableField<PostsRecord, String> MESSAGE = createField(DSL.name("message"), SQLDataType.VARCHAR(1000).nullable(false), this, "");

    /**
     * The column <code>public.posts.created_at</code>.
     */
    public final TableField<PostsRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column <code>public.posts.updated_at</code>.
     */
    public final TableField<PostsRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("updated_at"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column <code>public.posts.created_by</code>.
     */
    public final TableField<PostsRecord, Long> CREATED_BY = createField(DSL.name("created_by"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.posts.modified_by</code>.
     */
    public final TableField<PostsRecord, Long> MODIFIED_BY = createField(DSL.name("modified_by"), SQLDataType.BIGINT, this, "");

    private Posts(Name alias, Table<PostsRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Posts(Name alias, Table<PostsRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.posts</code> table reference
     */
    public Posts(String alias) {
        this(DSL.name(alias), POSTS);
    }

    /**
     * Create an aliased <code>public.posts</code> table reference
     */
    public Posts(Name alias) {
        this(alias, POSTS);
    }

    /**
     * Create a <code>public.posts</code> table reference
     */
    public Posts() {
        this(DSL.name("posts"), null);
    }

    public <O extends Record> Posts(Table<O> path, ForeignKey<O, PostsRecord> childPath, InverseForeignKey<O, PostsRecord> parentPath) {
        super(path, childPath, parentPath, POSTS);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class PostsPath extends Posts implements Path<PostsRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> PostsPath(Table<O> path, ForeignKey<O, PostsRecord> childPath, InverseForeignKey<O, PostsRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private PostsPath(Name alias, Table<PostsRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public PostsPath as(String alias) {
            return new PostsPath(DSL.name(alias), this);
        }

        @Override
        public PostsPath as(Name alias) {
            return new PostsPath(alias, this);
        }

        @Override
        public PostsPath as(Table<?> alias) {
            return new PostsPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<PostsRecord, Long> getIdentity() {
        return (Identity<PostsRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<PostsRecord> getPrimaryKey() {
        return Keys.POSTS_PK;
    }

    private transient CommentsPath _comments;

    /**
     * Get the implicit to-many join path to the <code>public.comments</code>
     * table
     */
    public CommentsPath comments() {
        if (_comments == null)
            _comments = new CommentsPath(this, null, Keys.COMMENTS__FKH4C7LVSC298WHOYD4W9TA25CR.getInverseKey());

        return _comments;
    }

    @Override
    public Posts as(String alias) {
        return new Posts(DSL.name(alias), this);
    }

    @Override
    public Posts as(Name alias) {
        return new Posts(alias, this);
    }

    @Override
    public Posts as(Table<?> alias) {
        return new Posts(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Posts rename(String name) {
        return new Posts(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Posts rename(Name name) {
        return new Posts(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Posts rename(Table<?> name) {
        return new Posts(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Posts where(Condition condition) {
        return new Posts(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Posts where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Posts where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Posts where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Posts where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Posts where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Posts where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Posts where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Posts whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Posts whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
