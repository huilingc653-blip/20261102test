var $, form, dateType, selectType, mulselectType, corp;
bt.use(['form', 'datatable', 'btdate', 'money', 'laypage', 'relaypage', 'dateType', 'selectType', 'mulselect', 'corp'], function(){
    $ = bt.jquery;
    form = bt.form();
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    mulselectType = bt.mulselect.init();
    corp = bt.corp.init();

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
        jump: function(obj, first){
            if(!first){
                location.href = encodeURI(basePath + "/cms/rat/ratbkrationadd/list?" + getParams() + "&start=" + obj.curr + "&len=" + obj.limit)
            }
        }
    });

    //监听查询
    form.on('submit(search)', function(data){
    	search();
    });

});

// 重置
function setNull() {
    selectType.get("ratMode").val("");
    $("#contractNo").val("");
    $("#ratCode").val("");
    corp.get("corpId").val("");
    dateType.get("gBeginDate").val("");
    dateType.get("lBeginDate").val("");
    dateType.get("gEndDate").val("");
    dateType.get("lEndDate").val("");
    mulselectType.get("itemCode").val({value:""});
    mulselectType.get("bankCode").val({value:""});

}

function search(){
    location.href = encodeURI(basePath + "/cms/rat/ratbkrationadd/list?" + "start=1" + "&len=10" + getParams());
}

function getParams() {
    var param = "";
    var ratMode = $.trim(selectType.get("ratMode").val());
    var ratCode = $("#ratCode").val();
    var contractNo = $("#contractNo").val();
    var corpId = corp.get("corpId").val();
    var bankInfos = mulselectType.get("bankCode").val();
    var itemCodeInfos = mulselectType.get("itemCode").val();
    var gBeginDate = dateType.get("gBeginDate").val();
    var lBeginDate = dateType.get("lBeginDate").val();
    var gEndDate = dateType.get("gEndDate").val();
    var lEndDate = dateType.get("lEndDate").val();
    var bankCode = "";
    for (var i = 0; i < bankInfos.length; i++) {
        if (!(bankInfos[i].bankcode == undefined || bankInfos[i].bankcode == null || bankInfos[i].bankcode == "")) {
            bankCode = bankCode + bankInfos[i].bankcode + ",";
        }
    }
    var itemCode = "";
    for (var i = 0; i < itemCodeInfos.length; i++) {
        if (!(itemCodeInfos[i].id == undefined || itemCodeInfos[i].id == null || itemCodeInfos[i].id == "")) {
            itemCode = itemCode + itemCodeInfos[i].id + ",";
        }
    }
    param = "&ratMode=" + ratMode + "&ratCode=" + ratCode + "&contractNo=" + contractNo;
    param += "&corpId=" + corpId + "&bankCode=" + bankCode + "&itemCode=" + itemCode;
    param += "&gBeginDate=" + gBeginDate + "&lBeginDate=" + lBeginDate + "&gEndDate=" + gEndDate + "&lEndDate=" + lEndDate;
    return param;
}

function add(id){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationadd.add'),basePath + "/cms/rat/ratbkrationadd/add?ratCode=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationadd/add?ratCode="+id,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}
function viewAdd(id){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationadd.viewAdd'),basePath + "/cms/rat/ratbkrationadd/addList?ratCode=" + id);
        return;
    }
    layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationadd/addList?ratCode="+id,
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

/**
 * 导出功能
 */
exports = function (type) {
    var url = basePath + "/cms/rat/ratbkrationadd/exportListRatBkRationAddBank";
    var bankInfos = mulselectType.get("bankCode").val();
    var bankCode = "";
    for (var i = 0; i < bankInfos.length; i++) {
        if (!(bankInfos[i].bankcode == undefined || bankInfos[i].bankcode == null || bankInfos[i].bankcode == "")) {
            bankCode = bankCode + bankInfos[i].bankcode + ",";
        }
    }
    var itemCodeInfos = mulselectType.get("itemCode").val();
    var itemCode = "";
    for (var i = 0; i < itemCodeInfos.length; i++) {
        if (!(itemCodeInfos[i].id == undefined || itemCodeInfos[i].id == null || itemCodeInfos[i].id == "")) {
            itemCode = itemCode + itemCodeInfos[i].id + ",";
        }
    }
    var formData;
    if (type === 0) {
        formData = {
            exportType: "0",
            start: $("#curr").val(),
            pageSize: $("#pageSize").val(),
            ratMode: $.trim(selectType.get("ratMode").val()),
            ratCode: $("#ratCode").val(),
            contractNo: $("#contractNo").val(),
            corpId: corp.get("corpId").val(),
            bankCode: bankCode,
            itemCode: itemCode,
            gBeginDate: dateType.get("gBeginDate").val(),
            lBeginDate: dateType.get("lBeginDate").val(),
            gEndDate: dateType.get("gEndDate").val(),
            lEndDate: dateType.get("lEndDate").val()
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
