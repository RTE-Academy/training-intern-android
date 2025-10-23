package com.app.imagerandom.domain.usecase.person

import com.app.imagerandom.data.repository.person.PersonRepository
import javax.inject.Inject

class SearchPersonUseCase @Inject constructor(
    private val repository: PersonRepository,
) {
    fun searchPerson(query: String, page: Int) = repository.searchPerson(query, page)
}