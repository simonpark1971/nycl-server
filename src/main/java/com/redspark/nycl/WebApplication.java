package com.redspark.nycl;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.PropertiesRealm;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebApplication.class, args);
  }

  @Bean
  public Realm realm() {
    PropertiesRealm realm = new PropertiesRealm();

    realm.setCachingEnabled(true);
    return realm;
  }

  @Bean
  public ShiroFilterChainDefinition shiroFilterChainDefinition()  {
    DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

    chainDefinition.addPathDefinition("/*", "authcBasic[permissive]");
    return chainDefinition;
  }

  @Bean
  public CacheManager cacheManager() {
    return new MemoryConstrainedCacheManager();
  }
}
