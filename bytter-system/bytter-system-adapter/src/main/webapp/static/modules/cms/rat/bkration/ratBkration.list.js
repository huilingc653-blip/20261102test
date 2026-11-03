var $, form, dateType, corp, selectType, moneyType, dicts;
bt.use(['form','btdate','moneyType','laypage','dateType','corp','selectType','dicts','btImport', 'relaypage'], function(){
    $ = bt.jquery;
    form = bt.form();
    dateType = bt.dateType.init();
    corp = bt.corp.init();
    moneyType = bt.moneyType.init();
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();//下拉框

    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
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
                location.href = encodeURI(basePath + "/cms/rat/ratbkration/list?" + getParams() + "&start=" + obj.curr + "&len=" + obj.limit);
            }
        }
    });

    //监听查询
    form.on('submit(search)', function(data){
    	search();
    });
});

function setNull() {
    $("#contractNo").val("");
    $("#bankName").val("");
    $("#userName").val("");
    $("#ratCode").val("");
    dateType.get("ratDate").val("");
    dateType.get("gBeginDate").val("");
    dateType.get("lBeginDate").val("");
    dateType.get("gEndDate").val("");
    dateType.get("lEndDate").val("");
    moneyType.get("gMoney").val("");
    moneyType.get("lMoney").val("");
    dicts.get("itemFlag").val("");
    selectType.get("itemCode").val("");
    corp.get("corpId").val("");
    dicts.get("status").val("")

    dateType.get("gGuaranteeBeginDate").val("");
    dateType.get("lGuaranteeBeginDate").val("");
    dateType.get("gGuaranteeEndDate").val("");
    dateType.get("lGuaranteeEndDate").val("");
}

function search(){
    location.href = encodeURI(basePath + "/cms/rat/ratbkration/list?" + "start=1" + "&len=10" + getParams());
}

function getParams() {
    var param = "";
    var ratDate = dateType.get("ratDate").val();
    var gMoney = moneyType.get("gMoney").val();
    var lMoney = moneyType.get("lMoney").val();
    var gBeginDate = dateType.get("gBeginDate").val();
    var lBeginDate = dateType.get("lBeginDate").val();
    var gEndDate = dateType.get("gEndDate").val();
    var lEndDate = dateType.get("lEndDate").val();
    // var bankType = selectType.get("bankType").val();
    var bankName = $("#bankName").val();
    var contractNo = $("#contractNo").val();
    var corpId = corp.get("corpId").val();
    var userName = $("#userName").val();
    var itemflag = $.trim(dicts.get("itemFlag").val());
    var itemCode = $.trim(selectType.get("itemCode").val());
    var ratCode = $("#ratCode").val();
    var status = $.trim(dicts.get("status").val());

    var gGuaranteeBeginDate = dateType.get("gGuaranteeBeginDate").val();
    var lGuaranteeBeginDate = dateType.get("lGuaranteeBeginDate").val();
    var gGuaranteeEndDate = dateType.get("gGuaranteeEndDate").val();
    var lGuaranteeEndDate = dateType.get("lGuaranteeEndDate").val();

    param = "&contractNo=" + contractNo + "&ratDate=" + ratDate + "&gMoney=" + gMoney + "&lMoney=" + lMoney+"&bankName="+bankName;//分页时查询，页码从第一页开始
    param += "&corpId=" + corpId + "&userName=" + userName + "&itemFlag=" + itemflag + "&itemCode=" + itemCode + "&ratCode=" + ratCode + "&status=" + status;
    param += "&gBeginDate=" + gBeginDate + "&lBeginDate=" + lBeginDate+"&gEndDate=" + gEndDate+"&lEndDate=" + lEndDate;
    param += "&gGuaranteeBeginDate=" + gGuaranteeBeginDate + "&lGuaranteeBeginDate=" + lGuaranteeBeginDate + "&gGuaranteeEndDate=" + gGuaranteeEndDate + "&lGuaranteeEndDate=" + lGuaranteeEndDate;
    return param;
}

function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkration.add'),basePath + "/cms/rat/ratbkration/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkration/add",
        end:function(){
            //datatable && datatable.draw();
        }
    });
}
function view(id){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkration.view'),basePath + "/cms/rat/ratbkration/view/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1120px', '800px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkration/view/" + id
    });
}
function update(id){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkration.update'),basePath + "/cms/rat/ratbkration/update?id=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkration/update?id=" + id,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}
function deleteColumn(id){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/rat/ratbkration/delete/" + id, function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            search();
        },'JSON');
    });
}

/**
 * 判断为空
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {
    if (value == null || value == "" || value == undefined || value == " ") {
        return true;
    } else {
        return false;
    }
}

/**
 * 判断不为空
 * @param value
 * @returns {boolean}
 */
function isNotEmpty(value) {
    return !isEmpty(value);
}

/**
 * 导入功能
 */
function importExcel() {
    up = bt.btImport.init({
        code: "70000062",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                url: basePath + "/cms/rat/ratbkration/saveImport",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(up.getSelectData()),
                success: function (result) {
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    search();
                }
            });
        }
    });
};

/**
 * 导出功能
 */
exports = function (type) {
    var url = basePath + "/cms/rat/ratbkration/exportListRatBkration";
    var formData;
    if (type === 0) {
        formData = {
            exportType: "0",
            start: $("#curr").val(),
            pageSize: $("#pageSize").val()
        }
        let params = getParams().split("&");
        for (const i in params) {
            if (params[i]){
                let ps = params[i].split("=");
                if (ps[1]){
                    formData[ps[0]] = ps[1];
                }
            }
        }
    } else {
        formData = {
            exportType: "1",
        }
    }
    DownLoadFile({url: url, data: formData, method: 'post'});
}


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
