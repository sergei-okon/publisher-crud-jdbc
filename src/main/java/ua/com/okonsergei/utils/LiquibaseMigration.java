package ua.com.okonsergei.utils;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.pro.packaged.J;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;

public class LiquibaseMigration {

    public static void runLiquibaseMigration() {
        try (Connection connection = JdbcUtils.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new liquibase.Liquibase("db.migration/changelog.xml",
                    new ClassLoaderResourceAccessor(), database);

            liquibase.update(new Contexts(), new LabelExpression());

        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        }
    }
}
