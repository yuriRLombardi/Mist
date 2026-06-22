package com.example.mist.data.local

import com.example.mist.data.remote.ApiClient.mistService
import com.example.mist.domain.Cor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class CorRepositoryImpl (
    private val corDAO: CorDAO
): CorRepository {

    override fun getAll(): Flow<List<Cor>> {
        return corDAO.getAll()
            .map { entities->
                entities.map { entity->
                    Cor(
                        id = entity.id,
                        cor = entity.cor
                    )
                }
            }

    }

    override suspend fun getById(id: Long): Cor {
        return corDAO.getById(id)?.let { corEntity ->
            Cor(
                id = corEntity.id,
                cor = corEntity.cor
            )
        }!!
    }

    override suspend fun insert(id: Long?, nome: String) {
        val cor = Cor(
            id = -1,
            cor =  nome
        )
        var entity: CorEntity? = null
        try {
            val remoteCor = if (id == null) {
                mistService.createCor(cor)
            } else {
                mistService.updateCor(id, cor)
            }

            entity = id?.let {
                corDAO.getById(id)?.copy(
                    cor = nome,
                )
            } ?: CorEntity(
                id = remoteCor.id,
                cor = nome
            )
            corDAO.insert(entity)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                entity?.let { corDAO.delete(it) }
            } else {
                entity?.let { corDAO.insert(it) }
            }
        } catch (e: Exception) {
            throw e
        }
    }

}