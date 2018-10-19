package com.sdase.commons.server.hibernate.test;

import com.sdase.commons.server.dropwizard.bundles.ConfigurationSubstitutionBundle;
import com.sdase.commons.server.hibernate.HibernateBundle;
import com.sdase.commons.server.hibernate.test.model.Person;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class HibernateApp extends Application<HibernateITestConfiguration> {

   private final HibernateBundle<HibernateITestConfiguration> hibernateBundle = HibernateBundle.builder()
         .withConfigClass(HibernateITestConfiguration.class)
         .withEntityScanPackageClass(Person.class)
         .build();

   public static void main(String[] args) throws Exception {
      new HibernateApp().run(args);
   }

   @Override
   public void initialize(Bootstrap<HibernateITestConfiguration> bootstrap) {
      bootstrap.addBundle(ConfigurationSubstitutionBundle.builder().build());
      bootstrap.addBundle(hibernateBundle);
   }

   @Override
   public void run(HibernateITestConfiguration configuration, Environment environment) {
      environment.jersey().register(new PersonEndPoint(hibernateBundle.sessionFactory()));
   }
}
