package com.example.opsc7311_poe_group20

import java.security.MessageDigest
import java.security.SecureRandom
import java.sql.Time
import java.util.*

data class TimesheetItems(
    val timesheetID: Int, //pk
    val date: Date,
    val startTime: Time,
    val endTime: Time,
    val duration: Int, //will be  the minutes
    val decsrp: String,
    val images: String,
    val projectName: String, //fk project
    val email: String //fk user
)


object Timesheetobj {
    val timesheetlist = mutableListOf<TimesheetItems>()
}

data class OwnBadges(
    var badge1 : Boolean = false,
    var badge2 : Boolean = false,
    var badge3 : Boolean = false,
    var badge4 : Boolean = false,
    var badge5 : Boolean = false,
    var email : String = ""
){
    constructor() : this(false, false, false, false, false, "" )
}

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
    val projectName: String = "", //pk
    var projectPriority: String = "",
    var isBillable: Boolean = false,
    var clientName: String = "",
    var rate: Double = 0.0,
    var maximum_goal: Int = 0,
    var minimum_goal: Int =0 ,
    var projectColor : String = "",
    var totalHours : Int = 0,
    val email : String = ""  //fk user
)

{
    constructor() : this("","", false, "", 0.0,0, 0, "", 0, "" )

    // Override toString() to return the project name
    override fun toString(): String {
        return projectName
    }
}

object ProjectManager {
    val projectList = mutableListOf<Project>()
}

data class Users(
    val email: String = "", //pk
    var passwordHash: String = "",
    var passwordSalt: String = "",
    var name: String = "",
    var surname: String = "",
    var company: String = "",
    var mobile: String = "",
    var min: Int = 0,
    var max: Int = 0,
    var profilePicture: String = ""
) {
    // No-argument constructor
    constructor() : this("", "", "", "", "", "", "", 0, 0, "")
}

object UserManager {
    val userList = mutableListOf<Users>()
}


data class UserSettings (
    val ID : Int= 0, //pk
    var dateFormat: String = "",
    var Is24HoursClock: Boolean = true,
    var firstDayOfTheWeek: String = "",
    var IsNotification: Boolean = true,
    var email: String = "" //fk user
){
    constructor() : this(0, "", true, "", true, "")
}

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