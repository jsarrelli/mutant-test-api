package com.example.muttanttestapi.perfomance


import com.example.mutanttestapi.ApplicationProperties
import com.example.mutanttestapi.controllers.requests.DNATestRequest
import com.google.gson.Gson
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.util.Random

class SpikeTest extends Simulation {
  val baseUrl: String = ApplicationProperties.INSTANCE.getProperty("base_url")
  val httpProtocol: HttpProtocolBuilder = http.baseUrl(baseUrl)
  val gson: Gson = new Gson();


  private def createRandomDna: List[String] = {
    val possibleValues = "ATCG".toList

    def generateRow = {
      for (_ <- 1 to 6) yield possibleValues(Random.nextInt(possibleValues.size))
      }.toString

    val matrix = for (_ <- 1 to 6) yield generateRow
    matrix.toList
  }

  private def createRequest = {
    val dnaTestRequest = new DNATestRequest();
    dnaTestRequest.setDna(createRandomDna.toArray)
    gson.toJson(dnaTestRequest);
  }

  val scn: ScenarioBuilder = scenario("spikeTestOnIsMutant").exec(
    http("isMutant")
      .post("/mutant")
      .body(StringBody(_ => createRequest)).asJson
  )
  setUp(scn.inject(
    constantUsersPerSec(20) during (1 minutes),
    constantUsersPerSec(20) during (20 minutes) randomized))
    .protocols(httpProtocol).assertions(
    global.responseTime.max.lt(30000),
  )
}
