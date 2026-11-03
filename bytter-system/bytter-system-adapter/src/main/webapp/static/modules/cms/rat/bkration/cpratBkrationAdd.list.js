var $, form, corpInit, dateType, dicts, selectType, mulselectType;
bt.use(['form', 'datatable', 'btdate', 'money', 'laypage', 'relaypage', 'dateType', 'corp', 'dicts', 'selectType', 'mulselect'], function(){
    $ = bt.jquery;
    form = bt.form();
    dateType = bt.dateType.init();
    corpInit = bt.corp.init();
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();//下拉框
    mulselectType = bt.mulselect.init();

    /**判断字符串是否为空*/
    bt.empty = function(ms){
    	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
    		return true;
    	}
    	return false;
    }

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
                location.href = encodeURI(basePath + "/cms/rat/ratbkrationadd/cplist?" + getParams() + "&start=" + obj.curr + "&len=" + obj.limit)
            }
        }
    });

    //监听查询
    form.on('submit(search)', function (data) {
        search();
    });
});

function search(){
    location.href = encodeURI(basePath + "/cms/rat/ratbkrationadd/cplist?" + "start=1" + "&len=10" + getParams());
}

function getParams() {
    var param = "";
    var corpIds = corpInit.get("corpId").val()// 授信单位
    var disCorpIds = corpInit.get("disCorpId").val()// 额度使用单位
    var contractNo = $("#contractNo").val();// 授信合同号
    var ratCode = $("#ratCode").val();// 授信编号
    var gBeginDate = dateType.get("gBeginDate").val();
    var lBeginDate = dateType.get("lBeginDate").val();
    var gEndDate = dateType.get("gEndDate").val();
    var lEndDate = dateType.get("lEndDate").val();

    var bankInfos = mulselectType.get("bankCode").val();// 银行信息
    var bankCode = "";
    for (var i = 0; i < bankInfos.length; i++) {
        if (!(bankInfos[i].bankcode == undefined || bankInfos[i].bankcode == null || bankInfos[i].bankcode == "")) {
            bankCode = bankCode + bankInfos[i].bankcode + ",";
        }
    }

    var itemCodeInfos = mulselectType.get("itemCode").val();// 授信品种
    var itemCode = "";
    for (var i = 0; i < itemCodeInfos.length; i++) {
        if (!(itemCodeInfos[i].id == undefined || itemCodeInfos[i].id == null || itemCodeInfos[i].id == "")) {
            itemCode = itemCode + itemCodeInfos[i].id + ",";
        }
    }
    param += "&corpId=" + corpIds + "&disCorp=" + disCorpIds + "&bankCode=" + bankCode + "&itemCode=" + itemCode;
    param += "&contractNo=" + contractNo + "&ratCode=" + ratCode;
    param += "&gBeginDate=" + gBeginDate + "&lBeginDate=" + lBeginDate + "&gEndDate=" + gEndDate + "&lEndDate=" + lEndDate;
    return param;
}

//监听重置
function resetForm () {
    corpInit.get("corpId").val("");// 授信单位
    corpInit.get("disCorpId").val("");// 额度使用单位
    mulselectType.get("bankCode").val({value:""});// 银行信息
    mulselectType.get("itemCode").val({value:""});// 授信品种
    $("#contractNo").val("");// 授信合同号
    $("#ratCode").val("");// 授信编号
    dateType.get("gBeginDate").val("");
    dateType.get("lBeginDate").val("");
    dateType.get("gEndDate").val("");
    dateType.get("lEndDate").val("");
}

function add(id,addCorpId){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationadd.cpadd'),basePath + "/cms/rat/ratbkrationadd/cpadd?ratCode=" + id+ "&corpId="+addCorpId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationadd/cpadd?ratCode="+id+ "&corpId="+addCorpId,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}
function viewAdd(id,addCorpId){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationadd.cpviewAdd'),basePath + "/cms/rat/ratbkrationadd/cpaddList?ratCode=" + id + "&corpId="+addCorpId);
        return;
    }
    layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationadd/cpaddList?ratCode="+id+ "&corpId="+addCorpId,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}
function view(id){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkration.view'),basePath + "/cms/rat/ratbkrationdis/viewDis/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1120px', '800px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationdis/viewDis/" + id
    });
}

/**
 * 导出功能
 */
exports = function (type) {
    var url = basePath + "/cms/rat/ratbkrationadd/exportListRatBkRationAddCorp";
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
    var formData = {}
    if (type === 0) {
        formData = {
            exportType: "0",
            start: $("#curr").val(),
            pageSize: $("#pageSize").val(),
            corpId: corpInit.get("corpId").val(),
            disCorp: corpInit.get("disCorpId").val(),
            bankCode: bankCode,
            itemCode: itemCode,
            contractNo: $("#contractNo").val(),
            ratCode: $("#ratCode").val(),
            gBeginDate: dateType.get("gBeginDate").val(),
            lBeginDate: dateType.get("lBeginDate").val(),
            gEndDate: dateType.get("gEndDate").val(),
            lEndDate: dateType.get("lEndDate").val(),
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
