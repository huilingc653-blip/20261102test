bt.use(['form','datatable','corp','btdate','date','moneyType','bank','dicts','selectTableType'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    corp = bt.corp.init();
    bt.dicts.init();
    bt.bank.init({
        elem:'.bank',
    });
    
    bt.btdate.init({
        elem:".date-interval",
        startname:"ged_adjustDate",
        endname:"led_adjustDate"
        }
    );

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
    bt.datatable.init({
        elem: "#appTable",
        dom: 'rtlip',
        colReorder: true,checkbox:true,
        order: [1,'desc'],
        ajax: {
            'url': basePath+"/finacial/check/fcbankbalanceadjust/datatable?type=1"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        columns: [
			{data:"beginDate",className:'center',bSortable:true,
				render: function (data, type, full, meta) {
					return bt.date.format(full.beginDate, 'yyyy-mm-dd');
				}
			},
			{data:"endDate",className:'center',bSortable:true,
				render: function (data, type, full, meta) {
					return bt.date.format(full.endDate, 'yyyy-mm-dd');
				}
			},
			{data:"corpName",bSortable:true},
			{data:"bankAcc",className:'center',bSortable:true},
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
			{data:"account",className:'center',bSortable:true},
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
					return bt.date.format(full.adjustDate, 'yyyy-mm-dd');
				}
			},
			{data:"checkType",className:'center',bSortable:true,
				render: function ( data, type, full, meta ) {
                    return bt.dict("fcBankBalanceAdjust.checkType",full.checkType);
                }
			}
        ]
    });
    
    form.on('submit(restore)', function(data){
    	var selectData = bt.datatable.getSelectData();
    	if(selectData.length<1){
    		layer.alert(bt.lang("common.button.selectnum"));
    		return false;
    	}
    	var ids = "";
    	for(var i = 0;i<selectData.length;i++){
    		ids += selectData[i].id +",";
    	}
        bt.loading.show();
        $.post(basePath +"/finacial/check/fcbankbalanceadjust?doRedoAdjustOver", {"ids":ids}, function(result) {
            if(result.success){
                bt.loading.hide();
                bt.navtab.refreshCurrentTable();
				parent.layer ? parent.layer.alert(bt.lang("common.message.success"), {icon: result.success ? 1:2}) : layer.alert(bt.lang("common.message.success"), {icon: result.success?1:2});
            } else {
                bt.loading.hide();
				parent.layer ? parent.layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success ? 1:2}) : layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success?1:2});
            }
            return false;
        },'JSON');
        return false;

        // $.ajax({
		// 	url:basePath+"/finacial/check/fcbankbalanceadjust.do?doRedoAdjustOver",
		// 	method:'POST',
		// 	data:{"ids":ids},
		// 	dataType:'json',
		// 	success:function(data){
		// 		parent.layer?parent.layer.alert(data.msg, {icon: data.success?1:2}):layer.alert(data.msg, {icon: data.success?1:2});
		// 		if(data.success){
		// 			location.reload();
		// 		} else {
        //
         //        }
		// 	}
		// });
    });
    
});
/**
 * 单位回调
 */
function corpCallBack() {
    //var corpId = corp.get("corpId").val();
    //selectTable.get("bankAcc").setUrl({corpId:corpId});
}
