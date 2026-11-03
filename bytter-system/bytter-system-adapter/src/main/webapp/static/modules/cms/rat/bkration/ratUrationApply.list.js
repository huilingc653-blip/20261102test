var $, form, dateType, corpInit;
bt.use(['form', 'datatable', 'btdate', 'money', 'laypage', 'dateType', 'corp', 'relaypage'], function () {
    $ = bt.jquery;
    form = bt.form();
    dateType = bt.dateType.init();
    corpInit = bt.corp.init();

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    //时间
    $.each($("div[type='date']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });

    bt.relaypage({
        cont: 'page',
        curr: $("#curr").val(),
        pages: $("#pageCount").val(),
        resultCount: $("#resultCount").val(),
        limit: $("#pageSize").val(), // 每页显示的数量
        limits: [10, 20, 50, 100, 500], // 每页条数选项
        layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip'],
        skip: true,
        jump: function (obj, first) {
            if (!first) {
                location.href = encodeURI(basePath + "/cms/rat/raturationapply/list?" + getParams() + "&start=" + obj.curr + "&len=" + obj.limit);
            }
        }
    });

    //监听查询
    form.on('submit(search)', function (data) {
        search();
    });

});

function search() {
    location.href = encodeURI(basePath + "/cms/rat/raturationapply/list?" + "start=1" + "&len=10" + getParams());
}

//监听重置
function setNull() {
    corpInit.get("corpId").val("");
    $("#contractNo").val("");
    $("#bankName").val("");
    $("#ratCode").val("");
    dateType.get("gBeginDate").val("");
    dateType.get("lBeginDate").val("");
    dateType.get("gEndDate").val("");
    dateType.get("lEndDate").val("");
}

function getParams() {
    var param = "";
    var corpId = corpInit.get("corpId").val();
    var contractNo = $("#contractNo").val();
    var ratCode = $("#ratCode").val();
    var bankName = $("#bankName").val();
    var gBeginDate = dateType.get("gBeginDate").val();
    var lBeginDate = dateType.get("lBeginDate").val();
    var gEndDate = dateType.get("gEndDate").val();
    var lEndDate = dateType.get("lEndDate").val();

    param += "&corpId=" + corpId + "&contractNo=" + contractNo + "&ratCode=" + ratCode + "&bankName=" + bankName;
    param += "&gBeginDate=" + gBeginDate + "&lBeginDate=" + lBeginDate + "&gEndDate=" + gEndDate + "&lEndDate=" + lEndDate;
    return param;
}

function add(id) {
    var corpId = $("#uCorpId").val();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('ration.urationapply.add'), basePath + "/cms/rat/raturationapply/add?ratCode=" + id + "&corpId=" + corpId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/raturationapply/add?ratCode=" + id + "&corpId=" + corpId,
        end: function () {
            //datatable && datatable.draw();
        }
    });
}

function viewAdd(id) {
    var corpId = $("#uCorpId").val();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('ration.urationapply.list'), basePath + "/cms/rat/raturationapply/addList?ratCode=" + id + "&corpId=" + corpId);
        return;
    }
    layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/raturationapply/addList?ratCode=" + id + "&corpId=" + corpId,
        end: function () {
            //datatable && datatable.draw();
        }
    });
}

function view(id) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('ration.bkration.view'), basePath + "/cms/rat/ratbkration/view/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1120px', '800px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkration/view/" + id
    });
}

function updateRat(ratCode) {
    var params = {
        "ratCode": ratCode
    };
    $.ajax({
        type: "POST",
        url: basePath + "/cms/rat/raturationapply/updateRatDis",
        data: params,
        dataType: "json",
        success: function (result) {
            layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }
    });
}

/**
 * 导出功能
 */
exports = function (type) {
    var url = basePath + "/cms/rat/raturationapply/exportListRatUrationApply";
    var corpId = bt.corp.init().get("corpId").val();
    if (bt.empty(corpId)) {
        layer.alert(bt.lang("table.ratbkrationAdd.selectCorp"));
        return false;
    }
    var formData = {
        exportType: "0",
        start: $("#curr").val(),
        pageSize: $("#pageSize").val(),
        corpId: corpId, // 额度使用单位
        bankName: $("#bankName").val(),
        contractNo: $("#contractNo").val(), // 合同号
        ratCode: $("#ratCode").val(), // 授信编号
        gBeginDate: dateType.get("gBeginDate").val(),
        lBeginDate: dateType.get("lBeginDate").val(),
        gEndDate: dateType.get("gEndDate").val(),
        lEndDate: dateType.get("lEndDate").val()
    }
    if (type === 0) {
        formData.exportType = "0";
    } else {
        formData.exportType = "1";
    }
    DownLoadFile({url: url, data: formData, method: 'post'});
};

function DownLoadFile(options) {
    var config = $.extend(true, {method: 'post'}, options);
    var form = $("<form></form>").attr("style", "display:none").attr("action", config.url).attr("method", "post");
    try {
        let sum = 0;
        for (const key in config.data) {
            //console.log(key+"="+config.data[key]);
            form.append("<input type='hidden' name='"+key+"' value='"+config.data[key]+"'/>");
            sum += 1;
        }
        form.append($("<input></input>").attr("type", "hidden").attr("name", "totalParam").attr("value",  sum));
        form.appendTo('body');
        form.submit();
    } catch (e) {
        console.log(e);
    } finally {
        form.remove();
    }
}
