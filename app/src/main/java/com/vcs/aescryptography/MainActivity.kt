package com.vcs.aescryptography

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ask for external storage permission
        if (shouldAskPermissions()) {
            askPermissions()
        }

        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

        val file = File(filesDir.path + "/sampleFile.jpg")
        Log.d(TAG,"file dir: ${filesDir.path}")
        val encryptedFileName = "encryptedFile.bin"
        val fileToWrite = File(filesDir.path, encryptedFileName)
        val encryptedFile = EncryptedFile.Builder(
            fileToWrite,
            applicationContext,
            mainKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        try {
            val readingByteArray = ByteArray(8192)
            val fis = FileInputStream(file)
            val bis = BufferedInputStream(fis)

            encryptedFile.openFileOutput()
            val bos = BufferedOutputStream(encryptedFile.openFileOutput())
            bos.flush()

            do {
                bis.read(readingByteArray)
                Log.d(TAG,"byte array: $readingByteArray")
                bos.write(readingByteArray)

            } while (bis.available() != 0)

            bos.close()
            bis.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }




    }

    companion object {
        const val TAG = "CRYPTOGRAPHY APP"
    }

    //request external memory permission
    private fun shouldAskPermissions(): Boolean {
        return (true)
    }

    /**
     * This function request for important permissions, if user accept, application will work correctly
     */
    private fun askPermissions() {
        val permissions = arrayOf(
            "android.permission.MANAGE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
        )
        val requestCode = 200
        requestPermissions(permissions, requestCode)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}