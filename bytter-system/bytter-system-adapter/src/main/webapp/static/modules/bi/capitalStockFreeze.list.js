var selectType,form;
bt.use(['form', 'datatable', 'money', 'dicts', 'selectType','date'], function () {
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
            {data: "corpName", bSortable: false},
            {data: "curName", bSortable: false},
            {data: "bankTypeName", bSortable: false},
            {data: "bankAcc", bSortable: false},
            {data: "accName", bSortable: false},
            {data: "bankName", bSortable: false},
            {
                data: "freezeMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(dicts.get("moneyUnit").val() === "million"){
                        return bt.money.format(bt.money.toMillion(full.freezeMoney));
                    }else {
                        return bt.money.format(full.freezeMoney);
                    }
                }
            },
            {data: "freezeDate", bSortable: false},
            {data: "freezeRession", bSortable: false}
        ],
        summary: {
            pageSum: true,
            convertMoney: function () {
                return "yuan" === dicts.get("moneyUnit").val() ? "" : dicts.get("moneyUnit").val();
            },
            groupColumn: 'curName',//分类汇总列参数
            format:
                {
                    decimal: 2
                }
            ,
            sumList: [
                {
                    colNum: '6',
                    dataName: 'freezeMoney'
                }
            ]
        }
    });
})
;

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