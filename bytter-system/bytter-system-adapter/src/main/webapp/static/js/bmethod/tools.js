/**
 * 组件：公共方法js
 * liuyj
 * 2020年12月30日
 * 0.0.1 特殊符号转换方法；
 */

bt.define(function(exports) {
    var arrEntities = {'lt':'<','gt':'>','nbsp':' ','amp':'&','quot':'"','#39':"'"};
    var tools = {
        /**
         * 特殊符号转换方法
         * @param str 字符串
         * @returns 转换后的字符串
         */
        escapeHtml: function(str){
            if (str && typeof str ==="string"){
                return str.replace(/&(lt|gt|nbsp|amp|quot|#39);/ig,function(all,t){return arrEntities[t];});
            }
        },

    };
    exports('tools',tools)
});