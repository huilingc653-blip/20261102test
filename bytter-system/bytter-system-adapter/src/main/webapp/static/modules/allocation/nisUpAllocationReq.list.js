var datatable;
var dateFun;
bt.use(['form', 'datatable', 'corp', 'accountTable', 'dicts', 'moneyType','dateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.moneyType.init();
    accountCallBack = function (data) {
        $("#likes_payeeAcc").val(data.bankAcc);
    }
    dateFun = bt.dateType.getFun();
    var accountTable = bt.accountTable.init();
    // 选择单位回调
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_payeeAcc").setCorp(id);
    }
    bt.corp.init();

    var htmlx =
        '<tr class="title">'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.billNo')+'</th>'+
            '<th colspan="4">'+bt.lang('table.nisAllocationReq.payerAcc')+'</th>'+
            '<th colspan="4">'+bt.lang('table.nisAllocationReq.payeeAcc')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.curId')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.wishPayday')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.purpose')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.up.money')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.createBy')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.createDate')+'</th>'+
            '<th rowspan="2">'+bt.lang('table.nisAllocationReq.status')+'</th>'+
        '</tr>'+
        '<tr class="title">'+
            '<th>'+bt.lang('table.nisAllocationReq.acc')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.corp')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.accName')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.bankName')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.acc')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.corp')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.accName')+'</th>'+
            '<th>'+bt.lang('table.nisAllocationReq.bankName')+'</th>'+
        '</tr>';

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: false,
        checkbox: true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/allocation/nisallocationreq/datatable"
        },
        exportFile: {
            customPrint: {
                header: function () {
                    var printHtml = htmlx;
                    return printHtml;
                },
            },
        },
        // ignoreButtons: ['output'],
        buttons: [
            {
                hide: bt.isnotResource('netbank:allocation:typeUp:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('netbank:allocation:typeUp:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    batchDelete(e, dt, node, config);
                }
            }, {
                hide: bt.isnotResource('netbank:allocation:typeUp:apply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    batchSubmit(e, dt, node, config);
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
            // , {
            //     extend: 'collection',
            //     text: '导出',
            //     className: 'btn btn-sm btn-primary',
            //     buttons: [
            //         {
            //             text: '导出选中行',
            //             action: function(e, dt, node, config) {
            //                 var selectedData = dt.rows('.selected').data();
            //                 if (selectedData.length === 0) {
            //                     layer.msg('请先选择要导出的数据', {icon: 0});
            //                     return;
            //                 }
            //                 var dataTablesInput = JSON.parse(dt.ajax.params());
            //                 doExport(dt, dataTablesInput, 'selected', selectedData);
            //             }
            //         },
            //         {
            //             text: '导出当前页',
            //             action: function(e, dt, node, config) {
            //                 var dataTablesInput = JSON.parse(dt.ajax.params());
            //                 doExport(dt, dataTablesInput, 'current', null);
            //             }
            //         },
            //         {
            //             text: '导出全部数据',
            //             action: function(e, dt, node, config) {
            //                 var dataTablesInput = JSON.parse(dt.ajax.params());
            //                 doExport(dt, dataTablesInput, 'all', null);
            //             }
            //         }
            //     ]
            // }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "billNo", bSortable: true,
                render: function (data, type, full, meta) {
                    if(full.status == -1){
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data: "payerAcc", bSortable: true},
            {data: "payerCorpName", bSortable: true},
            {data: "payerAccName", bSortable: true},
            {data: "payerBankName", bSortable: false},
            {data: "payeeAcc", bSortable: true},
            {data: "payeeCorpName", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {data: "payeeBankName", bSortable: false},
            {data: "curName", bSortable: true},
            {data: "wishPayday", bSortable: true},
            {data: "purpose", bSortable: true},
            {
                data: "money", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    //证书信息
                    var cfcaInfo = '<input type="hidden" id="random' + full.id + '" name="random" value=""><input type="hidden" id="encrySource' + full.id + '" name="encrySource" value=""><input type="hidden" id="caCoding' + full.id + '" name="caCoding" value="">';
                    var html = '';
                    html = cfcaInfo +bt.money.format(full.money);
                    return html;
                }
            },
            {data: "createByName", bSortable: true},
            {data: "createDate", bSortable: true},
            {
                data: "status", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("netbank.status", full.status);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:allocation:typeUp:apply')) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    return html;
                }
            }
        ],
        // summary: {
        //     allSum: true,
        //     sumList: [
        //         {
        //             colNum: '10',
        //             dataName: 'money'
        //         }
        //     ]
        // }
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('table.nisAllocationReq.btn.create.up'), basePath + "/allocation/nisallocationreq/add/10");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/allocation/nisallocationreq/add/10",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['1000px', '530px'],
        maxmin: true,
        content: basePath + "/allocation/nisallocationreq/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.nisAllocationReq.btn.update.up'), basePath + "/allocation/nisallocationreq/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/allocation/nisallocationreq/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/allocation/nisallocationreq/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 批量删除
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchDelete(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/allocation/nisallocationreq/batchDelete/", {ids: ids}, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

/**
 * 批量提交
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchSubmit(e, dt, node, config) {
    bt.loading.show();
    var data = dt.rows('.selected').data();
    if($("#isOpenLimit").val()=="1") {
        limitBankAcc(data);
    }else{
        submit(data);
    }
}

function submit(data){
    if (data.length < 1) {
        bt.loading.hide();
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var signDataMsg = "";
    var randoms = '';
    var sourceDatas = '';
    var caCodings = '';
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
        var key = rowData[i].id;
        // //数据源规则：付款账号+随机数（10位）+金额（14位）+期望支付日期+收款人+收款账号
        var sourceData = rowData[i].payerAcc;//付款账号
        var random = String(parseInt(Math.random() * 10000000000));//随机数
        while (random.length < 10) {
            random = random + '0';
        }
        $("#random" + key).val(random);
        var payMoney = rowData[i].money+"";//金额
        while (payMoney.length < 14) {
            payMoney = '0' + payMoney;
        }
        var wishPayday = dateFun.format(rowData[i].wishPayday, 'yyyyMMdd');//期望支付日期
        sourceData += random + payMoney + wishPayday +rowData[i].payeeAccName + rowData[i].payeeAcc;
        $("#encrySource" + key).val(sourceData);
        signDataMsg = batchSignData(sourceData, "caCoding" + key, "3");
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
            caCodings += $("#caCoding"+key).val();
        } else {
            caCodings += "," + $("#caCoding"+key).val();
        }
    }
    //验证银行余额
    $.post(basePath + "/allocation/nisallocationreq/checkBal/" ,{ids: ids,type: 10} ,function (result) {
        var bankaccOver = $("#bankaccOver").val();
        if(result.obj != ""){
            if(bankaccOver == "1"){
                parent.layer.confirm(result.obj, function () {
                    bt.loading.hide();
                    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
                        if($("#isCFCACheck").val() != ""){
                            bt.loading.hide();
                            layer.alert(bt.lang($("#isCFCACheck").val()), {icon: 2});
                            return false;
                        }
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
                        bt.loading.show();
                        $.post(basePath + "/allocation/nisallocationreq/batchSubmit/", {ids: ids,randoms:randoms,sourceDatas:sourceDatas,caCodings:caCodings}, function (result) {
                            bt.loading.hide();
                            bt.datatable.refreshCurrentPage();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });
                });
                bt.loading.hide();
            }else if(bankaccOver == "2"){
                bt.loading.hide();
                parent.layer.alert(result.obj, {icon: 2});
                return false;
            }
        }else{
            parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
                if($("#isCFCACheck").val() != ""){
                    bt.loading.hide();
                    layer.alert(bt.lang($("#isCFCACheck").val()), {icon: 2});
                    return false;
                }
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
                bt.loading.show();
                $.post(basePath + "/allocation/nisallocationreq/batchSubmit/", {ids: ids,randoms:randoms,sourceDatas:sourceDatas,caCodings:caCodings}, function (result) {
                    bt.loading.hide();
                    bt.datatable.refreshCurrentPage();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }, 'JSON');
            });
            bt.loading.hide();
        }
    }, 'JSON');

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
            data: {curId: newArr[i].curId, payMoney: newArr[i].money, corpId: newArr[i].payerCorpId,wishPayDate: newArr[i].wishPayday},
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
            bankAcc1+=noticeBankAcc1[i].payerCorpName+"，";
        }
        bankAcc1=bankAcc1.substring(0,bankAcc1.length-1)
        msg+= bt.lang("alert.over.daylimit.msg.corp")+'<br>'+bankAcc1 +'<br><br>';
    }
    if(noticeBankAcc2.length>0){
        noticeBankAcc2 = arrayFilterForCorp(noticeBankAcc2);
        noticeBankAcc2 = arrayExcludeForCorp(noticeBankAcc2,noticeBankAcc1);
        var bankAcc2='';
        for(var i =0;i<noticeBankAcc2.length;i++){
            bankAcc2+=noticeBankAcc2[i].payerCorpName+"，";
        }
        if(bankAcc2 != null && bankAcc2 != '' && bankAcc2.length!=0){
            bankAcc2=bankAcc2.substring(0,bankAcc2.length-1)
            msg+= bt.lang("alert.over.monthlimit.msg.corp")+'<br>'+bankAcc2+'<br><br>';
        }
    }
    if(msg.length>0){
        msg+= bt.lang("billPool.isContinue");
        bt.loading.hide();
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
            data: {curId: rowData[i].curId, payMoney: rowData[i].money, payerAcc: rowData[i].payerAcc,wishPayDate: rowData[i].wishPayday},
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
            url: basePath + "/netbank/nisbillhead/limit",
            method: 'GET',
            async: false,
            data: {curId: newArr[i].curId, payMoney: newArr[i].money, payerAcc: newArr[i].payerAcc,wishPayDate: newArr[i].wishPayday},
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
                result = bt.money.mul(array[i].money,data,2)
            }
        });
        result = parseFloat(result);

        var rData = array[i];
        if(!temp[rData.payerAcc+rData.wishPayday]){
            newArr.push({
                payerAcc: rData.payerAcc,
                money: result,
                wishPayday: rData.wishPayday
            });
            temp[rData.payerAcc+rData.wishPayday] = rData;
        }else{
            for(var j = 0; j < newArr.length; j++){
                var sum = newArr [j];
                if(sum.payerAcc+sum.wishPayday === rData.payerAcc+rData.wishPayday){
                    sum.money=(parseFloat(sum.money) + result).toString();
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
                result = bt.money.mul(array[i].money,data,2)
            }
        });
        result = parseFloat(result);

        var rData = array[i];
        if(!temp[rData.payerCorpId+rData.wishPayday]){
            newArr.push({
                money : result,
                payerCorpId : rData.payerCorpId,
                payerCorpName: rData.payerCorpName,
                wishPayday: rData.wishPayday
            });
            temp[rData.payerCorpId+rData.wishPayday] = rData;
        }else{
            for(var j = 0; j < newArr.length; j++){
                var sum = newArr [j];
                if(sum.payerCorpId+sum.wishPayday === rData.payerCorpId+rData.wishPayday){
                    sum.money=(parseFloat(sum.money) + result).toString();
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
        if(!temp[rData.payerCorpName]){
            newArr.push(rData);
            temp[rData.payerCorpName] = rData.payerCorpName;
        }
    };
    return newArr;
}
function arrayExcludeForCorp(array1,array2){
    var newArr = [];
    for(var i = 0; i < array1.length; i++){
        var flag = 0;
        for(var j = 0; j < array2.length; j++){
            if(array1[i].payerCorpName !== array2[j].payerCorpName){
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
function doExport(dt, dataTablesInput, type, selectedData) {
    bt.loading.show();
    try {
        var params = {
            dataTablesInputParam: JSON.stringify(dataTablesInput),
            exportExcelHeadListParam: '',
            totalParam: '',
            fileNameParam: 'nisDownAllocationReq',
            exportType: type,
        };

        if (type === 'selected' && selectedData && selectedData.length > 0) {
            params.selectedIds = JSON.stringify(selectedData.toArray().map(function(item) { return item.id; }));
        } else if (type === 'current') {
            var currentPageData = dt.rows({page: 'current'}).data();
            params.selectedIds = JSON.stringify(currentPageData.toArray().map(function(item) { return item.id; }));
        }

        var form = $('<form method="post" target="_blank"></form>');
        form.attr('action', basePath + '/allocation/nisallocationreq/exportAll');
        form.css('display', 'none');

        for (var key in params) {
            var input = $('<input type="hidden">');
            input.attr('name', key).val(params[key]);
            form.append(input);
        }

        $(document.body).append(form);
        form.submit();
        form.remove();

        setTimeout(function() {
            bt.loading.hide();
        }, 1000);
    } catch (e) {
        bt.loading.hide();
        layer.alert(bt.lang('common.msg.operation_failed'), {icon: 2});
        console.error('Export error:', e);
    }
}
/**限额end*/
