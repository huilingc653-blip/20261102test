var $, form, dateTypes, businessType;
bt.use(['form', 'btdate', 'money', 'relaypage', 'laypage', 'dateType'], function () {
    $ = bt.jquery;
    form = bt.form();
    dateTypes = bt.dateType.init();

    businessType = $("#businessType").val();//业务种类（0-授信录入，1-授信分配）

    bt.relaypage({
        cont: 'page',
        curr: $("#curr").val(),
        pages: $("#pageCount").val(),
        resultCount: $("#resultCount").val(),
        limit: $("#pageSize").val(), // 每页显示的数量
        limits: [10, 20, 50, 100, 500], // 每页条数选项
        layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip'],
        skip: true,
        jump: function(obj, first){
            if(!first){
                if (businessType === "0") {
                    location.href = encodeURI(basePath + "/cms/rat/ratbkrationadjust/ratInputAdjust?" + getParams() + "&start=" + obj.curr + "&len=" + obj.limit);
                } else if (businessType === "1") {
                    location.href = encodeURI(basePath + "/cms/rat/ratbkrationadjust/ratDisAdjust?" + getParams() + "&start=" + obj.curr + "&len=" + obj.limit);
                }
            }
        }
    });

});

function getParams() {
    var param = "";

    var corpId = $("#corpId").val(); // 授信单位
    var contractNo = $("#contractNo").val(); // 授信合同号
    var ratCode = $("#ratCode").val(); // 授信编号
    var ratMode = $("#ratMode").val(); // 授信模式
    var bankCodes = $("#bankCode").val();
    var itemCodes = $("#itemCode").val();
    var gBeginDate = $("#gBeginDate").val();
    var lBeginDate = $("#lBeginDate").val();
    var gEndDate = $("#gEndDate").val();
    var lEndDate = $("#lEndDate").val();

    param = "&ratMode=" + ratMode + "&contractNo=" + contractNo + "&ratCode=" + ratCode;
    param += "&gBeginDate=" + gBeginDate + "&lBeginDate=" + lBeginDate + "&gEndDate=" + gEndDate + "&lEndDate=" + lEndDate;
    param += "&corpId=" + corpId + "&bankCode=" + bankCodes + "&itemCode=" + itemCodes;
    return param;
}

function adjust(id) {
    var businessType = $("#businessType").val();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('ration.maxMoneyAdjust.add'), basePath + "/cms/rat/ratbkrationadjust/addAdjust?businessId=" + id + "&businessType=" + businessType);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationadjust/addAdjust" + "&businessType=" + businessType,
        end: function () {
            //datatable && datatable.draw();
        }
    });
}

function view(id) {
    if (bt.navtab) {
        if (businessType == "0") {//授信录入调整
            bt.navtab.addNavtab(bt.lang('ration.bkration.view'), basePath + "/cms/rat/ratbkration/view/" + id);
        } else if (businessType == "1") {
            bt.navtab.addNavtab(bt.lang('ration.bkrationdis.view'), basePath + "/cms/rat/ratbkrationdis/view/" + id);
        }
        return;
    }
}

function adjustList(id) {
    var businessType = $("#businessType").val();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('ration.maxMoneyAdjust.list'), basePath + "/cms/rat/ratbkrationadjust/adjustList?businessId=" + id + "&businessType=" + businessType);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationadjust/adjustList?businessId=" + id + "&businessType=" + businessType,
        end: function () {
            //datatable && datatable.draw();
        }
    });
}

/**
 * 导出功能
 */
exports = function (type) {
    var url = ""
    if (businessType === "0") {
        url = basePath + "/cms/rat/ratbkrationadjust/exportListRatBkInputAdjustTab"
    } else if (businessType === "1") {
        url = basePath + "/cms/rat/ratbkrationadjust/exportListRatBkInputAdjustTabDis"
    }
    var formData;
    if (type === 0) {
        formData = {
            exportType: "0",
            start: $("#curr").val(),
            pageSize: $("#pageSize").val(),
            corpId: $("#corpId").val(),
            contractNo: $("#contractNo").val(),
            ratCode: $("#ratCode").val(),
            itemFlag: $("#itemFlag").val(),
            bankCode: $("#bankCode").val(),
            itemCode: $("#itemCode").val(),
            ratMode: $("#ratMode").val(),
            gBeginDate: $("#gBeginDate").val(),
            lBeginDate: $("#lBeginDate").val(),
            gEndDate: $("#gEndDate").val(),
            lEndDate: $("#lEndDate").val()
        }
    } else {
        formData = {
            exportType: "1",
        }
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
