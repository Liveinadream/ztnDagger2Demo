package com.ztn.app

import android.os.Environment
import com.ztn.app.base.BaseApplication
import java.io.File

/**
 * Created by 冒险者ztn on 2019/2/12.
 * 介绍 todo
 */
object Constants {

    //================= TYPE ====================

    val TYPE_ZHIHU = 101

    val TYPE_ANDROID = 102

    val TYPE_IOS = 103

    val TYPE_WEB = 104

    val TYPE_GIRL = 105

    val TYPE_WECHAT = 106

    val TYPE_GANK = 107

    val TYPE_GOLD = 108

    val TYPE_VTEX = 109

    val TYPE_SETTING = 110

    val TYPE_LIKE = 111

    val TYPE_ABOUT = 112

    //================= KEY ====================

    //    public static final String KEY_API = "f95283476506aa756559dd28a56f0c0b"; //需要APIKEY请去 http://apistore.baidu.com/ 申请,复用会减少访问可用次数
    val KEY_API = "52b7ec3471ac3bec6846577e79f20e4c" //需要APIKEY请去 http://www.tianapi.com/#wxnew 申请,复用会减少访问可用次数

    val KEY_ALIPAY = "aex07566wvayrgxicnaraae"

    val LEANCLOUD_ID = "mhke0kuv33myn4t4ghuid4oq2hjj12li374hvcif202y5bm6"

    val LEANCLOUD_SIGN = "badc5461a25a46291054b902887a68eb,1480438132702"

    val BUGLY_ID = "257700f3f8"

    val FILE_PROVIDER_AUTHORITY = "com.codeest.geeknews.fileprovider"

    //================= PATH ====================

    val PATH_DATA = BaseApplication.instance.cacheDir?.absolutePath + File.separator + "data"

    val PATH_CACHE = PATH_DATA + "/NetCache"

    val PATH_SDCARD =
        Environment.getExternalStorageDirectory().absolutePath + File.separator + "codeest" + File.separator + "GeekNews"

    //================= PREFERENCE ====================

    val SP_NIGHT_MODE = "night_mode"

    val SP_NO_IMAGE = "no_image"

    val SP_AUTO_CACHE = "auto_cache"

    val SP_CURRENT_ITEM = "current_item"

    val SP_LIKE_POINT = "like_point"

    val SP_VERSION_POINT = "version_point"

    val SP_MANAGER_POINT = "manager_point"

    //================= INTENT ====================
    val IT_GANK_TYPE = "gank_type"

    val IT_GANK_TYPE_CODE = "gank_type_code"

    val IT_GANK_DETAIL_TITLE = "gank_detail_title"

    val IT_GANK_DETAIL_URL = "gank_detail_url"

    val IT_GANK_DETAIL_IMG_URL = "gank_detail_img_url"

    val IT_GANK_DETAIL_ID = "gank_detail_id"

    val IT_GANK_DETAIL_TYPE = "gank_detail_type"

    val IT_GANK_GRIL_ID = "gank_girl_id"

    val IT_GANK_GRIL_URL = "gank_girl_url"

    val IT_GOLD_TYPE = "gold_type"

    val IT_GOLD_TYPE_STR = "gold_type_str"

    val IT_GOLD_MANAGER = "gold_manager"

    val IT_VTEX_TYPE = "vtex_type"

    val IT_VTEX_TOPIC_ID = "vtex_id"

    val IT_VTEX_REPLIES_TOP = "vtex_replies_top"

    val IT_VTEX_NODE_NAME = "vtex_node_name"

    val IT_ZHIHU_DETAIL_ID = "zhihu_detail_id"

    val IT_ZHIHU_DETAIL_TRANSITION = "zhihu_detail_transition"

    val IT_ZHIHU_THEME_ID = "zhihu_theme_id"

    val IT_ZHIHU_SECTION_ID = "zhihu_section_id"

    val IT_ZHIHU_SECTION_TITLE = "zhihu_section_title"

    val IT_ZHIHU_COMMENT_ID = "zhihu_comment_id"

    val IT_ZHIHU_COMMENT_ALL_NUM = "zhihu_comment_all_num"

    val IT_ZHIHU_COMMENT_SHORT_NUM = "zhihu_comment_short_num"

    val IT_ZHIHU_COMMENT_LONG_NUM = "zhihu_comment_long_num"
}
