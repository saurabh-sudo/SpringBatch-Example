package com.techprimers.springbatchexample1.batch;

import org.springframework.batch.item.ItemProcessor;

import com.techprimers.springbatchexample1.model.User;
import org.springframework.stereotype.Component;


@Component("userItemProcessor")
public class UserItemProcessor implements ItemProcessor<User , User>
{
    @Override
    public User process(User user) throws Exception {
        System.out.println("In userItemProcessor"+user);
        return user;
    }
}
