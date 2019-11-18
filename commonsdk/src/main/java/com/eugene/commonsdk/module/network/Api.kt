/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eugene.commonsdk.module.network

/**
 * ================================================
 * CommonSDK 的 Api 可以定义公用的关于 API 的相关常量, 比如说请求地址, 错误码等, 每个组件的 Api 可以定义组件自己的私有常量
 *
 * Created by JessYan on 30/03/2018 17:16
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
interface Api {
    companion object {
        val APP_DOMAIN = "https://api.github.com"

        val REQUEST_SUCCESS = "200"

        // 错误码
        val ERROR_USER_INCORRECT = "1001"
        val ERROR_PHONE_EXIST = "1019"
        val VALIDATION_OVERTIME = "1020"
        val NOT_PERMISSIONS = "403"
        val API_EXCPTION = "500"
        val REQUEST_PARAM_INCORRECT = "1000"
        val VADATION_INCORRECT = "1002"
        val FIRST_STOP_AFTER_DELETE = "1003"
        val UPLOADING_FILE_OVERSIZE = "1004"
        val EDIT_COMMO_LEVEL = "1005"
        val COMMO_CLASS_RE = "1006"
        val COMMO_TRADEMARK_RE = "1007"
        val LOGISTICS_TEMPLATE_RE = "1008"
        val COMMO_ATTRIBUTE_RE = "1009"
        val USER_NOT_REGISTER = "1010"
        val USER_ALREADY_ADD = "1011"
        val COMMO_CLASS_DELETE = "1012"
        val COMMO_TRANDMARK_DELETE = "1013"
        val COMMO_STORE_OFF = "1014"
        val USER_FORBIDDEN = "1015"
        val ONLY_FIVE_OPERATION = "1024"
        val NAME_ONE_MODIFICATION = "1017"
        val NAME_EXIST = "1018"
        val FIVE_ERR_PWDD = "1016"
        val PWD_NOT_LIKE = "1053"
        val BINDPHONELIKE = "1025"
        val MODIFICOUNTTHREE = "1026"
        val MODIFIPHONEOVERTHREE = "1027"
        val NOTLOGININ = "1052"
        val PASSOWRDINCOREECT = "1055"
        val NOTMATCHIMGBANKCARD = "1041"
        val IDCARDEXIT = "1038"
        val STORENAMEEXIT = "1028"
        val COMPANYNAMEEXIT = "1030"
        val CODEEXIT = "1029"
        val BANKREPETION = "1074"
        val REPETITION = "1032"
        val SERIVENULL = "1044"
        val LIMITMESSAGE = "1082"
        val NO_DEAL = "1081"
    }
}
