var corp;
var selectType;
bt.use(['form', 'datatable', 'dateType', 'selectType',  'dicts', 'corp','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.mulselect.init();
    bt.dicts.init();
    bt.dateType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: false,
        isInitData:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/report/bankAccountCount/datatable"
        },
        btInitComplete: function (settings, data) {
            $(".count").removeAttr("data-money");
            initBar();
        },
        btDrawCallback:function(setting){
            // tableData = setting.json.data;
            initBar();
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "corpName", bSortable: false},
            {data: "bankTypeName", bSortable: false, className: 'textRight'},
            {data: "accType", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAcc.accType", full.accType);
                }
            },
            {data: "natureName", bSortable: false, className: 'textRight'},
            {data: "curName", bSortable: false, className: 'textRight'},
            {data: "cancelCount", bSortable: false, className: 'textRight'},
            {data: "availableCount", bSortable: false, className: 'textRight'},
            {data: "count", bSortable: false, className: 'textRight'}
        ],
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: false,
            format: {
                decimal: 0
            },
            sumList: [
                {
                    colNum: '5',
                    dataName: 'cancelCount'
                },
                {
                    colNum: '6',
                    dataName: 'availableCount'
                },
                {
                    colNum: '7',
                    dataName: 'count'
                }
            ]
        }
    });
    selectType = bt.selectType.init();
    corp = bt.corp.init();

    var myChartSum = echarts.init(document.getElementById('chartSum'));
    var myChartSum2 = echarts.init(document.getElementById('chartSum2'));
    function initBar(){
        var bankTypeIdStr;
        if (bt.mulselect.get("ins_bankTypeId").val()){
            bankTypeIdStr = formatBankTypeId(bt.mulselect.get("ins_bankTypeId").val());
        }
        var data = {
            corpId: $("#corpIdHidden").val(),
            // corpId: corp.get("ins_corpId").val(),
            bankTypeId: bankTypeIdStr,
            startDate: bt.dateType.get("ged_regDate").val(),
            endDate: bt.dateType.get("led_regDate").val(),
            // natureId: bt.selectType.get("eqs_natureId").val(),
            natureId: $("#natureIdHidden").val(),
            accType: bt.dicts.get("eqs_accType").val(),
            curId: $("#curIdHidden").val(),
            // curId: curId.get("eqs_curId").val()
            status: bt.dicts.get("eqs_status").val()
        };
        var url = basePath + "/report/bankAccountCount/getBankCount";
        $.ajax({
            type: "post",
            async: false, //同步执行
            url: url,
            data: data,
            dataType: "json", //返回数据形式为json
            success: function (result) {
                // result = $.parseJSON(result);
                var option = initChartSum(result);
                option.tooltip.formatter="{a} </br>{b} : {c} ({d}%)"
                myChartSum.hideLoading();
                myChartSum.setOption(option, true);
                window.onresize = myChartSum.resize;

            }
        });
        var url2 = basePath + "/report/bankAccountCount/getBankCountByNature";
        $.ajax({
            type: "post",
            async: false, //同步执行
            url: url2,
            data: data,
            dataType: "json", //返回数据形式为json
            success: function (result) {
                // result = $.parseJSON(result);
                var option = initChartSum(result);
                option.tooltip.formatter="{a} </br>{b} : {c} ({d}%)"
                myChartSum2.hideLoading();
                myChartSum2.setOption(option, true);
                window.onresize = myChartSum2.resize;

            }
        });
    }

    function initChartSum(result) {
        var option = result;
        return option;
    }

    function formatBankTypeId(typeIdList) {
        var typeIdStr = '';
        for (var i=0;i<typeIdList.length;i++){
            if (typeIdList[i].id) {
                if (typeIdStr == '') {
                    typeIdStr = typeIdStr + typeIdList[i].id;
                } else {
                    typeIdStr = typeIdStr + "," + typeIdList[i].id;
                }
            }
        }
        return typeIdStr;
    }
});

bt.initValue = function () {
    $("#corpIdHidden").removeAttr('name');
};
callbackCurId = function(){
    $("#curIdHidden").val(selectType.get("eqs_curId").val());
};
callbackCorp = function(){
    $("#corpIdHidden").val(corp.get("ins_corpId").val());
};
callbackNatureId = function(){
    $("#natureIdHidden").val(selectType.get("eqs_natureId").val());
};
bt.initSelectValue = function () {
    $("#curIdHidden").removeAttr('name');
};
callbackReset = function () {
    // $("#corpIdHidden").val(corp.get("ins_corpId").val());
    try {
        // 重置单位树
        var corpTreeObj = window.biz['corp']['ins_corpId'];
        var defaultValue = corpTreeObj.defaultValue
        if(defaultValue != $("#corpIdHidden").val()){
            corpTreeObj.obj.val(defaultValue)
        }
    }catch (e) {

    }

    $("#curIdHidden").val(selectType.get("eqs_curId").val())
    $("#natureIdHidden").val(selectType.get("eqs_natureId").val());
};
