var corp;
var selectType,mulselectType,dateType,form;
bt.use(['form', 'datatable', 'dateType', 'selectType',  'dicts', 'corp','mulselect','moneyType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.mulselect.init();
    dicts=bt.dicts.init();
    dateType = bt.dateType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = moneyType.getFun();
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
            'url': basePath + "/report/bankBalDistribute/datatable"
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
            {data: "bankTypeName", bSortable: false},
            {data: "accTypeName", bSortable: false},
            {data: "natureName", bSortable: false},
            {data: "curName", bSortable: false},
            {data: "balMoney", bSortable: false,className:'textRight',
                render:function(data,type,full,meta){
                    if(full.balMoney){
                        return moneyFun.format(full.balMoney);
                    }else{
                        return moneyFun.format(0);
                    }
                }
            }
        ],
        summary: {
            groupColumn: 'curName',
            pageSum: 'pageSum',
            sumTextCol: 1,  //统计名称
            format: {
                decimal: 2
            },
            customSum:{
                text: function(){
                    var changeCur = dicts.get("changeCur").val();
                    if(changeCur=="1") {
                        return bt.lang('table.fexForeign.report.convert.currency.total');
                    }
                    return '';
                },
                sumCallback:function(currentPageData,colName){ //汇总回调，必须要有返回值
                    //currentPageData　当前页面数据 colName　当前列名称
                    var result=0;
                    if(currentPageData) {
                        bt.each(currentPageData, function (index, item) {
                            if (colName == "balMoney") {
                                result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.balMoney),moneyFun.toNumber(item.curExrate)));
                            }
                        });
                    }
                    return moneyFun.format(result);
                }
            },
            sumList: [
                {
                    money: true,
                    colNum: '6',
                    dataName: 'balMoney'
                }
            ]
        }
    });
    selectType = bt.selectType.init();
    corp = bt.corp.init();

    var myChartSum = echarts.init(document.getElementById('chartSum'));
    function initBar(){
        var bankTypeIdStr;
        if (bt.mulselect.get("ins_bankTypeId").val()){
            bankTypeIdStr = formatBankTypeId(bt.mulselect.get("ins_bankTypeId").val());
        }
        var data = {
            corpId: $("#corpIdHidden").val(),
            // corpId: corp.get("ins_corpId").val(),
            bankTypeId: bankTypeIdStr,
            // startDate: bt.dateType.get("ged_regDate").val(),
            endDate: bt.dateType.get("led_regDate").val(),
            // natureId: bt.selectType.get("eqs_natureId").val(),
            natureId: $("#natureIdHidden").val(),
            accType: bt.dicts.get("eqs_accType").val(),
            curId: $("#curIdHidden").val()
            // curId: curId.get("eqs_curId").val()
        };
        var url = basePath + "/report/bankBalDistribute/getBankBal";
        $.ajax({
            type: "post",
            async: false, //同步执行
            url: url,
            data: data,
            dataType: "json", //返回数据形式为json
            success: function (result) {
                var option = initChartSum(result);
                myChartSum.hideLoading();
                myChartSum.setOption(option, true);
                window.onresize = myChartSum.resize;

            }
        });
    }
	function parseJSON(str){
	  try{
	    return JSON.parse(str);
	  }catch(e){
	  }
	  return str;
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
    $("#corpIdHidden").val(corp.get("ins_corpId").val());
    $("#curIdHidden").val(selectType.get("eqs_curId").val())
    $("#natureIdHidden").val(selectType.get("eqs_natureId").val());
};

function callToCurr(data){
    if(data.value =="1"){
        selectType.get("reducedCurId").attr({'data-disabled':false,'data-required':true,'data-value':$("#rmbCur").val()});
        selectType.get("rateType").attr({'data-disabled':false,'data-required':true});
        dateType.get("rateDate").attr({'readonly':false,'data-required':true});
    }else {
        selectType.get("reducedCurId").attr({'data-disabled':true,'data-required':false});
        selectType.get("rateType").attr({'data-disabled':true,'data-required':false});
        dateType.get("rateDate").attr({'readonly':true,'data-required':false});
    }
    form.render();
    form.renderIcon();
}

/**
 * 币种转换校验
 */
function checkConvertCurCallBack() {
    var curId = selectType.get("eqs_curId").val();
    var reducedCurId = selectType.get("reducedCurId").val();
    var rateType = selectType.get("rateType").val();
    var rateDate = dateType.get('rateDate').val();
    if (isNotEmpty(curId) && isNotEmpty(reducedCurId) && isNotEmpty(rateType) && isNotEmpty(rateDate)) {
        $.ajax({
            url: basePath + "/report/allocationsettlement/checkConvertCurId",
            type:"post",
            data:{
                "origCurId":curId,
                "convertCurId": reducedCurId,
                "rateType": rateType,
                "rateDate": rateDate,
            },
            dataType: "json",
            success:function (data) {
                if (!data.success) {
                    parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
                    selectType.get("reducedCurId").val("");
                }
            }
        });
    }
}

/**
 * 为空判断
 * @param stringData
 * @returns {boolean}
 */
function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
        return true;
    } else {
        return false;
    }
}

/**
 * 非空判断
 * @param stringData
 * @returns {boolean}
 */
function isNotEmpty(stringData) {
    return !isEmpty(stringData)
}