package com.learn.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableAuthorizationServer

public class AuhthServer extends AuthorizationServerConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }


    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("abcd");
        return converter;
    }


    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {

        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));


        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
//        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);


        return defaultTokenServices;
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(
//                Arrays.asList(tokenEnhancer(), accessTokenConverter()));

        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));

        System.out.println("------------------------------------configure------------");

        endpoints
                // .authenticationManager(authenticationManager)
                //     .tokenStore(tokenStore())
                .tokenServices(tokenServices())

                //          .accessTokenConverter(accessTokenConverter());
                //     .tokenEnhancer(tokenEnhancerChain)
                // .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
    }


//    @Override
//    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
//        endpoints
//                .tokenStore(tokenStore()).
//                tokenEnhancer(tokenEnhancerChain).
//                authenticationManager(authenticationManager);
//    }


//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints
//                .authenticationManager(authenticationManager)
//                .approvalStoreDisabled()
//                .tokenStore(tokenStore);
//    }


    //    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security
//                .tokenKeyAccess("permitAll()")
//                .checkTokenAccess("permitAll()")
//                .allowFormAuthenticationForClients().passwordEncoder(passwordEncoder());
//        //     permitAll
//        //.checkTokenAccess("isAuthenticated()")
//
//    }
    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients().passwordEncoder(passwordEncoder());


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));

        // Maybe there's a way to use config from AuthorizationServerEndpointsConfigurer endpoints?
        source.registerCorsConfiguration("/oauth/token", config);
        CorsFilter filter = new CorsFilter(source);
        security.addTokenEndpointAuthenticationFilter(filter);


        //     permitAll
        //.checkTokenAccess("isAuthenticated()")

    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("client")
//                .secret("client")
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
//                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
//                .scopes("read", "write")
//                // .resourceIds("resource-server-rest-api")
//                .accessTokenValiditySeconds(60);
        clients.inMemory()
                .withClient("client")
                .secret("client")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                .scopes("read", "write")
                // .resourceIds("resource-server-rest-api")
                .accessTokenValiditySeconds(3600)
                .and()
                .withClient("rest")
                .secret("rest")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                .scopes("read", "write")
                // .resourceIds("resource-server-rest-api")
                .accessTokenValiditySeconds(86400);

    }


    //    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//
//        clients.inMemory()
//                .withClient("my-trusted-client")
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
//                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
//                .scopes("read", "write", "trust")
//                .secret("secret")
//                .accessTokenValiditySeconds(120).//Access token is only valid for 2 minutes.
//                refreshTokenValiditySeconds(600);//Refresh token is only valid for 10 minutes.
//    }
//
    //    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory().withClient("android-client")
//                .authorizedGrantTypes("client-credentials", "password","refresh_token")
//                .authorities("ROLE_CLIENT", "ROLE_ANDROID_CLIENT")
//                .scopes("read", "write", "trust")
//                .resourceIds("oauth2-resource")
//                .accessTokenValiditySeconds(5000)
//                .secret("android-secret").refreshTokenValiditySeconds(50000);
//    }
    @Autowired
    private UserService userDetailsService;

    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
}


// class CustomTokenConverter extends JwtAccessTokenConverter {
//
//    @Override
//    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
//                                     OAuth2Authentication authentication) {
//
//            final Map<String, Object> additionalInfo = new HashMap<String, Object>();
//
//            additionalInfo.put("clientId", "------hello world----" );
//
//            ((DefaultOAuth2AccessToken) accessToken)
//                    .setAdditionalInformation(additionalInfo);
//        }
//        accessToken = super.enhance(accessToken, authentication);
//        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(new HashMap<>());
//        return accessToken;
//    }
//}