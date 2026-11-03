var dateTypeComp;
bt.use(['form','datatable','corp','date','btdate','moneyType','bank','dicts','selectTableType'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    corp = bt.corp.init();
    bt.dicts.init();
    bt.bank.init({
        elem:'.bank',
    });
    dateTypeComp = bt.dateType.init();
    var dateFun = dateTypeComp.getFun();

    bt.btdate.init({
        elem:".date-interval",
        startname:"ged_adjustDate",
        endname:"led_adjustDate"
        }
    );

    /**
     * 帐号下拉框初始化
     */
    selectTable = bt.selectTableType.init({
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
                $("#curId").val(data.curId);
            }
        }
    });

    
    //生成列表
    bt.datatable.init({
        elem: "#appTable2",
        dom: 'rtlip',
        colReorder: true,
        order: [0,'desc'],
        ajax: {
            'url': basePath+"/finacial/check/fccompareerp/dataTableCorp"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        columns: [
			{data:"corpName",className:'center',bSortable: true},
			{data:"checkDate",className:'center',bSortable:true,
				render: function (data, type, full, meta) {
					return bt.date.format(full.checkDate, 'yyyy-mm-dd');
				}
			},
			{data:"beginDate",className:'center',bSortable:true,
				render: function (data, type, full, meta) {
			        if("" == full.beginDate || null == full.beginDate){
			            return "";
                    }
					return bt.date.format(full.beginDate, 'yyyy-mm-dd');
				}
			},
			{data:"endDate",className:'center',bSortable:true,
				render: function (data, type, full, meta) {
                    if("" == full.endDate || null == full.endDate){
                        return "";
                    }
					return bt.date.format(full.endDate, 'yyyy-mm-dd');
				}
			},
            {data:"queryDate",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return dateTypeComp.get("endDate").val();
                }
            },
            {data:"status",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    var endDate = bt.date.format(full.endDate, 'yyyy-mm-dd');//查询结束日期
                    var queryDate = dateTypeComp.get("endDate").val();//对账结束日期
                    if(null == endDate || "" == endDate){
                        endDate = bt.date.format(full.checkDate, 'yyyy-mm-dd');//查询结束日期
                    }
                    if (dateFun.compare(endDate, queryDate) >= 0) {
                        return bt.dict("finacial.CompareSign", "1");
                    } else {
                        return bt.dict("finacial.CompareSign", "0");
                    }
                    return dateType.get("endDate").val();
                }
            },
			{data:"erpCnt",className:'center',bSortable:true},
			{data:"bankCnt",className:'center',bSortable:true},
            {data:null,bSortable:false,className:'center',
				render: function (data, type, full, meta) {
	                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="check(this)">'+bt.lang("table.fcCompare.check")+'</a>';
	                return html;
				}
			}
        ]
    });
});

function check(el){
    var data = layui.jquery("#appTable2").DataTable().row(layui.jquery(el).data("row")).data();
    if(checkPermission(data.corpId)){
        var params = "bankAcc="+data.bankAcc + "&account=" + data.account + "&curId=" + data.curId + "&transTime=" + dateTypeComp.get("endDate").val();
        console.log(params);
        var href = "/finacial/check/fccompareerp/addCorpCompareErp?" + params;
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('table.fcCompare.check'), basePath + href);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['75%', '80%'],
            maxmin: true,
            content: basePath + href,
            end:function(){
                //datatable && datatable.draw();
            }
        });
    }
}

/**
 * 单位回调
 */
function corpCallBack() {
    var corpId = corp.get("corpId").val();
    selectTable.get("bankAcc").setUrl({corpId:corpId});
}

function checkPermission(corpId){
    $.post(basePath + "/finacial/check/fccompareerp/checkPermission", {
        corpId : corpId
    }, function(data) {
        if(data){
            return true;
        }else{
            layer.alert(bt.lang("请对该单位所有帐号授权明细、余额的查询权限"), {icon: 2});
            return false;
        }
    }, 'JSON');
}