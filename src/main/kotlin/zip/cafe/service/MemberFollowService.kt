package zip.cafe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import zip.cafe.repository.MemberRepository
import zip.cafe.repository.findOneById

@Transactional(readOnly = true)
@Service
class MemberFollowService(
    private val memberRepository: MemberRepository
) {

    @Transactional
    fun follow(fromMemberId: Long, toMemberId: Long) {
        val from = memberRepository.findOneById(fromMemberId)
        val to = memberRepository.findOneById(toMemberId)
        from.follow(to)
    }

    @Transactional
    fun unfollow(fromMemberId: Long, toMemberId: Long) {
        val from = memberRepository.findOneById(fromMemberId)
        val to = memberRepository.findOneById(toMemberId)
        from.unfolow(to)
    }
}
