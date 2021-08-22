package de.dhbwheidenheim.informatik.assfalg.personspring;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestParam;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends
   WebSecurityConfigurerAdapter {
	// Wenn diese Klasse vorhanden ist, braucht man keinen Login auf dem Server, 
	// und es funktioniert auch POST, was normalerweise per Cross Site Request Forgery (CSRF) abgesichert ist. 

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable();
  }
}