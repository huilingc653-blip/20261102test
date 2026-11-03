/*
* 0.0.1 新增回车事件，触发blur事件
* 0.0.2 根据后台的要求，整数位保证12位数即可
* 0.0.3 解决可编辑表格中有输入框点击时，阻止冒泡
* 0.0.4 新增加回车提交事件
* 0.0.5 后台提供的数据为科学计数格式,使用new Number再转一次
* 0.0.6　输入的值不能超过１００亿，超过了，直接提示并清空
* */

layui.define(["jquery","money"], function(exports) {
    var $ = layui.jquery;
    var money = bt.money;

    /*
     * Created with Sublime Text 3.
     * license: http://www.lovewebgames.com/jsmodule/index.html
     * github: https://github.com/tianxiangbing/format-number
     * User: 田想兵
     * Date: 2015-08-01
     * Time: 11:27:55
     * Contact: 55342775@qq.com
     */
    ;
    (function(root, factory) {
        //amd
        if (typeof define === 'function' && define.amd) {
            define(['$'], factory);
        } else if (typeof exports === 'object') { //umd
            module.exports = factory();
        } else {
            root.FormatNumber = factory(window.Zepto || window.jQuery || $);
        }
    })(this, function($) {
        $.fn.FormatNumber = function(settings) {
            var arr = [];
            $(this).each(function() {
                var options = $.extend({
                    elem: $(this)
                }, settings);
                var number = new FormatNumber();
                number.init(options);
                arr.push(number);
            });
            return arr;
        };
        var FormatNumber = function() {};
        FormatNumber.prototype = {
            init: function(settings) {
                var _this = this,
                    minus = '',
                    settings = $.extend({
                        elem: '[data-type="money"]',
                        decimal: 2,
                        minus: false, //是否支持负数,默认不支持
                        parent: 'body',
                        percent:false,
                        separator:',',
                        linkElemDiv:'',//关联大写的div id
                        rateType:'',
                        ratiotype:false, //比率默认为false
                        isSuperMin:false //用于利率控件，最小值根据min参数，可以小于等于0
                    }, settings);
                this.settings = settings;
                var excludeKey = {
                    left: 37,
                    right: 39,
                    top: 38,
                    down: 40,
                    home: 36,
                    end: 35,
                    shift: 16,
                };

                var regex = '([1-9]\\d*(\\.\\d{1,2})?|0(\\.\\d{1,2})?)';
                if (settings.decimal <= 0 && settings.minus == true) {
                    regex = '(^-?[1-9]\\d*$)|(^[0]$)'; //正负整数
                } else if (settings.decimal == 0) {
                    regex = '(^[1-9]\\d*$)|(^[0]$)'; //正整数
                } else if (settings.minus == true) {
                    //regex = '^-?([1-9]\\d*(\\.\\d{1,' + settings.decimal + '})?|([^0]{1}(\\.\\d{1,' + settings.decimal + '}))?)';
                    regex = '(^-)?((([1-9]d*(\\.\\d{1,' + settings.decimal + '})?)|((^[0]{1}\\.(\\d{1,' + settings.decimal + '})?$))|^[0]$))'
                } else {
                    regex = '([1-9]\\d*(\\.\\d{1,' + settings.decimal + '})?|([^0]{1}(\\.\\d{1,' + settings.decimal + '}))?)';
                }
                var parent = parent || 'body';
                $(settings.elem, parent).each(function(i, v) {
                    var txt, formatTxt;
                    if ($(v)[0].tagName == 'INPUT') {
                        txt = $(v).val();
                        if (txt){
                            if( txt.indexOf(',') !== -1){
                                txt = _this.getMoneyfloat( txt );
                            }
                            txt = Number(txt);
                            if ( isNaN(txt)){
                                txt = ''
                            }
                            $(v).val(txt);
                        }
                        if ($(v).attr('data-name')) {
                            var name = $(v).attr('data-name'),
                                parentId = $(v).attr('id') || $(v).data('id'),
                                id = parentId ? parentId:name.split('.')[1] || name.split('.')[0];
                            $(v).removeAttr('id').attr("data-id",id);
                            $(v).parent().append('<input data-rule="number" id="' + id + '" type="hidden" name="' + name + '" />');
                            if(txt){
                                txt = parseFloat(txt);//解决与最大值比较的问题
                            }
                            //当初始值小于最小值或者大于最大值时，对数据进行截取
                            if( (settings.min !== undefined || settings.max !== undefined ) && txt ) {
                                if(txt < settings.min ){
                                    txt = settings.min;
                                    $(v).val(txt);
                                }
                                if( txt > settings.max ){
                                    txt = settings.max;
                                    $(v).val(txt);
                                }
                            }
                            //解决传过来的值有效数字不对的问题，如传入的值带有好多个0的问题，对传入的值进行有效数字格式化
                            //开始
                            var valueFloatMoney = _this.getMoneyfloat( $(v).val() );
                            if(txt != ''){
                                var initDecimal = settings.decimal;
                                if(settings.rateType && !settings.ratiotype){
                                    initDecimal = 6;
                                }

                                if(settings.ratiotype){ //比例时改成4
                                    initDecimal = 4;
                                }
                                var initValue =  money.format(valueFloatMoney,{
                                    separator:'',
                                    decimal:initDecimal
                                });
                                txt = initValue;

                                $(v).val(txt);

//                               $('#' + id).val(txt);
                              }
                              $('#' + id).val(valueFloatMoney);


                            // 结束
                        }
                        //存贮上一个值beforeValue;
                        var beforeValue = $('#' + id).val();
                        $('#' + id).data('beforevalue',beforeValue)
                        if(txt ===''){
                            return false;
                        }
                        txt = transferFromRate(txt,settings);
                        formatTxt = _this.doFormat(txt);
                        $(v).val(formatTxt);
                        // $(this).attr('data-value', _this.getMoneyfloat($(v).val()));

                        $(this).data('value',_this.getMoneyfloat(formatTxt))
                    } else {
                        txt = $(v).text();
                        formatTxt = _this.doFormat(txt);
                        //$(v).attr('data-value', txt).text(formatTxt);
                        $(v).data('value',txt).text(formatTxt)
                    }
                });

                if(!$('.inputNumber')[0] && !$(settings.elem).hasClass('bt-biz-rateType') && !$(settings.elem).hasClass('bt-biz-ratioType')){
                    var html = getPaneHtml();
                    $("body").append(html);
                }
              //  $(settings.elem, parent).on('keyup keydown', function(e) {
                $(settings.elem, parent).on('keyup keydown', function(e) {
                    if(e.keyCode == 13){ // 新增加 回车事件，触发失去焦点事件
                        //新增回车提交事件
                        if(!$(this).parents('.layui-form').hasClass('enter-switch') ){ //当form表单中没有hasSwitch样式，代表有切换功能,关闭回车提交事情功能
                            $(this).trigger('blur');
                            setTimeout(function(){
                                $("form").find('button[type="submit"]').trigger('click');
                            }, 50);
                        }else{
                            if(e.type == 'keydown'){
                                enterToNextInput($(this));
                                return false;
                            }
                        }
                        return;
                    }
                    checkNumber.call(this, e)
                });

                /**
                 * 回车切换至下一个input text focus
                 * */
                var enterToNextInput = function(currentElem){
                    var currentRowNextInputElem = function(td,next){
                        var nextTd = td?td.next('td'):next;
                        if(nextTd[0]){ //1、当前行有，那就寻找td下面的input；
                            var inputElem = nextTd.find('input[type="text"]');
                            if(inputElem[0] && inputElem.is(':visible')){
                                setTimeout(function(){
                                    inputElem.trigger('focus');
                                }, 500);
                                return ''
                            }else{ //如果没有可见输入框，继续查询下一个
                                currentRowNextInputElem(nextTd)
                            }
                        }else{ //如果没有说明是当前行最后一个td
                            if(td.parent('tr').next('tr')[0]){ //2、当前行没有，那就寻找下一行；
                                var nextRowFirstTd =  td.parent('tr').next('tr').find('td:eq(0)');
                                currentRowNextInputElem('',nextRowFirstTd)
                            }else{
                                return ''
                            }
                        }
                    }

                    var currentTd = currentElem.parents('td');
                    currentRowNextInputElem(currentTd);
                }

                //解决可编辑表格中，行被选择，点击输入框时，阻止冒泡
                $(settings.elem, parent).on('click', function(e) {
                    layui.stope(e);
                });

                $(settings.elem, parent).siblings('a').on('click', function(e) {
                    // $(this).siblings("input").val('');

                });
                //解决整数部分太长的问题
                var substringInt = function(v){
                    if(v.length > 12 && v.indexOf('-') === -1 ){ //整数部分最长为12位数
                        return v.substring( 0,12 );
                    }else if( v.length > 13 && v.indexOf('-') !== -1){//整数部分有负数时，加一位
                        return v.substring( 0,13 );
                    }else if( v.length >1 && v.indexOf('0') === 0 ){
                        return v.substring( 1,v.length );
                    }else{
                        return v;
                    }
                }

                 $(settings.elem).off('input propertychange').on('input propertychange', function(e) {
                     var v = $.trim ( $(this).val());
                     if( settings.maxlength && v ){
                         if( settings.minus ){
                             v = v.replace(/[^\.-\d]/g, '');
                         }else{
                             v = v.replace(/[^\.\d]/g, '');
                         }

                         if(v.indexOf('.') !== -1){//小数
                             var a = v.split('.');
                             a[0] = substringInt(a[0]);
                             if( a[1].length > settings.decimal ){ //
                                 a[1] = a[1].substring(0,settings.decimal);
                             }
                             v = a[0] +'.' + a[1];
                         }else{//整数
                             v = substringInt(v);//整数分超长时12时
                         }
                         $(this).val(v);
                     }
                 })
                // $(settings.elem).off('blur').on('blur propertychange', function(e) {
                $(settings.elem).off('blur').on('blur', function(e) {
                    $('.inputNumber').hide();
                    var v = $(this).val();
                    /*if ( ( v -0 ) > 999900000000 ) { //控制为千亿则不会进如该方法，则注掉
                        layer.alert(bt.lang('common.msg.moreThanNineBillion'), {icon: 3,closeBtn:0}, function (index) {//'输入的金额不能超过100亿'
                            layer.close(index);
                        });
                        $(this).val('');
						v = "";
                    }*/

                    var id = $(this).attr('data-id');
                    var preValue = $('#' + id).data('beforevalue');
                    if(settings.rateType && !settings.ratiotype){ //利率时，值必须大于0，小于0直接清空
                        if( v  <= 0 && !settings.isSuperMin){ //当isSuperMin 为false时，最小值才是大于0；当是超级最小值为true，最小值可以自定义
                            $(this).val('');
                            $('#' + id).val('');
                            if(settings.callback && typeof settings.callback === 'function'){
                                settings.callback('',$(this),preValue)
                            }
                            return false
                        }
                        if (settings.isSuperMin && v < settings.min){
                            $(this).val('');
                            $('#' + id).val('');
                            if(settings.callback && typeof settings.callback === 'function'){
                                settings.callback('',$(this),preValue)
                            }
                            return false
                        }
                    }
                    if(v !=''){ //当内容为空时，不进行数字验证
                        // return false;
                        GetNumberResult(e, $(this)[0], regex);
                    }else{
                        if(settings.ratiotype){
                            value = transferFromRate(0,settings);
                            $(this).val(value);
                            $('#' + id).val(0);
                            return false
                        }
                    }
                    // var id = $(this).attr('data-id');
                    v = _this.getMoneyfloat($(this).val());
                    if(v){
                        v = parseFloat(v);//转为数字
                    }
                    //起始框
                    if(settings.moneyEndId){
                        var endValue = $("#"+settings.moneyEndId).val();//截止框的输入值；
                        var endMax = $('input[data-id="'+settings.moneyEndId+'"]').data('max');//获取截止框的最大值；

                        if(endValue !==0 && endValue || endValue === 0){ //如果截止框的有输入值
                            settings.max = endValue;//起始框的最大值就是它
                        }else {
                            if(endMax){
                                settings.max = endMax;//起始框的最大值就是截止框的最大值参数
                            }else{
                                settings.max = '';
                            }
                        }
                    }
                    //截止框
                    if(settings.link){
                        var startValue = $('input[name="'+settings.linkstart+'"]').val();//通过name值，可以获取起始框的输入值;
                        var startMin = $('input[data-name ="'+settings.linkstart+'"]').data('min');
                        if(startValue !==0 && startValue || startValue === 0 ){
                            settings.min = startValue;
                        }else{
                            if(startMin){
                                settings.min = startMin;
                            }
                        }
                    }
                    var num = $(this).val();
                    if( (settings.max !== undefined && settings.max !=='') && v > settings.max && num){
                        v = _this.getMoneyfloat(settings.max);
                        if(settings.linkElemDiv){
                            $(settings.linkElemDiv).html(bt.money.toChinese(v.toString()))
                        }
                    }

                    if(v < settings.min && num){
                        v = _this.getMoneyfloat(settings.min);
                        if(settings.linkElemDiv){
                            $(settings.linkElemDiv).html(bt.money.toChinese(v.toString()))
                        }
                    }

                    var formatValue = _this.doFormat(v);
                    v = transferToRate(v,settings);
                    if (isNaN(v)){
                        v = ''
                        $(this).val('')
                    }
                    $('#' + id).val(v);
                    if(formatValue) $(this).val(formatValue);

                    if(settings.ratiotype){ //比例组件时，输入非法数字时，赋值为0
                        if(formatValue === ''){
                            $(this).val(_this.doFormat(0));
                        }
                    }
                  //  $(this).attr('data-value', formatValue);
                    if (isNaN(formatValue)){
                        formatValue = '';
                    }
                    $(this).data('value',formatValue)
                    if(settings.callback && typeof settings.callback === 'function'){
                        settings.callback(v,$(this),preValue)
                    }
                    //如果有关联大写数字，需要失去焦点时，重新大写格式化
                    var $linkElemDiv = $(settings.linkElemDiv);
                    if($linkElemDiv[0]){
                        $linkElemDiv.html(bt.money.toChinese(v));
                    }
                });
                // 新增加 回车事件，触发失去焦点事件
                //$(settings.elem).off('keyup').on('keyup', function(e) {
                //    if(e.keyCode == 13){
                //        $(this).trigger('blur');
                //    }
                //})

                $(settings.elem).off('focus').on('focus', function(e) {
                    var id = $(this).attr('data-id');
                    var value = $('#'+id).val();
                    $('#'+id).data('beforevalue',value);
                    // if(value ==''){
                    if(value ==''){
                        return false;
                    }
                    //去千分或百分符号
                    value = transferFromRate(value,settings);
                    $(this).val(value);
                    setTxtCursorPosition($(this)[0], value.length);
                    $(this).select();//获取焦点时，选中内容
                })
                //转成百分位或千分位
                function transferFromRate(value,settings){
                    if(settings.rateType){
                        if(settings.rateType =='percent'){
                            if(settings.ratiotype){
                                value =  money.mul(value,100,settings.decimal);
                            }else{
                                value =  money.mul(value,100,4);
                            }

                            // value = 10000*value*100 / 10000;
                        }else if(settings.rateType =='thousands'){
                            // value = 10000*value*1000 / 10000;
                            if(settings.ratiotype){
                                value =  money.mul(value,1000,settings.decimal);
                            }else{
                                value =  money.mul(value,1000,3);
                            }

                        }
                    }
                    return value
                }

                //转成小数位，去除百分比或千分比
                function transferToRate(value,settings){
                    if(settings.rateType){
                        if(settings.rateType =='percent'){
                            // value = value / 100;
                            if(settings.ratiotype){
                                var decial = settings.decimal + 2;
                                value =  money.div(value,100,decial);//比率时转业默认为四位

                            }else{
                                value =  money.div(value,100,6)
                            }

                        }else if(settings.rateType =='thousands'){

                            if(settings.ratiotype){
                                value =  money.div(value,1000,4);//比率时转业默认为四位

                            }else{
                                value =  money.div(value,1000,6)
                            }

                        }
                    }
                    return value
                }

                function getPaneHtml(){
                    var temp ='';
                    temp +='<div class="inputNumber" ><div style="width:260px"><div class="panelCard"></div></div></div>'
                    return temp;
                }

                function setoffset(inputElem,divElem){
                    var bodyWidth = $('body').width();
                    var offset = inputElem.offset();
                    var inputWidth = inputElem.width()-3;
                    var lt = bodyWidth - inputWidth-offset.left;
                    var tp = offset.top ;
                    var h = inputElem.height()+2;
                    if(tp < 70){
                        tp = tp + h +2;
                    }else{
                        tp = tp - h -11;
                    }
                    divElem.css({
                        right:lt + "px",
                        top:tp +"px"
                    })
                }

                function checkNumber(e) {
                    var id = $(this).attr('data-id');
                    var v = _this.getMoneyfloat($(this).val());
                    $('#' + id).val(v);
                    // $(this).attr('data-value', v);
                    $(this).data('value',v);
                    var pane = $('.inputNumber');

                    if(!$(this).hasClass('bt-biz-rateType')){
                        pane.hide().show();
                    }

                    if(v == null){
                        pane.hide()
                    }
                    var val = $(this).val();
                    if(val.indexOf('.') !==-1){
                        var valueArry = val.split('.');
                        val = valueArry[0].replace(/\d{1,3}(?=(\d{3})+$)/g, '$&'+_this.settings.separator+'') +'.'+valueArry[1]
                    }else{
                        val = val.replace(/\d{1,3}(?=(\d{3})+$)/g, '$&'+_this.settings.separator+'');
                    }
                    pane.html(val)
                    setoffset($(this),pane)
                }

                function GetNumberResult(e, obj, reg) {
                    var valueLength = obj.value.length;
                    // var position = getTxtCursorPosition(obj);
                    var key = window.event ? e.keyCode : e.which;
                    var result = convertNumberN(key, obj.value, reg);
                    obj.value = result
                    // position += (result.length - valueLength);
                    // setTxtCursorPosition(obj, position);
                }

                /**
                 *   说明: 检查不做处理的键盘Key
                 *   参数: 键盘KeyCode {string}
                 * 返回值: 如果是不做处理的key返回true，反之false {bool}
                 */
                function checkInactionKey(keyCode) {
                    for (var key in excludeKey) {
                        if (keyCode == excludeKey[key]) {
                            return true;
                        }
                    }
                    return false;
                }

                /**
                 *   说明: 获取文本框的光标位置
                 *   参数: 文本框对象 {dom object}
                 * 返回值: {int}
                 */
                function getTxtCursorPosition(txtObj) {
                    var tempObj = txtObj;
                    var cursurPosition = -1;

                    if (tempObj.selectionStart != undefined) { //非IE浏览器
                        cursurPosition = tempObj.selectionStart;
                    } else { //IE
                        var range = document.selection.createRange();
                        range.moveStart("character", -tempObj.value.length);
                        cursurPosition = range.text.length;
                    }

                    return cursurPosition;
                }

                /**
                 *   说明: 转换数字为千分位，常用于财务系统
                 *   参数: 键盘key {string}，被处理的字符串 {string}
                 * 返回值: 返回转换的结果 {string}
                 */
                function convertNumberN(key, value, reg) {

                    if (checkInactionKey(key)) {
                        return value;
                    }
                    var tempValue = value;
                    var isminus = false;
                    var replaceReg = /[^\d\.]/g;
                    if ((settings.minus || settings.min === 0 || settings.min ) && /^\-/.test(tempValue)) {
                        tempValue = tempValue.slice(1);
                        isminus = true;
                    }
                    if (tempValue.indexOf(".") <= 0) {
                        //replaceReg = /[^\d]/g;
                        tempValue = tempValue.replace(replaceReg, "");
                    } else {
                        tempValue = tempValue.replace(replaceReg, "");
                        var isNaNNum = parseFloat(tempValue + "00");
                        if (isNaN(isNaNNum)) {
                            tempValue = isNaNNum;
                        }
                        if (/\./.test(tempValue) && settings.decimal == 0) {
                            tempValue = tempValue.split(".")[0];//解决decial 参数为0时，只取整数部分
                            // tempValue = tempValue.toString().replace(/\./g, '');
                        }
                    }
                    var re = new RegExp(reg);
                    if (!re.exec(tempValue) && tempValue != "") {
                        tempValue = "0";
                    }

                    var tempValueArray = tempValue.split(".");
                    tempValue = tempValueArray.length > 1 ?
                       // (tempValueArray[0].length > 14 ? tempValueArray[0].substr(0, 14) : tempValueArray[0]) + "." + tempValueArray[1] :
                        (tempValueArray[0].length > 12 ? tempValueArray[0].substr(0, 12) : tempValueArray[0]) + "." + tempValueArray[1] :
                        (tempValue.length > 12 ? tempValue.substr(0, 12) : tempValue);
                        //(tempValue.length > 14 ? tempValue.substr(0, 14) : tempValue);

                    var result = _this.doFormat(tempValue);
                    if (isminus) result = '-' + result;
                    if (result == null) {
                        return;
                    }

                    var resultArray = result.split(".");

                    if (tempValue.lastIndexOf(".") >= 0) {
                        if (tempValue.lastIndexOf(".") == tempValue.length - 1) {
                            tempValue = resultArray[0] + '.';
                        } else {
                            var subLength = tempValue.length - (tempValue.lastIndexOf(".") + 1);
                            tempValue = resultArray[0] + "." + (resultArray[1] ? resultArray[1].substring(0, subLength) : '0');
                        }
                    } else {
                        tempValue = resultArray[0];
                    }
                    return tempValue;
                }

                /**
                 *   说明: 设置文本框的光标位置
                 *   参数: 文本框对象 {dom object}, 光标的位置 {int}
                 * 返回值: {void}
                 */
                function setTxtCursorPosition(txtObj, pos) {

                    var tempObj = txtObj;
                    var cursurPosition = -1;

                    if (tempObj.selectionStart != undefined) { //非IE浏览器
                        tempObj.setSelectionRange(pos, pos);
                    } else { //IE
                        var range = tempObj.createTextRange();
                        range.move("character", pos);
                        range.select();
                    }
                }
            },
            getMoneyfloat: function(s) {
                if(s === 0){
                    return 0
                }
                if (s == '') {
                    return null;
                }
                return parseFloat((s + "").replace(/[^\d\.-]/g, ""));
            },
            doFormat: function(s) {
                var _this = this;
                if (!s && s !== 0){
                    return "";
                }
                if ($.isNumeric(s)) {
                    s = s.toString();
                }
                if (typeof s === 'string') {
                    var xx = _this.settings.elem;
                    var isminus = false;
                    if(s.indexOf('-') === 0){
                        isminus = true;
                        var arr = s.split("-",2);
                        s = arr[1];
                    }
                    var s = (s+'').replace(/^(\d+)((\.\d*)?)$/, function(v1, v2, v3) {
                        v3 = v3 ? v3 + '00000000' : '.00000000';

                        // var ss = _this.settings.decimal == 0?'':v3.slice(0, _this.settings.decimal + 1);
                        //解决数据无四舍五入的问题
                        var s1 = _this.settings.decimal == 0?'':v3.slice(0, _this.settings.decimal);
                        var s2 = _this.settings.decimal == 0?'':v3.slice(_this.settings.decimal, _this.settings.decimal + 1) -0;
                        var s3 = _this.settings.decimal == 0?'':v3.slice(_this.settings.decimal+1, _this.settings.decimal + 2) - 0;
                        if(s3>4){
                            s2 = s2 + 1;
                            if( s2 > 9){ //大于9时，要向前进一位
                                var n = s1.replace('.','');
                                n = n - 0 + 1;
                                n = n.toString();
                                if( s1.indexOf(9) == 1){
                                    n = n.slice(2);
                                    v2 = v2 - 0 + 1;//
                                    v2 = v2.toString();
                                }
                                s1 = '.' + n;
                                s2 = s2 - 10;
                            }
                            if ($.isNumeric(s2)) {
                                s2 = s2.toString();
                            }
                        }
                        // 结束
                        return v2.replace(/\d{1,3}(?=(\d{3})+$)/g, '$&'+_this.settings.separator+'') + '' + ( s1 + s2) || '.00';
                    });
                    if(isminus) s = '-' + s
                }
                return s.replace(/^\./, "0.")
            },

        };
        return FormatNumber;
    });

    exports("formatnumber")

})
