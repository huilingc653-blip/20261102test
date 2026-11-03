var cdefine = layui.define(["jquery","form"], function(exports) {
    jQuery = layui.jquery;
    $ = layui.jquery;
    caledarForm = bt.form();
    //获取字典数据

    /**
     * 异步加载依赖的javascript文件
     * src：script的路径
     * callback：当外部的javascript文件被load的时候，执行的回调
     */
    function load_js(src, callback) {
        var srcArray = src.split("?")[0].split("/");
        var scr_src = srcArray[srcArray.length - 1];
        // 判断要 添加的脚本是否存在如果存在则不继续添加了
        var scripts = document.getElementsByTagName("script");
        if (!!scripts && 0 != scripts.length) {
            for (var i = 0; i < scripts.length; i++) {
                if (-1 != scripts[i].src.indexOf(scr_src)) {
                    callback();
                    return true;
                }
            }
        }
        // 不存在需要的则添加
        var head = document.getElementsByTagName("head")[0];
        var script = document.createElement("script");
        script.setAttribute("type", "text/javascript");
        script.setAttribute("src", src);
        script.setAttribute("async", true);
        script.setAttribute("defer", true);
        head.appendChild(script);
        //fuck ie! duck type
        if (document.all) {
            script.onreadystatechange = function() {
                var state = this.readyState;
                if (state === 'loaded' || state === 'complete') {
                    callback();
                }
            };
        } else {
            //firefox, chrome
            script.onload = function() {
                callback();
            };
        }
    }
    var calendar;
    var CalenDar = function(options){
        this.options = {
            elem: undefined,//容器
            header: {
                left: 'prev,next today',
                center: 'title'//,
                //right: 'month,basicWeek'
            },
            locale: 'zh-cn',
            editable: true,
            eventLimit: true,
            initWork: undefined,
            dayClick: undefined
        };
        $.extend(true, this.options, options);
        caledarElem = this.options.elem;
        return this
    };
    CalenDar.prototype.init = function(){
        var that = this;
        var _config = that.options;
        var elem = _config.elem,$elem = $(elem);
        if (elem === undefined) {
            new Error('calendar Error:请配置参数elem,此参数为jQuery选择器!');
        }
        calendar = $elem.fullCalendar(_config);
        return calendar;
    };
    load_js(basePath+'/static/plugins/calendar/js/moment-with-locales.min.js',function(){
        load_js(basePath+'/static/plugins/calendar/js/fullcalendar.js',function () {
            load_js(basePath+'/static/plugins/calendar/js/locale-all.js',function () {
                var obj = {
                    init:function(ops){
                        moment.locale(ops.locale?ops.locale:'zh-cn');
                        var c = new CalenDar(ops);
                        return (c.init())
                    }
                };
                exports("calendar",obj)
            });
        });
    });
});
cdefine.addcss('../../calendar/css/fullcalendar.css', 'fullcalendar');
cdefine.addcss('../../calendar/css/fullcalendar.print.css', 'fullcalendarprint');