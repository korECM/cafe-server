package zip.cafe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import zip.cafe.connector.S3ConnectorConfig

@EnableConfigurationProperties(value = [S3ConnectorConfig::class])
@SpringBootApplication
class CafeApplication

fun main(args: Array<String>) {
    runApplication<CafeApplication>(*args)
}
