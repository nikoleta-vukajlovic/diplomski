/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diplomski.xapi.exceptions;

import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

/**
 *
 * @author Dusan
 */
public class ExceptionTranslator {

    public void exception(ExecuteContext context) {
        SQLDialect dialect = context.configuration().dialect();
        SQLExceptionTranslator translator
                = new SQLErrorCodeSQLExceptionTranslator(dialect.name());
        context.exception(translator
                .translate("Access database using Jooq", context.sql(), context.sqlException()));
    }
}
