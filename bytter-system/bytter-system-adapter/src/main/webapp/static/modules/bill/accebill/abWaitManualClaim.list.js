var billTable={},accTbale={},dicts,selectType,corp,moneyType;
bt.use(['form','datatable','moneyType','btdate','corp','select','dateType','dicts','selectType','mulselect','rateType','ratioType'], function(){
    $ = bt.jquery,form = bt.form();
    corp = bt.corp.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    dicts=bt.dicts.init();
    bt.mulselect.init();

    //银行流水列表
    accTbale = bt.datatable.initMultiTables({
        elem: "#appAccTable",
        dom: 'Brtlip',
        colReorder: true,
        mulselect: true,
        selectCss: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchAccForm",
            formFilter: "searchAcc"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abClaim/datatableAcc"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "transTime", bSortable: true},
            {data:"amt",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.amt);
                }
            },
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            // {data: "cdSignName", bSortable: true},
            {data: "cdSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAccDtl.cdSign", full.cdSign);
                }
            },
            {data: "claimSequence", bSortable: true},
            {data: "remark", bSortable: true},
            {data: "uses", bSortable: true},
            {data: "postscript", bSortable: true},
            {data: "curName", bSortable: true},
            {data:"bal",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.bal);
                }
            },
            {data: "bankSerialNo", bSortable: true},
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "oppAccBank", bSortable: true},
        ],
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'2',
                    dataName:'amt'
                }
            ]
        },
        btClickRowCallback: function (data) {
            if (data[0].isSelect == true) {
                var accTable = $('#appAccTable').DataTable();
                var accData = accTable.rows('.selected').data();
                var accLen = accData.length
                var accRowData = accData.splice(0,accLen);
                accJudge(accRowData, accLen)
            }
        }
    });
    
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        mulselect: true,
        selectCss: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abClaim/datatable"
        },
        buttons: [
            //提交认领
            {
                text: bt.lang('bill.accebill.submitClaim'),
                hide:bt.isnotResource('bill:accebill:claim:apply'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                    var table = $('#appAccTable').DataTable();
                    var accData = table.rows('.selected').data();
                	submit(data, accData);
                }
            },
            {
                text: bt.lang('调整票据解付金额'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    setBillEndPayMoney(data);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
                    {data: "origNo", bSortable: true},
                    {data: "startDate", bSortable: true},
                    {data: "endDate", bSortable: true},
                    {data: "billBankAcc", bSortable: true},
                    {data: "claimTypeName", bSortable: true},
                    // {data: "claimType", bSortable: true, className: 'center',
                    //     render: function (data, type, full, meta) {
                    //         return bt.dict("AB_BILL_CLAIM_TYPE", full.claimType);
                    //     }
                    // },
                    // {data: "billSortName", bSortable: true},
                    {data: "billSort", bSortable: true, className: 'center',
                        render: function (data, type, full, meta) {
                            return bt.dict("BILL_PAYERPAYEE_TYPE", full.billSort);
                        }
                    },
                    {data: "claimDate", bSortable: true},
                    {data:"billMoney",bSortable:true,className:'textRight',
                        render: function ( data, type, full, meta ) {
                            return bt.money.format(full.billMoney);
                        }
                    },
                    {
                        data: "bailpercent", bSortable: true, className: "textRight",
                        render: function (data, type, full, meta) {
                            var html="";
                            if (data || data == "0") {
                                html = bt.rateType.getFun().format(full.bailpercent, 'percent', 2, ',');
                            }
                            return html;
                        }
                    },
                    {
                        data: "bailmoney", bSortable: true, className: 'textRight',
                        render: function (data, type, full, meta) {
                            if (data) {
                                return bt.money.format(data);
                            }
                            return '0.00';
                        }
                    },
                    {
                        data: "payMoney", bSortable: true, className: 'textRight',
                        render: function (data, type, full, meta) {
                            return bt.money.format(data ? data : 0);
                        }
                    },
                    {data: "applyCorpName", bSortable: true},
                    {data: "busCorpName", bSortable: true},
                    {data: "corpName", bSortable: true},
                    {data: "bankAcc", bSortable: true},
                    {data: "bankTypeName", bSortable: true},
                    {data: "claimSequence", bSortable: true},
                    // {data: "claimStatusName", bSortable: true},
                    {data: "claimStatus", bSortable: true, className: 'center',
                        render: function (data, type, full, meta) {
                            return bt.dict("AB_BILL_CLAIM_STATUS", full.claimStatus);
                        }
                    },
                    {data: "claimPerson", bSortable: true},
                    {data: "claimTime", bSortable: true},
                    // {data: "billFlagName", bSortable: true},
                    {data: "billFlag", bSortable: true, className: 'center',
                        render: function (data, type, full, meta) {
                            return bt.dict("AB_BILL_FLAG", full.billFlag);
                        }
                    },
                    // {data: "billTypeName", bSortable: true},
                    {data: "billType", bSortable: true, className: 'center',
                        render: function (data, type, full, meta) {
                            return bt.dict("AB_BILL_TYPE", full.billType);
                        }
                    },
                    {data: "payer", bSortable: true},
                    {data: "payerBankName", bSortable: true},
                    {data: "payee", bSortable: true},
                    {data: "cnAccrecords", bSortable: true},
                    {data: "rearAccrecords", bSortable: true},
                    // {data: "sendSignName", bSortable: true},
                    {data: "sendSign", bSortable: true, className: 'center',
                        render: function (data, type, full, meta) {
                            return bt.dict("AB_BILL_SEND_SIGN", full.sendSign);
                        }
                    },
                    // {data: "newStmIssuofpprIndName", bSortable: true},
                    {data: "newStmIssuofpprInd", bSortable: true, className: 'center',
                        render: function (data, type, full, meta) {
                            return bt.dict("AB_BILL_NEW_STM_ISSUOFPPR_IND", full.newStmIssuofpprInd);
                        }
                    },
                    {data: "sbbllRngStrtSn", bSortable: true},
                    {data: "sbbllRngEndSn", bSortable: true}
        ],
        btClickRowCallback: function (data) {
            // if (data[0].isSelect == true) {
                bt.corp.get("corpId").val('')
                bt.dicts.get("eqi_cdSign").val('')
                bt.selectType.get("bankTypeId").val('')
                moneyType.get("startMoney").val('')
                moneyType.get("endMoney").val('')
                $("#uses").val('');
                $("#remark").val('');
                $("#postscript").val('');
                var table = $('#appTable').DataTable();
                var appData = table.rows('.selected').data();
                var appLen = appData.length
                var appRowData = appData.splice(0,appLen);
                if(appLen>0 && billJudge(appRowData, appLen)){
                    bt.dicts.get("eqi_cdSign").val(data[0].billSort == 1 ? 0 : 1)
                    bt.selectType.get("bankTypeId").val(data[0].bankTypeId)
                    bt.corp.get("corpId").val(data[0].corpId)
                    //根据配置文件设置交易明细查询条件
                    setBankAccDtlQueryByConfig(appRowData);
                }else{
                    return false;
                }
            // }
        },
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'5',
                    dataName:'billMoney'
                },
                {
                    colNum: '7',
                    dataName: 'bailmoney'
                },
                {
                    colNum: '8',
                    dataName: 'payMoney'
                }
            ]
        }
    });
    //格式化数字
    $.each($("input[decimal]"),function(i,item){
        if($(item).attr("decimal")!=""){
            bt.moneynum.init({elem:$(item),decimal:$(item).attr('decimal')*1});
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
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
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
   var dd = bt.btdate.init({
	   elem:".date-single",
	   // labelname:bt.lang('table.abVoucherinfo.Business.date'),
	   name:"businessdate"
   });
   dd.setValue({value:new Date().Format("yyyy-MM-dd")})
});

/**
 * 根据配置文件设置交易明细查询条件
 * @param dataInfo
 */
function setBankAccDtlQueryByConfig(dataInfos){
    if(!dataInfos || dataInfos.length == 0) return;
    var dataInfo = dataInfos[0];
    if(!dataInfo) return;
    var money=0;
    var claimTypeName=dataInfo.claimTypeName;
    var bankTypeId=dataInfo.bankTypeId;
    for(var i=0;i<dataInfos.length;i++){
        var info = dataInfos[i];
        if(claimTypeName == '兑付'){
            money+=parseFloat(info.payMoney);
        }else{
            money+=parseFloat(info.billMoney);
        }
    }

    $.ajax({
        type: 'post',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            claimTypeName: claimTypeName,
            bankTypeId: bankTypeId
        }),
        asycn: false,
        url: basePath+"/bill/accebill/abClaim/getBankAccDtlQueryConfig",
        success: function (result) {
            if (result.success) {
                if(result.obj.downMoney && result.obj.upMoney){
                    var beginMoney=money - parseFloat(result.obj.downMoney);
                    var endMoney=money + parseFloat(result.obj.upMoney);
                    moneyType.get("startMoney").val(bt.money.format(beginMoney))
                    moneyType.get("endMoney").val(bt.money.format(endMoney))
                }else{
                    moneyType.get("startMoney").val(money)
                    moneyType.get("endMoney").val(money)
                }
                if(result.obj.uses){
                    $("#uses").val(result.obj.uses);
                }
                if(result.obj.remark){
                    $("#remark").val(result.obj.remark);
                }
                if(result.obj.postscript){
                    $("#postscript").val(result.obj.postscript);
                }
                bt.datatable.refreshTable("appAccTable");
            }
        },
        dataType: 'json'
    });
}

function billJudge(appRowData, appLen) {
    var billTypeFlag = true, corpFlag = true, flag = true
    if(appLen){
        var billSort = appRowData[0].billSort
        var corpId = appRowData[0].corpId
        var bankTypeName = appRowData[0].bankTypeName
        var claimTypeName = appRowData[0].claimTypeName
        if (appLen > 1) {
            for (var i = 0; i < appLen; i++) {
                //不允许同时选择应付和应收票据！
                if (appRowData[i].billSort !== billSort) {
                    billTypeFlag = false
                }
                //不允许同时选择贴现和托收业务！
                if (appRowData[i].claimTypeName !== claimTypeName) {
                    flag = false
                }
                //不允许同时选择不同单位的票据！
                if (appRowData[i].corpId !== corpId) {
                    corpFlag = false
                }
                //不允许同时选择不同银行的票据！
                if (appRowData[i].bankTypeName !== bankTypeName) {
                    layer.alert(bt.lang('不允许同时选择不同银行的票据!'), {icon: 2});
                    return false
                }
            }
        }
    }
    if (!billTypeFlag) {
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectDiffBillType'), {icon: 2});
        return false
    }
    if (!flag) {
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectDiffReceiveBillType'), {icon: 2});
        return false
    }
    if (!corpFlag) {
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectDiffCorpBill'), {icon: 2});
        return false
    }
    return true;
}

function accJudge(accRowData, accLen) {
    var accCorpFlag = true
    if (accLen) {
        var corpId = accRowData[0].corpId
        if (accLen > 1) {
            for (var i = 0; i < accLen; i++) {
                //不允许同时选择不同单位的流水！
                if (accRowData[i].corpId !== corpId) {
                    accCorpFlag = false
                }
            }
        }
    }
    if (!accCorpFlag) {
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectDiffFlow'), {icon: 2});
        return false
    }
    return true
}

//提交认领
function submit(data, accData){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectBill'), {icon: 7});
    	return;
    }
    var accLen = accData.length
    if(!accLen){
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectFlow'), {icon: 7});
        return;
    }
    var accRowData = accData.splice(0,accLen);
    var rowData = data.splice(0,len);

    if (!billJudge(rowData, len) || !accJudge(accRowData, accLen)) {
        return;
    }
    if ((len > 1 && accLen > 1)) {
        layer.alert('只支持票据记录与银行流水记录一对多或多对一，不支持多对多', {icon: 2});
        return false
    }
    var billSum = 0, accSum = 0
    for (var i = 0; i < len; i++) {
        billSum += rowData[i].payMoney
    }
    for (var i = 0; i < accLen; i++) {
        accSum += accRowData[i].amt
    }
    if (accRowData[0].corpId !== rowData[0].corpId) {
        parent.layer.confirm(bt.lang('bill.accebill.abClaim.billAndFlowCorpIsDiff'),function(){
            if (accRowData[0].bankAcc !== rowData[0].bankAcc) {
                parent.layer.confirm(bt.lang('bill.accebill.abClaim.billAndFlowAccIsDiff'),function(){
                    if (billSum !== accSum) {
                        parent.layer.confirm(bt.lang('bill.accebill.abClaim.amountNotEqual'),function(){
                            detailSubmit(rowData, accRowData, len, accLen)
                        })
                    } else {
                        detailSubmit(rowData, accRowData, len, accLen)
                    }
                })
            } else {
                detailSubmit(rowData, accRowData, len, accLen)
            }
        })
    } else if (accRowData[0].bankAcc !== rowData[0].bankAcc) {
        parent.layer.confirm(bt.lang('bill.accebill.abClaim.billAndFlowAccIsDiff'),function(){
            if (billSum !== accSum) {
                parent.layer.confirm(bt.lang('bill.accebill.abClaim.amountNotEqual'),function(){
                    detailSubmit(rowData, accRowData, len, accLen)
                })
            } else {
                detailSubmit(rowData, accRowData, len, accLen)
            }
        })
    } else if (billSum !== accSum) {
        parent.layer.confirm(bt.lang('bill.accebill.abClaim.amountNotEqual'),function(){
            detailSubmit(rowData, accRowData, len, accLen)
        })
    } else {
        detailSubmit(rowData, accRowData, len, accLen)
    }
}

function detailSubmit(rowData, accRowData, len, accLen) {
    parent.layer.confirm(bt.lang('bill.accebill.abClaim.confirmClaim'),function(){
        var vids,vvids,aids,claimIds,billMoneys,origNos,briefId,accDtlIds,realMoneys;
        briefId = rowData[0].briefId
        for (i = 0; i < len; i++) {
            if (vids) {
                vids = vids + "," + rowData[i].vid
            } else {
                vids = rowData[i].vid
            }
            if (vvids) {
                vvids = vvids + "," + rowData[i].vvid
            } else {
                vvids = rowData[i].vvid
            }
            if (aids) {
                aids = aids + "," + rowData[i].aid
            } else {
                aids = rowData[i].aid
            }
            if (claimIds) {
                claimIds = claimIds + "," + rowData[i].id
            } else {
                claimIds = rowData[i].id
            }
            if (billMoneys) {
                billMoneys = billMoneys + "," + rowData[i].billMoney
            } else {
                billMoneys = rowData[i].billMoney
            }
            if (realMoneys) {
                realMoneys = realMoneys + "," + rowData[i].realMoney
            } else {
                realMoneys = rowData[i].realMoney
            }
            if (origNos) {
                origNos = origNos + "," + rowData[i].origNo
            } else {
                origNos = rowData[i].origNo
            }
        }

        for (var j = 0; j < accLen; j++) {
            if (accDtlIds) {
                accDtlIds = accDtlIds + "," + accRowData[j].id
            } else {
                accDtlIds = accRowData[j].id
            }
            if (claimIds) {
                claimIds = claimIds + "," + accRowData[j].claimId
            } else {
                claimIds = accRowData[j].claimId
            }
        }
        $.post(basePath + "/bill/accebill/abClaim/submitClaim",{"vids":vids,"vvids":vvids,"aids":aids, "claimIds": claimIds,
            "briefId": briefId, "billMoneys": billMoneys, "origNos": origNos, "accDtlIds": accDtlIds,"realMoneys": realMoneys}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                corp.get("corpId").val('')
                dicts.get("eqi_cdSign").val('')
                selectType.get("bankTypeId").val('')
                moneyType.get("startMoney").val('')
                moneyType.get("endMoney").val('')
                dateType.get("startDate").val('')
                dateType.get("endDate").val('')
                $("#bankAcc").val('')
                $("#uses").val('')
                $("#postscript").val('')
                $("#remark").val('')
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}


/**
 * 调整票据解付金额
 * @param data
 */
function setBillEndPayMoney(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }else if(len>1){
        layer.alert(bt.lang('不支持批量调整到期解付金额，请重新选择!'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    var dataInfo= rowData[0];
    if(dataInfo.claimTypeName!='兑付'){
        layer.alert(bt.lang('只能选兑付数据，请重新选择!'), {icon: 7});
        return;
    }
    var contentHtml='<form class="layui-form" method="post" id="inputForm"><div id="" class="layui-colla-content layui-show">' +
        '<table class="layui-table"><tbody>' +
        '<tr>' +
        '<td class="textRight" width="8%">'+bt.lang('table.abVoucherinfoVo.corpId')+'</td>' +
        '<td width="25%">' +  dataInfo.corpName +'</td>' +
        '<td class="textRight" width="8%">'+bt.lang('table.abVoucherinfo.origNo')+'</td>' +
        '<td width="25%">' + dataInfo.origNo +'</td>' +
        '</tr>' +
        '<tr>' +
        '<td class="textRight" width="8%">'+bt.lang('银行类型')+'</td>' +
        '<td width="25%">'+ dataInfo.bankTypeName +'</td>' +
        '<td class="textRight" width="8%">'+bt.lang('table.abVoucherinfoVice.bailPercent')+'</td>' +
        '<td width="25%">'+
        '<input type="text" id="bailpercent" class="layui-input bt-biz-ratioType" data-name="bailpercent" data-biz-name="bailpercent" value="'+dataInfo.bailpercent+'" placeholder="'+bt.lang('table.abVoucherinfoVice.bailPercent')+'" data-min="0" data-max="100" data-ratetype="percent" data-decimal="2" >' +
        '</td>' +
        '</tr>' +
        '<tr>' +
        '<td class="textRight" width="8%">'+bt.lang('table.abVoucherinfo.billMoney')+'</td>' +
        '<td width="25%">' + bt.money.format(dataInfo.billMoney) +'<input type="hidden" id="billMoney" value="'+dataInfo.billMoney+'" /></td>' +
        '<td class="textRight" width="8%"></td>' +
        '<td width="25%"></td>' +
        '</tr>' +
        '<tr>' +
        '<td class="textRight" width="8%">'+bt.lang('table.abVoucherinfoVice.bailMoney')+'</td>' +
        '<td width="25%">' +
        '<input type="text" id="bailmoney" class="layui-input bt-biz-moneyType" data-biz-name="bailmoney" value="'+dataInfo.bailmoney+'" placeholder="保证金金额" data-min="0" data-max="'+dataInfo.billMoney+'" data-callback="bailMoneyCallback" >' +
        '</td>' +
        '<td class="textRight" width="8%">到期解付金额</td>' +
        '<td width="25%">' +
        '<input type="text" id="endPayMoney" class="layui-input bt-biz-moneyType" data-biz-name="endPayMoney" value="'+dataInfo.payMoney+'" placeholder="票据解付金额" data-min="0" data-max="'+dataInfo.billMoney+'" data-callback="endPayMoneyCallback">' +
        '</td>' +
        '</tr>' +
        '</tbody></table>' +
        '</div></form>';
    layer.open({ type: 1,
        area: ['700px', '300px'],
        title: '调整票据解付金额',
        content: contentHtml,
        btn: ['确定', '取消'],
        success: function(layero, index) {
            bt.moneyType.init();
            bt.ratioType.init();
        },
        yes: function(index, layero){
            var endPayMoney = document.getElementById('endPayMoney').value;
            var bailmoney = document.getElementById('bailmoney').value;
            var bailpercent = bt.ratioType.get('bailpercent').val();
            if(endPayMoney && bailmoney){
                $.ajax({
                    type: 'post',
                    contentType: 'application/json; charset=UTF-8',
                    data: JSON.stringify({
                        accId: dataInfo.aid,
                        vvid: dataInfo.vvid,
                        bailpercent: bt.money.toNumber(bailpercent),
                        endPayMoney: bt.money.toNumber(endPayMoney),
                        bailmoney: bt.money.toNumber(bailmoney)
                    }),
                    asycn: false,
                    url: basePath + "/bill/accebill/abemitbillpay/setBillEndPayMoney",
                    success: function (result) {
                        parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        if (result.success) {
                            bt.datatable.refreshCurrentPage();
                            layer.close(index); // 关闭弹出层
                        }
                    },
                    dataType: 'json'
                });
            }else{
                layer.alert(bt.lang('保证金金额和票据解付金额不允许为空！'), {icon: 7});
            }
        },
        btn2: function(index, layero){
            layer.close(index); // 关闭弹出层
        }
    });
}

function bailMoneyCallback(){
    var billMoney=bt.money.toNumber($("#billMoney").val());
    var bailmoney=bt.money.toNumber($("#bailmoney").val());
    bt.moneyType.get('endPayMoney').val((billMoney-bailmoney).toFixed(2));
}
function endPayMoneyCallback(){
    var billMoney=bt.money.toNumber($("#billMoney").val());
    var endPayMoney=bt.money.toNumber($("#endPayMoney").val());
    bt.moneyType.get('bailmoney').val((billMoney-endPayMoney).toFixed(2));
}
