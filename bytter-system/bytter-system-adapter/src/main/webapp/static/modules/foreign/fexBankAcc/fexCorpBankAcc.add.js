var rowNum=$("#rowIndex").val();
var dicts,moneyType,accountTable,selectType,dateType;
bt.use(['form','dateType','bttable','selectType','corp','dicts','select','accountTable','allExternalCorp','allBankInfo'], function(){
	$ = bt.jquery,form = bt.form();

	var allExternalCorp=bt.allExternalCorp.init();//客商组件初始化
	bt.cotpExternalBeforeClick = function () {
		allExternalCorp.set({
			"type": "external,corp",
		});
		return true;
	}
	var allBankInfo=bt.allBankInfo.init();//银行组件初始化

	/**
	 * 初始化列表
	 */
	var initEditTable=function(){
		bt.bttable.init({
			scrollX:false,
			scrollY:false,
			elem: "#editTable",
			fixedColumns: {
				rightColumns:1
			},
			buttons: [
			],
			columns:[],
			delRowCallback:function(data,$tr){
				$tr.remove();
				form.render("select");
			},
		});
	}
	initEditTable();

	/**
	 * 选择账户回调
	 * @param data
	 */
	accountCallBack = function (data,$tr,elem,all) {
		var ins = elem.parents("tr").attr("data-row");
		if (!bt.empty(data)){
			//单位回写
			var corpId = corp.get("corpId").val();
			if(bt.empty(corpId)){
				corp.get("corpId").val(data.corpId);
				bt.corpCallback(data.corpId);
			}

			// 银行账户下拉表格文本框
			$("#bankTab"+ins).val(data.bankAcc);
			// 银行账户
			$('#bankAcc'+ins+'').val(data.bankAcc);
			//账户名称
			$('#accName'+ins+'').val(data.accName);
			// 账号id
			$('#accId'+ins+'').val(data.bankAccId);
			//币种
			$('#curName'+ins+'').val(data.curEnglishCode);
			$('#curId'+ins+'').val(data.curId);
			selectType.get('fexCorpBankAccDtl['+ins+'].bankType').val(data.bankTypeId);
		}
	};

	tranTypeCallback = function(data){
		var url = data.value == "2"
			? basePath + "/foreign/fexproduct/listProduct/" + data.value
			: basePath + "/foreign/fexproduct/listProductAndGroup?tranType=" + data.value;
		selectType.get('proId').attr({"data-url":url,"data-defaultValue":""});

		/**交易类型控制交易对手组件选择*/
		if(bt.empty(data.value)){
			$("#oppCorpNameDiv").show();
			$("#externalDiv").hide();
			$("#corpDiv").hide();
			$("#bankDiv").hide();
		}else if("1"==data.value || "2"==data.value){
			$("#oppCorpNameDiv").hide();
			$("#externalDiv").hide();
			$("#corpDiv").hide();
			$("#bankDiv").show();
		}else if("3"==data.value){
			$("#oppCorpNameDiv").hide();
			$("#externalDiv").hide();
			$("#corpDiv").show();
			$("#bankDiv").hide();
		}else if("4"==data.value){
			$("#oppCorpNameDiv").hide();
			$("#externalDiv").show();
			$("#corpDiv").hide();
			$("#bankDiv").hide();
		} else if("7"==data.value || "8"==data.value || "9"==data.value){
			$("#oppCorpNameDiv").hide();
			$("#externalDiv").hide();
			$("#corpDiv").hide();
			$("#bankDiv").show();
		}
		$("#oppCorpName").val("");
		$("#cotpNameEn").val("");
		$("#oppId").val("");
		$("#eOppCorpName").val("");
		corp.get("cOppCorpName").val("");
		$("#bOppCorpName").val("");
	}
	dicts = bt.dicts.init();

	/**
	 * 单位回调函数
	 * */
	bt.corpCallback = function (id) {
		var curId = selectType.get("curId").val();
		if (curId == 'all'){
			curId = '';
		}
		if (id == 'all'){
			id = '';
		}
		$("#editTable").find("tbody tr").each(function(j){
			var index = $(this).attr("data-row");
			if (!bt.empty(index)) {
				var bankTypeId = selectType.get('fexCorpBankAccDtl['+index+'].bankType').val();
				reSetBankAccInfo(index,id,curId,bankTypeId);
				// 默认支付方式
				// var url = basePath+"/foreign/fexcotpdefaultacc/json?corpId=" + id;
				var url = basePath+"/netbank/fcvouchertype/getFcVoucherTypeForForeign";
				selectType.get('fexCorpBankAccDtl['+index+'].voucherTypeId').attr("data-url", url);
				selectType.render();
			}
		});
	}

    beforeInitCorp = function (nodes) {
		if (!bt.empty(nodes) && nodes.length>0){
			var data = {};
			data.children = null;
			data.code = null;
			data.id = 'all';
			data.corpName = bt.lang('common.placeholder.all');
			data.name = bt.lang('common.placeholder.all');;
			data.netId = null;
			data.netName = null;
			data.parentId = null;
			data.isLeaf = false;
			data.parentIds = null;
            nodes.unshift(data);
		}
        return nodes;
    }
	var corp = bt.corp.init();

	/**
	 * 银行类别回调函数
	 * */
	bankTypeCallback = function(data,elem){
		var ins = $(elem.othis).closest('tr').attr("data-row");
		var corpId = corp.get("corpId").val();
		var curId = selectType.get("curId").val();
		if (curId == 'all') {
			curId = '';
		}
		if (corpId == 'all') {
			corpId = '';
		}
		reSetBankAccInfo(ins,corpId,curId,data.id);
	}

	/**
	 * 币种回调函数
	 */
	curCallback = function(data,elem){
		var corpId = corp.get("corpId").val();
		if (corpId == 'all'){
			corpId = '';
		}
		if (data.id == 'all') {
			data.id = '';
		}
		$("#editTable").find("tbody tr").each(function(j){
			var index = $(this).attr("data-row");
			if (!bt.empty(index)) {
				var bankTypeId = selectType.get('fexCorpBankAccDtl['+index+'].bankType').val();
				reSetBankAccInfo(index,corpId,data.id,bankTypeId, "cur");
			}
		});
	}

	/**
	 * 交易对手回调初始化
	 * @param nodes
	 */
	beforeInitCotp = function(nodes){
		if (!bt.empty(nodes) && nodes.length>0){
			var data = {};
			data.id = 'all';
			data.cotpName = bt.lang('common.placeholder.all');
			nodes.unshift(data);
		}
		return nodes;
	}

	/**
	 * 产品初始化回调函数
	 * @param nodes
	 */
	beforeInitPro = function(nodes){
		if (!bt.empty(nodes) && nodes.length>0){
			var data = {};
			data.id = 'all';
			data.proName = bt.lang('common.placeholder.all');
			nodes.unshift(data);
		}
		return nodes;
	}

	/**
	 * 币别初始化回调函数
	 * @param nodes
	 */
	beforeInitCur = function(nodes){
		if (!bt.empty(nodes) && nodes.length>0){
			var data = {};
			data.id = 'all';
			data.englishCode = bt.lang('common.placeholder.all');
			nodes.unshift(data);
		}
		return nodes;
	}
	selectType = bt.selectType.init();

	/**
	 * 重置银行账号信息
	 */
	reSetBankAccInfo = function(index,corpId,curId,bankTypeId,changeType) {
		// 币种修改时，只保留对应币种数据
		if ((curId==$('#curId'+index+'').val() || !curId) && "cur" == changeType) {
			return false;
		} else {
			accountTable.get("bankTab"+index).reload({urldata:{corpId:corpId,curId:curId,bankTypeId:bankTypeId}});
			// 银行账户下拉表格文本框
			$("#bankTab"+index).val('');
			// 银行账户
			$('#bankAcc'+index+'').val('');
			//账户名称
			$('#accName'+index+'').val('');
			// 账号id
			$('#accId'+index+'').val('');

			//币种
			$('#curId'+index+'').val('');
			var curIdTemp = $('#curId'+index+'').val();
			if(!curIdTemp && curId != curIdTemp){
				$("#curName"+index).val("");
			}
		}

	}

	// 监听提交
	form.on('submit', function(data){
		bt.loading.show();//显示加载中
        if (!$("#editTable").find("tbody tr:visible").length) {
            bt.loading.hide(); //关闭加载中
            parent.layer.msg(bt.lang("fex.message.accInfo.notexist"), {icon: 2});
            return false;
        }
		data.field.status = $(this).attr("value");
		var action = "/foreign/fexcorpbankacc/submit";
		$.post(basePath + action,data.field,function(result){
			bt.loading.hide(); //关闭加载中
			parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
			if(result.success){
				if(!bt.navtab){
					parent.layer?parent.layer.closeAll():layer.closeAll();
				} else {
					bt.navtab.closeCurrent();
					bt.navtab.refreshParentTable();
				}
			}
		},'JSON');
		return false;
	});

	// 新增行
	$("#add").click(function(){
		var row = $("#editTable").find("tbody tr:visible").length;
		if (row>19) {
			parent.layer.msg(bt.lang("table.fexCorp.noMoreThan"), {icon: 2});
			return false;
		}
		var corpId = corp.get("corpId").val();
		if (corpId == 'all'){
			corpId = '';
		}
		var curId = selectType.get("curId").val();
		if (curId == 'all'){
			curId = '';
		}
		var html = '<tr data-row="'+rowNum+'"><input type="hidden" id="rowSign'+rowNum+'" value="'+rowNum+'">';
		//生效日期
		html += '<td class="center" width="15%"><div class="layui-inline">';
		html += '<input type="text" name="fexCorpBankAccDtl['+rowNum+'].effectDate" style="width: 140px" data-biz-name="'+rowNum+'effectDate" class="layui-input bt-biz-dateType" data-type="date" lay-verify="required" data-callback="changeDate"/>'
		html += '</div></td>';
		//银行类型
		html += '<td class="center" width="15%"><div class="layui-inline">';
		html += '<div data-name="fexCorpBankAccDtl['+rowNum+'].bankType" data-width="150px" class="bt-biz-selectType" data-url="' + basePath + '/basic/business/btbanktype/json" data-valueField="id" '+
			' data-biz-name="fexCorpBankAccDtl['+rowNum+'].bankType"  data-textField="typeName" data-search="true" data-callback="bankTypeCallback" data-required="true"></div>';
		html += '</div></td>';
		//银行账户
		html += '<td class="center" width="15%"><div class="layui-inline">';
			//银行账户选择
		html +=  '<input type="text" id="bankTab'+rowNum+'"  data-name="bankTab'+rowNum+'" data-biz-name="bankTab'+rowNum+'" accountType="2,3" data-defaultcorp="'+corpId+'"' +
			' data-curId="'+curId+'" class="layui-input bt-biz-accountTable" data-resource="netbank:bankAcc:bisAccBal:apply" data-callback="accountCallBack" lay-verify="required|tableData"/>';
			//银行账号
		html += '<input type="hidden" name="fexCorpBankAccDtl['+rowNum+'].bankAcc" id="bankAcc'+rowNum+'" />';
		html += '</div></td>';
		//账户名称
		html += '<td class="center" width="15%"><div class="layui-inline">';
			//账号名称
		html += '<input type="text" name="fexCorpBankAccDtl['+rowNum+'].accName" id="accName'+rowNum+'" class="layui-input" readonly="true"/>';
			//账号id
		html += '<input type="hidden" name="fexCorpBankAccDtl['+rowNum+'].accId" id="accId'+rowNum+'" /></div></td>';
		//币种
		html += '<td class="center" width="15%"><div class="layui-inline">';
		html += '<input type="text" name="fexCorpBankAccDtl['+rowNum+'].curName" id="curName'+rowNum+'" style="width: 140px" readonly="true" class="layui-input"/>';
		html += '<input type="hidden" name="fexCorpBankAccDtl['+rowNum+'].curId" id="curId'+rowNum+'" /></div></td>';
		//默认支付方式
		html += '<td class="center" width="15%"><div class="layui-inline">';
		html += '<div type="list" data-name ="fexCorpBankAccDtl['+rowNum+'].voucherTypeId" class="bt-biz-selectType" data-width="150px" data-url="' + basePath + '/netbank/fcvouchertype/getFcVoucherTypeForForeign" data-valueField="id" ' +
			' data-textField="voucherName"  data-biz-name="fexCorpBankAccDtl['+rowNum+'].voucherTypeId"  data-search="true"></div>';
		html += '</div></td>';
		//删除按钮
		html += '<td class="center" width="15%"><button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button></td>';
		html += '</tr>';
		$("#editTable").children('tbody').append(html);
		// 银行账号
		accountTable = bt.accountTable.init();
		rowNum++;
		renderForm();
	});

	bt.dicts.init();
	dateType = bt.dateType.init();
	bt.selectType.init();
	// 银行账号
	accountTable = bt.accountTable.init();

	/**
	 * 渲染表单
	 */
	var renderForm=function(){
		bt.dicts.init();
		bt.dateType.init();
		bt.selectType.init();
	}
});

/**
 * 判断字符串是否为空
 * @param ms
 * @returns {boolean}
 */
bt.empty = function(ms){
	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
		return true;
	}
	return false;
}

/**
 * 关闭页面
 */
function closeWindow(){
	bt.navtab.closeCurrent();
}

function changeDate(value , elem){
	var dateInputs = $(elem).parents('tr').siblings('tr').find('.bt-biz-dateType');
	bt.each(dateInputs,function () {
		var beforValue = $(this).val();
		var res = bt.date.compare(value,beforValue);
		if(res == 0){
            var dateTypes = bt.dateType.init();
            var Index = elem.parents('tr').data('row');
            dateTypes.get(Index+"effectDate").val("");
            parent.layer.msg(bt.lang("foreign.effectDate.not.eq"), {icon: 2});
		}
    })
}

//交易对手所有组件回调方法
//客商回调
function cotpExternalCallback(data) {
	$("#oppCorpName").val(data.name);
	$("#cotpNameEn").val(data.nameEn);
	$("#oppId").val(data.id);
	$("#eOppCorpName").val(data.name);
}
//单位回调
function oppCorpTreeCallback(data) {
	$("#oppCorpName").val(data.corpName);
	$("#oppId").val(data.id);
}
//银行回调
function bankCallback(data) {
	$("#oppCorpName").val(data.bankName);
	$("#oppId").val(data.id);
	$("#bOppCorpName").val(data.bankName);
}

function oppCorpNameClick() {
	parent.layer.msg(bt.lang("table.fexBankAcc.tranType.null"), {icon: 2});
}
