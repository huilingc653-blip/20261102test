var datatable;
var dateFun;
bt.use(['form','datatable','select','dateType','corp','money','moneyType','dicts', 'selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.selectType.init();
    dateFun = bt.dateType.getFun();

    //生成列表
    datatable = bt.datatable.init({
    	elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/netbank/nisbillhead/datatable"
        },
        buttons: [
			{
			    hide:bt.isnotResource('netbank:expense:nisExpenseDetail:handleApply'),
			    text: bt.lang('common.button.batchSubmit'),
			    action: function ( e, dt, node, config ) {
			    	 var data = dt.rows('.selected').data();
			    	 submitBatch(data);
			    }
			},{
			    hide:bt.isnotResource('netbank:expense:nisExpenseDetail:handleApply'),
			    text: bt.lang('common.button.batchDelete'),
			    action: function ( e, dt, node, config ) {
			    	var data = dt.rows('.selected').data();
			    	deleteBatch(data);
			    }
            },{
                hide:bt.isnotResource('netbank:expense:nisExpenseDetail:handleApply'),
                text: bt.lang('common.button.batchBack'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    backBatch(data);
                }
			},{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"billNo",bSortable:true,className:'center',
                render: function (data, type, full, meta) {
                    if(full.status == -1){
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data:"corpName",bSortable:true},
            {data:"voucherName",bSortable:true},
            {data:"payerAcc",bSortable:true},
            {data:"payerAccName",bSortable:true},
            {data:"payerBank",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    //证书信息
                    var cfcaInfo = '<input type="hidden" id="random' + full.id + '" name="random" value=""><input type="hidden" id="encrySource' + full.id + '" name="encrySource" value=""><input type="hidden" id="caCoding' + full.id + '" name="caCoding" value="">';
                    var html = '';
                    html = cfcaInfo +bt.money.format(full.payMoney);
                    return html;
                }
            },
            {data:"wishPayday",bSortable:true,className:'center'},
            {data:"busTypeName",bSortable:true},
            {data:"bursarBillNo",bSortable:true},
            {data:"purpose",bSortable:true},
            {data:"addWord",bSortable:true},
            {
                data: "isPayed", bSortable: true, className: 'center', visible: setIsPayedVisible(),
                render: function (data, type, full, meta) {

                    return bt.dict("nisBillhead.isPayed", full.isPayed);
                }
            },
            {data:"status",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("nisBillhead.status",full.status);
                }
            },
            {data:"landingLimitResult",bSortable:true,className:'center',
                render: function (data, type, full, meta) {
                    return bt.dict("enterpriseLimit.landingResult", full.landingLimitResult);
                }
            },
            {data:"voucherStat",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                	return bt.dict("bisExc.voucherStat",full.voucherStat);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('netbank:expense:nisExpenseDetail:handleApply',full.corpId)){
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="backDetailColumn(this)">'+bt.lang("common.button.back")+'</a><span class="text-explode">|</span>';
                    if(full.voucherStat && (full.voucherStat == "2" || full.voucherStat == "9" || full.voucherStat == "B")){//付款指令失败的才能打回失败明细
                    	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="backFailDetailColumn(this)">'+bt.lang("common.button.backDetail")+'</a><span class="text-explode">|</span>';
                    }
                }
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ]
    });
});
function setIsPayedVisible(){
    var showIsPayed = $("#showIsPayed").val();
    if (showIsPayed == "1") {
        return true;
    } else {
        return false;
    }
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/netbank/nisbillhead/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/view/" + data.id
    });
}
function update(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/netbank/nisbillhead/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/netbank/nisbillhead/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.ajax({
            type: 'DELETE',
            dataType: 'json',
            url: basePath + "/netbank/nisbillhead/delete/" + data.id + "/0",
            success: function(result){
                location.reload();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
            }
        });
    });
}

//账务校验
function validDetail(data) {
    var result = "0";
    $.ajax({
        url: basePath + "/netbank/nisbillhead?validDetail",
        method: 'POST',
        async: false,
        data: {billId: data},
        dataType: 'json',
        success: function (data) {
            if (data.obj) {
                result = data.obj;
            }
        }
    });
    return result;
}

//打回明细
function backDetailColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var result = validDetail(data.id);//账务校验
    var msg = bt.lang('common.message.confirm.back');
    if (result) {
        msg = bt.lang('netbank.message.confirm.back',[data.billNo,result]);
    }
    //校验是否是部分成功部分失败
    parent.layer.confirm(bt.lang(msg),function(){
        $.post(basePath + "/netbank/nisbillhead/backDetail/" + data.id, function(result){
            location.reload();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
//打回失败明细
function backFailDetailColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.back'),function(){
        $.post(basePath + "/netbank/nisbillhead/backFailDetail/" + data.id, function(result){
            location.reload();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
//批量删除
function deleteBatch(data){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    var ids="";
 	var rowData = data.splice(0,len);
 	for(var i=0;i<rowData.length;i++){
 		if (ids == "") {
			ids += rowData[i].id;
		} else {
			ids += "," + rowData[i].id;
		}
 	}
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'),function(){
     	$.post(basePath + "/netbank/nisbillhead/batchDelete",{ids:ids,chargeBackType:''}, function(result){
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
 	});
}
//批量提交
function submitBatch(data){
    if($("#isOpenLimit").val()=="1") {
        limitBankAcc(data);
    }else {
        submit(data);
    }
}

//批量打回
function backBatch(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids="";
    var rowData = data.splice(0,len);
    var msg = 'common.message.confirm.batchBack';
    for(var i=0;i<rowData.length;i++){
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
        if (msg != "netbank.message.confirm.batchBack") {
            var result = validDetail(rowData[i].id);//账务校验
            if (result) {
                msg = 'netbank.message.confirm.batchBack';
            }
        }
    }
    parent.layer.confirm(bt.lang(msg),function(){
        $.post(basePath + "/netbank/nisbillhead/batchBackDetail",{ids:ids}, function(result){
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

function submit(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids="";
    var rowData = data.splice(0,len);
    var signDataMsg = "";
    var randoms = '';
    var sourceDatas = '';
    var caCodings = '';
    for(var i=0;i<rowData.length;i++){
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
        //性能考虑：批量提交证书验签第一笔
        if (i == 0) {
            var key = rowData[i].id;
            //数据源规则：付款账号+随机数（10位）+金额（14位）+期望支付日期+收款人+收款账号
            var sourceData = rowData[i].payerAcc;//付款账号
            var random = String(parseInt(Math.random() * 10000000000));//随机数
            while (random.length < 10) {
                random = random + '0';
            }
            $("#random" + key).val(random);
            var payMoney = rowData[i].payMoney + "";//金额
            while (payMoney.length < 14) {
                payMoney = '0' + payMoney;
            }
            var wishPayday = dateFun.format(rowData[i].wishPayday, 'yyyyMMdd');//期望支付日期
            sourceData += random + payMoney + wishPayday;
            $("#encrySource" + key).val(sourceData);
            signDataMsg = batchSignData(sourceData, "caCoding" + key, "2");
            if (randoms == "") {
                randoms += random;
            } else {
                randoms += "," + random;
            }
            if (sourceDatas == "") {
                sourceDatas += sourceData;
            } else {
                sourceDatas += "," + sourceData;
            }
            if (caCodings == "") {
                caCodings += $("#caCoding" + key).val();
            } else {
                caCodings += "," + $("#caCoding" + key).val();
            }
        }
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'),function(){
        if(caCodings == ""){
            for (var i = 0; i < rowData.length; i++) {
                var key = rowData[i].id;
                if (caCodings == "") {
                    caCodings += $("#caCoding"+key).val();
                } else {
                    caCodings += "," + $("#caCoding"+key).val();
                }
            }
        }
        $.post(basePath + "/netbank/nisbillhead/batchSubmit",{ids:ids,randoms:randoms,sourceDatas:sourceDatas,caCodings:caCodings}, function(result){
            datatable.draw();
            showBatchSubmitResult(result);
        },'JSON');
    });
}

function showBatchSubmitResult(result) {
    if (paymentLimitAlert.isBatchResult(result)) {
        parent.layer.alert(paymentLimitAlert.batchContent(result), paymentLimitAlert.batchAlertOptions());
        return;
    }
    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
}

/**限额begin*/
// 单位限额
function limitCorp(data,msg){
    var cloneData = data.slice()
    var rowData = data.splice(0, data.length);
    var newArr = arrayConversionForCorp(rowData);
    var noticeBankAcc1 = [];//日累计
    var noticeBankAcc2 = [];//月累计
    for(var i=0;i<newArr.length;i++){
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limitForCorp",
            method: 'GET',
            async: false,
            data: {curId: newArr[i].curId, payMoney: newArr[i].payMoney, corpId: newArr[i].corpId,wishPayDate: newArr[i].wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        if (result=== 1) {
            noticeBankAcc1.push(newArr[i]);
        } else if(result === 2){
            noticeBankAcc2.push(newArr[i]);
        }
    }
    if(noticeBankAcc1.length>0){
        noticeBankAcc1 = arrayFilterForCorp(noticeBankAcc1);
        var bankAcc1='';
        for(var i =0;i<noticeBankAcc1.length;i++){
            bankAcc1+=noticeBankAcc1[i].corpName+"，";
        }
        bankAcc1=bankAcc1.substring(0,bankAcc1.length-1)
        msg+= bt.lang("alert.over.daylimit.msg.corp")+'<br>'+bankAcc1 +'<br><br>';
    }
    if(noticeBankAcc2.length>0){
        noticeBankAcc2 = arrayFilterForCorp(noticeBankAcc2);
        noticeBankAcc2 = arrayExcludeForCorp(noticeBankAcc2,noticeBankAcc1);
        var bankAcc2='';
        for(var i =0;i<noticeBankAcc2.length;i++){
            bankAcc2+=noticeBankAcc2[i].corpName+"，";
        }
        if(bankAcc2 != null && bankAcc2 != '' && bankAcc2.length!=0){
            bankAcc2=bankAcc2.substring(0,bankAcc2.length-1)
            msg+= bt.lang("alert.over.monthlimit.msg.corp")+'<br>'+bankAcc2+'<br><br>';
        }
    }
    if(msg.length>0){
        msg+= bt.lang("billPool.isContinue")
        parent.layer.confirm(msg, {icon: 3, title:bt.lang('fbs.budget.input.message.info'),area: ['500px','300px']},function (index) {
            //原批量提交代码逻辑
            submit(cloneData);
        });
    }else{
        submit(cloneData);
    }
}

//账号限额
function limitBankAcc(data){
    var cloneData = data.slice()
    var rowData = data.splice(0, data.length);
    var noticeBankAcc1 = [];//单次付款校验的账号
    for(var i=0;i<rowData.length;i++){
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limit",
            method: 'GET',
            async: false,
            data: {curId: rowData[i].curId, payMoney: rowData[i].payMoney, payerAcc: rowData[i].payerAcc,wishPayDate: rowData[i].wishPayday},
            dataType: 'json',
            success: function (data) {
                result = data.obj;
            }
        });
        if (result=== 1) {
            noticeBankAcc1.push(rowData[i]);
        }
    }
    noticeBankAcc1 = arrayConversion(noticeBankAcc1);
    var newArr = arrayConversion(rowData);

    var noticeBankAcc2 = [];//日累计
    var noticeBankAcc3 = [];//月累计
    for(var i=0;i<newArr.length;i++){
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limit",
            method: 'GET',
            async: false,
            data: {curId: newArr[i].curId, payMoney: newArr[i].payMoney, payerAcc: newArr[i].payerAcc,wishPayDate: newArr[i].wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        if (result=== 2) {
            noticeBankAcc2.push(newArr[i]);
        } else if(result === 3){
            noticeBankAcc3.push(newArr[i]);
        }
    }
    var msg='' ;
    if(noticeBankAcc1.length>0){
        noticeBankAcc1 = arrayFilterForBankAcc(noticeBankAcc1);
        var bankAcc1='';
        for(var i =0;i<noticeBankAcc1.length;i++){
            bankAcc1+=noticeBankAcc1[i].payerAcc+"，";
        }
        bankAcc1=bankAcc1.substring(0,bankAcc1.length-1)
        msg+= bt.lang("alert.over.paylimit.msg")+'<br>'+bankAcc1 +'<br><br>';
    }
    if(noticeBankAcc2.length>0){
        noticeBankAcc2 = arrayFilterForBankAcc(noticeBankAcc2);
        noticeBankAcc2 = arrayExcludeForBankAcc(noticeBankAcc2,noticeBankAcc1);
        var bankAcc2='';
        for(var i =0;i<noticeBankAcc2.length;i++){
            bankAcc2+=noticeBankAcc2[i].payerAcc+"，";
        }
        if(bankAcc2 != null && bankAcc2 != '' && bankAcc2.length!=0){
            bankAcc2=bankAcc2.substring(0,bankAcc2.length-1)
            msg+= bt.lang("alert.over.daylimit.msg")+'<br>'+bankAcc2+'<br><br>';
        }
    }
    if(noticeBankAcc3.length>0){
        noticeBankAcc3 = arrayFilterForBankAcc(noticeBankAcc3);
        noticeBankAcc3 = arrayExcludeForBankAcc(noticeBankAcc3,noticeBankAcc1);
        noticeBankAcc3 = arrayExcludeForBankAcc(noticeBankAcc3,noticeBankAcc2);
        var bankAcc3='';
        for(var i =0;i<noticeBankAcc3.length;i++){
            bankAcc3+=noticeBankAcc3[i].payerAcc+"，";
        }
        if(bankAcc3 != null && bankAcc3 != '' && bankAcc3.length!=0){
            bankAcc3=bankAcc3.substring(0,bankAcc3.length-1)
            msg+= bt.lang("alert.over.monthlimit.msg")+'<br>'+bankAcc3+'<br><br>';
        }
    }
    limitCorp(cloneData,msg);
}

//过滤数组
function arrayConversion(array){
    var temp= {}
    var newArr = [];
    for(var i = 0; i < array.length; i++){
        //获取单位本位币换算汇率
        var result = 0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/standardRate",
            method: 'GET',
            async: false,
            data: {curId: array[i].curId, corpId: array[i].corpId},
            dataType: 'json',
            success: function (data) {
                result = bt.money.mul(array[i].payMoney,data,2)
            }
        });
        result = parseFloat(result);

        var rData = array[i];
        if(!temp[rData.payerAcc+rData.wishPayday]){
            newArr.push({
                payerAcc: rData.payerAcc,
                payMoney: result,
                wishPayday: rData.wishPayday
            });
            temp[rData.payerAcc+rData.wishPayday] = rData;
        }else{
            for(var j = 0; j < newArr.length; j++){
                var sum = newArr [j];
                if((sum.payerAcc+sum.wishPayday) === (rData.payerAcc+rData.wishPayday)){
                    sum.payMoney=(parseFloat(sum.payMoney) + result).toString();
                    break;
                }
            }
        }
    };
    return newArr;
}

function arrayConversionForCorp(array){
    var temp= {}
    var newArr = [];
    for(var i = 0; i < array.length; i++){
        //获取单位本位币换算汇率
        var result = 0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/standardRate",
            method: 'GET',
            async: false,
            data: {curId: array[i].curId, corpId: array[i].corpId},
            dataType: 'json',
            success: function (data) {
                result = bt.money.mul(array[i].payMoney,data,2)
            }
        });
        result = parseFloat(result);

        var rData = array[i];
        if(!temp[rData.corpName+rData.wishPayday]){
            newArr.push({
                corpName: rData.corpName,
                payMoney : result,
                corpId : rData.corpId,
                wishPayday: rData.wishPayday
            });
            temp[rData.corpName+rData.wishPayday] = rData;
        }else{
            for(var j = 0; j < newArr.length; j++){
                var sum = newArr [j];
                if((sum.corpName+sum.wishPayday) === (rData.corpName+rData.wishPayday)){
                    sum.payMoney=(parseFloat(sum.payMoney) + result).toString();
                    break;
                }
            }
        }
    };
    return newArr;
}

function arrayFilterForCorp(array){
    var temp= {}
    var newArr = [];
    for(var i = 0; i < array.length; i++){
        var rData = array[i];
        if(!temp[rData.corpName]){
            newArr.push(rData);
            temp[rData.corpName] = rData.corpName;
        }
    };
    return newArr;
}
function arrayExcludeForCorp(array1,array2){
    var newArr = [];
    for(var i = 0; i < array1.length; i++){
        var flag = 0;
        for(var j = 0; j < array2.length; j++){
            if(array1[i].corpName !== array2[j].corpName){
                flag++;
            }
        }
        if(array2.length === 0 || flag === array2.length){
            newArr.push(array1[i]);
        }
    }
    return newArr;
}
function arrayFilterForBankAcc(array){
    var temp= {}
    var newArr = [];
    for(var i = 0; i < array.length; i++){
        var rData = array[i];
        if(!temp[rData.payerAcc]){
            newArr.push(rData);
            temp[rData.payerAcc] = rData.payerAcc;
        }
    };
    return newArr;
}
function arrayExcludeForBankAcc(array1,array2){
    var newArr = [];
    for(var i = 0; i < array1.length; i++){
        var flag = 0;
        for(var j = 0; j < array2.length; j++){
            if(array1[i].payerAcc !== array2[j].payerAcc){
                flag++;
            }
        }
        if(array2.length === 0 || flag === array2.length){
            newArr.push(array1[i]);
        }
    }
    return newArr;
}
/**限额end*/
