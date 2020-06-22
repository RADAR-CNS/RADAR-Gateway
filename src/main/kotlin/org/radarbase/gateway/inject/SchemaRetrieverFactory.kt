package org.radarbase.gateway.inject

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG
import org.radarbase.config.ServerConfig
import org.radarbase.gateway.Config
import org.radarbase.producer.rest.SchemaRetriever
import java.util.function.Supplier
import javax.ws.rs.core.Context

/** Creates a Schema Retriever based on the current schema registry configuration. */
class SchemaRetrieverFactory: Supplier<SchemaRetriever> {
    @Context
    private lateinit var config: Config

    override fun get(): SchemaRetriever {
        return SchemaRetriever(ServerConfig(config.kafka.serialization[SCHEMA_REGISTRY_URL_CONFIG]), 30)
    }
}
