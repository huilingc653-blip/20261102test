bt.use(['form','datatable','corp','btdate','date','moneyType','bank','dicts','selectTableType'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    corp = bt.corp.init();
    bt.dicts.init();
    bt.bank.init({
        elem:'.bank',
    });

    /**
     * 帐号下拉框初始化
     */
    /*selectTable = bt.selectTableType.init({
        elem: "#bankAcc",
        type: 'POST',
        paging: true,
        // scrollX:"500px",scrollY:"200px",
        url: basePath + "/finacial/check/erpDataManager/jsonBankAccErpData",
        columns: [
            {"data": "bankAcc", title: bt.lang("table.btBankAcc.bankAcc")},
            {"data": "curName", title: bt.lang("international.payment.curName")},
            {"data": "accName", title: bt.lang("table.btBankAcc.accName")},
            {"data": "bankName", title: bt.lang("table.btBankAcc.bankName")}
        ],
        clickCallback: function (data, tr, elem, full) {
            if (data&&data.bankAcc) {
                $("#bankAcc").val(data.bankAcc);
                $("#curCode").val(data.curId);
            }
        }
    });*/

    //生成列表
    var tableInitFlag = true;
    bt.datatable.init({
        elem: "#appTable",
        dom: 'rtlip',
        colReorder: true,
        checkbox:false,order: [1,'desc'],
        ajax: {
            'url': basePath+"/finacial/check/fcbankbalanceadjust/datatable?type=3"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search",
            initQueryParams:function (params){
                if(tableInitFlag){
                    params.eqs_checkType = '0';
                    tableInitFlag = false;
                }
                return params;
            }
        },
        columns: [
			{data:"beginDate",className:'center',bSortable:true,
				render: function (data, type, full, meta) {
					return bt.date.format(full.beginDate,'yyyy-mm-dd');
				}
			},
			{data:"endDate",className:'center',bSortable:true,
				render: function (data, type, full, meta) {
					return bt.date.format(full.endDate,'yyyy-mm-dd');
				}
			},
			{data:"bankAcc",className:'center',bSortable:false},
            {data:"curCode",className:'center',bSortable:true, render: function (data, type, full, meta) {
                    return full.curName;
                }
            },
			{data:"bankInitBalance",className:'textRight',bSortable:true,
				render: function (data, type, full, meta) {
					return moneyFun.format(full.bankInitBalance);
				}	
			},
			{data:"bankAdjBal",className:'textRight',bSortable:true,
				render: function (data, type, full, meta) {
					return moneyFun.format(full.bankAdjBal);
				}	
			},
			{data:"account",className:'center',bSortable:false},
			{data:"blnInitBalance",className:'textRight',bSortable:true,
				render: function (data, type, full, meta) {
					return moneyFun.format(full.blnInitBalance);
				}
			},
			{data:"blnAdjBalance",className:'textRight',bSortable:true,
				render: function (data, type, full, meta) {
					return moneyFun.format(full.blnAdjBalance);
				}
			},           
			{data:"adjustDate",className:'center',bSortable:true,
				render: function (data, type, full, meta) {
					return bt.date.format(full.adjustDate,'yyyy-mm-dd');
				}
			},
			{data:"checkType",className:'center',bSortable:false,
				render: function ( data, type, full, meta ) {
                    return bt.dict("fcBankBalanceAdjust.checkType",full.checkType);
                }
			},
			{data:"adjCode",className:'center',bSortable:false,},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                html +='&nbsp;|&nbsp;';
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="print(this)">'+bt.lang("common.button.print")+'</a>';
                return html;
            }}
        ]
    });
    
    bt.btdate.init({
        elem:".date-interval",
        startname:"ged_adjustDate",
        endname:"led_adjustDate"
        }
    );
});

function view(el){
    var checkType =  bt.dicts.init().get("eqs_checkType").val();
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        title:bt.lang("table.balance.adjustDtl"),
        area: ['75%', '80%'],
        maxmin: true,
        content: basePath + "/finacial/check/fcbankbalanceadjust/viewAdjustDetails/" + data.id+"?param=0&checkType="+checkType
    });
}
function print(el){
    var checkType =  bt.dicts.init().get("eqs_checkType").val();
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        title:bt.lang("table.balance.adjustDtl"),
        area: ['75%', '80%'],
        maxmin: true,
        content: basePath + "/finacial/check/fcbankbalanceadjust/viewAdjustDetails/" + data.id+"?param=1&checkType="+checkType
    });
}

/**
 * 单位回调
 */
function corpCallBack() {
    //var corpId = corp.get("netId").val();
    //selectTable.get("bankAcc").setUrl({corpId:corpId});
}