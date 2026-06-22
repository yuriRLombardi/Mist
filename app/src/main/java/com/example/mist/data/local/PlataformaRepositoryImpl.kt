package com.example.mist.data.local

import com.example.mist.data.remote.ApiClient.mistService
import com.example.mist.domain.Console
import com.example.mist.domain.Cor
import com.example.mist.domain.Marca
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class PlataformaRepositoryImpl(
    private val dao: PlataformDAO,
    private val corDAO: CorDAO,
    private val marcaDAO: MarcaDAO,
    private val corRepository: CorRepository,
    private val marcaRepository: MarcaRepository
) : PlataformRepository {

    override suspend fun insert(
        nome: String,
        preco: Float,
        corId: Long,
        marcaId: Long,
        ano: Long,
        id: Long?
    ) {
        val corDoConsole = corRepository.getById(corId)
        val marcaDoConsole = marcaRepository.getById(marcaId)

        val console = Console(
            id = -1,
            nome = nome,
            preco = preco,
            cor = corDoConsole,
            marca = marcaDoConsole,
            ano = ano
        )

        var entity: PlataformEntity? = null

        try {
            val remotePlataforma = if (id == null) {
                mistService.createConsole(console)
            } else {
                mistService.updateConsole(id, console)
            }

            entity = id?.let {
                dao.getById(id)?.copy(
                    nome = nome,
                    preco = preco,
                    corId = corId,
                    marcaId = marcaId,
                    ano = ano
                )
            } ?: PlataformEntity(
                id = remotePlataforma.id,
                nome = nome,
                preco = preco,
                corId = corId,
                marcaId = marcaId,
                ano = ano
            )
            dao.insert(entity)
        } catch (e: HttpException) {
            if (e.code() == 404) {
                entity?.let { dao.delete(it) }
            } else {
                entity?.let { dao.insert(it) }
            }
        } catch (e: Exception) {
            throw e
        }
    }


    override suspend fun delete(id: Long) {
        var entity: PlataformEntity? = dao.getById(id)

        try {
            mistService.deleteConsole(
                id = id
            )
            entity = dao.getById(id) ?: return
        } catch (e: HttpException) {
            if (e.code() != 404) {
                entity?.let { dao.insert(it) }
            }
        } catch (e: Exception) {
            entity?.let { dao.insert(it) }
        } finally {
            entity?.let { dao.delete(it) }
        }
    }

    override fun getAll(): Flow<List<Console>> {
        return dao.getAll()
            .map { entities ->
                entities.map { entity ->
                    Console(
                        id = entity.id,
                        nome = entity.nome,
                        preco = entity.preco,
                        cor = Cor(
                            id = entity.corId,
                            cor = corRepository.getById(entity.corId).cor
                        ),
                        marca = Marca(
                            id = entity.marcaId,
                            marca = marcaRepository.getById(entity.marcaId).marca
                        ),
                        ano = entity.ano
                    )
                }
            }
            .onStart {
                withContext(Dispatchers.IO) {
                    val remoteCor = mistService.getAllCor()
                    corDAO.insertAll(
                        remoteCor.map { cor ->
                            CorEntity(
                                id = cor.id,
                                cor = cor.cor
                            )
                        }
                    )

                    val remoteMarca = mistService.getAllMarca()
                    marcaDAO.insertAll(
                        remoteMarca.map { marca ->
                            MarcaEntity(
                                id = marca.id,
                                marca = marca.marca
                            )
                        }
                    )

                    val remotePlataform = mistService.getAllCon()
                    dao.deleteAll()
                    dao.insertAll(remotePlataform.map { console ->
                        PlataformEntity(
                            id = console.id,
                            nome = console.nome,
                            preco = console.preco,
                            corId = console.cor.id,
                            marcaId = console.marca.id,
                            ano = console.ano
                        )

                    })

                }
            }
    }

    override suspend fun getById(id: Long): Console? {
        return dao.getById(id)?.let { console ->
            Console(
                id = console.id,
                nome = console.nome,
                preco = console.preco,
                cor = Cor(
                    id = console.corId,
                    cor = ""
                ),
                marca = Marca(
                    id = console.marcaId,
                    marca = ""
                ),
                ano = console.ano
            )
        }
    }
}