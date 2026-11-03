var datatable;
var dateFun;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'btImport', 'bank', 'selectType', 'element','selectTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.selectType.init();
    dateFun = bt.dateType.getFun();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });
    //付款账号
    var payerAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",}
    ];
    var payerAccInit = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath + "/netbank/nisbillhead/getPayerAccQuery",
        urldata: {resource:'netbank:netBank:nisBillHeadPool:query'},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#eqs_payerAcc",
        paging: true,
        columns: payerAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#eqs_payerAcc').val(data.bankAcc);
            } else {
                $('input.text').val('');
            }
        }
    });
    //根据是否挂 计划 选择不同的模板
    var importCode = "10000005";
    if ($("#budgetSel").val() === "1") {
        importCode = "100000051";
    }
    importExcel = function () {
        up = bt.btImport.init({
            code: importCode,
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/netbank/nisbillheadPool/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
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
        checkbox: true,
        order: [[3, 'asc'], [13, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/netbank/nisbillheadPool/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:netBank:nisBillHeadPool:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('netbank:netBank:nisBillHeadPool:apply'),
                text: bt.lang('common.button.batchSubmit.nisbillhead'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    //校验黑名单
                    corpCheck(data);
                    // submitBatch(data);
                    // limitBankAcc(data);
                }
            }, {
                hide: bt.isnotResource('netbank:netBank:nisBillHeadPool:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            }, {
                hide: $("#isEnableMerge").val() != 1 ? true : bt.isnotResource('netbank:netBank:nisBillHeadPool:apply'),
                text: bt.lang('common.button.merge'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    merge(data);
                }
            }, {
                hide: $("#isEnableMerge").val() != 1 ? true : bt.isnotResource('netbank:netBank:nisBillHeadPool:apply'),
                text: bt.lang('common.button.mergecancel'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    mergeCancel(data);
                }
            }, {
                hide: bt.isnotResource('netbank:netBank:nisBillHeadPool:apply'),
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            }, {
                hide: bt.isnotResource('netbank:netBank:nisBillHeadPool:apply'),
                text: bt.lang('common.button.batchSetPayerAcc'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSetPayerAcc(data);
                }
            },{
                hide: bt.isnotResource('netbank:netBank:nisBillHeadPool:apply'),
                text: bt.lang('table.nisPaySchedulingElements.schedulingElements'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    schedulingElements(data);
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "schedulingElementsName", bSortable: true,className: 'center'},
            {data: "schedulingDate", bSortable: true,className: 'center'},
            {
                data: "billNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if (full.status == -1) {
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data: "corpName", bSortable: true},
            {data: "voucherName", bSortable: true},
            {data: "payerAcc", bSortable: true},
            {data: "payerAccName", bSortable: true},
            {data: "payerBank", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    //证书信息
                    var cfcdatanfo = '<input type="hidden" id="random' + full.id + '" name="random" value=""><input type="hidden" id="encrySource' + full.id + '" name="encrySource" value=""><input type="hidden" id="caCoding' + full.id + '" name="caCoding" value="">';
                    var html = '';
                    html = cfcdatanfo + bt.money.format(full.payMoney);
                    return html;
                }
            },
            {data:"billTypeName",bSortable:true},
            {data:"billFlagName",bSortable:true},
            {data: "payeeAcc", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {data: "payeeBankCode", bSortable: true},
            {data: "payeeBank", bSortable: true},
            {data: "wishPayday", bSortable: true, className: 'center'},
            {
                data: "splitBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (full.splitBillNo != null && full.splitBillNo != "") {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewSplit(this)">' + full.splitBillNo + '</a>';
                    }
                    return html;
                }
            },
            {data: "busTypeName", bSortable: true},
            {data: "bursarBillNo", bSortable: true},
            {data: "purpose", bSortable: true},
            {data: "addWord", bSortable: true},
            {data: "isPayedName", bSortable: true, className: 'center', visible: setIsPayedVisible()},
            {data: "billFromName", bSortable: true, className: 'center'},
            {data: "statusName", bSortable: true, className: 'center'},
            {data: "isForindividualName", bSortable: true, className: 'center'},
            {data: "createDate", bSortable: true, className: 'center', format: ""},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:netBank:nisBillHeadPool:apply', full.corpId) && full.status != 95) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    }
                    if (bt.isResource('netbank:netBank:nisBillHeadPool:apply', full.corpId) && full.status != 95) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }
                    if ($("#isEnableSplit").val() == 1 && full.status != 95) {
                        if (bt.isResource('netbank:netBank:nisBillHeadPool:apply', full.corpId)) {
                            if (full.splitId && full.splitId != "") {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="splitCancel(this)">' + bt.lang("common.button.splitcancel") + '</a><span class="text-explode">|</span>';
                            }
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="split(this)">' + bt.lang("common.button.split") + '</a><span class="text-explode">|</span>';
                        }
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: true,
            groupColumn:'curName',
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8',
                    dataName: 'payMoney'
                }
            ]
        },
        // 暂时注释导出全部回调
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/netbank/nisbillheadPool/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        }
    });
    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
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

function setIsPayedVisible() {
    var showIsPayed = $("#showIsPayed").val();
    if (showIsPayed == "1") {
        return true;
    } else {
        return false;
    }
}

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/netbank/nisbillheadPool/add");
        return;
    }
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/netbank/nisbillheadPool/view/" + data.id);
        return;
    }

}

function viewSplit(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/netbank/nisbillheadPool/view/" + data.splitId);
        return;
    }
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/netbank/nisbillheadPool/update?id=" + data.id);
            return;
        }
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var reply = $("#reply").val();
    // var chargeBackType = $("select[name='eqs_chargeBackType']").val();
    // if (data.billFrom == "6" && chargeBackType.trim() == "") {
    //     layer.alert(bt.lang('common.message.not.chargeBackType'), {icon: 7});
    //     return;
    // } else {
    //     if (chargeBackType.trim() == "") {
    //         //默认不允许为空
    //         chargeBackType = "-1";
    //     }
    // }
    // //删除明细单据校验
    // var result = validSplitDel(data);
    // var msg = bt.lang('common.message.confirm.delete');
    // if(result){
    //     msg = bt.lang('nis.message.splitdtl.confirm.delete', [data.billNo]);
    // }
    if(reply.trim() == ""){
        layer.alert(bt.lang('common.message.not.reply'), {icon: 7});
        return;
    }
    var msg = bt.lang('common.message.confirm.reply');
    parent.layer.confirm(msg, function () {
        $.post(basePath + "/netbank/nisbillheadPool/delete/" + data.id + "/" + reply, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}


function split(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        //系统结算方式为50-应收票据，不可进行拆分
        /*if (data.sysVoucherCode == "50") {
            layer.alert(bt.lang('nis.pay.message.bill.split'), {icon: 7});
            return;
        }*/
        //单据来源为承兑汇票，不可进行拆分
        if (data.billFrom == "3") {
            layer.alert(bt.lang('nis.pay.message.billfrom.bill.split'), {icon: 7});
            return;
        }
        $.ajax({
            url: basePath + "/netbank/nisbillheadPool?isMergeBill",
            method: 'POST',
            async: false,
            data: {
                id: data.id
            },
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    layer.alert(result.msg, {icon: 2});
                    return;
                }
                if (bt.navtab) {
                    bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/netbank/nisbillheadPool/split?id=" + data.id);
                    return;
                }
            }
        });
    }
}

function splitCancel(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.splitcancel'), function () {
        $.post(basePath + "/netbank/nisbillheadPool/splitCancel/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量删除
function deleteBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    var reply = $("#reply").val();
    for (var i = 0; i < rowData.length; i++) {
        if (reply.trim() == "") {
            layer.alert(bt.lang('common.message.not.reply'), {icon: 7});
            return;
        }
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    var msg = bt.lang('common.message.confirm.reply');
    parent.layer.confirm(msg, function () {
        $.post(basePath + "/netbank/nisbillheadPool/batchDelete", {
            ids: ids,
            reply: reply
        }, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量提交
function submitBatch(rowData) {
    var ids = "";
    var signDataMsg = "";
    var randoms = '';
    var sourceDatas = '';
    var caCodings = '';
    var billNos = "";
    //当前日期
    var curDate = new Date();
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }

        //判断期望支付日期（根据支付结算参数是否控制期望支付日期）
        if (dateFun.compare(rowData[i].wishPayday, curDate) > 0) {
            billNos = billNos + rowData[i].billNo + ",";
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
            sourceData += random + payMoney + wishPayday + rowData[i].payeeAccName + rowData[i].payeeAcc;
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
    if (billNos != "") {
        billNos = billNos.substring(0,(billNos.length-1));
        layer.confirm(bt.lang('nis.message.wishpayday.pass',[billNos]), {icon: 3}, function () {
            rePay(ids, randoms, sourceDatas, caCodings, rowData);
        });
    } else {
        rePay(ids, randoms, sourceDatas, caCodings, rowData);
    }
    return false;
}

/**限额begin*/
//单位限额
function limitCorp(rowData,msg){
    var newArr = arrayConversionForCorp(rowData);//将所有单位数据进行合并，计算金额总和
    var noticeBankAcc1 = [];//日累计
    var noticeBankAcc2 = [];//月累计

    //依次遍历每个单位
    for(var i=0;i<newArr.length;i++){
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillheadPool/limitForCorp",
            method: 'GET',
            async: false,
            data: {curId: newArr[i].curId, payMoney: newArr[i].payMoney, corpId: newArr[i].corpId,wishPayDate: newArr[i].wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        //1：日累计；2：月累计
        if (result=== 1) {
            noticeBankAcc1.push(newArr[i]);
        } else if(result === 2){
            noticeBankAcc2.push(newArr[i]);
        }
    }
    if(noticeBankAcc1.length>0){
        noticeBankAcc1 = arrayFilterForCorp(noticeBankAcc1);//单位去重
        var bankAcc1='';
        for(var i =0;i<noticeBankAcc1.length;i++){
            bankAcc1+=noticeBankAcc1[i].corpName+"，";
        }
        bankAcc1=bankAcc1.substring(0,bankAcc1.length-1)
        msg+= bt.lang("alert.over.daylimit.msg.corp")+'<br>'+bankAcc1 +'<br><br>';
    }
    if(noticeBankAcc2.length>0){
        noticeBankAcc2 = arrayFilterForCorp(noticeBankAcc2);//单位去重
        noticeBankAcc2 = arrayExcludeForCorp(noticeBankAcc2,noticeBankAcc1);//如果该单位已存在日累计，则排除该单位于月累计
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
            submitBatch(rowData);
        });
    }else{
        submitBatch(rowData);
    }
}

//校验黑白名单
function corpCheck(res){
    var len = res.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = res.splice(0, len);
    var params=[];
    for (var i = 0; i < rowData.length; i++) {
        var param = {};
        param.name = rowData[i].payeeAccName;
        param.appNo = rowData[i].billNo;
        param.corpId = rowData[i].corpId;
        params.push(param);
    }
    var data = {};
    data.params = params;
    //对黑白名单进行验证
    var reData=externalCorpCheckBatch(data);
    if(reData.reCode=='3'){
        bt.loading.hide();
        parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});
        return;
    }else if(reData.reCode=='2'){
        bt.loading.hide();
        parent.layer.confirm(reData.msg,function(){
            if($("#isOpenLimit").val()=="1") {
                limitBankAcc(rowData);
            }else{
                submitBatch(rowData);
            }
        });
    }else{
        if($("#isOpenLimit").val()=="1") {
            limitBankAcc(rowData);
        }else {
            submitBatch(rowData);
        }
    }
}
//账号限额
function limitBankAcc(rowData){
    var noticeBankAcc1 = [];//单次付款校验的账号
    for(var i=0;i<rowData.length;i++){
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillheadPool/limit",
            method: 'GET',
            async: false,
            data: {curId: rowData[i].curId, payMoney: rowData[i].payMoney, payerAcc: rowData[i].payerAcc,wishPayDate: rowData[i].wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
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
            url: basePath + "/netbank/nisbillheadPool/limit",
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
    limitCorp(rowData,msg);
}

//过滤数组与数组分类-账号
function arrayConversion(array){
    var temp= {}
    var newArr = [];
    for(var i = 0; i < array.length; i++){
        //获取单位本位币换算汇率
        var result = 0;
        $.ajax({
            url: basePath + "/netbank/nisbillheadPool/standardRate",
            method: 'GET',
            async: false,
            data: {curId: array[i].curId, corpId: array[i].corpId},
            dataType: 'json',
            success: function (data) {
                result = bt.money.mul(array[i].payMoney,data,2)
            }
        });
        result = parseFloat(result);

        //开始分离
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
                if(sum.payerAcc+sum.wishPayday === rData.payerAcc+rData.wishPayday){
                    sum.payMoney=(parseFloat(sum.payMoney) + result).toString();
                    break;
                }
            }
        }
    };
    return newArr;
}
//过滤数组与数组分类-单位
function arrayConversionForCorp(array){
    var temp= {}
    var newArr = [];
    for(var i = 0; i < array.length; i++){
        //获取单位本位币换算汇率
        var result = 0;
        $.ajax({
            url: basePath + "/netbank/nisbillheadPool/standardRate",
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
                if(sum.corpName+sum.wishPayday === rData.corpName+rData.wishPayday){
                    sum.payMoney=(parseFloat(sum.payMoney) + result).toString();
                    break;
                }
            }
        }
    };
    return newArr;
}

//单位数组去重
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
//单位数组排除
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
//账号去重
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
//账号数组排除
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

//批量选择付款账号
function batchSetPayerAcc(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var corpId = "";
    var voucherType = "";
    var editPayerAcc = $("#editPayerAcc").val();
    for (var i = 0; i < rowData.length; i++) {
        //校验付款单位是否一致
        if (corpId == "") {
            corpId = rowData[i].corpId;
        } else {
            if (corpId != rowData[i].corpId) {
                layer.alert(bt.lang('nis.setpayeracc.message.corp.same'), {icon: 7});
                return false;
            }
        }

        //校验付款方式是否一致
        if (voucherType == "") {
            voucherType = rowData[i].voucherTypeId;
        } else {
            if (voucherType != rowData[i].voucherTypeId) {
                layer.alert(bt.lang('nis.setpayeracc.message.vouchertype.same'), {icon: 7});
                return false;
            }
        }

        //校验是否有erp且不允许编辑金额的数据
        if(editPayerAcc == "0"){
            billFrom = rowData[i].billFrom;
            if(billFrom == "6"){
                layer.alert(bt.lang('nis.setpayeracc.message.billfrom.erp'), {icon: 7});
                return false;
            }
        }
    }
    layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillheadPool/toSetPayerAcc/" + corpId + "/" + voucherType
    });
}

function schedulingElements(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var id = "";
    var schedulingElementsId = $("select[name='schedulingElementsId']").val();
    if(schedulingElementsId.trim() == ""){
        layer.alert(bt.lang('common.message.confirm.selectData.schedulingElements'), {icon: 7});
        return;
    }
    for (var i = 0; i < rowData.length; i++) {
        if(i==0){
            id = rowData[i].id;
        }else{
            id += ","+rowData[i].id;
        }
    }
    $.ajax({
        url: basePath + "/netbank/nisbillheadPool?schedulingElements",
        method: 'POST',
        async: false,
        data: {
            ids: id,
            schedulingElementsId:schedulingElementsId
        },
        dataType: 'json',
        success: function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }
    });
}

//重复付款校验
function rePay(ids, randoms, sourceDatas, caCodings, rowData) {
    bt.loading.show();
    var result = false;
    var msg = "";
    $.ajax({
        url: basePath + "/netbank/nisbillheadPool?batchRePay",
        method: 'POST',
        async: false,
        data: {
            ids: ids
        },
        dataType: 'json',
        success: function (data) {
            result = data.success;
            msg = "1" == data.obj ? "netbank.message.confirm.repay" : "netbank.message.confirm.repay.temp";
        }
    });
    if (result) {
        bt.loading.hide();
        parent.layer.confirm(bt.lang(msg), function (index) {
            parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
                if (caCodings == "") {
                    for (var i = 0; i < rowData.length; i++) {
                        var key = rowData[i].id;
                        if (caCodings == "") {
                            caCodings += $("#caCoding" + key).val();
                        } else {
                            caCodings += "," + $("#caCoding" + key).val();
                        }
                    }
                }
                bt.loading.show();
                $.post(basePath + "/netbank/nisbillheadPool/batchSubmit", {
                    ids: ids,
                    randoms: randoms,
                    sourceDatas: sourceDatas,
                    caCodings: caCodings
                }, function (result) {
                    bt.loading.hide();
                    var icon = result.success ? (result.attributes ? result.attributes.succIcon : 1) : 2;
                    datatable.draw();
                    parent.layer.alert(result.msg, {icon: icon});
                }, 'JSON');
            });
        });
    } else {
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
            if (caCodings == "") {
                for (var i = 0; i < rowData.length; i++) {
                    var key = rowData[i].id;
                    if (caCodings == "") {
                        caCodings += $("#caCoding" + key).val();
                    } else {
                        caCodings += "," + $("#caCoding" + key).val();
                    }
                }
            }
            bt.loading.show();
            $.post(basePath + "/netbank/nisbillheadPool/batchSubmit", {
                ids: ids,
                randoms: randoms,
                sourceDatas: sourceDatas,
                caCodings: caCodings
            }, function (result) {
                bt.loading.hide();
                var icon = result.success ? (result.attributes ? result.attributes.succIcon : 1) : 2;
                datatable.draw();
                parent.layer.alert(result.msg, {icon: icon});
            }, 'JSON');
        });
    }
    return false;
}


//合并
function merge(data) {
    var len = data.length;
    if (len < 2) {
        layer.alert(bt.lang('netbank.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.merge'), function () {
        $.post(basePath + "/netbank/nisbillheadPool/merge", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//撤销合并
function mergeCancel(data) {
    var len = data.length;
    if (len < 1) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.mergecancel'), function () {
        $.post(basePath + "/netbank/nisbillheadPool/mergeCancel", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}