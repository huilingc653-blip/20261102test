var $,form,dateType,corpInit, dicts, selectType, mulselectType;
bt.use(['form', 'btdate', 'money', 'relaypage', 'laypage', 'dateType', 'corp', 'dicts', 'selectType', 'mulselect'], function(){
    $ = bt.jquery;
    form = bt.form();
    dateType = bt.dateType.init();
    corpInit = bt.corp.init();
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();//下拉框
    mulselectType = bt.mulselect.init();

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
                location.href = encodeURI(basePath + "/cms/rat/ratbkrationdelay/list?" + getParams() + "&start=" + obj.curr + "&len=" + obj.limit);
            }
        }
    });

    //监听查询
    form.on('submit(search)', function(data){
    	search();
    });
});

function search(){
    location.href = encodeURI(basePath + "/cms/rat/ratbkrationdelay/list?" + "start=1" + "&len=10" + getParams());
}

function getParams() {
    var param = "";
    var ratMode = selectType.get("ratMode").val(); // 授信模式
    var contractNo = $("#contractNo").val(); // 授信合同号
    var ratCode = $("#ratCode").val(); // 授信编号
    var corpId = corpInit.get("corpId").val(); // 授信单位
    var bankInfos = mulselectType.get("bankCode").val(); // 银行信息
    var itemCodeInfos = mulselectType.get("itemCode").val(); // 授信品种
    var gBeginDate = dateType.get("gBeginDate").val();
    var lBeginDate = dateType.get("lBeginDate").val();
    var gEndDate = dateType.get("gEndDate").val();
    var lEndDate = dateType.get("lEndDate").val();

    var bankCodes = "";
    for (var i = 0; i < bankInfos.length; i++) {
        if (!(bankInfos[i].bankcode == undefined || bankInfos[i].bankcode == null || bankInfos[i].bankcode == "")) {
            bankCodes = bankCodes + bankInfos[i].bankcode + ",";
        }
    }
    var itemCodes = "";
    for (var i = 0; i < itemCodeInfos.length; i++) {
        if (!(itemCodeInfos[i].id == undefined || itemCodeInfos[i].id == null || itemCodeInfos[i].id == "")) {
            itemCodes = itemCodes + itemCodeInfos[i].id + ",";
        }
    }

    param = "&ratMode=" + ratMode + "&contractNo=" + contractNo + "&ratCode=" + ratCode;
    param += "&gBeginDate=" + gBeginDate + "&lBeginDate=" + lBeginDate + "&gEndDate=" + gEndDate + "&lEndDate=" + lEndDate;
    param += "&corpId=" + corpId + "&bankCode=" + bankCodes + "&itemCode=" + itemCodes;
    return param;
}

//监听重置
function resetForm () {
    corpInit.get("corpId").val("");
    mulselectType.get("bankCode").val({value:""});
    mulselectType.get("itemCode").val({value:""});
    selectType.get("ratMode").val('')
    $("#contractNo").val("");
    $("#ratCode").val("");
    dateType.get("gBeginDate").val("");
    dateType.get("lBeginDate").val("");
    dateType.get("gEndDate").val("");
    dateType.get("lEndDate").val("");
}

function add(ratCode){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationdelay.add'),basePath + "/cms/rat/ratbkrationdelay/add?ratCode="+ratCode);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationdelay/add?ratCode="+ratCode,
        end:function(){
            datatable && datatable.draw();
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
        bt.navtab.addNavtab(bt.lang('ration.bkrationdelay.update'),basePath + "/cms/rat/ratbkrationdelay/update?id=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationdelay/update?id=" + id,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}
function viewdelay(id){
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('ration.bkrationdelay.view'),basePath + "/cms/rat/ratbkrationdelay/view/" + id);
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/cms/rat/ratbkrationdelay/view/" + id,
		end:function(){
			//datatable && datatable.draw();
		}
	});
}
function deleteColumn(id){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/rat/ratbkrationdelay/delete?id=" + id, function(result){
        	search();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

exports = function (type) {
    var url = basePath + "/cms/rat/ratbkrationdelay/exportListRatBkrationDelay";

    var bankInfos = mulselectType.get("bankCode").val();
    var itemCodeInfos = mulselectType.get("itemCode").val();
    var bankCodes = "";
    for (var i = 0; i < bankInfos.length; i++) {
        if (!(bankInfos[i].bankcode == undefined || bankInfos[i].bankcode == null || bankInfos[i].bankcode == "")) {
            bankCodes = bankCodes + bankInfos[i].bankcode + ",";
        }
    }

    var itemCodes = "";
    for (var i = 0; i < itemCodeInfos.length; i++) {
        if (!(itemCodeInfos[i].id == undefined || itemCodeInfos[i].id == null || itemCodeInfos[i].id == "")) {
            itemCodes = itemCodes + itemCodeInfos[i].id + ",";
        }
    }

    var formData;
    if (type === 0) {
        formData = {
            exportType: "0",
            start: $("#curr").val(),
            pageSize: $("#pageSize").val(),
            ratMode: selectType.get("ratMode").val(),
            contractNo: $("#contractNo").val(),
            ratCode: $("#ratCode").val(),
            corpId: corpInit.get("corpId").val(),
            itemCode: itemCodes,
            bankCode: bankCodes,
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
