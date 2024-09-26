/*
 * This file is generated by jOOQ.
 */
package jooq.tables;


import java.time.OffsetDateTime;
import java.util.Collection;

import jooq.Keys;
import jooq.Public;
import jooq.tables.records.TasksRecord;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
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
public class Tasks extends TableImpl<TasksRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.tasks</code>
     */
    public static final Tasks TASKS = new Tasks();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TasksRecord> getRecordType() {
        return TasksRecord.class;
    }

    /**
     * The column <code>public.tasks.id</code>.
     */
    public final TableField<TasksRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.tasks.created_at</code>.
     */
    public final TableField<TasksRecord, OffsetDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    /**
     * The column <code>public.tasks.created_by</code>.
     */
    public final TableField<TasksRecord, Long> CREATED_BY = createField(DSL.name("created_by"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>public.tasks.progress</code>.
     */
    public final TableField<TasksRecord, Integer> PROGRESS = createField(DSL.name("progress"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.tasks.status</code>.
     */
    public final TableField<TasksRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.tasks.updated_at</code>.
     */
    public final TableField<TasksRecord, OffsetDateTime> UPDATED_AT = createField(DSL.name("updated_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    private Tasks(Name alias, Table<TasksRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Tasks(Name alias, Table<TasksRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.tasks</code> table reference
     */
    public Tasks(String alias) {
        this(DSL.name(alias), TASKS);
    }

    /**
     * Create an aliased <code>public.tasks</code> table reference
     */
    public Tasks(Name alias) {
        this(alias, TASKS);
    }

    /**
     * Create a <code>public.tasks</code> table reference
     */
    public Tasks() {
        this(DSL.name("tasks"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<TasksRecord> getPrimaryKey() {
        return Keys.TASKS_PKEY;
    }

    @Override
    public Tasks as(String alias) {
        return new Tasks(DSL.name(alias), this);
    }

    @Override
    public Tasks as(Name alias) {
        return new Tasks(alias, this);
    }

    @Override
    public Tasks as(Table<?> alias) {
        return new Tasks(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Tasks rename(String name) {
        return new Tasks(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Tasks rename(Name name) {
        return new Tasks(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Tasks rename(Table<?> name) {
        return new Tasks(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Tasks where(Condition condition) {
        return new Tasks(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Tasks where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Tasks where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Tasks where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Tasks where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Tasks where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Tasks where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Tasks where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Tasks whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Tasks whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
