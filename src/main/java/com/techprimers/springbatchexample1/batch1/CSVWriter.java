package com.techprimers.springbatchexample1.batch1;

import com.techprimers.springbatchexample1.model.User;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("CSVWriter")
public class CSVWriter {

//    @Bean
//    public FlatFileItemWriter<User> write()
//    {
//        System.out.println("inside write ");
//        FlatFileItemWriter<User> write = new FlatFileItemWriter<User>();
//        write.setResource(new ClassPathResource("kp.csv"));
//        write.setLineAggregator(new DelimitedLineAggregator<User>() {{
//            setDelimiter(",");
//            setFieldExtractor(new BeanWrapperFieldExtractor<User>() {{
//                setNames(new String[] { "id", "name","dept" ,"salary"});
//            }});
//        }});
//
//        System.out.println("write" +write);
//        return write;
//    }
private Resource outputResource = new FileSystemResource("output/kp.csv");


    public FlatFileItemWriter write(List<? extends User> list) throws Exception {
        System.out.println("inside write CSVWriterFile "+list);
        FlatFileItemWriter<User> write = new FlatFileItemWriter<User>();
 // write.setResource(new ClassPathResource("kp.csv"));
     write.setResource(outputResource);
        write.setLineAggregator(new DelimitedLineAggregator<User>() {{
            setDelimiter(",");
            setFieldExtractor(new BeanWrapperFieldExtractor<User>() {{
                setNames(new String[] { "id", "name","dept" ,"salary"});
            }});
        }});

        System.out.println("write" +write.toString());
     return write;
    }
}
