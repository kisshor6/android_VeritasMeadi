package com.example.chatapplication

class Utils {
    companion object{
        private const val SECOND_MILLIS = 1000
        private const val MINUTES_MILLIS = 60 * SECOND_MILLIS
        private const val HOUR_MILLIS = 60 * MINUTES_MILLIS
        private const val DAY_MILLIS = 60 * HOUR_MILLIS


        fun getImageAgo(time : Long):String? {
            val now : Long = System.currentTimeMillis()
            if (time > now || time <= 0){
                return null
            }

            val differ = now - time
            return if (differ < MINUTES_MILLIS){
                "a Just now"
            }else if (differ < 2 * MINUTES_MILLIS){
                "a Minutes ago"
            }else if (differ < 50 * MINUTES_MILLIS){
                (differ/ MINUTES_MILLIS).toString() + "minutes ago"
            }else if (differ < 90 * MINUTES_MILLIS){
                "an hour ago"
            }else if (differ < 24 * HOUR_MILLIS){
                (differ/ HOUR_MILLIS).toString() + "hour ago"
            }else if(differ < 48 * HOUR_MILLIS){
                "yesterday"
            }else{
                (differ/ DAY_MILLIS).toString() + "days ago"
            }
        }
    }
}