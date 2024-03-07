package park.sangeun.studyspringbatch.common

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class JasyptConfig(
    @Value("\${jasypt.encryptor.password}") private val key: String
) {
    @Bean("jasyptStringEncryptor")
    open fun stringEncryptor(): StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        val config = SimpleStringPBEConfig()
        config.password = key
        config.algorithm = "PBEWithMD5AndDES"
        config.poolSize = 1
        encryptor.setConfig(config)

        return encryptor
    }
}