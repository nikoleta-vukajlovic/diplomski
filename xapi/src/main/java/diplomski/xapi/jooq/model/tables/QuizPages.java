/*
 * This file is generated by jOOQ.
 */
package diplomski.xapi.jooq.model.tables;


import diplomski.xapi.jooq.model.Keys;
import diplomski.xapi.jooq.model.Public;
import diplomski.xapi.jooq.model.tables.records.QuizPagesRecord;

import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class QuizPages extends TableImpl<QuizPagesRecord> {

    private static final long serialVersionUID = 380314091;

    /**
     * The reference instance of <code>public.QUIZ_PAGES</code>
     */
    public static final QuizPages QUIZ_PAGES = new QuizPages();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<QuizPagesRecord> getRecordType() {
        return QuizPagesRecord.class;
    }

    /**
     * The column <code>public.QUIZ_PAGES.page</code>.
     */
    public final TableField<QuizPagesRecord, Integer> PAGE = createField(DSL.name("page"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.QUIZ_PAGES.question</code>.
     */
    public final TableField<QuizPagesRecord, String> QUESTION = createField(DSL.name("question"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.QUIZ_PAGES.quizId</code>.
     */
    public final TableField<QuizPagesRecord, String> QUIZID = createField(DSL.name("quizId"), org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>public.QUIZ_PAGES.answer</code>.
     */
    public final TableField<QuizPagesRecord, String> ANSWER = createField(DSL.name("answer"), org.jooq.impl.SQLDataType.CLOB, this, "");

    /**
     * Create a <code>public.QUIZ_PAGES</code> table reference
     */
    public QuizPages() {
        this(DSL.name("QUIZ_PAGES"), null);
    }

    /**
     * Create an aliased <code>public.QUIZ_PAGES</code> table reference
     */
    public QuizPages(String alias) {
        this(DSL.name(alias), QUIZ_PAGES);
    }

    /**
     * Create an aliased <code>public.QUIZ_PAGES</code> table reference
     */
    public QuizPages(Name alias) {
        this(alias, QUIZ_PAGES);
    }

    private QuizPages(Name alias, Table<QuizPagesRecord> aliased) {
        this(alias, aliased, null);
    }

    private QuizPages(Name alias, Table<QuizPagesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> QuizPages(Table<O> child, ForeignKey<O, QuizPagesRecord> key) {
        super(child, key, QUIZ_PAGES);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public List<ForeignKey<QuizPagesRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<QuizPagesRecord, ?>>asList(Keys.QUIZ_PAGES__FKQUIZID);
    }

    public Quiz quiz() {
        return new Quiz(this, Keys.QUIZ_PAGES__FKQUIZID);
    }

    @Override
    public QuizPages as(String alias) {
        return new QuizPages(DSL.name(alias), this);
    }

    @Override
    public QuizPages as(Name alias) {
        return new QuizPages(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public QuizPages rename(String name) {
        return new QuizPages(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public QuizPages rename(Name name) {
        return new QuizPages(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Integer, String, String, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
