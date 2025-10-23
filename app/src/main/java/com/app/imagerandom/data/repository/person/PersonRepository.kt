package com.app.imagerandom.data.repository.person

import com.app.imagerandom.domain.model.PersonDetail
import com.app.imagerandom.domain.model.PersonResponse
import com.app.imagerandom.domain.model.Response
import kotlinx.coroutines.flow.Flow

interface PersonRepository {
    fun getPopularPerson(language: String, page: Int): Flow<Response<PersonResponse>>
    fun getPersonDetailById(personId: Int): Flow<Response<PersonDetail>>
    fun searchPerson(query: String, page: Int): Flow<Response<PersonResponse>>
}