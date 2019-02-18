package org.sdase.commons.server.weld.testing.test.resources;

import org.sdase.commons.server.weld.testing.test.util.BarSupplier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(DummyResource.ROOT_PATH)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DummyResource {

   public static final String ROOT_PATH = "/dummy";

   @Inject
   private BarSupplier bar;

   @GET
   public String helloWorld() {
      return "hello " + bar.get();
   }

}
