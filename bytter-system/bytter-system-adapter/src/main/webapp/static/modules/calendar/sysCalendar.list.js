bt.use(['form','calendar','dateType','selectType','selectTable'], function(){
    $ = bt.jquery,form = bt.form();
    /** 币别下拉框 begin*/
    bt.selectType.init();
    /** 币别下拉框 end*/
    bt.dateType.init();

    /**初始化国家下拉表格，组件：selectTable;    begin*/
    bt.selectTable.init({
        elem: "#countryCode",
        url: basePath + "/basic/business/syscountrycust/listAll",
        searchInput: ".text",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data, $tr) {
            $("input[name='nameZh']").val(data.nameZh);
            $("input[name='countryId']").val(data.id);
            $('#calendar').fullCalendar('refetchEvents');
        },
    })
    /**初始化国家下拉表格 end*/

    form.on('radio(changeType)', function (data) {
        if (data.value == 0) {
            $("#curId").hide();
            $("#countryId").show();
            // $("#curId").attr('data-required','');
            // $("#countryId").attr('lay-verify','required');
        } else {
            $("#curId").show();
            $("#countryId").hide();
            // $("#curId").attr('data-required','required|numberOrletter|strlength');
            // $("#countryId").attr('lay-verify','');
        }
        $('#calendar').fullCalendar('refetchEvents');
    });

    var dayDate = new Date();
    bt.calendar.init({
        elem: "#calendar",
        defaultDate: dayDate,
        locale: top&&top.webLocale||'zh-cn',
        events: function(start, end, timezone, callback) {
            var fstart  = $.fullCalendar.formatDate(start, "YYYY-MM-DD");
            var fend  = $.fullCalendar.formatDate(end, "YYYY-MM-DD");
            var type = $("input[name='type']:checked").val();
            var curId = $("select[name='curId']").val();
            var countryId = $("input[name='countryId']").val();
            $.post(basePath + "/calendar/syscalendar/getWorkList",{startTime:fstart,endTime: fend,type: type,curId:curId,countryId:countryId}, function(data){
                var events = [];
                if(data.success){
                    $.each(data.obj,function(i,item){
                        events.push({
                            title: bt.dict("calendar.sign.status",item.workDaySign),
                            start: moment(item.createDate).format("YYYY-MM-DD"),
                            time: moment(item.createDate).format("YYYY-MM-DD"),
                            status: item.workDaySign,
                            className: "tc",
                            editable: false,
                            color: '#fff',
                            textColor: item.workDaySign?'#25992E':'#ed6d23',
                            allDay: true
                        });
                    });
                }
                try{
                    callback(events);
                }catch(e){}
            },'JSON');
        },
        eventClick: function (dayDate, jsEvent, view) {
            var status = dayDate.status,time = dayDate.time;
            var type = $("input[name='type']:checked").val();
            var curId = $("select[name='curId']").val();
            var countryId = $("input[name='countryId']").val();
            $("input[name=startTime]").val(time),$("input[name=endTime]").val(time);
            var now = new Date();
            if (now.getDate() == dayDate.start._d.getDate() && now.getMonth() == dayDate.start._d.getMonth() && now.getFullYear() == dayDate.start._d.getFullYear()){
                $(".today_icon").show();
            }else{
                $(".today_icon").hide();
            }
            var msg = bt.lang("common.confirm.canlendar",[bt.dict("calendar.sign.status",status?1:0),bt.dict("calendar.sign.status",!status?1:0)]);
            layer.confirm(msg,function(){
                $.post(basePath + "/calendar/syscalendar/modifyWork",{startTime:time, status:!status?1:0,type: type,curId:curId,countryId:countryId}, function(data){
                    if(data.success){
                        layer.msg(bt.lang("common.message.success"));
                        $("input[name=status]").prop("checked",false);
                        $("input[name='status'][value="+(!status?1:0)+"]").prop("checked",true);
                        form.render('radio');
                        dayDate.textColor = !status?'#25992E':'#ed6d23';
                        dayDate.title = bt.dict("calendar.sign.status",!status?1:0);
                        dayDate.status = !status?1:0;
                        $('#calendar').fullCalendar('updateEvent',dayDate);
                    }
                },'JSON');
            });
        },
        dayClick : function(dayDate, jsEvent, view) {//点击单元格事件
            var d = $.fullCalendar.formatDate(dayDate,"dddd");
            var m = $.fullCalendar.formatDate(dayDate,"YYYY-MM-DD");
            $(".alm_date").html(m + "&nbsp;" + d);
            var now = new Date();
            if (now.getDate() == dayDate._d.getDate() && now.getMonth() == dayDate._d.getMonth() && now.getFullYear() == dayDate._d.getFullYear()){
                $(".today_icon").show();
            }else{
                $(".today_icon").hide();
            }
            var t = $(jsEvent.target).data("date");
            //var status = $("#calendar .fc-day-cnTerm[data-time="+t+"]").find("input[type=checkbox]").prop("checked");
            var status = $("#calendar .fc-day-cnTerm[data-time="+t+"]").data("status");
            $("input[name=status]").prop("checked",false);
            $("input[name='status'][value="+(status?1:0)+"]").prop("checked",true);
            $("input[name=startTime]").val(t),$("input[name=endTime]").val(t);
            form.render('radio');
        }
    });
    var d = moment(dayDate).format("dddd");
    var m = moment(dayDate).format("YYYY-MM-DD");
    $(".alm_date").html(m + "&nbsp;" + d);

    //TODO
    form.on("switch(switchCalendar)",function(data){
        alert(2)
        var that = data,status = this.checked,time = $(data.elem).parent("div.fc-day-cnTerm").data("time");
        $("input[name=startTime]").val(time),$("input[name=endTime]").val(time);
        var msg = bt.lang("common.confirm.canlendar",[bt.dict("calendar.sign.status",!status?1:0),bt.dict("calendar.sign.status",status?1:0)]);
        layer.confirm(msg,function(){
            $.post(basePath + "/calendar/syscalendar/modifyWork",{startTime:time, status:status?1:0}, function(data){
                if(data.success){
                    layer.msg(bt.lang("common.message.success"));
                    $("input[name=status]").prop("checked",false);
                    $("input[name='status'][value="+(status?1:0)+"]").prop("checked",true);
                    form.render('radio');
                }else{
                    $(that.elem).prop('checked', !status);
                    $("input[name=status]").prop("checked",false);
                    $("input[name='status'][value="+(!status?1:0)+"]").prop("checked",true);
                    rederForm();
                }
            },'JSON');
        },function(){
            $(that.elem).prop('checked', !status);
            $("input[name=status]").prop("checked",false);
            $("input[name='status'][value="+(!status?1:0)+"]").prop("checked",true);
            rederForm();
        });
        //layer.tips('温馨提示：请注意开关状态的文字可以随意定义，而不仅仅是ON|OFF', data.othis)
    });
    form.on("submit(submit)",function(data){
        var startTime = $("input[name=startTime]").val(),endTime = $("input[name=endTime]").val();
        var status = $("input[name=status]:checked").val();
        var type = $("input[name='type']:checked").val();
        var curId = $("select[name='curId']").val();
        var countryId = $("input[name='countryId']").val();
        $.post(basePath + "/calendar/syscalendar/modifyWork",{startTime:startTime,endTime:endTime,status:status,type: type,curId:curId,countryId:countryId}, function(data){
            if(data.success){
                layer.msg("操作成功");
                //location.reload();
                $('#calendar').fullCalendar('refetchEvents');
            }
        },'JSON');
    });
    resetForm = function() {
        $("input[name=startTime]").val(""),$("input[name=endTime]").val("");
        rederForm();
    };
    rederForm = function () {
        //form && form.render(),$("#calendar .layui-form-switch i").remove();
        form && form.render("radio");
    };

    bt.changeType = function(){
        $('#calendar').fullCalendar('refetchEvents');
    }
});