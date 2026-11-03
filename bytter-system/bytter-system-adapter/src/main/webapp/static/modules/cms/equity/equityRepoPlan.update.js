var datatable = {};
var bttable = {};
bt.use(["jquery", "datatable", "form", "moneyType", "selectType", "dicts", "corp","dateType",'bttable'], function () {
    $ = bt.jquery;
    form = bt.form()
    bt.dicts.init();
    bt.selectType.init();
    bt.dateType.init();//日期时间
    bt.moneyType.init();
    bt.corp.init();//单位


    /**
     * 生成列表
     */
    bttable =   bt.bttable.init({
        elem: "#appTable",
        mulselect:false,
        checkbox: false,
        'url': basePath + "/cms/equity/repoPlan/infoDetailUpdateDatatable?id="+$("#equityId").val(),
        buttons: [
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data:"equityCode",render:function(data, row){
                    return data.equityCode;
                }},
            {data:"financingWay", className: 'center',render:function(data, row){
                    return bt.dict("cms.equity.financingWay", data.financingWay);
                }},
            {data:"investor",render:function(data, row){
                    return data.investor;
                }},
            {data:"invWay", className: 'center',render:function(data, row){
                    return bt.dict("cms.equity.invWay", data.invWay);
                }},
            {data:"invMoney",className:"textRight",render: function ( data ){
                    return bt.money.format(data.invMoney);
                }},
            {data:"rmk",render:function(data, row){
                    return data.rmk;
                }},
            {
                data: "bizStatusName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(data.bizStatus==40){
                        return bt.lang("cms.equity.waitUpdate")
                    }else{
                        return data.bizStatusName;
                    }
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    if (data.bizStatus == 40 || data.bizStatus == 41 || data.bizStatus == 50 ||data.bizStatus == 56) {//计划登记审批通过、计划修改审批打回、计划修改审批通过
                        html += '<a class="" href="#" equityCode="' + data.equityCode + '" onclick="updateRepoPlanDetail(\'' + data.equityCode + '\')">' + bt.lang("cms.equity.repoPlanUpdate") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#"  onclick="viewByEquityCode(\'' + data.equityCode + '\')">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }}
        ]
    });

});


function updateRepoPlanDetail(equityCode){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('cms.equity.repoPlanUpdate'),basePath + "/cms/equity/repoPlan/updateRepoPlanDetail/" + equityCode);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/equity/repoPlan/updateRepoPlanDetail/" + equityCode,
        end:function(){
            location.reload();
        }
    });
}


function view(id){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/equity/repoPlan/view/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/equity/repoPlan/" + id
    });
}

function viewByEquityCode(equityCoe){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/equity/repoPlan/viewByEquityCode/" + equityCoe);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/equity/repoPlan/viewByEquityCode/" + equityCoe
    });
}
