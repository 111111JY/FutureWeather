package com.example.futureweather.logic.model

object Wind {
    /**
     * 获取风向
     * @param anchor Float 输入风向角度
     * @return String 风向
     */
     fun getWindDirection(anchor: Float): String {
        var direction = ""
        when (anchor.toInt()) {
            0 -> direction = "北风"
            in 1..89 -> direction = "东北风"
            90 -> direction = "东风"
            in 91..179 -> direction = "东南风"
            180 -> direction = "南风"
            in 181..269 -> direction = "西南风"
            270 -> direction = "西风"
            in 271..359 -> direction = "西北风"
        }
        return direction
    }

    /**
     * 获取风力
     * @param anchor Float 输入风速 km/h
     * @return String 风力
     */
     fun getWindStrength(speed: Float): String {
        var strength = ""
        when (speed.toInt()) {
            0 -> strength = "0级(无风)"
            in 1..5 -> strength = "1级(轻风)"
            in 6..11 -> strength = "2级(轻风)"
            in 12..19 -> strength = "3级(微风)"
            in 20..28 -> strength = "4级(和风)"
            in 29..38 -> strength = "5级(清风)"
            in 39..49 -> strength = "6级(强风)"
            in 50..61 -> strength = "7级(劲风)"
            in 62..74 -> strength = "8级(大风)"
            in 75..88 -> strength = "9级(烈风)"
            in 89..102 -> strength = "10级(狂风)"
            in 103..117 -> strength = "11级(暴风)"
            in 118..134 -> strength = "12级(台风)"
            in 135..149 -> strength = "13级(台风)"
            in 150..166 -> strength = "14级(强台风)"
            in 167..183 -> strength = "15级(强台风)"
            in 184..201 -> strength = "16级(超强台风)"
            in 202..220 -> strength = "17级(超强台风)"
        }
        return strength
    }
}