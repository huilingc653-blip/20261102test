var selectType,form;
bt.use(['form', 'datatable', 'money', 'dicts', 'selectType','btdate','moneyType','corp','element','rateType','date'], function () {
    $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();
    selectType = bt.selectType.init();
    //生成列表
    var datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: false,
        checkbox: false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bi/capitalStock/datatable"
        },
        buttons: [],
        columns: [
            {data:"corpName",bSortable:true},
            {data:"origNo",bSortable:true},
            {data:"busTypeName",bSortable:true},
            {data:"sourceName",bSortable:true},
            {data:"billFlagName",bSortable:true},
            {data:"billTypeName",bSortable:true},
            {data:"businessDate",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(dicts.get("moneyUnit").val() === "million"){
                        return bt.money.format(bt.money.toMillion(full.billMoney));
                    }else {
                        return bt.money.format(full.billMoney);
                    }
                }
            },
            {data:"applyName",bSortable:true},
            {data:"applyDate",bSortable:true},
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"interestRates",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.interestRates==null
                        ||full.interestRates==undefined){
                        return ""
                    }else{
                        return bt.rateType.getFun().format(full.interestRates);
                    }
                }
            },
            {data:"interest",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.interest==null
                        ||full.interest==undefined){
                        return ""
                    }else{
                        return bt.money.format(full.interest);
                    }
                }
            },
            {data:"cnAccrecords",bSortable:true},
            {data:"rearAccrecords",bSortable:true},
            {data:"bankAcc",bSortable:true},
            {data:"busStatusName",bSortable:true}
        ],
        summary:{
            pageSum:true,
            convertMoney: function () {
                return "yuan" === dicts.get("moneyUnit").val() ? "" : dicts.get("moneyUnit").val();
            },
            sumList:[
                {
                    colNum:'7',
                    dataName:'billMoney'
                },{
                    colNum:'13',
                    dataName:'interest'
                }
            ]
        }
    });
});


/**
 * 币种转换校验
 */
function checkConvertCurCallBack() {
    var origCur = selectType.get("curId").val();
    var reducedCurId = selectType.get("reducedCurId").val();
    if (isNotEmpty(origCur) && isNotEmpty(reducedCurId)) {
        $.ajax({
            url: basePath + "/report/deposit/balance/checkConvertCurId",
            type:"post",
            data:{
                "origCurId": origCur,
                "convertCurId": reducedCurId,
                "date": bt.date.format(new Date(), 'yyyy-mm-dd')
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
