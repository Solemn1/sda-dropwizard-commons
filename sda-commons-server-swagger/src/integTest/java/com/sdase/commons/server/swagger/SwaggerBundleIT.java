package com.sdase.commons.server.swagger;

import static com.sdase.commons.server.swagger.test.SwaggerAssertions.assertValidSwagger2Json;
import static com.sdase.commons.server.swagger.test.SwaggerAssertions.assertValidSwagger2Yaml;
import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jetty.http.HttpStatus.OK_200;

import com.sdase.commons.server.swagger.test.SwaggerJsonLight;
import com.sdase.commons.server.swagger.test.SwaggerJsonLight.SwaggerDefinition;
import com.sdase.commons.server.swagger.test.SwaggerJsonLight.SwaggerOperation;
import com.sdase.commons.server.swagger.test.SwaggerJsonLight.SwaggerPath;
import io.dropwizard.Configuration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.swagger.models.Info;
import java.util.Map;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.ClassRule;
import org.junit.Test;

public class SwaggerBundleIT {

   @ClassRule
   public static final DropwizardAppRule<Configuration> DW = new DropwizardAppRule<>(
         SwaggerBundleTestApp.class, resourceFilePath("test-config.yaml"));

   private static Builder getJsonRequest() {
      return DW.client()
            .target(getTarget())
            .path("/swagger.json")
            .request(APPLICATION_JSON);
   }

   private static Builder getYamlRequest() {
      return DW.client()
            .target(getTarget())
            .path("/swagger.yaml")
            .request("application/yaml");
   }

   private static String getTarget() {
      return "http://localhost:" + DW.getLocalPort();
   }

   @Test
   public void shouldProvideSchemaCompliantJson() {
      Response response = getJsonRequest().get();

      assertThat(response.getStatus()).isEqualTo(OK_200);
      assertThat(response.getMediaType()).isEqualTo(APPLICATION_JSON_TYPE);

      assertValidSwagger2Json(response);
   }

   @Test
   public void shouldProvideValidYaml() {
      Response response = getYamlRequest().get();

      assertThat(response.getStatus()).isEqualTo(OK_200);
      assertThat(response.getMediaType()).isEqualTo(MediaType.valueOf("application/yaml"));

      assertValidSwagger2Yaml(response);
   }

   @Test
   public void shouldIncludeInfo() {
      SwaggerJsonLight response = getJsonRequest().get(SwaggerJsonLight.class);

      Info info = response.getInfo();

      assertThat(info.getTitle()).isEqualTo(SwaggerBundleTestApp.class.getSimpleName());
      assertThat(info.getVersion()).isEqualTo("1.0");
   }

   @Test
   public void shouldIncludeBasePath() {
      SwaggerJsonLight response = getJsonRequest().get(SwaggerJsonLight.class);

      assertThat(response.getBasePath()).isEqualTo("/");
   }

   @Test
   public void shouldIncludePaths() {
      SwaggerJsonLight response = getJsonRequest().get(SwaggerJsonLight.class);

      Map<String, SwaggerPath> paths = response.getPaths();

      String path = "/jdoe";

      assertThat(paths).hasSize(1);
      assertThat(paths).containsKeys(path);

      SwaggerPath swaggerPath = paths.get(path);

      assertThat(swaggerPath.getGet()).isNotNull().extracting(SwaggerOperation::getSummary)
            .isEqualTo("get");
      assertThat(swaggerPath.getPost()).isNotNull().extracting(SwaggerOperation::getSummary)
            .isEqualTo("post");
      assertThat(swaggerPath.getDelete()).isNotNull().extracting(SwaggerOperation::getSummary)
            .isEqualTo("delete");
   }

   @Test
   public void shouldIncludeDefinitions() {
      SwaggerJsonLight response = getJsonRequest().get(SwaggerJsonLight.class);

      Map<String, SwaggerDefinition> definitions = response.getDefinitions();

      String definition = PersonResource.class.getSimpleName();

      assertThat(definitions).hasSize(1);
      assertThat(definitions).containsKeys(definition);

      SwaggerDefinition swaggerDefinition = definitions.get(definition);

      Map<String, Object> properties = swaggerDefinition.getProperties();

      assertThat(properties.keySet()).containsExactlyInAnyOrder("firstName", "lastName");
   }
}
