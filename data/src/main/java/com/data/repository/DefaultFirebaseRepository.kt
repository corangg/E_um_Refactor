package com.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.core.di.IoDispatcher
import com.core.di.LocalDataSources
import com.core.util.getLocalTimeToString
import com.data.config.FRIEND_REQUEST_CODE
import com.data.config.FRIEND_RESPONSE_CODE
import com.data.config.SCHEDULE_REQUEST_CODE
import com.data.datasource.LocalDataSource
import com.data.mapper.toExternal
import com.domain.model.ChatMessageData
import com.domain.model.FriendAlarmData
import com.domain.model.FriendRequestResult
import com.domain.model.SignInResult
import com.domain.model.SignUpResult
import com.domain.model.UserInfo
import com.domain.repository.FirebaseRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultFirebaseRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val database: FirebaseDatabase,
    @LocalDataSources private val localDataSource: LocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationContext private val context: Context
) : FirebaseRepository {
    override fun isUserLoggedIn() = flow {
        val auth = FirebaseAuth.getInstance()
        emit(auth.currentUser != null)
    }

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

    override suspend fun trySignIn(email: String, password: String) = withContext(ioDispatcher) {
        return@withContext try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                SignInResult.Success
            } else {
                SignInResult.Failure
            }
        } catch (e: FirebaseAuthException) {
            when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> SignInResult.InvalidEmail
                "ERROR_USER_NOT_FOUND" -> SignInResult.UserNotFound
                "ERROR_WRONG_PASSWORD" -> SignInResult.InvalidPassword
                else -> SignInResult.Failure
            }
        } catch (e: Exception) {
            SignInResult.Failure
        }
    }

    override suspend fun getUserInfo() = withContext(ioDispatcher) {
        val userEmail = auth.currentUser?.email ?: return@withContext null
        getFirebaseUserInfo(userEmail)
    }

    override suspend fun updateProfileImage(uri: String) = withContext(ioDispatcher) {
        return@withContext try {
            val userEmail = auth.currentUser?.email ?: return@withContext ""
            val storagePath = firebaseStorage.reference.child("userProfile").child(userEmail)
                .child("profile_img.jpg")
            storagePath.putFile(uri.toUri()).await()
            storagePath.downloadUrl.await().toString()
        } catch (e: Exception) {
            ""
        }
    }

    override suspend fun checkPassword(password: String) = withContext(ioDispatcher) {
        val userEmail = auth.currentUser?.email ?: return@withContext false
        val credential = EmailAuthProvider.getCredential(userEmail, password)
        return@withContext try {
            auth.currentUser?.reauthenticate(credential)?.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun changePassword(password: String) = withContext(ioDispatcher) {
        return@withContext try {
            auth.currentUser?.updatePassword(password)?.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun trySignOut() = withContext(ioDispatcher) {
        auth.signOut()
    }

    override suspend fun getFriendList() = withContext(ioDispatcher) {
        val userEmail = auth.currentUser?.email ?: return@withContext listOf()
        val docRef = firestore.collection("FriendList").document(userEmail).get().await()
        return@withContext docRef.data?.keys?.toList() ?: return@withContext listOf()
    }

    override suspend fun getEmailInfo(email: String) = withContext(ioDispatcher) {
        getFirebaseUserInfo(email)
    }

    private suspend fun getFirebaseUserInfo(email: String): UserInfo? {
        return try {
            val docRef = firestore.collection("UserInfo").document(email).get().await()
            val userDataMap = docRef.data ?: return null
            UserInfo(
                email = userDataMap["email"] as? String ?: "",
                password = userDataMap["password"] as? String ?: "",
                name = userDataMap["name"] as? String ?: "",
                nickname = userDataMap["nickname"] as? String ?: "",
                phone = userDataMap["phone"] as? String ?: "",
                zoneCode = userDataMap["zoneCode"] as? String ?: "",
                address = userDataMap["address"] as? String ?: "",
                imgUrl = userDataMap["imgUrl"] as? String ?: "",
                statusMessage = userDataMap["statusMessage"] as? String ?: "",
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateFriendValue(email: String) = withContext(ioDispatcher) {
        val userEmail = auth.currentUser?.email ?: return@withContext false
        return@withContext addFriend(userEmail, email) && addFriend(email, userEmail)
    }

    private suspend fun addFriend(userEmail: String, friendEmail: String): Boolean{
        return try {
            firestore.collection("FriendList").document(userEmail).set(mapOf(friendEmail to getLocalTimeToString()), SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun DataSnapshot.toFriendAlarmData(): FriendAlarmData? {
        val type = child("type").getValue(Int::class.java) ?: return null
        val email = child("email").getValue(String::class.java) ?: return null
        val nickname = child("nickname").getValue(String::class.java) ?: return null
        val time = key ?: return null

        return when (type) {
            FRIEND_REQUEST_CODE -> FriendAlarmData.RequestFriendFriendAlarmData(email, nickname, time)
            FRIEND_RESPONSE_CODE -> {
                val booleanValue = child("value").getValue(Boolean::class.java) ?: false
                FriendAlarmData.ResponseFriendFriendAlarmData(email, nickname, booleanValue, time)
            }
            else -> null
        }
    }

    override fun getAlarmListFlow() = callbackFlow {
        val replaceUserEmail = auth.currentUser?.email?.replace(".", "_") ?: return@callbackFlow
        val reference = database.getReference("alarm").child(replaceUserEmail)
        val friendAlarmDataList = mutableListOf<FriendAlarmData>()

        val initialListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                friendAlarmDataList.clear()
                friendAlarmDataList.addAll(snapshot.children.mapNotNull { it.toFriendAlarmData() })
                trySend(friendAlarmDataList.toList()).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        reference.addListenerForSingleValueEvent(initialListener)

        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newAlarm = snapshot.toFriendAlarmData() ?: return
                if (!friendAlarmDataList.any { it.time == newAlarm.time }) {
                    friendAlarmDataList.add(newAlarm)
                    trySend(friendAlarmDataList.toList()).isSuccess
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removedAlarm = snapshot.toFriendAlarmData() ?: return
                friendAlarmDataList.removeAll { it.time == removedAlarm.time }
                trySend(friendAlarmDataList.toList()).isSuccess
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val updatedAlarm = snapshot.toFriendAlarmData() ?: return
                val index = friendAlarmDataList.indexOfFirst { it.time == updatedAlarm.time }
                if (index != -1) {
                    friendAlarmDataList[index] = updatedAlarm
                    trySend(friendAlarmDataList.toList()).isSuccess
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        reference.addChildEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }.distinctUntilChanged()

    override suspend fun requestFriend(email: String) = withContext(ioDispatcher) {
        val userInfo = localDataSource.getUserInfoData()?: return@withContext FriendRequestResult.Fail.code
        val replaceUserEmail = userInfo.email.replace(".", "_")
        val replaceEmail = email.replace(".", "_")
        try {
            database.reference.child("alarm").child(replaceEmail).child(getLocalTimeToString()).apply {
                updateChildren(
                    mapOf(
                        "type" to FRIEND_REQUEST_CODE,
                        "email" to replaceUserEmail,
                        "nickname" to userInfo.nickname
                    )
                ).await()
            }
            FriendRequestResult.Success.code
        } catch (e: Exception) {
            FriendRequestResult.Fail.code
        }
    }

    override suspend fun requestSchedule(
        email: String,
        dateTime: String,
        scheduleAddress: String
    ) = withContext(ioDispatcher) {
        val userInfo = localDataSource.getUserInfoData() ?: return@withContext false
        val replaceUserEmail = userInfo.email.replace(".", "_")
        val replaceEmail = email.replace(".", "_")
        try {
            database.reference.child("alarm").child(replaceEmail).child(getLocalTimeToString())
                .apply {
                    updateChildren(
                        mapOf(
                            "type" to SCHEDULE_REQUEST_CODE,
                            "email" to replaceUserEmail,
                            "nickname" to userInfo.nickname,
                            "dateTime" to dateTime,
                            "address" to scheduleAddress
                        )
                    ).await()
                }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun responseFriendRequest(email: String, value: Boolean) = withContext(ioDispatcher) {
        val userInfo = localDataSource.getUserInfoData() ?: return@withContext false
        val replaceUserEmail = userInfo.email.replace(".", "_")
        try {
            database.reference.child("alarm").child(email).child(getLocalTimeToString()).apply {
                updateChildren(
                    mapOf(
                        "type" to FRIEND_RESPONSE_CODE,
                        "email" to replaceUserEmail,
                        "nickname" to userInfo.nickname,
                        "value" to value
                    )
                ).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteAlarmMessage(time: String) = withContext(ioDispatcher) {
        return@withContext try {
            val replaceUserEmail = auth.currentUser?.email?.replace(".", "_") ?: return@withContext false
            database.getReference("alarm").child(replaceUserEmail).child(time).removeValue().await()
            true
        } catch (e: Exception) {
            false
        }
    }


    //Chat

    override suspend fun getChatCode(email: String) = withContext(ioDispatcher) {
        return@withContext try {
            val userEmail = auth.currentUser?.email ?: return@withContext null
            val docRef = firestore.collection("Chat").document(userEmail).get().await()
            if (docRef.exists()) {
                docRef.data?.get(email).toString()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getNewChatCode() = withContext(ioDispatcher) {
        database.reference.child("chat").push().key
    }


    override suspend fun writeChatCode(email: String, code: String) = withContext(ioDispatcher) {
        return@withContext try {
            val userEmail = auth.currentUser?.email ?: return@withContext false
            val reference = firestore.collection("Chat")
            reference.document(userEmail).set(mapOf(email to code), SetOptions.merge()).await()
            reference.document(email).set(mapOf(userEmail to code), SetOptions.merge()).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getChatData(code: String) = withContext(ioDispatcher) {
        try {
            val reference = database.getReference("chat").child(code).get().await()
            reference.children.mapNotNull { it.toExternal() }
        }catch (e: Exception){
            emptyList()
        }
    }

    override fun collectChatData(code: String) = callbackFlow {
        val reference = database.getReference("chat").child(code)

        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newChat = snapshot.toExternal()
                trySend(newChat).isSuccess
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val changedChat = snapshot.toExternal()
                trySend(changedChat).isSuccess
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        reference.addChildEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }.distinctUntilChanged()

    override suspend fun sendChatMessage(message: String, code: String) = withContext(ioDispatcher){
        val userInfo = localDataSource.getUserInfoData() ?: return@withContext
        try {
            val time = getLocalTimeToString()
            val docRef = database.reference.child("chat").child(code)
            val chatData = ChatMessageData(
                email = userInfo.email,
                nickname = userInfo.nickname,
                time = time,
                message = message
            )
            val updateMap = mapOf("/$time" to chatData)
            docRef.updateChildren(updateMap).await()
        } catch (e: Exception) {
            return@withContext
        }
    }

    override suspend fun getChatMemberEmail(code: String) = withContext(ioDispatcher) {
        val userInfo = localDataSource.getUserInfoData() ?: return@withContext null
        return@withContext try {
            val document = firestore.collection("Chat").document(userInfo.email).get().await()
            val dataList = document.data?.map { entry ->
                entry.key to entry.value.toString()
            } ?: emptyList()
            dataList.first { it.second == code }.first
        } catch (e: Exception) {
            null
        }
    }

    override fun collectChatRoomData(email: String) = callbackFlow {
        val docRef = firestore.collection("Chat").document(email)

        val listenerRegistration: ListenerRegistration =
            docRef.addSnapshotListener { snapshots, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                if (snapshots != null && snapshots.exists()) {
                    snapshots.data?.forEach { (key, value) ->
                        trySend(key to value.toString())
                    }
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
}