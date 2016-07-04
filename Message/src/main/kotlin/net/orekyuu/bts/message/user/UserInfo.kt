package net.orekyuu.bts.message.user

class UserInfo (
        var id: Int = 0,
        var name: String = "",
        var type: UserType = UserType.GITHUB
)

class CreateUserMessage (
        var userId: String = ""
)

enum class UserType {
    GITHUB
}
