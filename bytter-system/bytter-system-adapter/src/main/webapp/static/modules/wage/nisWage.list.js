var datatable;
bt.use(['form','datatable','select','dateType','corp','money','moneyType','dicts','btImport','bank','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.selectType.init();
    bt.bank.init({
        elem:'.bank',
    });

    //根据是否挂 计划 选择不同的模板
    var importCode = "10000010";
    if ($("#budgetSel").val() === "1") {
        importCode = "100000101";
    }
    importExcel = function () {
        up = bt.btImport.init({
            code: importCode,
        	width: "100%",
            height: "100%",
        	titleRows: "0,2",
            headRows: "1,1",
            endRows: "1,0",
            success: function (head,data,select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type:"POST",
                    url: basePath + "/wage/niswage/saveWageImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    success:function(result){
                        bt.loading.hide();
                        $('.import_excel_common #data_tab .dataTables_scrollBody table').DataTable().destroy(true);
                        $('.import_excel_common #error_data_tab .dataTables_scrollBody table').DataTable().destroy(true);
                        layer.closeAll();
                    	bt.datatable.refreshCurrentPage("appTable");
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                    }
                });
            }
        });
    }

    importExcelBatch = function () {
        up = bt.btImport.init({
            code: "10000011",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            autoCloseWindown:false,
            success: function (head,data,select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type:"POST",
                    url: basePath + "/wage/niswage/saveWageImportBatch",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    success:function(result){
                        bt.loading.hide();
                        $('.import_excel_common #data_tab .dataTables_scrollBody table').DataTable().destroy(true);
                        $('.import_excel_common #error_data_tab .dataTables_scrollBody table').DataTable().destroy(true);
                        layer.closeAll();
                    	bt.datatable.refreshCurrentPage("appTable");
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                    }
                });
            }
        });
    }

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
            'url': basePath+"/wage/niswage/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:wage:nisWage:touchApply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function ( e, dt, node, config ) {
                	 var data = dt.rows('.selected').data();
                	 submitBatch(data);
                }
            },{
                hide:bt.isnotResource('netbank:wage:nisWage:touchApply'),
                text: bt.lang('common.button.batchDelete'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                	deleteBatch(data);
                }
            },{
                hide:bt.isnotResource('netbank:wage:nisWage:touchApply'),
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                	importExcel();
                }
            },{
                hide:bt.isnotResource('netbank:wage:nisWage:touchApply'),
                text: bt.lang('common.button.batch:imports'),
                action: function ( e, dt, node, config ) {
                    importExcelBatch();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        otherExport:{ // 其他导出
            text:bt.lang('nis.button.exportSelect'), // 按钮文本
            callback:function(processing){ // 其他导出执行方法
                var data = datatable.rows('.selected').data();
                exports(data);
                processing(false)
            }
        },
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data: "wageNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.status == -1){
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data:"wageTitle",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"payerAcc",bSortable:true},
            {data:"payerAccName",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"wages",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.wages);
                }
            },
            {data:"wageDay",bSortable:true,className:'center'},
            {data:"wishPayday",bSortable:true,className:'center'},
            {data:"addWord",bSortable:true},
            {data:"busTypeName",bSortable:true},
            {data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisBillhead.status", full.status);
                }
            },
            // {data:"voucherStat",bSortable:true,className:'center',
            //     render: function ( data, type, full, meta ) {
            //     	//高亮显示交易失败的明细
            //     	if(full.voucherStat == "2" || full.voucherStat == "9"){
            //     		var row = datatable.row(meta.row).node();
            //     		$(row).addClass("remind");
            //     	}
            //     	return bt.dict("bisExc.voucherStat",full.voucherStat);
            //     }
            // },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('netbank:wage:nisWage:touchApply',full.corpId)){
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                }
            	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	// html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ],
        summary:{
            pageSum:false,
            allSum:true,
            selectSum:true,
            format:{
            	decimal:2
            },
            sumList:[
                {
                    colNum:'7',
                    dataName:'wages'
                }
            ]
        }
    });
    //下拉框
    $.each($("div[type='list']"),function(i,item){
        bt.select.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });
});

/**
 * 导入功能
 */
exports = function (data) {
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
    var url = basePath + "/wage/niswage/exportDtl?ids=" + ids + "&opType=list";
    window.location.href = url;
};

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/wage/niswage/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/wage/niswage/view/" + data.id
    });
}
function update(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/wage/niswage/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/wage/niswage/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var chargeBackType = $("select[name='eqs_chargeBackType']").val();
    if(data.erpId != null && chargeBackType.trim() == ""){
        layer.alert(bt.lang('common.message.not.chargeBackType'), {icon: 7});
        return;
    }else{
        if(chargeBackType.trim() == ""){
            //默认不允许为空
            chargeBackType = "-1";
        }
    }
    //校验工资单是否存在已成功的明细
    var returnWageNo = "";
    $.ajax({
        url: basePath + "/wage/niswage?validSuccessWageDetail",
        method: 'POST',
        async: false,
        data: {
            ids: data.id
        },
        dataType: 'json',
        success: function (data) {
            returnWageNo = data.obj;
        }
    });
    if(returnWageNo){
        parent.layer.confirm(bt.lang('niswage.message.exist.success.dtl', [returnWageNo]),function(){
            $.post(basePath + "/wage/niswage/delete/" + data.id+"/" + chargeBackType, function(result){
                datatable && datatable.draw();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
            },'JSON');
        });
    }else {
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/wage/niswage/delete/" + data.id + "/" + chargeBackType, function (result) {
                datatable && datatable.draw();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
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
    var chargeBackType = $("select[name='eqs_chargeBackType']").val();
 	for(var i=0;i<rowData.length;i++){
        if(rowData[i].erpId != null && chargeBackType.trim() == ""){
            layer.alert(bt.lang('common.message.not.chargeBackType'), {icon: 7});
            return;
        }
 		if (ids == "") {
			ids += rowData[i].id;
		} else {
			ids += "," + rowData[i].id;
		}
 	}
    //校验工资单是否存在已成功的明细
    var returnWageNo = "";
    $.ajax({
        url: basePath + "/wage/niswage?validSuccessWageDetail",
        method: 'POST',
        async: false,
        data: {
            ids: ids
        },
        dataType: 'json',
        success: function (data) {
            returnWageNo = data.obj;
        }
    });
    if(returnWageNo){
        parent.layer.confirm(bt.lang('niswage.message.exist.success.dtl', [returnWageNo]),function(){
            $.post(basePath + "/wage/niswage/batchDelete", {
                ids: ids,
                chargeBackType: chargeBackType
            }, function (result) {
                datatable.draw();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }else {
        parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
            $.post(basePath + "/wage/niswage/batchDelete", {
                ids: ids,
                chargeBackType: chargeBackType
            }, function (result) {
                datatable.draw();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
}

/**
 * 校验账户余额
 * @param data
 * @returns {boolean}
 */
function checkBalance(data) {
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    //代发工资总额
    var bankaccOver = $("#bankaccOver").val();//银行账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
    //付款金额大于账户余额
    var flag = false;
    //工资单号
    var billNos;
    for(var i=0;i<rowData.length;i++){
        var wages = rowData[i].wages
        var balance = rowData[i].balance
        if (wages > balance) {
            if (bankaccOver == "1") {
                flag = true;
            } else if (bankaccOver == "2") {
                bt.loading.hide();
                layer.alert("工资单号："+rowData[i].wageNo+bt.lang("netbank.message.balance"), {icon: 2});
                return false;
            }
            if (rowData.length == 1) {
                billNos = rowData[i].wageNo;
            } else {
                if (isEmpty(billNos)) {
                    billNos += rowData[i].wageNo;
                } else {
                    billNos += "," + rowData[i].wageNo;
                }
            }
        }
    }
    if (flag) {
        bt.loading.hide();
        parent.layer.confirm("工资单号："+billNos+bt.lang('netbank.message.confirm.balance'), function (index) {
            bt.loading.show();
            submit(rowData);
            bt.loading.hide();
        });
    } else {
        submit(rowData);
    }
}

/**判断字符串是否为空*/
function isEmpty(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == ""){
        return true;
    }
    return false;
}

//批量提交
function submitBatch(data){
    if($("#isOpenLimit").val()=="1") {
        limitBankAcc(data);
    }else {
        checkBalance(data);
    }
}

function submit(rowData){
    var ids="";
    for(var i=0;i<rowData.length;i++){
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    //证书签名
    var signDataMsg = signData("代发工资批量提交", 7);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'),function(){
        $.post(basePath + "/wage/niswage/batchSubmit",{ids:ids}, function(result){
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
            data: {curId: newArr[i].curId, payMoney: newArr[i].wages, corpId: newArr[i].corpId,wishPayDate: newArr[i].wishPayday},
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
            checkBalance(cloneData);
        });
    }else{
        checkBalance(cloneData);
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
            data: {curId: rowData[i].curId, payMoney: rowData[i].wages, payerAcc: rowData[i].payerAcc,wishPayDate: rowData[i].wishPayday},
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
            data: {curId: newArr[i].curId, payMoney: newArr[i].wages, payerAcc: newArr[i].payerAcc,wishPayDate: newArr[i].wishPayday},
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
                result = bt.money.mul(array[i].wages,data,2)
            }
        });
        result = parseFloat(result);

        var rData = array[i];
        if(!temp[rData.payerAcc+rData.wishPayday]){
            newArr.push({
                payerAcc: rData.payerAcc,
                wages: result,
                wishPayday: rData.wishPayday
            });
            temp[rData.payerAcc+rData.wishPayday] = rData;
        }else{
            for(var j = 0; j < newArr.length; j++){
                var sum = newArr [j];
                if(sum.payerAcc+sum.wishPayday === rData.payerAcc+rData.wishPayday){
                    sum.wages=(parseFloat(sum.wages) + result).toString();
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
                result = bt.money.mul(array[i].wages,data,2)
            }
        });
        result = parseFloat(result);

        var rData = array[i];
        if(!temp[rData.corpName+rData.wishPayday]){
            newArr.push({
                corpName: rData.corpName,
                wages : result,
                corpId : rData.corpId,
                wishPayday: rData.wishPayday
            });
            temp[rData.corpName+rData.wishPayday] = rData;
        }else{
            for(var j = 0; j < newArr.length; j++){
                var sum = newArr [j];
                if(sum.corpName+sum.wishPayday === rData.corpName+rData.wishPayday){
                    sum.wages=(parseFloat(sum.wages) + result).toString();
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
