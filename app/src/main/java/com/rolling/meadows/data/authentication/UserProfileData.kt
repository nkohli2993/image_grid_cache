package com.rolling.meadows.data.authentication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.rolling.meadows.R
import com.rolling.meadows.data.FieldErrors
import com.rolling.meadows.utils.extensions.isValidEmail
import kotlinx.android.parcel.Parcelize


@Parcelize
data class UserProfileData(
    @SerializedName("address")
    var address: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = "",
    @SerializedName("bank_details")
    var bankDetails: BankDetails = BankDetails(),
    @SerializedName("country")
    var country: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("driving_license")
    var drivingLicense: DrivingLicense = DrivingLicense(),
    @SerializedName("email")
    var email: String = "",
    @SerializedName("email_notification")
    var emailNotification: String = "",
    @SerializedName("email_verification_otp")
    var emailVerificationOtp: String = "",
    @SerializedName("email_verified_at")
    var emailVerifiedAt: String = "",
    @SerializedName("first_name")
    var firstName: String = "",
    @SerializedName("full_name")
    var fullName: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("iso_code")
    var isoCode: String = "",
    @SerializedName("last_name")
    var lastName: String = "",
    @SerializedName("license_expiry_date")
    var licenseExpiryDate: String = "",
    @SerializedName("license_file")
    var licenseFile: String = "",
    @SerializedName("license_issue_date")
    var licenseIssueDate: String = "",
    @SerializedName("license_number")
    var licenseNumber: String = "",
    @SerializedName("notification")
    var notification: Int = 0,
    @SerializedName("on_duty")
    var onDuty: Int = -1,
    @SerializedName("rating")
    var rating: String = "0",
    @SerializedName("phone_code")
    var phoneCode: String = "",
    @SerializedName("phone_number")
    var phoneNumber: String = "",
    @SerializedName("phone_verification_otp")
    var phoneVerificationOtp: Int = 0,
    @SerializedName("phone_verified_at")
    var phoneVerifiedAt: String = "",
    @SerializedName("profile_image")
    var profileImage: String = "",
    @SerializedName("role")
    var role: Int = 0,
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("step_completed")
    var stepCompleted: Int = 0,
    @SerializedName("total_rides")
    var totalRides: Int = 0,
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("auth_token")
    var auth_token: String = "",
    @SerializedName("account_verification")
    var accountVerification: Int = -1,
    @SerializedName("vehicle_details")
    var vehicleDetails: VehicleDetails = VehicleDetails(),
    @SerializedName("personal_details_status")
    var personalDetailsStatus:Int = -1,
    @SerializedName("driving_license_status")
    var drivingLicenseStatus:Int = -1,
    @SerializedName("vehicle_details_status")
    var vehicleDetailsStatus:Int = -1,
    @SerializedName("vehicle_documents_status")
    var vehicleDocumentsStatus:Int = -1,
    @SerializedName("bank_details_status")
    var bankDetailsStatus:Int = -1
) : FieldErrors(), Parcelable {


    fun isValid(): Boolean {
        reset()
        when {
            firstName.isNullOrEmpty() -> {
                errorName = R.string.plz_enter_full_name
            }
            lastName.isNullOrEmpty() -> {
                errorLastName = R.string.plz_enter_last_name
            }
            email.isNullOrEmpty() -> {
                errorEmail = R.string.plz_enter_email_address
            }
            !email!!.isValidEmail() -> {
                errorEmail = R.string.plz_enter_valid_email_address
            }
            phoneNumber.isNullOrEmpty() -> {
                errorPhoneNum = R.string.plz_enter_phone_number
            }
            phoneNumber!!.length < 6 -> {
                errorPhoneNum = R.string.plz_enter_valid_phone_num
            }
            else -> {
                return true
            }
        }
        return false
    }


    private fun reset() {
        errorEmail = null
        errorName = null
        errorLastName = null
        errorPhoneNum = null
        errorAddress = null
    }
}