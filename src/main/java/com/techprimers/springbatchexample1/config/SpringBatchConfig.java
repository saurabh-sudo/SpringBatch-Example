package com.techprimers.springbatchexample1.config;

import com.techprimers.springbatchexample1.SpringBatchExample1Application;
//import com.techprimers.springbatchexample1.batch.UserItemProcessor;

import com.techprimers.springbatchexample1.batch.UserItemProcessor;
import com.techprimers.springbatchexample1.batch1.DBReader;
import com.techprimers.springbatchexample1.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.batch.item.file.FlatFileItemWriter;

import javax.annotation.PostConstruct;
import javax.jws.soap.SOAPBinding;
import javax.sql.DataSource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
//
////    @Autowired
////            @Lazy
    DataSource dataSource;




    @Autowired
    DBReader dbReader;



    //   @Bean
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

    @Bean
    public Job loadJob(JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory,
                  ItemReader<User> itemReader,
                   ItemProcessor<User, User> Processor,
                   ItemWriter<User> DBWriter

//                     ItemWriter<User> CSVWriter
                  //     UserItemProcessor userItemProcessor
    ) {


        System.out.println("started reading from csv");
     Step step = stepBuilderFactory.get("ETL-file-load")
                .<User, User>chunk(100)
                .reader(itemReader)
                .processor(Processor)
                .writer(DBWriter)
                .build();

        Step step1 =    stepBuilderFactory.get("step1").<User, User> chunk(100)

                .reader(fromDBReader())
                .processor(DBToCSVprocessor())
                .writer(CSVWrite())
//                .writer(CSVWrite())
                .build();


        return jobBuilderFactory.get("ETL-Load")
                .incrementer(new RunIdIncrementer())
                .start(step)
          .next(step1)
//                .start(step)
                .build();
    }

 @Bean
    public FlatFileItemReader<User> itemReader(@Value("${input}") Resource resource) {

        System.out.println("Inside itemRreaer");

        FlatFileItemReader<User> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        System.out.println("flatFileItemReader  "+flatFileItemReader);
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<User> lineMapper() {

        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"id", "name", "dept", "salary"});

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }





 //@Bean
    public JdbcCursorItemReader<User> fromDBReader()
  {
      System.out.println("inside reader JdbcCursorItemReader");

      JdbcCursorItemReader<User> reader = new JdbcCursorItemReader<>();
   //   System.out.println("dataSource"+dataSource);
      reader.setDataSource(dataSource());
      reader.setSql("SELECT id,name,dept,salary,time FROM User");
      reader.setRowMapper(new UerRowMapper());

      System.out.println("reader "+reader);

      return reader;

  }

  public class UerRowMapper implements RowMapper<User>
  {
      @Override
      public User mapRow(ResultSet rs, int rowNum) throws SQLException{
          User user = new User();
          user.setId(rs.getInt("id"));
          user.setName(rs.getString("name"));
          user.setDept(rs.getString("dept"));
          user.setSalary(rs.getInt("salary"));
          System.out.println("user SpringBa" +user);
          return  user;
      }

  }

 // @Bean("DBToCSVprocessor")
   public UserItemProcessor DBToCSVprocessor()
   {
       System.out.println("inside processor Spring ");
       return  new UserItemProcessor();
   }

// @Bean("CSVWrite")
   public FlatFileItemWriter<User> CSVWrite()
   {
        Resource outputResource = new FileSystemResource("output/kp.csv");
       System.out.println("inside write CSVWrite Spring");
       FlatFileItemWriter<User> write = new FlatFileItemWriter<User>();
       write.setResource(outputResource);
//       write.setResource(new ClassPathResource("kp.csv"));
       write.setLineAggregator(new DelimitedLineAggregator<User>() {{
           setDelimiter(",");
           setFieldExtractor(new BeanWrapperFieldExtractor<User>() {{
               setNames(new String[] { "id", "name","dept" ,"salary"});
           }});
       }});

       System.out.println("write" +write);
       return write;
   }

}
