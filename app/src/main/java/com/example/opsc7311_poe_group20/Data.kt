package com.example.opsc7311_poe_group20

import android.graphics.Bitmap
import android.media.Image
import android.widget.ImageView
import java.security.MessageDigest
import java.security.SecureRandom
import java.sql.Time
import java.util.Date
import java.util.Base64

data class TimesheetItems(
    val timesheetID: Int, //pk
    val date: Date,
    val startTime: Time,
    val EndTime: Time,
    val duration: Int, //will be the minutes
    val decsrp: String,
    val images: String,
    val projectName: String, //fk project
    val email: String //fk user
)
object Timesheetobj {
    val timesheetlist = mutableListOf<TimesheetItems>()
}

data class OwnBadges(
    var badge1 : Boolean,
    var badge2 : Boolean,
    var badge3 : Boolean,
    var badge4 : Boolean,
    var badge5 : Boolean,
    var email : String
)

object OwnBadgesObj {
    val ownBadgeslist = mutableListOf<OwnBadges>()
}

data class myBadges(
    var number : Int,
    var badgeTitle : String,
    var desc : String,
    var image : Int,
    var email : String
)

object BadgesObj {
    val Badgeslist = mutableListOf<myBadges>()
}

data class AllBadges(
    var number : Int,
    var badgeTitle : String,
    var desc : String,
    var image : Int
)

object AllBadgesObj {
    val AllBadgeslist = mutableListOf<AllBadges>()
}

data class Project(
    val ProjectID: Int,
    val ProjectName: String, //pk
    var ProjectPriority: String,
    var IsBillable: Boolean,
    var ClientName: String,
    var Rate: Double,
    var maximum_goal: Int,
    var minimum_goal: Int,
    var projectColor : String,
    var totalHours : Int,
    val email : String //fk user
)

object ProjectManager {
    val projectList = mutableListOf<Project>()
}

data class Users(
    val email: String, //pk
    var passwordHash: String,
    var passwordSalt: String,
    var name: String,
    var surname: String,
    var company: String,
    var mobile: String,
    var min: Int,
    var max: Int,
    var profilePicture: String
)
object UserManager {
    val userList = mutableListOf<Users>()
}


data class UserSettings (
    val ID : Int, //pk
    var dateFormat: String,
    var Is24HoursClock: Boolean,
    var firstDayOfTheWeek: String,
    var IsNotification: Boolean,
    var email: String //fk user
)

object UserSettingsManager {
    val userSettingsList = mutableListOf<UserSettings>()
}

fun generateSalt(): ByteArray {
    val salt = ByteArray(128 / 8)
    SecureRandom().nextBytes(salt)
    return salt
}

fun hashPassword(password: String, salt: ByteArray): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashedBytes = digest.digest(salt + password.toByteArray())
    return Base64.getEncoder().encodeToString(hashedBytes)
}