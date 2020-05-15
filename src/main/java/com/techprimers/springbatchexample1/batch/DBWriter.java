package com.techprimers.springbatchexample1.batch;

import com.techprimers.springbatchexample1.model.User;
import com.techprimers.springbatchexample1.repository.UserRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
////@Primary
@Component("DBWriter")
public class DBWriter implements ItemWriter<User> {

    @Autowired
    private UserRepository userRepository;

//    @Override
//    public void write(List<? extends User> list) throws Exception {
//
//        System.out.println("inside writeeeeeeeeee");
//
//    }


    public void write(List<? extends User> users) throws Exception {

        System.out.println("Data Saved for Users: " + users);
        userRepository.save(users);

        System.out.println("user value "+users);
        //userRepository.flush(users);
    }

//    @Override
//    public void write(List<? extends User> list) throws Exception {
//        userRepository.save(list);
//    }
//
//    @Override
//    public void write(List<? extends User> list) throws Exception {
//
//        List<User> abc= (List<User>) list;
//        userRepository.save(abc);
//
//    }
}
