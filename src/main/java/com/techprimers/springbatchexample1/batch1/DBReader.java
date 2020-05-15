package com.techprimers.springbatchexample1.batch1;

import com.techprimers.springbatchexample1.config.SpringBatchConfig;
import com.techprimers.springbatchexample1.model.User;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component("DBReader")
public class DBReader implements ItemReader {


    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;");
//      dataSource.setUrl("jdbc:h2:./test;DB_CLOSE_DELAY=-1;");
        // dataSource.setUrl("jdbc:h2:file:C:/data/sample;DB_CLOSE_DELAY=-1;");

        //    dataSource.setUrl("e`");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }


    public class UerRowMapper implements RowMapper<User>
    {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName("name");
            user.setDept("dept");
            user.setSalary(rs.getInt("salary"));
            System.out.println("user UerRowMapper" +user);
            return  user;
        }

    }





    @Override
    public JdbcCursorItemReader read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        System.out.println("inside reader JdbcCursorItemReader");

        JdbcCursorItemReader<User> reader = new JdbcCursorItemReader<>();
        //   System.out.println("dataSource"+dataSource);
        reader.setDataSource(dataSource());
        reader.setSql("SELECT id,name,dept,salary,time FROM User");
        reader.setRowMapper(new UerRowMapper());

        System.out.println("reader "+reader);

        return reader;
    }
}
