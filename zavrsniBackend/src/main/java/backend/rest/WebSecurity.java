package backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Qualifier("patientUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder pwdEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        http.headers().frameOptions().sameOrigin();
        http.csrf().disable();
        //http.authorizeRequests().antMatchers("/record/create").permitAll();
        http.authorizeRequests().antMatchers("/register/*").permitAll();
        http.authorizeRequests().antMatchers("/login").permitAll();
        http.authorizeRequests().antMatchers("/user/loggedin").permitAll();
        http.authorizeRequests().antMatchers("/patient/list").permitAll();
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll();
        http.authorizeRequests().antMatchers( HttpMethod.GET,"/image/**").permitAll()
                //.anyRequest()
                //.access("hasRole('ROLE_PATIENT')")
                .and()
                .formLogin()
                .loginProcessingUrl("/perform-login")
                .successHandler((req, res, auth) -> {res.setStatus(HttpStatus.OK.value());})
                .failureHandler((req,res,auth)->{res.reset();res.setStatus(HttpStatus.BAD_REQUEST.value());})
                .usernameParameter("email")
                .passwordParameter("password")
                .and()
                .logout()
                .logoutSuccessHandler((req,res,auth)->res.setStatus(HttpStatus.OK.value()))
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(pwdEncoder);
    }

    @Override
    public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web) throws Exception {
        super.configure(web);

        web.ignoring().antMatchers("/record/create");
        web.ignoring().antMatchers("/record/get");
    }
}
