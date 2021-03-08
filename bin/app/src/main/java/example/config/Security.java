package example.config;

import example.config.MySimpleUrlAuthenticationSuccessHandler;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    // roles admin allow to access /admin/**
    // roles user allow to access /user/**
    // custom 403 access denied handler
    @Override
    protected void configure(HttpSecurity http) throws Exception {
  		http
  			.authorizeRequests()
        .antMatchers("/user").access("hasRole('USER') or hasRole('SUPER')")
        .antMatchers("/admin").access("hasRole('ADMIN') or hasRole('SUPER')")
  				.antMatchers("/login").permitAll()
  				.anyRequest().authenticated()
  				.and()
  			.formLogin()
  				.loginPage("/login")
          .loginProcessingUrl("/login")
          .successHandler(myAuthenticationSuccessHandler())
  				.permitAll();

    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MySimpleUrlAuthenticationSuccessHandler();
    }

  	@Bean
  	@Override
  	public UserDetailsService userDetailsService() {

      List<UserDetails> userDetailsList = new ArrayList<>();

      userDetailsList.add(
        User.withDefaultPasswordEncoder()
          .username("admin")
          .password("pass")
          .roles("ADMIN")
          .build()
      );

      userDetailsList.add(
        User.withDefaultPasswordEncoder()
          .username("user")
          .password("pass")
          .roles("USER")
          .build()
      );

      userDetailsList.add(
        User.withDefaultPasswordEncoder()
          .username("super")
          .password("pass")
          .roles("SUPER")
          .build()
      );

      return new InMemoryUserDetailsManager(userDetailsList);
  	}
}
