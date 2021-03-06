package <%=packageName%>.config;

/*
 * #%L
 * %%
 * Copyright (C) 2013 - 2017 FF4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import org.ff4j.FF4j;
import org.ff4j.web.FF4jDispatcherServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import io.github.jhipster.config.JHipsterProperties;

<%_ if (databaseType === 'sql' || databaseType === 'mongodb') { _%>
import <%=packageName%>.config.DatabaseConfiguration;<% } %>

import <%=packageName%>.config.WebConfigurer;
import <%=packageName%>.service.UserService;
import <%=packageName%>.config.ff4j.JHipsterAuthorizationManager;
import <%=packageName%>.config.ff4j.JHipsterEventRepository;

<%_ if (ff4jFeatureStore === 'sql') { _%>
import org.ff4j.springjdbc.store.FeatureStoreSpringJdbc;<% } %>
<%_ if (ff4jPropertyStore === 'sql') { _%>
import org.ff4j.springjdbc.store.PropertyStoreSpringJdbc;<% } %>
<%_ if (ff4jEventRepository === 'sql') { _%>
import org.ff4j.springjdbc.store.EventRepositorySpringJdbc;<% } %>
<%_ if (ff4jFeatureStore === 'sql' || ff4jEventRepository === 'sql' || ff4jPropertyStore ==='sql') { _%>
import com.zaxxer.hikari.HikariDataSource;<% } %>
<%_ if (ff4jFeatureStore === 'mongodb') { _%>
import org.ff4j.mongo.store.FeatureStoreMongo;<% } %>
<%_ if (ff4jPropertyStore === 'mongodb') { _%>
import org.ff4j.mongo.store.PropertyStoreMongo;<% } %>
<%_ if (ff4jEventRepository === 'mongodb') { _%>
import org.ff4j.mongo.store.EventRepositoryMongo;<% } %>
<%_ if (ff4jFeatureStore === 'mongodb' || ff4jEventRepository === 'mongodb' || ff4jPropertyStore ==='mongodb') { _%>
import com.mongodb.MongoClient;<% } %>
<%_ if (ff4jFeatureStore === 'cassandra') { _%>
import org.ff4j.cassandra.store.FeatureStoreCassandra;<% } %>
<%_ if (ff4jPropertyStore === 'cassandra') { _%>
import org.ff4j.cassandra.store.PropertyStoreCassandra;<% } %>
<%_ if (ff4jEventRepository === 'cassandra') { _%>
import org.ff4j.cassandra.store.EventRepositoryCassandra;<% } %>
<%_ if (ff4jFeatureStore === 'cassandra' || ff4jEventRepository === 'cassandra' || ff4jPropertyStore ==='cassandra') { _%>
import com.datastax.driver.core.Cluster;
import org.ff4j.cassandra.CassandraConnection;<% } %>
<%_ if (ff4jFeatureStore === 'redis') { _%>
import org.ff4j.store.FeatureStoreRedis;<% } %>
<%_ if (ff4jPropertyStore === 'redis') { _%>
import org.ff4j.store.PropertyStoreRedis;<% } %>
<%_ if (ff4jEventRepository === 'redis') { _%>
import org.ff4j.store.EventRepositoryRedis;<% } %>
<%_ if (ff4jCache === 'redis') { _%>
import org.ff4j.cache.FF4jCacheManagerRedis;<% } %>
<%_ if (ff4jFeatureStore === 'redis' || ff4jEventRepository === 'redis' || ff4jPropertyStore ==='redis'|| ff4jCache ==='redis') { _%>
import org.ff4j.utils.Util;
import org.ff4j.redis.RedisConnection;<% } %>
<%_ if (ff4jFeatureStore === 'consul') { _%>
import org.ff4j.consul.store.FeatureStoreConsul;<% } %>
<%_ if (ff4jPropertyStore === 'consul') { _%>
import org.ff4j.consul.store.PropertyStoreConsul;<% } %>
<%_ if (ff4jFeatureStore === 'consul' || ff4jPropertyStore ==='consul') { _%>
import com.orbitz.consul.Consul;
import com.google.common.net.HostAndPort;
import org.ff4j.consul.ConsulConnection;<% } %>
<%_ if (ff4jFeatureStore === 'elastic') { _%>
import org.ff4j.elastic.store.FeatureStoreElastic;<% } %>
<%_ if (ff4jPropertyStore === 'elastic') { _%>
import org.ff4j.elastic.store.PropertyStoreElasti;<% } %>
<%_ if (ff4jEventRepository === 'elastic') { _%>
import org.ff4j.elastic.store.EventRepositoryElastic;<% } %>
<%_ if (ff4jFeatureStore === 'elastic' || ff4jEventRepository === 'elastic' || ff4jPropertyStore ==='elastic') { _%>
import java.net.MalformedURLException;
import java.net.URL;
import org.ff4j.elastic.ElasticConnection;
import org.ff4j.elastic.ElasticConnectionMode;
import org.ff4j.exception.FeatureAccessException;<% } %>
<%_ if (ff4jCache != 'no') { _%>
import org.ff4j.cache.FF4JCacheManager;
import org.ff4j.cache.FF4jCacheProxy;
import org.ff4j.cache.FF4jJCacheManager;<% } %>
<%_ if (ff4jCache === 'ehcache') { _%>
import <%=packageName%>.config.ff4j.JHipsterEhCacheCacheManager;<% } %>
<%_ if (ff4jCache === 'hazelcast') { _%>
import com.hazelcast.core.HazelcastInstance;
import <%=packageName%>config.ff4j.JHipsterEhCacheCacheManager;
import <%=packageName%>.config.ff4j.JHipsterHazelcastCacheManager;<% } %>

/**
 * Configuration of FF4J (ff4j.org) to work with JHipster
 *
 * @author Clunven (@clunven)
 */
@Configuration
@ComponentScan(basePackages={"org.ff4j.spring.boot.web.api.resources", "org.ff4j.services", "org.ff4j.aop"})
@AutoConfigureBefore(value = { WebConfigurer.class<%_ if (databaseType === 'sql' || databaseType === 'mongodb') { _%>, DatabaseConfiguration.class <% } %>})
public class FF4jConfiguration extends SpringBootServletInitializer {
	
	/** Default URL. */
	public static final String FF4J_WEBCONSOLE_URL  = "ff4j-web-console";
	
	/** logging. */
	private final Logger log = LoggerFactory.getLogger(FF4jConfiguration.class);
	
	/** User services of Jhispter to be used in FF4j. */
	private final UserService userServices;
	
	/** User services of Jhispter to be used in FF4j. */
	private final AuditEventRepository auditServices;
	
	/** User services of Jhispter to be used in FF4j. */
	private final JHipsterProperties jHipsterConfig;
	
	@Value("${ff4j.core.autocreate}")
	private boolean enableAutoCreate = false;
	
	@Value("${ff4j.audit.enabled}")
	private boolean enableAudit = true;
	
	@Value("${ff4j.audit.log2jhispter}")
	private boolean log2Jhipster = true;
	
	/**
	 * Inject JHipster Settings.
	 *
	 * @param jHipsterProperties
	 * 		settings
	 */
    public FF4jConfiguration(JHipsterProperties jHipsterProperties, UserService user, AuditEventRepository audit) {
        this.userServices 		= user;
        this.auditServices		= audit;
        this.jHipsterConfig     = jHipsterProperties;
        log.info("Configuration Jhipster:" + jHipsterConfig.toString());
    }
    
    @Bean
	public FF4j getFF4j() {
		FF4j ff4j = new FF4j();		
<%_ if (ff4jFeatureStore === 'sql') { _%>
		ff4j.setFeatureStore(new FeatureStoreSpringJdbc(hikariDataSource));
		log.info("Features are stored in RDBMS.");
<% } else if (ff4jFeatureStore === 'elastic') { _%>
		ff4j.setFeatureStore( new FeatureStoreElastic(getElasticConnection()));
		log.info("Features are stored in ElasticSearch.");
<% } else if (ff4jFeatureStore === 'redis') { _%>
        ff4j.setFeatureStore(new FeatureStoreRedis(getRedisConnection()));
		log.info("Features are stored in Redis.");
<% } else if (ff4jFeatureStore === 'consul') { _%>
        ff4j.setFeatureStore(new FeatureStoreConsul(getConsulConnection()));
		log.info("Features are stored in Consul.");
<% } else if (ff4jFeatureStore === 'mongodb') { _%>
		log.info("Features are stored in MongoDB dbName=[" + mongoDatabaseName + "]");
		ff4j.setFeatureStore(new FeatureStoreMongo(mongoClient, mongoDatabaseName));
<% } else if (ff4jFeatureStore === 'cassandra') { _%>
        ff4j.setFeatureStore(new FeatureStoreCassandra(getCassandraConnection()));
		log.info("Features are store in Cassandra.");
<%_ } _%>
<%_ if (ff4jPropertyStore === 'sql') { _%>
        ff4j.setPropertiesStore(new PropertyStoreSpringJdbc(hikariDataSource));
        log.info("Properties are stored in RDBMS.");
<% } else if (ff4jPropertyStore === 'elastic') { _%>
        ff4j.setPropertiesStore(new PropertyStoreElastic(getElasticConnection()));
        log.info("Properties are stored in ElasticSearch.");
<% } else if (ff4jPropertyStore === 'redis') { _%>
        ff4j.setPropertiesStore(new PropertyStoreRedis(getRedisConnection()));
        log.info("Properties are stored in Redis.");
<% } else if (ff4jPropertyStore === 'consul') { _%>
        ff4j.setPropertiesStore(new PropertyStoreConsul(getConsulConnection()));
        log.info("Properties are stored in Consul.");
<% } else if (ff4jPropertyStore === 'mongodb') { _%>
        ff4j.setPropertiesStore(new PropertyStoreMongo(mongoClient, mongoDatabaseName));
        log.info("Properties are stored in MongoDB.");
<% } else if (ff4jPropertyStore === 'cassandra') { _%>
        ff4j.setPropertiesStore(new PropertyStoreCassandra(getCassandraConnection()));
        log.info("Properties are store in Cassandra.");
<%_ } _%>
<%_ if (ff4jEventRepository === 'sql') { _%>
       ff4j.setEventRepository(new EventRepositorySpringJdbc(hikariDataSource));
       log.info("AuditEvents are stored in RDBMS.");
<% } else if (ff4jEventRepository === 'elastic') { _%>
       ff4j.setEventRepository(new EventRepositoryElastic(getElasticConnection()));
       log.info("AuditEvents are stored in ElasticSearch.");
<% } else if (ff4jEventRepository === 'redis') { _%>
       ff4j.setEventRepository(new EventRepositoryRedis(getRedisConnection()));
       log.info("AuditEvents are stored in Redis.");
<% } else if (ff4jPropertyStore === 'mongodb') { _%>
       ff4j.setEventRepository(new EventRepositoryMongo(mongoClient, mongoDatabaseName));
       log.info("AuditEvents are stored in MongoDB.");
<% } else if (ff4jEventRepository === 'cassandra') { _%>
       ff4j.setEventRepository(new EventRepositoryCassandra(getCassandraConnection()));
       log.info("AuditEvents are store in Cassandra.");
<%_ } _%>		
<%_ if (ff4jCache === 'redis') { _%>
		FF4JCacheManager ff4jCache = new FF4jCacheManagerRedis(getRedisConnection());<%_ } _%>
<%_ if (ff4jCache === 'ehcache') { _%>
		FF4JCacheManager ff4jCache = new JHipsterEhCacheCacheManager();<%_ } _%>
<%_ if (ff4jCache === 'hazelcast') { _%>
		FF4JCacheManager ff4jCache  = new JHipsterHazelcastCacheManager(hazelcastInstance);<%_ } _%>		
<%_ if (ff4jCache != 'no') { _%>
		FF4jCacheProxy cacheProxy = new FF4jCacheProxy(ff4j.getFeatureStore(), ff4j.getPropertiesStore(), ff4jCache);
		ff4j.setFeatureStore(cacheProxy);
		ff4j.setPropertiesStore(cacheProxy);<%_ } _%>
		if (log2Jhipster) {
			ff4j.setEventRepository(new JHipsterEventRepository(ff4j.getEventRepository(), auditServices));
		}
		ff4j.audit(enableAudit);
		ff4j.autoCreate(enableAutoCreate);
		ff4j.setAuthorizationsManager(new JHipsterAuthorizationManager(userServices));
		return ff4j;
    }
    
<%_ if (ff4jCache === 'hazelcast') { _%>    
 	private HazelcastInstance hazelcastInstance;

 	@Autowired(required = false)
 	public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
 		this.hazelcastInstance = hazelcastInstance;
 	}<%_ } _%>
 <%_ if (ff4jFeatureStore === 'sql' || ff4jEventRepository === 'sql' || ff4jPropertyStore ==='sql') { _%>
   private HikariDataSource hikariDataSource;

	@Autowired(required = false)
	public void setHikariDataSource(HikariDataSource hikariDataSource) {
		this.hikariDataSource = hikariDataSource;
	}<% } %>
<%_ if (ff4jFeatureStore === 'cassandra' || ff4jPropertyStore ==='cassandra' || ff4jEventRepository === 'cassandra') { _%>
    private Cluster cluster;

    @Autowired(required = false)
    public void setClusterCassandra(Cluster cluster) {
	  this.cluster = cluster;
    }
    
    @Bean
    public CassandraConnection getCassandraConnection() {
	    return new CassandraConnection(cluster);
    }<% } %>
<%_ if (ff4jFeatureStore === 'mongodb' || ff4jPropertyStore ==='mongodb' || ff4jEventRepository === 'mongodb') { _%>
    @Value("${spring.data.mongodb.database}")
	private String mongoDatabaseName = "appmongo";

    @Autowired
    private MongoClient mongoClient;<% } %>
<%_ if (ff4jFeatureStore === 'elastic' || ff4jEventRepository === 'elastic' || ff4jPropertyStore ==='elastic') { _%>
     @Value("${ff4j.elastic.index}")
     private String elasticIndexName;

     @Value("${ff4j.elastic.hostName}")
     private String elasticHostName;

     @Value("${ff4j.elastic.port}")
     private Integer elasticPort;
     
     @Bean
     public ElasticConnection getElasticConnection() {
 		URL urlElastic;
 		try {
 			urlElastic = new URL("http://" + elasticHostName + ":" + elasticPort);
 			return new ElasticConnection(ElasticConnectionMode.JEST_CLIENT, elasticIndexName, urlElastic);
 		} catch (MalformedURLException e) {
 			throw new FeatureAccessException("Cannot access elastic", e);
 		}
 	 }
<% } %>
<%_ if (ff4jFeatureStore === 'redis' || ff4jEventRepository === 'redis' || ff4jPropertyStore ==='redis' || ff4jCache === 'redis') { _%>
    
     @Value("${ff4j.redis.hostname}")
     private String redisHostName;

     @Value("${ff4j.redis.port}")
     private Integer redisPort;

     @Value("${ff4j.redis.password}")
     private String redisPassword;

     @Bean
     public RedisConnection getRedisConnection() {
	    if (Util.hasLength(redisPassword)) {
		  return new RedisConnection(redisHostName, redisPort, redisPassword);
		}
		return new RedisConnection(redisHostName, redisPort); 
     }
<% } %>

<%_ if (ff4jFeatureStore === 'consul' || ff4jPropertyStore ==='consul') { _%>
    // --------- Consul ---------

    @Value("${ff4j.consul.hostname}")
    private String consulHost;

    @Value("${ff4j.consul.port}")
    private Integer consulPort;

    @Bean
    public ConsulConnection getConsulConnection() {
	   return new ConsulConnection(
			Consul.builder().withHostAndPort(
					HostAndPort.fromParts(consulHost, consulPort)).build());
    }<% } %>

    @Bean
    public ServletRegistrationBean ff4jDispatcherServletRegistrationBean(FF4jDispatcherServlet ff4jDispatcherServlet) {
        return new ServletRegistrationBean(ff4jDispatcherServlet, "/" + FF4J_WEBCONSOLE_URL + "/*");
    }

    @Bean
    public FF4jDispatcherServlet getFF4jDispatcherServlet(FF4j ff4j) {
        FF4jDispatcherServlet ff4jConsoleServlet = new FF4jDispatcherServlet();
        ff4jConsoleServlet.setFf4j(ff4j);
        return ff4jConsoleServlet;
    }
}
