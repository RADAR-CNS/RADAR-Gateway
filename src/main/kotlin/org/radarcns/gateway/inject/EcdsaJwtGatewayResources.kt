package org.radarcns.gateway.inject

import org.glassfish.jersey.internal.inject.AbstractBinder
import org.glassfish.jersey.server.ResourceConfig
import org.radarcns.gateway.auth.AuthValidator
import javax.inject.Singleton

class EcdsaJwtGatewayResources : GatewayResources {
    override fun registerAuthentication(resources: ResourceConfig) {
        // none needed
    }

    override fun registerAuthenticationUtilities(binder: AbstractBinder) {
        binder.bind(EcdsaJwtTokenValidator::class.java)
                .to(AuthValidator::class.java)
                .`in`(Singleton::class.java)
    }
}