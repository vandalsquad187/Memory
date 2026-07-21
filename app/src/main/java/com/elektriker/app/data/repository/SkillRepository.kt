package com.elektriker.app.data.repository

import com.elektriker.app.data.local.dao.SkillDao
import com.elektriker.app.data.local.entity.SkillEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkillRepository @Inject constructor(
    private val skillDao: SkillDao
) {
    fun getAllSkills(): Flow<List<SkillEntity>> = skillDao.getAllSkills()

    fun getSkillsForCategory(category: String): Flow<List<SkillEntity>> =
        skillDao.getSkillsForCategory(category)

    suspend fun getSkillsForCategoryOnce(category: String): List<SkillEntity> =
        skillDao.getSkillsForCategoryOnce(category)

    suspend fun addXp(skillId: String, xp: Int) {
        val skill = skillDao.getSkillById(skillId) ?: return
        val newXp = skill.currentXp + xp
        var newLevel = skill.level
        var remainingXp = newXp

        while (remainingXp >= skill.nextLevelXp && newLevel < skill.maxLevel) {
            remainingXp -= skill.nextLevelXp
            newLevel++
        }

        skillDao.updateSkill(skill.copy(
            currentXp = remainingXp.coerceAtLeast(0),
            level = newLevel,
            nextLevelXp = xpForNextLevel(newLevel)
        ))
    }

    private fun xpForNextLevel(level: Int): Int {
        return level * 100
    }
}
