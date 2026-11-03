var dateTypeComp,dateFun,dateTypes;
bt.use(['form','jquery','datatable','bttable','dateType','selectTableType','btdate','corp','selectType','element','money','moneyType','dicts','mulBankAccInfo','mulselect','bank'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    corp = bt.corp.init();
    bt.dicts.init();
    bt.mulselect.init();//初始化单位
    var corp = bt.corp.init();
    var selectTypes = bt.selectType.init();
    var mulBankAccInfo=bt.mulBankAccInfo.init();
    dateTypes = bt.dateType.init()
    bt.bank.init({
        elem:'.bank',
    });
    dateTypeComp = bt.dateType.init();
    dateFun = dateTypeComp.getFun();

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


    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'rtlip',
        colReorder: true,
        checkbox: true,
        order: [1,'desc'],
        ajax: {
            'url': basePath+"/finacial/check/fccompareAutoCenter/autoCheckDataTable"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        buttons: [
            {
                hide: bt.isnotResource('finacial:check:cenrality:autoCheck'),
                text: bt.lang('fc.corpCompareBill.autoCompare'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    autoCompare(data);
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
                    return dateTypeComp.get("transTime").val();
                }
            },
            {data:"status",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    var endDate = bt.date.format(full.endDate, 'yyyy-mm-dd');//查询结束日期
                    var queryDate = dateTypeComp.get("transTime").val();//对账结束日期
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

    //内部账户选择调用控件后传递值
    bt.externalMulAccInfoCallback=function (data,dom) {
        var bankAcc;
        $.each(data,function (key, val) {
                if(key==0){
                    bankAcc=val.bankAcc;
                }else if(key>0){
                    bankAcc=bankAcc+","+val.bankAcc;
                }

            }
        )
        $("#bankAcc").val(bankAcc);
    }

    //内部账户选择调用控件前传递netId
    bt.externalMulAccInfoNetIdParams=function(){
        if($("#netIdStr").val()==""){
            layer.alert(bt.lang('btwarnstrategy.corpselect'), {icon: 7});
            return false;
        }else {
            mulBankAccInfo.set({
                "netId": $("#netIdStr").val(),
                "curId":$("#curId").val()
            });
            return true;
        }
    }

});

/**
 * 单笔对账
 * @param el
 */
function check(el){
    var checkType = $("select[name='checkType']").val();
    var netId = $("#netIdStr").val();
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var params = "bankAcc="+data.bankAcc + "&account=" + data.account + "&curId=" + data.curId + "&transTime=" + dateTypeComp.get("transTime").val()
        +"&checkType="+checkType +"&netId="+netId;
    var href = "/finacial/check/fccomparecenter/list?" + params;
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

//网点变更
changeNet = function (data) {
    $("#netIdStr").val(data.value);
    if (!bt.empty($.trim(data.value))) {//网点id不为空
        $.ajax({
            type: "POST",
            url: basePath + "/finacial/check/fccompareAutoCenter?getDayCloseDate",
            data: {"netId": data.value},
            dataType: "json",
            success: function (rs) {
                if(rs.success){
                    var dateValue = bt.date.format(rs.obj,'YYYY-MM-DD');
                    dateTypes.get("transTime").attr("data-max", dateValue);
                    dateTypes.get("transTime").val(dateValue);
                    // dateTypes.get("transTime").render();
                }

            }
        });
    }
    $("#bankAcc").val("");//清空账号
    //$("#searchNetId").val($.trim(data.value));
}

/**
 * 一键对账
 * @param data 自动对账
 */
function autoCompare(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var accounts="";
    var bankAccs="";
    var curIds="";
    var endDates="";
    var checkDate="";

    var rowData = data.splice(0, len);
    var paramType = $("#paramType").val();
    // var checkDate = $("#checkDate").val();
    var checkType = $("select[name='checkType']").val();
    var transTime = $("#transTime").val();

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
            endDates += rowData[i].endDate;
            checkDate += rowData[i].checkDate;
        } else {
            accounts += "," + rowData[i].account;
            bankAccs += "," + rowData[i].bankAcc;
            curIds += "," + rowData[i].curId;
            endDates += "," + rowData[i].endDate;
            checkDate += "," +rowData[i].checkDate;
        }

    }
    //json转url参数
    var parseParam = function(param, key) {
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
    var params = {
        "account": accounts,
        "bankAcc": bankAccs,
        "curId": curIds,
        "endDate":endDates,
        "checkDate":checkDate,
        "paramType":paramType,
        "checkType": checkType,
        "transTime":transTime
    };
    parent.layer.confirm(bt.lang('fc.corpCompareBill.batchAutoCheck'), function () {
        $.post(basePath + "/finacial/check/fccompareAutoCenter/autoCompare", parseParam(params), function (result) {
            if(result.success) {
                if(bt.navtab){
                    bt.navtab.addNavtab(bt.lang('fc.corpCompareBill.autoAdjust'),  basePath + "/finacial/check/fccompareAutoCenter/adjustList?" + parseParam(params));
                    return;
                }
            }else{
                parent.layer.alert(result.msg, {icon:2});
            }
        }, 'JSON');
    });

}

