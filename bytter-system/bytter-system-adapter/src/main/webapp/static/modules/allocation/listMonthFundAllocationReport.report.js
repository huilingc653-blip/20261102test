var datatable,dateType;
bt.use(['form', 'datatable', 'dateType', 'mulselect' ,'selectType', 'moneyType', 'dicts', 'bank', 'corp','date','rateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dicts.init();
    dateType = bt.dateType.init();
    bt.selectType.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof(ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: false,
        paging:false,
        isInitData:false,
        ignoreButtons:['colvis','print','state'],// 忽略的表单默认按钮 state取消状态保存按钮
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
        },
        ajax: {
            'url': basePath + "/report/monthFundAllocation/datatable"
        },
        columns: [
            {data: "supCorpName", bSortable: false},
            {data: "operationAreaName", bSortable: false},
            {data: "name", bSortable: false},
            {data: "sumUpwardSystem", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    // 资金系统上划 bs005
                    if (isEmpty(full.name)){
                        return bt.money.format(full.sumUpwardSystem);
                    }else{
                        var html = "<a class='' href='#' data-row='"+meta.row+"' onclick='view(this,\"3e47ec272a8a48bca9d2894dbe50ee5b\")'>"+bt.money.format(full.sumUpwardSystem)+"</a>";
                        return html;
                    }

                }
            },
            {data: "sumUpwardBank", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    // 银行上划 bs006
                    if (isEmpty(full.name)){
                        return bt.money.format(full.sumUpwardBank);
                    }else{
                        var html = "<a class='' href='#' data-row='"+meta.row+"' onclick='view(this,\"ba4c7cfb747440938f642fef2f282f8d\")'>"+bt.money.format(full.sumUpwardBank)+"</a>";
                        return html;
                    }
                }
            },
            {data: "sumUpwardActive", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    // 自主上划 bs007
                    if (isEmpty(full.name)){
                        return bt.money.format(full.sumUpwardActive);
                    }else{
                        var html = "<a class='' href='#' data-row='"+meta.row+"' onclick='view(this,\"ec40b524497a443cb157d894e6b9a92a\")'>"+bt.money.format(full.sumUpwardActive)+"</a>";
                        return html;
                    }
                }
            },
            {data: "sumUpward", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {// 资金上划合计
                    return bt.money.format(full.sumUpward);
                }
            },
            {data: "sumAllocationSystem", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    // 资金系统下拨 bs008
                    if (isEmpty(full.name)){
                        return bt.money.format(full.sumAllocationSystem);
                    }else{
                        var html = "<a class='' href='#' data-row='"+meta.row+"' onclick='view(this,\"5f877f40e93c4c1880728deb8d0a80c4\")'>"+bt.money.format(full.sumAllocationSystem)+"</a>";
                        return html;
                    }
                }
            },
            {data: "sumAllocationBank", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    // 银行下拨 bs009
                    if (isEmpty(full.name)){
                        return bt.money.format(full.sumAllocationBank);
                    }else{
                        var html = "<a class='' href='#' data-row='"+meta.row+"' onclick='view(this,\"b71f0ae740d94ce1a238783f466850b3\")'>"+bt.money.format(full.sumAllocationBank)+"</a>";
                        return html;
                    }
                }
            },
            {data: "sumAllocationActive", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    // 自主下拨 bs010
                    if (isEmpty(full.name)){
                        return bt.money.format(full.sumAllocationActive);
                    }else{
                        var html = "<a class='' href='#' data-row='"+meta.row+"' onclick='view(this,\"fed53fc6d65d4c50b59593d18ac1ca16\")'>"+bt.money.format(full.sumAllocationActive)+"</a>";
                        return html;
                    }
                }
            },
            { data: "sumAllocation", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {// 资金下拨合计
                    return bt.money.format(full.sumAllocation);
                }
            },
            {data: "retainedAmount", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.retainedAmount);
                }
            },
            {data: "isExcess", bSortable: false},
            {data: "accountIncome", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accountIncome);
                }
            },
            {data: "accountPayOut", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.accountPayOut);
                }
            },
            {data: "remark", bSortable: false},
        ]
    });

});

function view(el,busType){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        var queryDateBegin = dateType.get("queryDateBegin").val();
        var queryDateEnd = dateType.get("queryDateEnd").val();
        var accountGroup = bt.empty(data.accountGroup) ? '' : data.accountGroup;
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('流水查询'), basePath + "/bankacc/btbankaccdtl/query?corpId="+data.id+"&busType="+busType+"&accountGroup="+accountGroup+"&beginDate="+queryDateBegin+"&endDate="+queryDateEnd);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/btbankaccdtl/query?corpId="+data.id+"&busType="+busType+"&accountGroup="+accountGroup+"&beginDate="+queryDateBegin+"&endDate="+queryDateEnd,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }

}

function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
        return true;
    } else {
        return false;
    }
}
