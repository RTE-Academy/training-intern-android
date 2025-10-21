package com.app.imagerandom.domain.usecase.people

import com.app.imagerandom.data.repository.people.PersonRepository
import javax.inject.Inject

class GetPersonUseCase @Inject constructor(
    private val repository: PersonRepository
) {
    fun getPerson(language: String, page: Int) = repository.getPopularPerson(language, page)
}