/**
 * Created by Peter on 2016/10/23.
 */
// CBoard settings
var settings = {
    // en/cn: Switch language to Chinese
    preferredLanguage: "cn"
};

var locale = null;
var CB_I18N;

// 从后台获取当前系统设置的语种类型
// 中文返回zh_CN；英文返回en_US
$.ajax({
    url: basePath + "/olap/commons/locale",
    type: "GET",
    success: function(data) {
        if (data) {
            data = data.toString().replace(/"/g, '');
        }
        return locale = data;
    },
    error: function (ex) {
        console.log(ex);
        return null;
    },
    async: false
});

if (locale == "en_US") {
    locale = 'en';
} else if (locale == "zh_CN") {
    locale = 'cn';
} else {
    locale = 'cn';
}
// 设置语种
settings.preferredLanguage = locale;
// 获取语种文件
$.ajax({
    url: basePath + "/olap/i18n/" + settings.preferredLanguage + "/cboard.json?_btv=" + resourceVersion,
    type: "GET",
    dataType: "json",
    success: function (data) {
        return CB_I18N = data;
    },
    async: false
});


// $.ajax({
//     url: "i18n/" + settings.preferredLanguage + "/cboard.json?_btv=" + resourceVersion,
//     type: "GET",
//     dataType: "json",
//     success: function(data) {
//         return CB_I18N = data;
//     },
//     async: false
// });