/* 
 *
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2007, Sualeh Fatehi.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */

package schemacrawler.integration.test;


import static org.junit.Assert.fail;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import schemacrawler.crawl.SchemaCrawlerOptions;
import schemacrawler.tools.OutputOptions;
import schemacrawler.tools.integration.SchemaCrawlerExecutor;
import schemacrawler.tools.integration.freemarker.FreeMarkerExecutor;
import schemacrawler.tools.integration.jung.JungExecutor;
import schemacrawler.tools.integration.velocity.VelocityExecutor;
import schemacrawler.tools.schematext.SchemaTextDetailType;
import schemacrawler.tools.schematext.SchemaTextOptions;
import dbconnector.datasource.PropertiesDataSourceException;
import dbconnector.test.TestUtility;

public class ExecutorIntegrationTest
{

  private static TestUtility testUtility = new TestUtility();

  @BeforeClass
  public static void beforeAllTests()
    throws PropertiesDataSourceException, ClassNotFoundException
  {
    testUtility.setApplicationLogLevel();
    testUtility.createMemoryDatabase();
  }

  @AfterClass
  public static void afterAllTests()
    throws PropertiesDataSourceException, ClassNotFoundException
  {
    testUtility.shutdownDatabase();
  }

  private void executorIntegrationTest(final SchemaCrawlerExecutor executor,
                                       final OutputOptions outputOptions)
    throws Exception
  {
    final SchemaTextOptions schemaTextOptions = new SchemaTextOptions(null,
                                                                      outputOptions,
                                                                      SchemaTextDetailType.BASIC);

    executor.execute(new SchemaCrawlerOptions(), schemaTextOptions, testUtility
      .getDataSource());

    // Check post-conditions
    final File outputFile = outputOptions.getOutputFile();
    if (!outputFile.exists())
    {
      fail("Output file '" + outputFile.getName() + "' was not created");
    }
  }

  @Test
  public void schemaGraphingWithJung()
    throws Exception
  {
    final String outputFilename = File.createTempFile("schemacrawler", ".jpg")
      .getAbsolutePath();
    final OutputOptions outputOptions = new OutputOptions("800x600",
                                                          outputFilename);
    executorIntegrationTest(new JungExecutor(), outputOptions);
  }

  @Test
  public void templatingWithVelocity()
    throws Exception
  {
    final String outputFilename = File.createTempFile("schemacrawler", ".txt")
      .getAbsolutePath();
    final OutputOptions outputOptions = new OutputOptions("plaintextschema.vm",
                                                          outputFilename);
    executorIntegrationTest(new VelocityExecutor(), outputOptions);
  }

  @Test
  public void templatingWithFreeMarker()
    throws Exception
  {
    final String outputFilename = File.createTempFile("schemacrawler", ".txt")
      .getAbsolutePath();
    final OutputOptions outputOptions = new OutputOptions("plaintextschema.ftl",
                                                          outputFilename);
    executorIntegrationTest(new FreeMarkerExecutor(), outputOptions);
  }

}
