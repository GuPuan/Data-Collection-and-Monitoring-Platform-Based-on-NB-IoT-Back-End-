package com.example.nbiot.config;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.nbiot.util") // Scan the package for MyBatis mappers
public class MyBatisPlusConfig {

    // Inner configuration class for MyBatis Plus setup
    public class MybatisPlusConfig {

        // Define a MyBatis Plus Interceptor bean
        @Bean
        public MybatisPlusInterceptor mybatisPlusInterceptor() {
            // Create a new MyBatis Plus Interceptor
            MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

            // Add a pagination interceptor specific to MySQL database
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

            // Return the configured interceptor
            return interceptor;
        }

    }
}

