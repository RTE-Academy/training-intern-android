package com.app.imagerandom.domain.usecase.person

import com.app.imagerandom.data.repository.person.PersonRepository
import javax.inject.Inject

class GetPersonUseCase @Inject constructor(
    private val repository: PersonRepository
) {
    fun getPerson(language: String, page: Int) = repository.getPopularPerson(language, page)
}