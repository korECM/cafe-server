package study.cafe.repository

import org.springframework.data.jpa.repository.JpaRepository
import study.cafe.entity.auth.LocalAuth

interface LocalAuthRepository : JpaRepository<LocalAuth, Long>
