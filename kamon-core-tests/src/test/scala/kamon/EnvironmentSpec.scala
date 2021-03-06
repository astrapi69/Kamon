/* =========================================================================================
 * Copyright © 2013-2017 the kamon project <http://kamon.io/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 * =========================================================================================
 */

package kamon

import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, WordSpec}

class EnvironmentSpec extends WordSpec with Matchers {
  private val baseConfig = ConfigFactory.parseString(
    """
      |kamon.environment {
      |  service = environment-spec
      |  host = auto
      |  instance = auto
      |}
    """.stripMargin
  )

  "the Kamon environment" should {
    "assign a host and instance name when they are set to 'auto'" in {
      val env = Environment.fromConfig(baseConfig)

      env.host shouldNot be("auto")
      env.instance shouldNot be("auto")
      env.instance shouldBe s"environment-spec@${env.host}"
    }

    "use the configured host and instance, if provided" in {
      val customConfig = ConfigFactory.parseString(
        """
          |kamon.environment {
          |  host = spec-host
          |  instance = spec-instance
          |}
        """.stripMargin)

      val env = Environment.fromConfig(customConfig.withFallback(baseConfig))

      env.host should be("spec-host")
      env.instance should be("spec-instance")
    }

    "always return the same incarnation name" in {
      val envOne = Environment.fromConfig(baseConfig)
      val envTwo = Environment.fromConfig(baseConfig)

      envOne.incarnation shouldBe envTwo.incarnation
    }
  }
}
