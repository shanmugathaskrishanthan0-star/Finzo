package com.myexpenseanalyzer.app.repository

import com.myexpenseanalyzer.app.data.dao.MemberDao
import com.myexpenseanalyzer.app.data.entity.MemberEntity
import kotlinx.coroutines.flow.Flow

class MemberRepository(
    private val dao: MemberDao
) {

    val members: Flow<List<MemberEntity>> = dao.observeAll()

    suspend fun save(member: MemberEntity): Long {
        return if (member.id == 0L) {
            dao.insert(member)
        } else {
            dao.update(member)
            member.id
        }
    }

    suspend fun delete(member: MemberEntity) {
        dao.delete(member)
    }

    suspend fun getById(id: Long): MemberEntity? {
        return dao.getById(id)
    }
}