package bogglegame

import android.content.Context
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class FileHelper(private val context: Context) {

    fun writeStatToFile(stat: BoggleStats, filename: String) {
        try {
            val fos: FileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(stat)
            oos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readStatFromFile(filename: String): BoggleStats? {
        try {
            val fis: FileInputStream = context.openFileInput(filename)
            val ois = ObjectInputStream(fis)
            val stat = ois.readObject() as BoggleStats
            ois.close()
            return stat
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }


}