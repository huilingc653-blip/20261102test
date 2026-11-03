var datatable;
var dateFun;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'btImport', 'bank', 'selectType', 'element'], function () {
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
    //根据是否挂 计划 选择不同的模板
    var importCode = "110000050";
    if ($("#budgetSel").val() === "1") {
        importCode = "110000051";
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
                    url: basePath + "/ttpay/tpPayment/saveImport",
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
        order: [[1, 'asc'], [13, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/ttpay/tpPayment/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('ttpay:tpPayment:add'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('ttpay:tpPayment:submitBatch'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    //校验黑名单
                    corpCheck(data);
                    // submitBatch(data);
                    // limitBankAcc(data);
                }
            }, {
                hide: bt.isnotResource('ttpay:tpPayment:delete'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            }, {
                hide: bt.isnotResource('ttpay:tpPayment:add'),
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
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
            {data: "busTitle", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "payerAcc", bSortable: true},
            {data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }},
            {data: "payeeAccName", bSortable: true,},
            {data: "payeeAcc", bSortable: true},
            {data: "wishPayday", bSortable: true, className: 'center'},
            {data: "payTypeName", bSortable: true, className: 'center'},
            {data: "statusName", bSortable: true, className: 'center'},
            {data: "isForindividualName", bSortable: true, className: 'center'},
            {data: "createDate", bSortable: true, className: 'center'},
            {data: "busTypeName", bSortable: true, className: 'center'},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('ttpay:tpPayment:update', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    }
                    if (bt.isResource('ttpay:tpPayment:delete', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }

                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: true,
            groupColumn:'',
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
            var exportUrl = basePath + "/ttpay/tpPayment/exportAll";
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


function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/ttpay/tpPayment/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/tpPayment/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/ttpay/tpPayment/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/tpPayment/view/" + data.id
    });
}


function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/ttpay/tpPayment/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/ttpay/tpPayment/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var chargeBackType = $("select[name='eqs_chargeBackType']").val();
    if (data.billFrom == "6" && chargeBackType.trim() == "") {
        layer.alert(bt.lang('common.message.not.chargeBackType'), {icon: 7});
        return;
    } else {
        if (chargeBackType.trim() == "") {
            //默认不允许为空
            chargeBackType = "-1";
        }
    }

    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/ttpay/tpPayment/delete/" + data.id + "/" + chargeBackType, function (result) {
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
    var chargeBackType = $("select[name='eqs_chargeBackType']").val();

    for (var i = 0; i < rowData.length; i++) {
        if (rowData[i].billFrom == "6" && chargeBackType.trim() == "") {
            layer.alert(bt.lang('common.message.not.chargeBackType'), {icon: 7});
            return;
        }
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }

    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/ttpay/tpPayment/batchDelete", {
            ids: ids,
            chargeBackType: chargeBackType
        }, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量提交
function submitBatch(rowData) {
    var ids = "";
    var randoms = '';
    var sourceDatas = '';
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

    }
    if (billNos != "") {
        billNos = billNos.substring(0,(billNos.length-1));
        layer.confirm(bt.lang('nis.message.wishpayday.pass',[billNos]), {icon: 3}, function () {
            rePay(ids, randoms, sourceDatas);
        });
    } else {
        rePay(ids, randoms, sourceDatas);
    }
    return false;
}

/**限额begin*/
//单位限额
function limitCorp(rowData){
    var msg='';
    var newArr = arrayConversionForPayerAcc(rowData);//将所有单位数据进行合并，计算金额总和
    var noticeBankAcc1 = [];//日单笔
    var noticeBankAcc2 = [];//日累计
    var noticeBankAcc3 = [];//月累计

    //依次遍历每个单位
    for(var i=0;i<newArr.length;i++){
        var result=0;
        $.ajax({
            url: basePath + "/ttpay/tpPayment/limitForMerchant",
            method: 'GET',
            async: false,
            data: {corpId: newArr[i].corpId, merchantNo: newArr[i].payerAcc,payMoney: newArr[i].payMoney,payType:newArr[i].payType,wishPayDate: newArr[i].wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        //1：日单笔；2：日累计；3：月累计
        if (result=== 1) {
            noticeBankAcc1.push(newArr[i]);
        } else if(result === 2){
            noticeBankAcc2.push(newArr[i]);
        } else if(result === 3){
            noticeBankAcc3.push(newArr[i]);
        }
    }
    if(noticeBankAcc1.length>0){
        noticeBankAcc1 = arrayFilterForCorp(noticeBankAcc1);//单位去重
        var bankAcc1='';
        for(var i =0;i<noticeBankAcc1.length;i++){
            bankAcc1+=noticeBankAcc1[i].payerAcc+"，";
        }
        bankAcc1=bankAcc1.substring(0,bankAcc1.length-1)
        msg+= bt.lang("alert.over.paylimit.msg")+'<br>'+bankAcc1 +'<br><br>';
    }
    if(noticeBankAcc2.length>0){
        noticeBankAcc2 = arrayFilterForCorp(noticeBankAcc2);//单位去重
        noticeBankAcc2 = arrayExcludeForCorp(noticeBankAcc2,noticeBankAcc1);//如果该单位已存在日累计，则排除该单位于月累计
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
        noticeBankAcc3 = arrayFilterForCorp(noticeBankAcc3);//单位去重
        noticeBankAcc3 = arrayExcludeForCorp(noticeBankAcc3,noticeBankAcc2);//如果该单位已存在日累计，则排除该单位于月累计
        noticeBankAcc3 = arrayExcludeForCorp(noticeBankAcc3,noticeBankAcc1);//如果该单位已存在日累计，则排除该单位于月累计
        var bankAcc3='';
        for(var i =0;i<noticeBankAcc3.length;i++){
            bankAcc3+=noticeBankAcc3[i].payerAcc+"，";
        }
        if(bankAcc3 != null && bankAcc3 != '' && bankAcc3.length!=0){
            bankAcc3=bankAcc3.substring(0,bankAcc3.length-1)
            msg+= bt.lang("alert.over.monthlimit.msg")+'<br>'+bankAcc3+'<br><br>';
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

    if($("#isOpenLimit").val()=="1") {
        limitCorp(rowData);
    }else{
        submitBatch(rowData);
    }
}

//过滤数组与数组分类-单位
function arrayConversionForPayerAcc(array){
    var temp= {}
    var newArr = [];
    for(var i = 0; i < array.length; i++){
        //获取单位本位币换算汇率
        var result = parseFloat(array[i].payMoney);
        var rData = array[i];
        if(!temp[rData.payerAcc+rData.wishPayday]){
            newArr.push({
                corpName: rData.corpName,
                corpId: rData.corpId,
                payerAcc: rData.payerAcc,
                payMoney : result,
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

//重复付款校验
function rePay(ids, randoms, sourceDatas) {
    bt.loading.show();
    var result = false;
    var msg = "";
    $.ajax({
        url: basePath + "/ttpay/tpPayment?batchRePay",
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
                bt.loading.show();
                $.post(basePath + "/ttpay/tpPayment/batchSubmit", {
                    ids: ids,
                    randoms: randoms,
                    sourceDatas: sourceDatas
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
            bt.loading.show();
            $.post(basePath + "/ttpay/tpPayment/batchSubmit", {
                ids: ids,
                randoms: randoms,
                sourceDatas: sourceDatas
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

//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}
