bt.use(['form', 'btdate', 'money', 'dateType', 'corp', 'dicts', 'selectType', 'treeTable', 'mulselect'], function () {
	$ = bt.jquery, form = bt.form();
	mulselect = bt.mulselect.init();
	/**判断字符串是否为空*/
	bt.empty = function (ms) {
		if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
			return true;
		}
		return false;
	}

	//监听查询
	form.on('submit(search)', function (data) {
		/*    	var bankCode = $("#bankCode").val();
                if(bt.empty(bankCode)){
                    parent.layer.msg(bt.lang("cms.rat.ratUration.bank.null"), {icon: 2});
                    return false;
                }*/
		var curCode = bt.selectType.init().get("curCode").val();
		if (bt.empty(curCode)) {
			parent.layer.msg(bt.lang("nis.pay.message.cur.null"), {icon: 2});
			return false;
		}
		/*    	var beginDate = bt.dateType.init().get("beginDate").val();
                var endDate = bt.dateType.init().get("endDate").val();
                if(bt.empty(beginDate) || bt.empty(endDate)){
                    parent.layer.msg(bt.lang("cms.rat.ratUration.ratDate.null"), {icon: 2});
                    return false;
                }*/

		var action = basePath + "/cms/rat/raturation/rationQuery";
		$("#searchForm").attr("action", action);
		$("#searchForm").attr("method", "post");
		$("#searchForm").submit();
	});

	//查看授信占用情况
	viewRatioUse = function (useType, curCode, corpId, ratCode, itemFlag, itemCode, businessName) {
		if (bt.navtab) {
			bt.navtab.addNavtab(bt.lang('ration.ratUration.view'), basePath + "/cms/rat/raturation/viewRatioUse?useType="
				+ useType + "&curCode=" + curCode + "&corpId=" + corpId + "&ratCode=" + ratCode + "&itemFlag=" + itemFlag
				+ "&itemCode=" + itemCode + "&businessName=" + businessName);
			return;
		}
		layer.open({
			type: 2,
			area: ['850px', '530px'],
			maxmin: true,
			content: basePath + "/cms/rat/raturation/viewRatioUse?useType="
				+ useType + "&curCode=" + curCode + "&corpId=" + corpId + "&ratCode=" + ratCode + "&itemFlag=" + itemFlag + "&itemCode=" + itemCode,
			end: function () {
			}
		});
	}

	bt.dateType.init();
	bt.selectType.init();
	bt.corp.init();
	bt.dicts.init();
	bt.treeTable.init({elem: "#appTable", column: 0});

	//监听重置
	resetCode = function () {
		var inputs = $('.rCode').find('input');
		inputs.each(function (i, item) {
			$(this).val("");
		});
		$("#corpId").val(" ");
		// document.getElementById("bankCode").options[0].selected = true;
		bt.selectType.init().get("moneyType").val("1");
		bt.selectType.init().get("itemCode").val("");
		bt.selectType.init().get("curCode").val($("#curId").val());
		var nowdate = bt.date.format(new Date(), 'yyyy-MM-dd');
		bt.dateType.init().get("beginDate").val(nowdate);
		bt.dateType.init().get("endDate").val(nowdate);
		mulselect.get("bankCode").val({value:""});
		bt.dicts.init().get("moneyType").val("1");
	}


// 导出按钮
	form.on('submit(excel)', function (data) {
		var url = basePath + "/cms/rat/raturation/exportExcel";
		DownLoadFile({url: url, data: data.field, method: 'post'});
		return false;
	});
});

/**
 * 查看合同信息
 * @param el
 */
function conView(conId){
	parent.layer.open({
		type: 2,
		area: ['1080px', '720px'],
		maxmin: true,
		content: basePath + "/contract/conCommon/conView?id=" + conId
	});
}

/**
 * 上传合同
 */
function uploadAttachFile(ratCode){
	if (bt.navtab) {
		bt.navtab.addNavtab(bt.lang('ration.bkration.view'), basePath + "/cms/rat/raturation/uploadAttachFile/" + ratCode);
		return;
	}
	layer.open({
		type: 2,
		area: ['1080px', '720px'],
		maxmin: true,
		content: basePath + "/cms/rat/raturation/uploadAttachFile/" + ratCode
	});
}


var DownLoadFile = function (options) {
	var config = $.extend(true, {method: 'post'}, options);
	var $iframe = $('<iframe id="down-file-iframe" />');
	var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
	$form.attr("action", config.url);
	for (var key in config.data) {
		$form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
	}
	$iframe.append($form);
	$(document.body).append($iframe);
	$form[0].submit();
	$iframe.remove();
}