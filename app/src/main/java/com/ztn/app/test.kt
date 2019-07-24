package com.ztn.app

/**
 * Created by 冒险者ztn on 2019/4/24.
 * 介绍 todo
 */

var arrayRight = ArrayList<Int>()

var leftLarge: Int = 0
var rightSmall = 1

fun test(giveArray: ArrayList<Int>): ArrayList<Int> {
    val showArrayList = ArrayList<Int>()

    if (giveArray.size > 0) {
        leftLarge = giveArray[0]

        if (giveArray.size == 1) {
            showArrayList.add(0)
        } else {

            for (i in 0 until giveArray.size) {
                val use: Boolean = if (i == 0) {
                    isSmallThanRight(giveArray[i])
                } else {
                    isLargeThanLeft(giveArray[i]) && isSmallThanRight(giveArray[i])
                }

                if (use) {
                    showArrayList.add(i)
                }
            }

        }

    }

    return showArrayList
}

//选中的数是否大于左边的数
fun isLargeThanLeft(select: Int): Boolean {
    return if (select > leftLarge) {
        true
    } else {
        leftLarge = select
        false
    }

}

//选中的
fun isSmallThanRight(select: Int): Boolean {
    return false
}


object Main {
    @JvmStatic
    fun main(args: Array<String>) {


    }
}
