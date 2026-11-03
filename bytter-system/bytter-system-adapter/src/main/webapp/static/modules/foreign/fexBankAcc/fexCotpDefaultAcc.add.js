var rowNum=$("#rowIndex").val(),clickNum;
var dicts, moneyType, accountTable, selectType, cotpBanAccInit,allExternalBankAcc,allExternalCorp;
var fexProductPayPalId = "4c182f17455f4614a79d66bdfd755c8d";
var fexProductFpsId = "53f8408e666b42b9abbe2dcf8372bc76";

function isProductId(proId, productId) {
	return proId == productId || (proId || "").indexOf(productId + "_") === 0;
}

function isQuickPayProduct(proId) {
	return isProductId(proId, fexProductPayPalId) || isProductId(proId, fexProductFpsId);
}

function isPayPalProduct(proId) {
	return isProductId(proId, fexProductPayPalId);
}

bt.use(['form','dateType','bttable','selectType','corp','dicts','select','accountTable','selectTable','allExternalCorp','allBankInfo','allExternalBankAcc'], function(){
	$ = bt.jquery,form = bt.form();
	 allExternalCorp=bt.allExternalCorp.init();//客商组件初始化
	bt.cotpExternalBeforeClick = function () {
		var proId=selectType.get("proId").val();
		if(isQuickPayProduct(proId)){
			allExternalCorp.set({
				"type": "external",
				"proId": proId
			});
		}else {
			allExternalCorp.set({
				"type": "external,corp",
			});
		}

		return true;
	}
	var allBankInfo=bt.allBankInfo.init();//银行组件初始化
	allExternalBankAcc=bt.allExternalBankAcc.init();
	if($("#oppId").val()){
		allExternalBankAcc.set({
			"oppId":$("#oppId").val()
		})
	}

	tranTypeCallback = function(data){
		var url = data.value == "4"
			? basePath + "/foreign/fexproduct/getInitProductDate"
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
		}
		$("#corpName").val("");
		$("#cotpNameEn").val("");
		$("#oppId").val("");
		$("#eOppCorpName").val("");
		corp.get("cOppCorpName").val("");
		$("#bOppCorpName").val("");

		//清空账号
		$("#editTable").children('tbody').html("");
		$("#fpsEditTable").children('tbody').html("");
		$("#fpsEditTable").hide();
		$("#editTable").show();
	}
	dicts = bt.dicts.init();

	/**
	 * 产品回调
	 * @param date
	 */
	proIdCallback=function(data){
		if(isQuickPayProduct(data.id)){
			$("#fpsEditTable").show();
			$("#editTable").hide();
			$("#editTable").children('tbody').html("");
			$("#fpsEditTable").children('tbody').html("");
			$("#corpName").val("");
			$("#cotpNameEn").val("");
			$("#oppId").val("");
			$("#eOppCorpName").val("");
		}else {
			$("#fpsEditTable").hide();
			$("#editTable").show();
			$("#fpsEditTable").children('tbody').html("");
		}
	}


	/**
	 * 交易对手回调函数
	 * */
	bt.corpCallback = function (id) {
		var curId = selectType.get("curId").val();
		var cotpId = selectType.get("corpId").val();
		$("#editTable").find("tbody tr").each(function(j){
			var index = $(this).attr("data-row");
			if (!bt.empty(index)) {
				cotpBanAccInit.get("bankTab"+index).reload({cotpId: cotpId,curCode: curId});
				form.render();
			}
		});
	}

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
	 * 初始化列表
	 */
	var initFpsEditTable=function(){
		bt.bttable.init({
			scrollX:false,
			scrollY:false,
			elem: "#fpsEditTable",
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
	initFpsEditTable();

	/**
	 * 选择账户回调
	 * @param data
	 */
	accountCallBack = function (data,elem) {
        var ins = $(elem.othis).closest('tr').attr("data-row");
        if (!bt.empty(data)) {
            // 银行账户
            $('#bankAcc' + ins + '').val(data.bankacc);
            //账户名称
            $('#accName' + ins + '').val(data.accname);
            // 账号id
            $('#accId' + ins + '').val(data.id);
            //币种
            $("#curName" + ins).val(data.curname);
            $('#curId' + ins + '').val(data.curid);
            $('#bankType' + ins + '').val(data.bankId);
		}
	};

	/**
	 * 重新渲染单位
	 * @param nodes
	 * @returns {*}
	 */
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

	/**
	 * 单位回调函数
	 * */
	bt.oppCorpIdCallback = function (id) {
	}
	var corp = bt.corp.init();

	/**
	 * 币种回调函数
	 */
	curCallback = function(data,elem){

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

	// 监听提交
	form.on('submit', function(data){
		bt.loading.show();//显示加载中
        if ((!$("#editTable").find("tbody tr:visible").length) && (!$("#fpsEditTable").find("tbody tr:visible").length)) {
            bt.loading.hide(); //关闭加载中
            parent.layer.msg(bt.lang("fex.message.accInfo.notexist"), {icon: 2});
            return false;
        }
		data.field.status = $(this).attr("value");
		var action = "/foreign/fexcotpdefaultacc/submit";
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

	var addVerify=function(){
		var tranTyp=dicts.get("tranType").val();
		var proId=selectType.get("proId").val();
		var corpName=$("#corpName").val();
		if(bt.empty(tranTyp)){
			parent.layer.msg(bt.lang("table.fexBankAcc.tranType.null"), {icon: 2});
			return false;
		}
		if(bt.empty(corpName) && (tranTyp=="3" || tranTyp=="4")){
			parent.layer.msg(bt.lang("请选择交易对手"), {icon: 2});
			return false;
		}
		if("4"==tranTyp && bt.empty(proId)){
			parent.layer.msg(bt.lang("资金收付交易请选择产品"), {icon: 2});
			return false;
		}
		return true;
	}

	$("#fpsAdd").click(function(){
		addFunction();
	});

	// 新增行
	$("#add").click(function(){
		addFunction();
	});
	var addFunction=function () {
		if(!addVerify()){
			return false;
		}
		var row = $("#editTable").find("tbody tr:visible").length;
		if (row>19) {
			parent.layer.msg(bt.lang("table.fexCorp.noMoreThan"), {icon: 2});
			return false;
		}
		var tranTyp=dicts.get("tranType").val();
		var proId=selectType.get("proId").val();
		var oppId=$("#oppId").val();
		var curId = selectType.get("curId").val();
		if (curId == 'all'){
			curId = ' ';
		}

		//输入框账号
		var inputBank_Acc = '<td class="center"><div class="layui-inline"><input type="text" class="layui-input" lay-verify="required"  name="fexCorpBankAccDtl['+rowNum+'].bankAcc" id="bankAcc'+rowNum+'"> </div></td>';
		//账户名称
		inputBank_Acc += '<td class="center"><div class="layui-inline">';
		//账号名称
		inputBank_Acc += '<input type="text" name="fexCorpBankAccDtl['+rowNum+'].accName" id="accName'+rowNum+'" class="layui-input" /> </div></td>';
		//币种
		inputBank_Acc += '<td class="center"><div class="layui-inline">';
		if(bt.empty(curId)) {
			inputBank_Acc += '<div class="layui-inline">' +
				'<div class="bt-biz-selectType" data-name="fexCorpBankAccDtl['+rowNum+'].curId"  data-url="' + basePath + '/basic/business/btcurrency/json" data-valuefield="id" data-textfield="englishCode"' +
				' data-biz-name="fexCorpBankAccDtl['+rowNum+'].curId" ></div>' +
				'</div>';
		}else {
			inputBank_Acc += '<div class="layui-inline">' +
				'<div class="bt-biz-selectType" data-name="fexCorpBankAccDtl['+rowNum+'].curId"  data-url="' + basePath + '/basic/business/btcurrency/json" data-valuefield="id" data-textfield="englishCode"' +
				' data-biz-name="fexCorpBankAccDtl['+rowNum+'].curId" data-value="'+curId+'" data-disabled="true"></div>' +
				'</div>';
		}
		inputBank_Acc += '</div></td>';

		//单位账号选择
		var corpBank_Acc = '<td class="center"><div class="layui-inline">';
		corpBank_Acc +='<input class="layui-input" name="fexCorpBankAccDtl['+rowNum+'].bankAcc" id="bankAcc'+rowNum+'" lay-verify="required" readonly="readonly" type="text"/>';
		corpBank_Acc +='<div class="bt-div-search">'+
			'<button type="button" class="bt-biz-allExternalBankAcc" id="externalBankAcc'+rowNum+'" onclick="bankAccClick(this)" data-callback="externalBankAccCallback">'+
			'<i class="layui-icon">&#xe615;</i></button>'+
			'</div></div></td>';
		//账户名称
		corpBank_Acc += '<td class="center"><div class="layui-inline">';
		//账号名称
		corpBank_Acc += '<input type="text" name="fexCorpBankAccDtl['+rowNum+'].accName" id="accName'+rowNum+'" class="layui-input" readonly="true"/>';
		corpBank_Acc += '</div></td>';
		//币种
		corpBank_Acc += '<td class="center"><div class="layui-inline">';
		corpBank_Acc += '<input type="text" name="fexCorpBankAccDtl['+rowNum+'].curName" id="curName'+rowNum+'" readonly="true" class="layui-input"/>';
		corpBank_Acc += '<input type="hidden" name="fexCorpBankAccDtl['+rowNum+'].curId" id="curId'+rowNum+'" /></div></td>';

			//fps信息框
			//收款账户类型
			var fpsBank_Acc='<td class="center">';
			fpsBank_Acc += '<div class="layui-inline">';
			fpsBank_Acc +='<div class="bt-biz-dict" data-type="select" data-name="fexCorpBankAccDtl['+rowNum+'].payModel" data-required="true" data-group="fex.tpay.paymodel"';
			if(isPayPalProduct(proId))	{
				fpsBank_Acc +=' data-displayData="EMAIL,MOBN" ';
			}
			fpsBank_Acc +=' data-biz-name="fexCorpBankAccDtl['+rowNum+'].payModel" data-callback="payModelCallBack"></div>' ;
		fpsBank_Acc +='</div></td>';

		//快捷支付信息
		fpsBank_Acc +='<td class="center">';
		fpsBank_Acc += '<div class="layui-inline payValue">' +
			// '<div class="bt-biz-selectType init payValue"  data-name="fexCorpBankAccDtl['+rowNum+'].payValue" data-required="true" data-url="'+basePath+'/sys/externalCorp/fexPayPayModelList" data-valuefield="id" data-textfield="id"' +
			// ' data-biz-name="test01" ></div>' +
			'</div></td>';


		var html = '<tr data-row="'+rowNum+'"><input type="hidden" id="rowSign'+rowNum+'" value="'+rowNum+'">';
		//生效日期
		html += '<td class="center"><div class="layui-inline">';
		html += '<input type="text" name="fexCorpBankAccDtl['+rowNum+'].effectDate" data-width="50px" data-biz-name="'+rowNum+'effectDate" class="layui-input bt-biz-dateType" data-type="date" lay-verify="required" data-callback="changeDate"/>'
		html += '</div></td>';

		if(tranTyp=="1" ||tranTyp=="2"){
			html +=inputBank_Acc;
		}
			if(tranTyp=="3"){
				html +=corpBank_Acc;
			}
			if(isQuickPayProduct(proId)){
				html +=fpsBank_Acc;
			}else if(tranTyp=="4"){
				html +=corpBank_Acc;
		}

		html += '<td class="center"><button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button></td>';
		html += '</tr>';

			if(isQuickPayProduct(proId)){
				$("#fpsEditTable").children('tbody').append(html);
			}else {
				$("#editTable").children('tbody').append(html);
		}
		// 银行账号

		allExternalBankAcc.set({
			"oppId": oppId
		});
		rowNum++;
		renderForm();
	}

	bt.selectType.init();
	bt.dicts.init();
	bt.dateType.init();


	/**
	 * 渲染表单
	 */
	var renderForm=function(){
		bt.selectType.init();
		bt.dateType.init();
		bt.dicts.init();

	}

});


// /**
//  * 账号下拉列表
//  * @param cls
//  */
// function initSelectTalbe(cls,curId,cotpId) {
//     cotpBanAccInit = bt.selectTable.init({
//         elem: "#example" + cls,
//         url: basePath + "/foreign/fexcotpbankacc/selectJoinCur",
//         urldata: {cotpId:cotpId, curCode: curId},
//         scrollX: true,
//         scrollY: "200px",
//         searchInput: "#bankAcc_index" + cls,
//         columns: [
//             {data: "bankAcc",},
//             {data: "accName",},
//             {data: "bankName",},
//             {data: "curName",}
//         ],
//         clickCallback: function (data) {
//             if (!bt.empty(data)) {
//                 // 银行账户
//                 $('#bankAcc_index' + cls + '').val(data.bankAcc);
//                 $('#bankAcc' + cls + '').val(data.bankAcc);
//                 //账户名称
//                 $('#accName' + cls + '').val(data.accName);
//                 // 账号id
//                 $('#accId' + cls + '').val(data.id);
//                 //币种
//                 $("#curName" + cls).val(data.curName);
//                 $('#curId' + cls + '').val(data.curId);
//                 $('#bankType' + cls + '').val(data.bankId);
//             }
//         }
//     });
// }

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
	$("#corpName").val(data.name);
	$("#cotpNameEn").val(data.nameEn);
	$("#oppId").val(data.id);
	$("#eOppCorpName").val(data.name);

	//清空账号
	$("#editTable").children('tbody').html("");
	$("#fpsEditTable").children('tbody').html("");
}
//单位回调
function oppCorpTreeCallback(data) {
	$("#corpName").val(data.corpName);
	$("#oppId").val(data.id);

	//清空账号
	$("#editTable").children('tbody').html("");
	$("#fpsEditTable").children('tbody').html("");
}
//银行回调
function bankCallback(data) {
	$("#corpName").val(data.bankName);
	$("#oppId").val(data.id);
	$("#bOppCorpName").val(data.bankName);

	//清空账号
	$("#editTable").children('tbody').html("");
	$("#fpsEditTable").children('tbody').html("");
}

function oppCorpNameClick() {
	parent.layer.msg(bt.lang("table.fexBankAcc.tranType.null"), {icon: 2});
}

function externalBankAccCallback(data) {
	if(data.type=="corpBankAcc") {
		$("#bankAcc" + clickNum).val(data.bankAcc);
		$("#accName" + clickNum).val(data.name);
		$("#curName" + clickNum).val(data.curName);
		$("#curId" + clickNum).val(data.curId);
	}else {
		$("#bankAcc" + clickNum).val(data.externalAcc);
		$("#accName" + clickNum).val(data.externalAccName);
		$("#curName" + clickNum).val(data.curName);
		$("#curId" + clickNum).val(data.curId);
	}
}

function bankAccClick(e) {
	var row = $(e).parents("tr").data("row");
	clickNum=row;
}

function payModelCallBack(data) {
	var rowNum = $(data.elem).parents("tr").data("row");
	var externalId= $("#oppId").val();
	var proId=selectType.get("proId").val();
	var payValueUrl= basePath + "/sys/externalCorp/fexPayPayModelList?externalId="+externalId+"&proId="+proId;
	var payAccValueUrl=basePath + "/sys/externalCorp/fexPayPayModelAccList?externalId="+externalId;

	var currentTr = $(data.elem).parents("tr");
	var html = '';


	if(data.value=="SVID"){
		payValueUrl=payValueUrl+"&field=fpsId";
		html = '<div class="bt-biz-selectType"  data-name="fexCorpBankAccDtl['+rowNum+'].payValue" data-required="true" data-url="'+payValueUrl+'" ' +
			'data-valuefield="fpsId" data-textfield="fpsId" data-biz-name="fexCorpBankAccDtl['+rowNum+'].payValue"></div>';
	}else if(data.value=="EMAIL"){
		payValueUrl=payValueUrl+"&field=email";
		html = '<div class="bt-biz-selectType"  data-name="fexCorpBankAccDtl['+rowNum+'].payValue" data-required="true" data-url="'+payValueUrl+'" ' +
			'data-valuefield="email" data-textfield="email" data-biz-name="fexCorpBankAccDtl['+rowNum+'].payValue"></div>';
	}else if(data.value=="MOBN"){
		payValueUrl=payValueUrl+"&field=phone";
		html = '<div class="bt-biz-selectType"  data-name="fexCorpBankAccDtl['+rowNum+'].payValue" data-required="true" data-url="'+payValueUrl+'" ' +
			'data-valuefield="phone" data-textfield="phone" data-biz-name="fexCorpBankAccDtl['+rowNum+'].payValue"></div>';
	}else if(data.value=="AIIN"){
		payValueUrl=payValueUrl+"&field=creditCard";
		html = '<div class="bt-biz-selectType"  data-name="fexCorpBankAccDtl['+rowNum+'].payValue" data-required="true" data-url="'+payValueUrl+'" ' +
			'data-valuefield="creditCard" data-textfield="creditCard" data-biz-name="fexCorpBankAccDtl['+rowNum+'].payValue"></div>';
	}else if(data.value=="HKID"){
		payValueUrl=payValueUrl+"&field=hkId";
		html = '<div class="bt-biz-selectType"  data-name="fexCorpBankAccDtl['+rowNum+'].payValue" data-required="true" data-url="'+payValueUrl+'" ' +
			'data-valuefield="hkId" data-textfield="hkId" data-biz-name="fexCorpBankAccDtl['+rowNum+'].payValue"></div>';
	}else if(data.value=="ACC"){
		html = '<div class="bt-biz-selectType"  data-name="fexCorpBankAccDtl['+rowNum+'].payValue" data-required="true" data-url="'+payAccValueUrl+'" ' +
			'data-valuefield="externalAcc" data-textfield="externalAcc" data-biz-name="fexCorpBankAccDtl['+rowNum+'].payValue"></div>';
	}
	currentTr.find('.payValue').html(html);
	bt.selectType.init();
}
