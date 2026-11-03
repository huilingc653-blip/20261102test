var billTable={},accTbale=null,dicts,selectType,corp;
bt.use(['form','datatable','moneyType','btdate','corp','select','dateType','dicts','selectType','mulselect','rateType'], function(){
    $ = bt.jquery,form = bt.form();
    corp = bt.corp.init();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    dicts=bt.dicts.init();
    bt.mulselect.init();
    
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
            'url': basePath+"/bill/accebill/abClaim/datatableConfirmed"
        },
        buttons: [
            //转待认领
            {
                text: bt.lang('bill.accebill.transferWaitClaim'),
                hide:bt.isnotResource('bill:accebill:claim:apply'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                    var table = $('#appAccTable').DataTable();
                    var accData = table.rows().data();
                	submit(data, accData);
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
                    // {data: "cnAccrecords", bSortable: true},
                    // {data: "rearAccrecords", bSortable: true},
                    {data: "sendSignName", bSortable: true},
                    {data: "newStmIssuofpprIndName", bSortable: true},
                    {data: "sbbllRngStrtSn", bSortable: true},
                    {data: "sbbllRngEndSn", bSortable: true}

        ],
        btClickRowCallback: function (data) {
            if (data[0].isSelect == true) {
                bt.dicts.get("eqi_cdSign").val(data[0].billSort == 1 ? 0 : 1)
                bt.selectType.get("bankTypeId").val(data[0].bankTypeId)
                var table = $('#appTable').DataTable();
                var appData = table.rows('.selected').data();
                var appLen = appData.length
                var appRowData = appData.splice(0,appLen);
                accInit(data[0])
                $("#claimId").val('')
            }
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

function accInit(data) {
    $("#claimId").val(data.id);
    if (accTbale == null) {
        //银行流水列表
        accTbale = bt.datatable.initMultiTables({
            elem: "#appAccTable",
            dom: 'Brtlip',
            colReorder: true,
            mulselect: true,
            selectCss: true,
            order: [[1,'desc']],
            searchForm: {
                formElem: "#searchAccForm",
                formFilter: "searchAcc"
            },
            ajax: {
                'url': basePath+"/bill/accebill/abClaim/datatableConfirmedAcc"
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
                {data: "origNo", bSortable: true},
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
                {data: "claimRmk", bSortable: true},
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
                sumList:[
                    {
                        colNum:'2',
                        dataName:'amt'
                    }
                ]
            }
        });
        bt.corp.get("corpId").val('')
        bt.corp.get("corpId").val(data.corpId)
    } else {
        bt.corp.get("corpId").val('')
        bt.corp.get("corpId").val(data.corpId)
        bt.datatable.refreshTable("appAccTable");
    }
}

//转待认领
function submit(data, accData){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('bill.accebill.abClaim.notSelectBill'), {icon: 7});
    	return;
    }
    var accLen = accData.length
    var accRowData = accData.splice(0,accLen);
    var rowData = data.splice(0,len);
    parent.layer.confirm(bt.lang('bill.accebill.abClaim.transferClaim'),function(){
        var vvids, aids, accDtlIds, briefIds, claimIds;
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

        for (var j = 0; j < accLen; j++) {
            if (accDtlIds) {
                accDtlIds = accDtlIds + "," + accRowData[j].id
            } else {
                accDtlIds = accRowData[j].id
            }
        }
        $.post(basePath + "/bill/accebill/abClaim/transferClaim",{"vvids":vvids,"aids":aids, "accDtlIds": accDtlIds,
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
