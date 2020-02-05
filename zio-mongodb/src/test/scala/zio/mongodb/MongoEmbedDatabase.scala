/*
 * Copyright 2020 Alberto Paro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zio.mongodb

import de.flapdoodle.embed.mongo.config.{ MongodConfigBuilder, Net, RuntimeConfigBuilder }
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.{ Command, MongodExecutable, MongodProcess, MongodStarter }
import de.flapdoodle.embed.process.config.IRuntimeConfig
import de.flapdoodle.embed.process.config.io.ProcessOutput
import de.flapdoodle.embed.process.runtime.Network

/**
 * Extends this trait provide to your test class a connection to embedMongo database
 */
trait MongoEmbedDatabase {

  private val runtimeConfig =
    new RuntimeConfigBuilder().defaults(Command.MongoD).processOutput(ProcessOutput.getDefaultInstanceSilent()).build()

  protected def mongoStart(
    port: Int = 12345,
    host: String = "127.0.0.1",
    version: Version = Version.V4_0_2,
    runtimeConfig: IRuntimeConfig = runtimeConfig
  ): MongodProps = {
    val mongodExe: MongodExecutable = mongodExec(host, port, version, runtimeConfig)
    MongodProps(mongodExe.start(), mongodExe)
  }

  protected def mongoStop(mongodProps: MongodProps): Unit = {
    Option(mongodProps).foreach(_.mongodProcess.stop())
    Option(mongodProps).foreach(_.mongodExe.stop())
  }

  protected def withEmbedMongoFixture(
    port: Int = 12345,
    host: String = "127.0.0.1",
    version: Version = Version.V4_0_2,
    runtimeConfig: IRuntimeConfig = runtimeConfig
  )(fixture: MongodProps => Any) {
    val mongodProps = mongoStart(port, host, version, runtimeConfig)
    try {
      fixture(mongodProps)
    } finally {
      Option(mongodProps).foreach(mongoStop)
    }
  }

  private def runtime(config: IRuntimeConfig): MongodStarter = MongodStarter.getInstance(config)

  private def mongodExec(host: String, port: Int, version: Version, runtimeConfig: IRuntimeConfig): MongodExecutable =
    runtime(runtimeConfig).prepare(
      new MongodConfigBuilder().version(version).net(new Net(host, port, Network.localhostIsIPv6())).build()
    )
}

sealed case class MongodProps(mongodProcess: MongodProcess, mongodExe: MongodExecutable)
