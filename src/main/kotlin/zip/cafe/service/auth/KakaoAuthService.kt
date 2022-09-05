package zip.cafe.service.auth

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate

@Transactional(readOnly = true)
@Service
class KakaoAuthService(
    private val restTemplate: RestTemplate
) {

    fun getUserInfo(accessToken: String) {
        val host = "https://kapi.kakao.com/v2/user/me"
        val entity = createEntityForGetUserInfo(accessToken)
        println(entity)
        val response = restTemplate.exchange(host, HttpMethod.GET, entity, String::class.java)
        println("response = $response")
    }

    private fun createEntityForGetUserInfo(accessToken: String) = HttpEntity<Nothing>(HttpHeaders().apply { setBasicAuth(accessToken) })
}


