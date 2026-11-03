var billTable={};
var idsValArray=[];
var rateType,selectType,dateType,mulselect,corp;
bt.use(['form','select','dicts','btdate','moneyType','corp','mulselect','element','datatable','rateType','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    rateType = bt.rateType.init();
    dateType = bt.dateType.init();
    mulselect= bt.mulselect.init();
    selectType = bt.selectType.init();
    corp = bt.corp.init();

    var inputOperationareaSearchLayerIndex = null;
    $("#inputOperationareaBtn").click(function (){
        //  layerType  = 1 表示 编辑
        var url = basePath + "/sys/operationArea/selectPage"
        if ($("#newPage").val()){
            url += '?layerType=1'
        }

        inputOperationareaSearchLayerIndex = parent.layer.open({
            type: 2,
            area: ['850px', '620px'],
            maxmin: true,
            content: url,
        })
    })

    /**
     * 营运区维护查询选择回调
     * @param data
     * @param dom
     */
    inputOperationAreaCallback = function (selectedData) {
        // console.log(selectedData);
        var ids = ''
        var names = ''
        bt.each(selectedData,function (i,item){
            ids += item.operationAreaCode
            names += item.operationAreaName
            if(i < (selectedData.length -1)){
                ids += ',';
                names += ','
            }
        })
        $("#operationAreaCode").val(ids)
        $("#operationAreaName").val(names)
        inputOperationareaSearchLayerIndex && parent.layer.close(inputOperationareaSearchLayerIndex)
    }

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[15,'asc'],[11,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abbillQuery/dataQueryBillBabByMonthTable"
        }
        ,columns: [
            {data: "parentName", bSortable: false},//1
            {data: "provinceName", bSortable: false},//1
            {data:"operationAreaName",bSortable:false},//2
            {data: "corpName", bSortable: false},//3
            {data: "equityName", bSortable: false},//4
            {data:"typeName",bSortable:false},//5
            {data:"bankName",bSortable:false},
            {data:"bankAcc", bSortable: false},//6
            {
                data: "billMoney", bSortable: false, className: 'textRight',//7
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {
                data: "bailMoney", bSortable: false, className: 'textRight',//8
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bailMoney);
                }
            },
            {data:"endDate",bSortable:true},//9
            {
                data: "applyRepayMoney", bSortable: false, className: 'textRight',//10
                render: function (data, type, full, meta) {
                    return bt.money.format(full.applyRepayMoney);
                }
            },
            {
                data: "settleMoney", bSortable: false, className: 'textRight',//11
                render: function (data, type, full, meta) {
                    return bt.money.format(full.settleMoney);
                }
            },
            {
                data: "bailPercent", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    var html;
                    if (data || data == "0") {
                        html = '<div name="rpdintrstRate">' + bt.rateType.getFun().format(full.bailPercent, 'percent', 2, ',') + '</div>'
                    } else {
                        html = '<div name="rpdintrstRate"></div>'
                    }
                    return html;
                }
            },
            {data:"parentSort",visible:false}
        ]
    });
    bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
});


//比较保证金比例
function compareBailpercent(){
    var geg_bailPercent = rateType.get("geg_bailPercent").val();
    var leg_bailPercent = rateType.get("leg_bailPercent").val();
    if(geg_bailPercent && leg_bailPercent && geg_bailPercent > leg_bailPercent){
        rateType.get("leg_bailPercent").val(geg_bailPercent);
    }
}

function resetFrom(){
    $('[name="ged_endDate"]').val('');
    $('[name="led_endDate"]').val('');
}
