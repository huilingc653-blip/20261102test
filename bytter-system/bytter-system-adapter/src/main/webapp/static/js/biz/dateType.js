/**
 * 时间组件
 * 本组件依赖layui的laydate，以及jquery
 * liuyj
 * 2017年7月2日
 * 0.0.1 支持页面标签参数及定义起始关系参数
 * 0.0.2 增加readonly参数
 * 0.0.3 对默认值中有带点的异常字符进行了处理
 * 0.0.4 添加max参数
 * 0.0.4 添加reset方法
 * 0.0.5 添加禁用的方法
 * 0.0.6 新增attr方法
 * 0.0.7 新增回调参数elem
 * 0.0.8 解决时间区间时，添加keyup事件
 * 0.0.9 解决时间区间时默认值关联的问题,新增加startDate形参
 * 0.0.10 新增initMax参数，用于设置起始值为固定最大值
 * 0.0.11 解决后台时间超过长度19的问题
 * 0.0.12 form重置统一转到form.js中处理
 * 0.0.13 新增加isEndDate 参数，判断endDate元素是否存在的问题
 * 0.0.14 重构时间控件，改用laydate 5.0
 * 0.0.15 优化并去掉两端空格
 * 0.0.16 解决输入值无校验的问题
 * 0.0.17 新增lang国际化变量
 * 0.0.18 新增起止时间val赋值时，将对最小值或最小值重新赋值
 * 0.0.19 新增按钮配置参数，月份及区间不支持这个参数
 */
bt.define(['laydate','jquery','form','date'], function(exports){
    "use strict";
    var laydate = layui.laydate,$ = layui.jquery,form = layui.form();
    var DateType =function(){
        this.bizName = "dateType";
        this.currentObject = null;
        this.resetStatus = true;
        this.lang = window.top.webLocale?(window.top.webLocale == 'en_US'?'en':'cn'):'cn'

    };

    //初始化 singlecallback为个组件btdate传过来的单一时间回调方法
    DateType.prototype.init = function(ops,singleCallback){
        this.render(ops,singleCallback);
    };

    //渲染组件
    DateType.prototype.render = function(obj,singleCallback){
        var that = this;
        var settings = {};
        var domObj = obj || $('.bt-biz-dateType:not(.init)');
        var reRenderFlag = obj?true:false;
        bt.each(domObj,function(){
            var config = that.initDateType($(this),reRenderFlag,singleCallback);
        })
    }
    //初始化时间擦伤
    DateType.prototype.initDateType= function($elem,reRenderFlag,singleCallback){
        var that = this;
        var $container = $elem;
        var $end;
        var type = $container.data('type'),//时间类型
            endId = $container.data("endid".toLowerCase()),
            min = $container.data("min"),//最小值
            max = $container.data("max"),//最大值
            id = $container.attr("id"),
            today = $container.data("today"),//当value无值，设置当前时间
            initMax = $container.data("initmax".toLowerCase()),//应用于起始值为固定最大值,不随着结束时间变化
            btns = $container.data("btns");//配置按钮参数
        // readonly = $container.attr("readonly"),//设置只读属性，不能选择时间，这个已经在laydate里面添加了
        var isHasEndDate = false;//判断是否有结束时间，即为范围时间
        //暂时不支持年区间
        if(type =='year'){
            //return;
        }

        //设置时间类型，支持date\datetime\month  暂时不支持年
        var dateType = 'date';
        if(type){
            dateType = type;
        }

        var format = type=="datetime"?"YYYY-MM-DD hh:mm:ss":"YYYY-MM-DD";

        if( max && max.length > 19 && max.indexOf(" ") === 10){ //解决后台时间数据长度超出19的问题
            max = max.toString();
            max = max.substr(0,19);
        }
        if( min && min.length > 19 && min.indexOf(" ") === 10){ //解决后台时间数据长度超出19的问题
            min = min.toString();
            min = min.substr(0,19);
        }

        var config = {
            elem:$container[0],//容器
            // 解决后台提供的时间格式不对的问题
            max:max?bt.date.format(max,format):"2099-12-31 23:59:59",//最大日期
            // 解决后台提供的时间格式不对的问题
            min:min?bt.date.format(min,format):"1900-01-01 00:00:00",//最大日期
            format: format,
            initMax:initMax
        };
        var value = $.trim( $container.val());
        if(!value){
            $container.val('');
            //设置默认时间为当天时间或当月
            if(today){
                var dat1;
                if(dateType =="date"){
                    dat1 = new Date().format('yyyy-MM-dd');
                }else if(dateType =='datetime'){
                    dat1 = new Date().format("yyyy-MM-dd hh:mm:ss");
                }else if(dateType == 'month'){
                    dat1 = new Date().format("yyyy-MM");
                }
                if(!reRenderFlag){
                    $container.val(dat1).data('defaultvalue',dat1)
                }
            }

        }else{
            var fmt = config.format =="YYYY-MM-DD" ?"yyyy-MM-dd":"yyyy-MM-dd hh:mm:ss";
            if(value.length > 19 && value.indexOf(" ") === 10){ //解决后台时间数据长度超出19的问题
                value = value.toString();
                value = value.substr(0,19);
            }
            if(type && ( type == 'date' || type == 'datetime')){
                value = bt.date.format(value,fmt);//格式转换
                if(bt.date.compare(config.min,value) >= 0){ //如果默认值小于最小值时，默认等于最小值
                    value = config.min;
                }
                if(value.indexOf(".") !==-1){ //如果默认值带“.”时，替换为"-"
                    value = value.replace(".",'-');
                }
            }
            if(!reRenderFlag){
                $container.val(value).data('defaultvalue',value);
            }
        }
        $container.addClass("init");


        //当此元素为时间范围的结束时间，直接return，因为起始时间已经完成了对截止时间的处理
        if( id && $("input[data-endid='"+id+"']").length >0){
            return;
        }
        //判断是否是时间范围，如果是，isHasEndDate = true;
        if(endId && $("#"+endId).length >0){
            isHasEndDate = true;
            $end = $("#"+endId);
            if( $container.parents('td').find(".date-mid").length ){
                $container.parents('td').addClass("input100");
            }else{
                if( $container.parents('td').find('#'+endId).length ){
                    $container.parents('td').addClass("input100");
                }
            }
        }

        if(!isHasEndDate){
            //单一时间处理
            config.btns = btns ? btns.split(',') : [ 'clear','confirm']
            that.renderSinlgeDate($container,dateType,config,reRenderFlag,singleCallback);

        }else if( dateType !='month'){ //时间范围
            config.btns = [ 'clear','confirm']
            that.renderCycleDate($container,$end,dateType,config,reRenderFlag);

        }else{ //月份范围 dateType == month
            config.btns = [ 'confirm']
            that.renderCycleMonth($container,$end,config,reRenderFlag);
        }
    }

    //单一时间渲染
    DateType.prototype.renderSinlgeDate = function($container,dateType,config,reRenderFlag,singleCallback){
        var that = this;
        //单一时间处理
        var start = laydate.render({
            elem:$container[0],
            type:dateType,
            value:$container.val(),
            min:config.min,
            max:config.max,
            trigger: 'click',
            btns: config.btns,
            lang:this.lang,
            // ready:function(date){
            //     console.log(this);
            // },
            done:function(value, date, endDate){
                that.doCallback(value,$container);
                if(singleCallback && typeof singleCallback ==='function'){ //兼容旧的btdate的单一时间回调方法
                    singleCallback(value,$container);
                }
                if(!value){
                    $(start.config.elem).val('')
                }
            }
        });


        //判断输入的值是否符合最大值或者最小值
        $container.off('blur').on('blur',function(){
            var $this = $(this);
            var v = $this.val();
            v = bt.date.format(v,config.format,config.min,config.max);
            if(v === undefined){
                v ='';
            }
            //当输入的值时，验证其是否符合要求
            var maxDate = start.config.max.year + '-' + ( start.config.max.month - 0 + 1) +'-' + start.config.max.date +
                ' ' + start.config.max.hours +':' + start.config.max.minutes +':'+ start.config.max.seconds
            var minDate =  start.config.min.year + '-' + ( start.config.min.month - 0 + 1 ) +'-' + start.config.min.date +
                ' ' + start.config.min.hours +':' + start.config.min.minutes +':'+ start.config.min.seconds;

            var getMaxGap = bt.date.getBetweenDay(v,maxDate);
            var getMinGap = bt.date.getBetweenDay(minDate,v);
            if( getMaxGap < 0 || getMinGap < 0){
                v='';
                $this.val(v);
            }
            start.config.value = v;
        })

        //判断输入的值是否符合最大值或者最小值
        $container.parents('form').find('[type ="reset"]').off('blur').on('blur',function(){
            var $this = $(this);
            var v = $this.val();
            v = that.correctValue(v,config.format,config.min,config.max);
            $this.val(v);
        })

        var bizName = $container.data('biz-name'.toLowerCase());
        that.saveDateObj(bizName,start,$container);//保存时间对象
    }

    // 时间范围渲染
    DateType.prototype.renderCycleDate = function($start,$end,dateType,config,reRenderFlag){
        var that = this;
        if($start.length === 0 || $end.length === 0){
            return;
        }else{//添加起止标识
            $start.addClass('start');
            $end.addClass('end');
        }
        var startMaxDate;
        if(config.initMax){ //如果有initMax，代表起始最大值为固定值
            startMaxDate = config.initMax;
        }else{
            var endValue = $end.val();
            endValue = $.trim(endValue);
            if(endValue){
                startMaxDate = endValue;
                if(!reRenderFlag){
                    $end.data('defaultvalue',endValue);
                }

            }else{
                var endMaxDate =  $end.data('max');
                if(endMaxDate){
                    startMaxDate = endMaxDate;
                }else{
                    startMaxDate = '2099-12-31 23:59:59';
                }
            }
        }
        //判断输入的值是否符合最大值或者最小值
        $start.off('blur').on('blur',function(){
            var $this = $(this);
            var v = $this.val();
            v = bt.date.format(v,config.format,config.min,startMaxDate);
            if(v === undefined){
                v ='';
            }
            //当输入值时，验证其是否符合要求
            var endDate = $(end.config.elem).val();
            if(!endDate){
                endDate = end.config.max.year + '-' + ( end.config.max.month - 0 + 1) +'-' + end.config.max.date +
                    ' ' + end.config.max.hours +':' + end.config.max.minutes +':'+ end.config.max.seconds
            }
            var minDate =  start.config.min.year + '-' + ( start.config.min.month - 0 + 1 ) +'-' + start.config.min.date +
                ' ' + start.config.min.hours +':' + start.config.min.minutes +':'+ start.config.min.seconds;

            var getMaxGap = bt.date.getBetweenDay(v,endDate);
            var getMinGap = bt.date.getBetweenDay(minDate,v);
            if(getMaxGap < 0 || getMinGap < 0){
                $this.val('');
                end.config.min = that.makeDateObj(minDate);
            }else{
                start.config.value = v;
                if(v === ''){
                    end.config.min = start.config.min;
                }else{
                    end.config.min = that.makeDateObj(v);
                }
            }
        })
        //开始时间
        var start = laydate.render({
            elem:$start[0],
            type:dateType,
            value:$start.val(),
            min:config.min,
            max:startMaxDate,
            trigger:'click',
            btns: config.btns,
            lang:that.lang,
            ready:function(date){
                var startValue = $start.val();
                var endValue = $end.val();
                startValue = $.trim(startValue);
                if(startValue){
                    //开始日选好后，重置结束日的最小日期
                    end.config.value = end.config.min  = that.makelayDateObj(date);
                }
                that.correctDateButtonStatus(startValue,endValue,startMaxDate);
            },
            // change:function(value, date, endDate){
            // },
            done:function(value, date,endDate){
                if(!date){
                    var endDate = $start.data('defaultvalue');
                    if(!endDate){
                        endDate = $start.data('min');
                        if(!endDate){
                            endDate = "1900-01-01 00:00:00";
                        }
                    }
                    if(endDate){
                        end.config.value = end.config.min = that.makeDateObj(endDate);
                    }
                }else{
                    //开始日选好后，重置结束日的最小日期
                    end.config.value = end.config.min  = that.makelayDateObj(date);
                }
                that.doCallback(value,$start);
            }
        })

        var endMinDate = $start.val();
        if(!endMinDate){
            endMinDate = config.min;
        }
        var endMaxDate = $end.data('max');
        if(!endMaxDate){
            endMaxDate = '2099-12-31 23:59:59';
        }

        //结束时间
        var end = laydate.render({
            elem:$end[0],
            type:$end.data('type')?$end.data('type'):dateType,
            value:$end.val(),
            min:endMinDate,
            max:endMaxDate,
            trigger:'click',
            lang:that.lang,
            btns: config.btns,
            ready:function(date){
                var startValue = $start.val();
                var endValue = $end.val();
                endValue = $.trim(endValue);
                if(endValue){
                    //结束日选好后，重置开始日的最大日期
                    start.config.max = that.makelayDateObj(date);
                }
                that.correctDateButtonStatus(startValue,endValue,endMaxDate);
            },
            done:function(value, date, endDate){
                if(!date){
                    var endDate = $end.data('defaultvalue');
                    if(!endDate){
                        endDate = $end.data('max');
                        if(!endDate){
                            endDate = '2099-12-31 23:59:59';
                        }
                    }
                    start.config.max = that.makeDateObj(endDate);
                }else{
                    //结束日选好后，重置开始日的最大日期
                    start.config.max = that.makelayDateObj(date);
                }
                that.doCallback(value,$end);
            }
        })
        //判断输入的值是否符合最大值或者最小值
        $end.off('blur').on('blur',function(){
            var $this = $(this);
            var v = $this.val();
            v = that.correctValue(v,config.format,endMinDate,endMaxDate);
            if(v === undefined){
                v = '';
            }
            //当输入值时，验证其是否符合要求
            var minDate = $(start.config.elem).val();
            if(!minDate){
                minDate =  start.config.min.year + '-' + ( start.config.min.month - 0 + 1) +'-' + start.config.min.date +
                    ' ' + start.config.min.hours +':' + start.config.min.minutes +':'+ start.config.min.seconds;
            }
            var maxDate = end.config.max.year + '-' + ( end.config.max.month - 0 + 1) +'-' + end.config.max.date +
                ' ' + end.config.max.hours +':' + end.config.max.minutes +':'+ end.config.max.seconds;
            var getMaxGap = bt.date.getBetweenDay(v,maxDate);
            var getMinGap = bt.date.getBetweenDay(minDate,v);
            if( getMaxGap < 0 || getMinGap < 0){
                $this.val('');
                start.config.max  = that.makeDateObj(maxDate);
            }else{
                $this.val(v);
                end.config.value = v;
                if(v === ''){
                    start.config.max  = end.config.max
                }else{
                    start.config.max = that.makeDateObj(v);
                }
            }
        })

        //重置时间
        $end.parents('form').find('[type ="reset"]').click(function(){
            end.config.done();
            start.config.done();
        })

        var bizName = $start.data('biz-name'.toLowerCase());
        that.saveDateObj(bizName,start,$start,end);//保存开始时间对象
        var eBizName = $end.data('biz-name'.toLowerCase());
        that.saveDateObj(eBizName,end,$end,start);//保存结束时间对象
    }

    //月份范围渲染
    DateType.prototype.renderCycleMonth = function($start,$end,config){
        var that = this;

        if($start.length === 0 || $end.length === 0){
            return;
        }else{//添加起止标识
            $start.addClass('start');
            $end.addClass('end');
        }

        var start = laydate.render({
            elem:$start[0],
            type:'month',
            value:$start.val(),
            btns: [ 'confirm'],
            lang:that.lang,
            ready:function(date){
                var startValue = $start.val();
                if(!startValue){
                    startValue = date.year +'-'+date.month;
                }
                var startMin = $start.data('min');
                if(startMin){//如果比最小值还小，直接确认禁用
                    var isBig = bt.date.getBetweenMonth(startMin,startValue);
                    if(isBig < 0){
                         $('.laydate-btns-confirm').addClass('laydate-disabled');
                         return;
                    }
                }

                var endValue = $end.val();
                endValue = $.trim(endValue);
                if(!endValue){
                    endValue = $end.data('max');
                }

                if( !startValue || !endValue){
                    return;
                }
                that.correctMonthButtonStatus(startValue,endValue);
            },
            change:function(value, date, endDate){
                var endValue = $end.val();
                endValue = $.trim(endValue);
                if(!endValue){
                    endValue = $end.data('max');
                }
                var startMin = $start.data('min');
                if(startMin){ //如果比最小值还小，直接确认禁用
                    var isBig = bt.date.getBetweenMonth(startMin,value);
                    if(isBig < 0){
                         $('.laydate-btns-confirm').addClass('laydate-disabled');
                         return;
                    }
                }

                that.correctMonthButtonStatus(value,endValue);
            },
            done:function(value, date, endDate){
                that.doCallback(value,$start);
            }
        })
        var end = laydate.render({
            elem:$end[0],
            type:'month',
            value:$end.val(),
            btns: ['confirm'],
            lang:that.lang,
            ready:function(date){
                var startValue = $start.val();
                startValue = $.trim(startValue);
                if(!startValue){
                    startValue = $start.data('min');
                }
                var endValue = $end.val();
                endValue = $.trim(endValue);
                if(!endValue){
                    endValue = date.year +'-'+date.month;
                }
                if( !startValue || !endValue){
                    return;
                }
                that.correctMonthButtonStatus(startValue,endValue);
            },
            change:function(value, date, endDate){
                var startValue = $start.val();
                startValue = $.trim(startValue);
                if(!startValue){
                    startValue = $start.data('min');
                }
                that.correctMonthButtonStatus(startValue,value);
            },
            done:function(value, date, endDate){
                that.doCallback(value,$end);
            }
        })

        var bizName = $start.data('biz-name'.toLowerCase());
        that.saveDateObj(bizName,start,$start,end);//保存开始时间对象
        var eBizName = $end.data('biz-name'.toLowerCase());
        that.saveDateObj(eBizName,end,$end,start);//保存结束时间对象
    }
    //制作一个时间对象
    DateType.prototype.makeDateObj = function(d){
        if(d){
            var ymd = [], hms = [];
            ymd = (d.match(/\d+-\d+-\d+/) || [''])[0].split('-');
            hms = (d.match(/\d+:\d+:\d+/) || [''])[0].split(':');
            var date = {
                year: ymd[0] | 0 || new Date().getFullYear()
                ,month: ymd[1] ? (ymd[1] | 0): new Date().getMonth()
                ,date: ymd[2] | 0 || new Date().getDate()
                ,hours: hms[0] | 0
                ,minutes: hms[1] | 0
                ,seconds: hms[2] | 0
            };
            return this.makelayDateObj(date);
        }
    }
    //制作一个laydate组件所需要的时间对象
    DateType.prototype.makelayDateObj = function(dateObj){
        if(dateObj){
            return {
                year: dateObj.year,
                month: dateObj.month - 1,
                date: dateObj.date,
                hours: dateObj.hours,
                minutes: dateObj.minutes,
                seconds: dateObj.seconds
            };
        }
    }

    //修正时间的值，并返回
    DateType.prototype.correctValue = function(v,fmt,min,max){
        fmt = fmt =="YYYY-MM-DD" ?"yyyy-MM-dd":"yyyy-MM-dd hh:mm:ss";
        if(fmt){
            v = bt.date.format(v,fmt);
            if(v === undefined){
                v = '';
            }

            if(min && bt.date.compare(min,v) > 0 ){ //如果值小于最小值时，清空
                v = '';
            }

            if(max && bt.date.compare(max,v) < 0 ){ //如果值大于最大值时，清空
                v = '';
            }
        }
        return v;
    }

    //修正非月份范围的确定按钮状态
    DateType.prototype.correctDateButtonStatus = function(start,end,max){
        var startNow = -1,endNow = -1,maxNow = -1;
        startNow = bt.date.compare(end,new Date());
        endNow = bt.date.compare(start,new Date());
        maxNow = bt.date.compare(new Date(),max);
        if(startNow < 0 || endNow < 0 || maxNow < 0){
            $('.laydate-btns-now').addClass('laydate-disabled');
        }
    }

    //修正月份范围的确定按钮状态
    DateType.prototype.correctMonthButtonStatus = function(startMonth,endMonth){
        if(!startMonth || !endMonth){
            return;
        }
        var num =  bt.date.getBetweenMonth(startMonth,endMonth);
        if(num < 0){
            $('.laydate-btns-confirm').addClass('laydate-disabled');
        }
        var startNow = bt.date.getBetweenMonth(new Date(),endMonth);
        var endNow = bt.date.getBetweenMonth(startMonth,new Date());
        if(startNow < 0 || endNow < 0){
            $('.laydate-btns-now').addClass('laydate-disabled');
        }
    }
    //把时间对象保存在window中
    DateType.prototype.saveDateObj = function (bizName,dateObj,elem,linkDate) {
        var that = this;
        if (bizName) {
            if (!window.biz) {
                window.biz = [];
            }
            if (!window.biz[that.bizName]) {
                window.biz[that.bizName] = [];
            }
            var obj = {date:dateObj,elem:elem};

            if (linkDate){
                obj.linkDate = linkDate;
            }
            window.biz[that.bizName][bizName] = obj;
        }
    }


    // 执行回调方法
    DateType.prototype.doCallback = function(value,elem){
        var callback = elem.data("callback");
        callback = this.makeCallBack(callback);
        if(callback){
            callback(value,elem);
        }
    }

    DateType.prototype.makeCallBack = function(callBack){
        //校验下拉树的回调方法是否存在
        if(callBack && typeof(callBack) != "function"){
            try {
                callBack = eval(callBack);
            } catch(e) {
                alert(callBack + 'not exist！');
            }
        }
        return callBack;
    }

    DateType.prototype.val = function(val){
        var that = this;
        var elem = that.currentObject.elem;

        if(val ===undefined){
            if(!elem){
                return "";
            }
            return elem.val();
        }else{
            elem.val(val);
            var linkDate = that.currentObject.linkDate;
            if (linkDate){ //赋值时，判断是否有关联时间对象
                if ( elem.data('endid') != undefined ){　//需要关联截止日期时，把最小值重新赋值
                    if (val == ''){
                        val = '1900-1-1'
                    }
                    linkDate.config.min = that.makeDateObj(val);
                }else {　//需要关联起启日期时，把最大值重新赋值
                    if (val == ''){ //如果关联时，把最大最小值设置为默认最大值
                        val = '2099-12-31'
                    }
                    linkDate.config.max = that.makeDateObj(val);
                }
            }
        }
    }

    DateType.prototype.reset = function(){
        var that = this;
        if(!that.currentObject){
            return "";
        }
        that.currentObject.elem.removeClass("init");
        that.render()
    }

    DateType.prototype.attr = function(attr,value){
        var that = this;
        var elem = that.currentObject.elem;
        if(!that.currentObject){
            return "";
        }

        if(attr && arguments.length === 1 ){
            return elem.attr(attr);
        }

        var dateObj = that.currentObject.date;

        if(arguments.length === 2 ){
            if( attr.indexOf('min') !== -1|| attr.indexOf('max') !== -1){
                var date = that.makeDateObj(value);
                elem.val('');
                if(attr.indexOf('min') !== -1){
                    dateObj.config.min = date;
                }else{
                    dateObj.config.max = date;
                }

            }
            if( attr.indexOf('type') !== -1){
                alert(bt.lang("common.msg.notSupportType"));//'暂时不支持type的更改'
                return;
            }

            if(attr.indexOf("data-") !== -1 ){
                var attrName = attr.replace("data-",'');
                elem.data(attrName,value);

            }else{
                elem.attr(attr,value);
            }

        }
    };

    /**
     * 返回指定format的string
     * format eg:'yyyy-MM-dd hh:mm:ss'
     **/
    Date.prototype.format = function(format) {
        var o = {
            "M+": this.getMonth() + 1,
            "d+": this.getDate(),
            "h+": this.getHours(),
            "m+": this.getMinutes(),
            "s+": this.getSeconds(),
            "q+": Math.floor((this.getMonth() + 3) / 3),
            "S": this.getMilliseconds()
        }
        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
        return format;
    }

    function throwError(msg) {
        throw new Error(msg);
    };
    var dateType = new DateType();
    var obj = {
        init:function(ops,singleCallback){//singlecallback是为了兼容旧的btdate的单一时间回调
            dateType.init(ops,singleCallback);
            return this
        },
        render:function(){
            dateType.render();
            return this;
        },
        get:function(bizName){
            // dateType.currentObject = $('[data-biz-name="'+bizName+'"]');
            // if(dateType.currentObject.length===0){
            //     throwError(bizName+"尚未配置data-biz-name属性！")
            // }

            var currentObj = window.biz[dateType.bizName][bizName];
            if (!currentObj) {
                throw new Error("[bt-biz-dateType:元素容器找不到，具体设置格式请参考文档API");
            }
            currentObj.elem = $('[data-biz-name="'+bizName+'"]');
            dateType.currentObject = currentObj;
            // return selectTableType;
            return dateType
        },
        getFun:function(){
            return bt.date;
        }
    };
    //输入口
    exports('dateType', obj);
});
