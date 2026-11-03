var dateTypeComp;
bt.use(['form','jquery','datatable','bttable','dateType','selectTableType','btdate','corp','selectType','element','money','moneyType','dicts','mulAccInfo','mulselect','bank'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    corp = bt.corp.init();
    bt.dicts.init();
    bt.mulselect.init();//初始化单位
    var corp = bt.corp.init();
    var selectTypes = bt.selectType.init();
    var mulAccInfo=bt.mulAccInfo.init();
    var dateTypes = bt.dateType.init();
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
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == ""){
            return true;
        }
        return false;
    }

    var params = {
        "account":$("#account").val(),
        "bankAcc":$("#bankAcc").val(),
        "curId": $("#curId").val(),
        "checkDate":$("#checkDate").val(),
        "paramType":$("#paramType").val(),
        "checkType":$("#checkType").val(),
        "transTime":$("#transTime").val()
    };

    //生成列表
    datatable = bt.datatable.init({
        elem: "#adjustTable",
        dom: 'rtlip',
        colReorder: true,
        checkbox: true,
        order: [1,'desc'],
        ajax: {
            'url': basePath+"/finacial/check/fccompareerp/autoAdjustDataTable",
            data: function (d) {
                d.formdata = params;
                return JSON.stringify(d);
            }
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        buttons: [
            {
                text: bt.lang('fc.corpCompareBill.autoAdjust'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    autoAdjust(data);
                }
            }
        ],
        columns: [
			{data:"corpName",className:'center',bSortable: true},
			{data:"bankAcc",className:'center',bSortable:true},
			{data:"accName",className:'center',bSortable:true},
			{data:"account",className:'center',bSortable:true},
			{data:"curName",className:'center',bSortable:true},
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
                    return bt.date.format($("#transTime").val(), 'yyyy-mm-dd');
                }
            },
            {data:"status",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    var endDate = bt.date.format(full.endDate, 'yyyy-mm-dd');//查询结束日期
                    var queryDate = bt.date.format($("#transTime").val(), 'yyyy-mm-dd');//对账结束日期
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
            {data:"blnBalance",className:'center',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.blnBalance);
                }
            },
            {data:"bankBalance",className:'center',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bankBalance);
                }
            },
            {data:"blnAdjBal",className:'center',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.blnAdjBal);
                }
            },
            {data:"bankAdjBal",className:'center',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bankAdjBal);
                }
            },
            {data:"erpCnt",className:'center',bSortable:true},
            {data:"bankCnt",className:'center',bSortable:true},
            {data:null,bSortable:false,className:'center',
				render: function (data, type, full, meta) {
	                // var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="check(this)">'+bt.lang("table.fcCompare.check")+'</a>|';
                      var  html = ' <a class="" href="#" data-row="'+meta.row+'" onclick="adjust(this)">'+bt.lang("table.fcCompare.doAdjustOver")+'</a>';
	                return html;
				}
			}
        ]
    });

});


function adjust(el){
    var checkType = $("#checkType").val();
    var netId = $("#netId").val();
    var transTime = $("#transTime").val();
    var data = layui.jquery("#adjustTable").DataTable().row(layui.jquery(el).data("row")).data();
    var params = "bankAcc="+data.bankAcc + "&account=" + data.account + "&curId=" + data.curId + "&transTime=" + transTime
        +"&checkType="+checkType +"&netId="+netId;
    var href = "/finacial/check/fccompareerp/list?" + params;
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

//一键结转
function autoAdjust(data) {

    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var accounts = "";
    var bankAccs = "";
    var curIds = "";
    var blnBal = "";
    var bankBal = "";
    var lastEndDate="";
    var rowData = data.splice(0, len);
    var paramType = $("#paramType").val();
    var checkType = $("#checkType").val();
    var transTime = $("#transTime").val();
    var checkDate="";

    for (var i = 0; i < rowData.length; i++) {
        if (checkType.trim() == "") {
            layer.alert(bt.lang('finacial.check.checktype.selectIsNull'), {icon: 7});
            return;
        }
        if (transTime.trim() == "") {
            layer.alert(bt.lang('finacial.check.selectEndDate'), {icon: 7});
            return;
        }

        if (accounts == "") {
            accounts += rowData[i].account;
            bankAccs += rowData[i].bankAcc;
            curIds += rowData[i].curId;
            blnBal += rowData[i].blnBalance;
            bankBal += rowData[i].bankBalance;
            lastEndDate += rowData[i].endDate;
            checkDate += rowData[i].checkDate;
        } else {
            accounts += "," + rowData[i].account;
            bankAccs += "," + rowData[i].bankAcc;
            curIds += "," + rowData[i].curId;
            blnBal += "," + rowData[i].blnBalance;
            bankBal += "," + rowData[i].bankBalance;
            lastEndDate += "," + rowData[i].endDate;
            checkDate += "," +rowData[i].checkDate;
        }

    }
    for(var i=0;i<rowData.length;i++){
        if($overCheck(rowData[i])){//结转
            return;
        }
    }
    var params = {
        "account": accounts,
        "bankAcc": bankAccs,
        "curId": curIds,
        "paramType":paramType,
        "checkType": checkType,
        "transTime":transTime,
        "blnBal": blnBal,
        "bankBal":bankBal,
        "lastEndDate":lastEndDate,
        "checkDate":checkDate
    };
    parent.layer.confirm(bt.lang('fc.corpCompareBill.batchAutoAdjust'), function () {
        $.post(basePath + "/finacial/check/fccompareerp/autoAdjust",parseParam(params) , function (result) {
            datatable.draw();
            if(result.success) {
                parent.layer.alert(bt.lang("finacial.check.Adjust.succeed"), {icon:1});
            }else{
                parent.layer.alert(bt.lang(result.messageEnum.toLowerCase().replaceAll("_","."),result.params), {icon:5});
            }
        }, 'JSON');
    });
}

//结转判断
$overCheck = function(rowData){
    blnBalance = bt.money.toNumber(rowData.blnAdjBal);
    bankBalance = bt.money.toNumber(rowData.bankAdjBal);
    if(bt.money.add(blnBalance, 0 - bankBalance) - 0 != 0){
        //调节后余额不平，不能+title
        layer.alert(bt.lang("finacial.check.Op.AdjustBalanceNotEq",new Array('结转')), {icon:5});
        return true;
     }
    return false;
}


//json转url参数
function parseParam(param, key) {
    var paramStr = "";
    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
        paramStr += "&" + key + "=" + encodeURIComponent(param);
    } else {
        $.each(param, function(i) {
            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
            paramStr += '&' + parseParam(this, k);
        });
    }
    return paramStr.substr(1);
};


function check(el){
    var checkType = $("#checkType").val();
    var netId = $("#netId").val();
    var transTime = $("#transTime").val();
    var data = layui.jquery("#adjustTable").DataTable().row(layui.jquery(el).data("row")).data();
    var params = "bankAcc="+data.bankAcc + "&account=" + data.account + "&curId=" + data.curId + "&transTime=" + transTime
        +"&checkType="+checkType +"&netId="+netId;
    var href = "/finacial/check/fccompareerp/list?" + params;
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