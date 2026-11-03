var $, form, corp;
bt.use(['form', 'datatable', 'corp', 'mulselect', 'btImport', 'dicts', 'moneyType', 'laydate'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.mulselect.init();
    bt.dicts.init();
    bt.moneyType.init();
    corp = bt.corp.init();
    laydate = layui.laydate;

    //周开始日期选择
    laydate.render({
        elem: '#weekStartDayId',
        type: 'date',
        btns: ['clear', 'confirm'],
        disabled: function (date) {
            // “周开始日”必须是周一
            var enabled = $('#showAllMonday').val();
            date = formatDate(date);
            return enabled.indexOf(date) === -1
        },
        done: function (value, date) {
            // “周开始日”必须是周一
            if (value) { // 如果有值
                var d = new Date(value);
                if (d.getDay() !== 1) {
                    layer.msg('只能选择周一作为周开始日', {icon: 2})
                    $(this.elem).val(''); // 清空选择
                    return;
                }
                // 创建日期对象
                var weekStartDay = new Date(value);
                // 计算周日（加6天）
                var weekEndDay = new Date(weekStartDay);
                weekEndDay.setDate(weekStartDay.getDate() + 6);

                // 格式化日期为 YYYY-MM-DD
                var weekEndDayStr = weekEndDay.getFullYear() + '-'
                    + (weekEndDay.getMonth() + 1).toString().padStart(2, '0') + '-'
                    + weekEndDay.getDate().toString().padStart(2, '0');

                // “周结束日”必须对应周开始日的周日
                $("#weekEndDayId").val(weekEndDayStr);
            } else {
                $("#weekEndDayId").val('');
            }
        }
    });

    function formatDate(dateString) {
        // 首先将日期字符串分割为年、月、日三部分
        var parts = dateString.split('-');
        var year = parts[0];
        var month = parts[1];
        var day = parts[2];

        // 使用padStart确保月份和日期始终为两位数
        month = month.padStart(2, '0');
        day = day.padStart(2, '0');

        // 重新组合日期字符串
        return year + '-' + month + "-" + day;
    }

    // 监听查询
    form.on('submit(search)', function (data) {
        var action = basePath + "/cms/report/creditBalance/list";
        $("#searchForm").attr("action", action);
        $("#searchForm").attr("method", "get");
        $("#searchForm").submit();
    });

    //监听导出
    form.on('submit(excel)', function (data) {
        if (!checkTables()) {
            layer.alert(bt.lang("common.message.notData"));
            return false;
        }
        var url = basePath + "/cms/report/creditBalance/export";
        DownLoadFile({url: url, data: data.field, method: 'post'});
        return false;
    });
});

/**表格不为空验证*/
function checkTables() {
    var flag = true;
    //获取所有tr
    var trs = $("#appTable").find("tr");
    if (trs.length <= 1) {
        flag = false;
    }
    return flag;
}

function DownLoadFile(options) {
    var config = $.extend(true, {method: 'post'}, options);
    var _iframe = $('<iframe id="down-file-iframe" style="display: none"/>');
    var _form = $('<form target="_self" method="post" />');
    _form.attr('action', config.url);
    for (var key in config.data) {
        _form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    _iframe.append(_form);
    $(document.body).append(_iframe);
    _form[0].submit();
    _iframe.remove();
    return false;
}

// 重置
function resetCreditBalance() {
    corp.get("corpId").val('');
    $("#weekStartDayId").val('');
    $("#weekEndDayId").val('');
}