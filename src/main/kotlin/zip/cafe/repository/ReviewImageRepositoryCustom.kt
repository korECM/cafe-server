package zip.cafe.repository

interface ReviewImageRepositoryCustom {
    fun findReviewImageUploaderIdsNumberOfReviewImagesIsGreaterThanExceptMembers(memberIds: List<Long>, numberOfReviewImagesThreshold: Long): List<Long>
}