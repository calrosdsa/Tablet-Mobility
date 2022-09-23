package com.coppernic.mobility.util.impl

import android.content.SharedPreferences
import com.coppernic.mobility.util.constants.MainDestination
import com.coppernic.mobility.util.interfaces.AppPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AppPreferencesImpl @Inject constructor(
    private val preferences: SharedPreferences
): AppPreferences {
    override val passwordStream: MutableStateFlow<String>
    override var password: String by PasswordPreferenceDelegate("password_pref", "1234")
    override val urlServidorStream: MutableStateFlow<String>
    override var urlServidor: String by UrlServidorPreferenceDelegate("url_servidor_pref","http://172.20.10.56:91")
    override val initialScreenStream: MutableStateFlow<String>
    override var initialScreen: String by InitialScreenPreferenceDelegate("initial_screen",MainDestination.INITIAL_SCREEN)
    override val accessPinStream: MutableStateFlow<String>
    override var accessPin: String by AccessPinPreferenceDelegate("access_pin","4321")
    override val tableNameStream: MutableStateFlow<String>
    override var tableName: String by TableNamePreferenceDelegate("table_name","Punto de Encuentro")
    init{
        passwordStream =MutableStateFlow(password)
        urlServidorStream = MutableStateFlow(urlServidor)
        initialScreenStream = MutableStateFlow(initialScreen)
        accessPinStream = MutableStateFlow(accessPin)
        tableNameStream = MutableStateFlow(tableName)
    }
    inner class TableNamePreferenceDelegate(
        private val name: String,
        private val default:String,
    ) : ReadWriteProperty<Any?, String> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): String =
            preferences.getString(name, default)?:""

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            tableNameStream.value = value
            with(preferences.edit()){
                putString(name, value)
                    .commit()
            }
        }
    }
    inner class AccessPinPreferenceDelegate(
        private val name: String,
        private val default:String,
    ) : ReadWriteProperty<Any?, String> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): String =
            preferences.getString(name, default)?:""

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            accessPinStream.value = value
            with(preferences.edit()){
                putString(name, value)
                    .commit()
            }
        }
    }

    inner class InitialScreenPreferenceDelegate(
        private val name: String,
        private val default:String,
    ) : ReadWriteProperty<Any?, String> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): String =
            preferences.getString(name, default)?:""

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            initialScreenStream.value = value
            with(preferences.edit()){
                putString(name, value)
                    .commit()
            }
        }
    }

    inner class UrlServidorPreferenceDelegate(
        private val name: String,
        private val default:String,
    ) : ReadWriteProperty<Any?, String> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): String =
            preferences.getString(name, default)?:""

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            urlServidorStream.value = value
            with(preferences.edit()){
                putString(name, value)
                    .commit()
            }
        }
    }

    inner class PasswordPreferenceDelegate(
        private val name: String,
        private val default:String,
    ) : ReadWriteProperty<Any?, String> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): String =
            preferences.getString(name, default)?:""

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            passwordStream.value = value
            with(preferences.edit()){
                putString(name, value)
                    .commit()
            }
        }
    }
}