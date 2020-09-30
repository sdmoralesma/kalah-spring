package kalah.perf

import com.intuit.karate.gatling.PreDef._
import io.gatling.core.Predef._

import scala.concurrent.duration._

class GamesSimulation extends Simulation {

  val protocol = karateProtocol(
    "/games" -> Nil,
    "/games/{gameId}" -> Nil,
    "/games/{gameId}/pits/{pitId}" -> Nil
  )

  val create = scenario("games-crud").exec(karateFeature("classpath:kalah/perf/games.feature"))

  setUp(
    create.inject(rampUsers(5500) during (10 seconds)).protocols(protocol)
  )

}