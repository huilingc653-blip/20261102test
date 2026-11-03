var billTable={},accTbale={},dicts,selectType,corp,moneyType;
bt.use(['form','datatable','moneyType','btdate','corp','select','dateType','dicts','selectType','mulselect','rateType'], function(){
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
        mulselect: false,
        selectCss: true,
        checkbox:false,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchAccForm",
            formFilter: "searchAcc"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abClaim/datatableWaitConfirmAcc"
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
            {data: "cdSignName", bSortable: true},
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
            'url': basePath+"/bill/accebill/abClaim/datatableWaitConfirm"
        },
        buttons: [
            //提交确认
            {
                text: bt.lang('bill.accebill.submitConfirm'),
                hide:bt.isnotResource('bill:accebill:claim:apply'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                	submit(data);
                }
            },
            //转待认领
            {
                text: bt.lang('bill.accebill.transferWaitClaim'),
                hide:bt.isnotResource('bill:accebill:claim:apply'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    transfer(data);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ], btClickRowCallback: function (data) {
                if (data[0].isSelect == true) {
                    bt.dicts.get("eqi_cdSign").val(data[0].billSort == 1 ? 0 : 1)
                    bt.selectType.get("bankTypeId").val(data[0].bankTypeId)
                    bt.corp.get("corpId").val('')
                    bt.corp.get("corpId").val(data[0].corpId)
                    $("#claimId").val(data[0].id);
                    bt.datatable.refreshTable("appAccTable");
                    $("#claimId").val('');
                }
            }
        ,columns: [
                    {data: "origNo", bSortable: true},
                    {data: "startDate", bSortable: true},
                    {data: "endDate", bSortable: true},
                    {data: "billBankAcc", bSortable: true},
                    {data: "claimTypeName", bSortable: true},
                    {data: "billSortName", bSortable: true},
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
                    {data: "claimStatusName", bSortable: true},
                    {data: "claimPerson", bSortable: true},
                    {data: "claimTime", bSortable: true},
                    {data: "billFlagName", bSortable: true},
                    {data: "billTypeName", bSortable: true},
                    {data: "payer", bSortable: true},
                    {data: "payerBankName", bSortable: true},
                    {data: "payee", bSortable: true},
                    {data: "cnAccrecords", bSortable: true},
                    {data: "rearAccrecords", bSortable: true},
                    {data: "sendSignName", bSortable: true},
                    {data: "newStmIssuofpprIndName", bSortable: true},
                    {data: "sbbllRngStrtSn", bSortable: true},
                    {data: "sbbllRngEndSn", bSortable: true}
        ],
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

//提交确认
function submit(data){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectBill'), {icon: 7});
    	return;
    }

    var rowData = data.splice(0,len);

    detailSubmit(rowData, len)

}

//转待认领
function transfer(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectBill'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    parent.layer.confirm(bt.lang('bill.accebill.abClaim.transferClaim'),function(){
        var vvids, aids, briefIds, claimIds;
        for (i = 0; i < len; i++) {
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
            if (briefIds) {
                briefIds = briefIds + "," + rowData[i].briefId
            } else {
                briefIds = rowData[i].briefId
            }
        }

        $.post(basePath + "/bill/accebill/abClaim/confirmToClaim",{"vvids":vvids,"aids":aids,
            "claimIds": claimIds, "briefIds": briefIds}, function(result){
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

function detailSubmit(rowData, len) {
    parent.layer.confirm(bt.lang('bill.accebill.abClaim.confirmClaim'),function(){
        var vids,vvids,aids,claimIds,billMoneys,origNos,briefId,realMoneys;
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

        $.post(basePath + "/bill/accebill/abClaim/submitConfirm",{"vids":vids,"vvids":vvids,"aids":aids, "claimIds": claimIds,
            "briefId": briefId, "billMoneys": billMoneys, "origNos": origNos ,"realMoneys": realMoneys}, function(result){
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
