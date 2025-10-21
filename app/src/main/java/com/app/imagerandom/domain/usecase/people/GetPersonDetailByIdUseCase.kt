package com.app.imagerandom.domain.usecase.people

import com.app.imagerandom.data.repository.people.PersonRepository
import javax.inject.Inject

class GetPersonDetailByIdUseCase @Inject constructor(
    private val repository: PersonRepository
) {
    fun getPersonDetailById(personId: Int) = repository.getPersonDetailById(personId)
}