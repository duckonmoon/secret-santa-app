package com.softserveinc.test.secretsanta.constans

import com.softserveinc.test.secretsanta.R


interface Constants {
    companion object {
        const val NICKNAMES = "nicknames"
        const val NICKNAME = "nickname"
        const val USERS = "users"
        const val GROUPS = "groups"
        const val HUMANS = "humans"
        const val REQUEST_CODE = 1
        const val ID_MESSAGE = "key=AAAAM41jq6I:APA91bF9FmQJ0UoFGLtw7LONFcHb_f0zRrABQ_-uSPs1KXG2iGkMROv_36tZebghR4M6t_hyhbaF0NjQLn9eqasMqIz_ANzGNRO7W1AOP4XKGXC8cJAmTLDb2OMf-5iPNWy8uVh3c8DY"
        const val MAIN_SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES"


        val images = mapOf(
                Pair(0, R.drawable.face),
                Pair(1, R.drawable.christmas_house),
                Pair(2, R.drawable.ic_candy_round),
                Pair(3, R.drawable.santaa)
        )
    }
}