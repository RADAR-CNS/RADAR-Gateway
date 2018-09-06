package org.radarcns.gateway

import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.server.ResourceConfig
import org.radarcns.gateway.auth.ManagementPortalAuthenticationFilter
import org.radarcns.gateway.inject.GatewayBinder
import java.util.concurrent.TimeUnit

open class GrizzlyServer(private val config: Config) {
    private var httpServer: HttpServer? = null

    private fun getResourceConfig(): ResourceConfig {
        val resources = ResourceConfig()
        resources.packages(
                "org.radarcns.gateway.exception",
                "org.radarcns.gateway.filter",
                "org.radarcns.gateway.io",
                "org.radarcns.gateway.resource")
        resources.register(GatewayBinder(config))
        registerAuthentication(resources)
        return resources
    }

    protected fun registerAuthentication(resources: ResourceConfig) {
        resources.register(ManagementPortalAuthenticationFilter::class.java)
    }

    fun start() {
        httpServer = GrizzlyHttpServerFactory.createHttpServer(config.baseUri, getResourceConfig())
        httpServer!!.start()
    }

    fun shutdown() {
        httpServer?.shutdown()?.get(5, TimeUnit.SECONDS)
    }
}
