package com.example.mist.data.local


import com.example.mist.data.remote.ApiClient.mistService
import com.example.mist.domain.Marca
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class MarcaRepositoryImpl (
    private val marcaDAO: MarcaDAO
): MarcaRepository {

    override fun getAll(): Flow<List<Marca>> {
        return marcaDAO.getAll()
            .map { entities->
                entities.map { entity->
                    Marca(
                        id = entity.id,
                        marca = entity.marca
                    )
                }
            }

    }

    override suspend fun getById(id: Long): Marca {
        return marcaDAO.getById(id)?.let { marcaEntity ->
            Marca(
                id = marcaEntity.id,
                marca = marcaEntity.marca
            )
        }!!
    }

    override suspend fun insert(id: Long?, nome: String) {
        val marca = Marca(
            id = -1,
            marca =  nome
        )
        var entity: MarcaEntity? = null
        try {
            val remoteCor = if (id == null) {
                mistService.createMarca(marca)
            } else {
                mistService.updateMarca(id, marca)
            }

            entity = id?.let {
                marcaDAO.getById(id)?.copy(
                    marca = nome,
                )
            } ?: MarcaEntity(
                id = remoteCor.id,
                marca = nome
            )
            marcaDAO.insert(entity)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                entity?.let { marcaDAO.delete(it) }
            } else {
                entity?.let { marcaDAO.insert(it) }
            }
        } catch (e: Exception) {
            throw e
        }
    }

}