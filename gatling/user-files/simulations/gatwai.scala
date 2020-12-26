
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class gatwai extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:9090/shipping/shippinginfo")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en-US;q=0.9,en;q=0.8")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.193 Safari/537.36")

	val headers_0 = Map(
		"Cache-Control" -> "max-age=0",
		"If-Modified-Since" -> "Tue, 10 Nov 2020 10:56:26 GMT",
		"If-None-Match" -> """"5faa71da-9da"""",
		"Proxy-Connection" -> "keep-alive",
		"Authorization" -> "Bearer 36df41ee-ed8f-4f7d-ac26-0481d2ce5c61")



	val scn = scenario("gatwai")
		.exec(http("request_0")
			.get("/")
			.headers(headers_0))

	setUp(scn.inject(constantUsersPerSec(5) during (120 seconds))).protocols(httpProtocol)
}