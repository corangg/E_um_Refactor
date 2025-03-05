package com.data.repository

import android.content.Context
import com.core.di.IoDispatcher
import com.core.di.LocalDataSources
import com.data.datasource.LocalDataSource
import com.domain.model.SignUpResult
import com.domain.model.UserInfo
import com.domain.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultFirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    @LocalDataSources private val localDataSource: LocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : FirebaseRepository {

    override suspend fun trySignUp(email: String, password: String) = withContext(ioDispatcher) {
        return@withContext try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            if (result.user != null) SignUpResult.Success
            else SignUpResult.Failure
        } catch (e: FirebaseAuthUserCollisionException) {
            SignUpResult.AlreadyExists
        } catch (e: Exception) {
            SignUpResult.Failure
        }
    }

    override suspend fun setUserData(userInfo: UserInfo) = withContext(ioDispatcher) {
        return@withContext try {
            firestore.collection("UserInfo").document(userInfo.email).set(userInfo).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}