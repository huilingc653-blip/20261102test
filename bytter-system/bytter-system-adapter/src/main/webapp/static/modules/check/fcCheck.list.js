var dateTypeComp;
bt.use(['form','datatable','corp','date','btdate','moneyType','bank','dicts','erpMulBankAccInfo','selectType'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    corp = bt.corp.init();
    bt.dicts.init();
    bt.selectType.init();
    bt.bank.init({
        elem:'.bank',
    });
    var erpMulBankAccInfo=bt.erpMulBankAccInfo.init();
    dateTypeComp = bt.dateType.init();
    var dateFun = dateTypeComp.getFun();

    bt.btdate.init({
            elem:".date-interval",
            startname:"ged_adjustDate",
            endname:"led_adjustDate"
        }
    );

    //内部账户选择调用控件后传递值
    bt.erpMulAccInfoCallback=function (data,dom) {
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

    // /**
    //  * 帐号下拉框初始化
    //  */
    // selectTable = bt.selectTableType.init({
    //     elem: "#bankAcc",
    //     type: 'POST',
    //     paging: true,
    //     // scrollX:"500px",scrollY:"200px",
    //     url: basePath + "/finacial/check/erpDataManager/jsonBankAccErpData",
    //     columns: [
    //         {"data": "bankAcc", title: bt.lang("table.btBankAcc.bankAcc")},
    //         {"data": "curName", title: bt.lang("international.payment.curName")},
    //         {"data": "accName", title: bt.lang("table.btBankAcc.accName")},
    //         {"data": "bankName", title: bt.lang("table.btBankAcc.bankName")}
    //     ],
    //     clickCallback: function (data, tr, elem, full) {
    //         if (data&&data.bankAcc) {
    //             $("#bankAcc").val(data.bankAcc);
    //             $("#curId").val(data.curId);
    //         }
    //     }
    // });


    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'rtlip',
        colReorder: true,
        checkbox: true,
        order: [1,'desc'],
        ajax: {
            'url': basePath+"/finacial/check/fccompareerp/datatable"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        buttons: [
            {
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
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var params = "bankAcc="+data.bankAcc + "&account=" + data.account + "&curId=" + data.curId + "&transTime=" + dateTypeComp.get("endDate").val();
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

/**
 * 单位回调
 */
function corpCallBack() {
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
    var transTime = dateTypeComp.get("endDate").val();

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
        $.post(basePath + "/finacial/check/fccompareerp/autoCompare", parseParam(params), function (result) {
            if(result.success) {
                if(bt.navtab){
                    bt.navtab.addNavtab(bt.lang('fc.corpCompareBill.autoAdjust'),  basePath + "/finacial/check/fccompareerp/adjustList?" + parseParam(params));
                    return;
                }
            }else{
                parent.layer.alert(bt.lang(result.messageEnum.toLowerCase().replaceAll("_","."),result.params), {icon:5});
            }
        }, 'JSON');
    });

}