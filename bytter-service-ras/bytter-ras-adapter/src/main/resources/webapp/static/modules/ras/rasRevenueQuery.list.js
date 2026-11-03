var datatable;
bt.use(['form','datatable','corp','dicts','dateType','moneyType','rasShop','date'], function(){
    $ = bt.jquery;
    bt.form();
    bt.corp.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.rasShop.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ras/revenue/datatable"
        }
        ,columns: [
            {data:"serialNo",className:'center',bSortable:true},
            {data:"orderNo",className:'center',bSortable:true},
            {data:"shopName",className:'center',bSortable:true},
            {data:"corpName",className:'center',bSortable:true},
            {data:"transDate",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.date.format(full.transDate, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {data:"money",className:'textRight',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.money);
                }
            },
            {data:"payTypeName",className:'center',bSortable:true},
            {data:"custName",className:'center',bSortable:true},
            {data:"custAcc",className:'center',bSortable:true},
            {data:"rmk",className:'center',bSortable:true},
            {data:"createDate",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    if(full.createDate){
                        return bt.date.format(full.createDate, "yyyy-MM-dd hh:mm:ss");
                	}
                	return '';
                }
            },
            {data:"statusName",className:'center',bSortable:true}
        ]
    });
});
//单位回调函数
function corpCallback(data){
    bt.rasShop.get("shopId").init({"corpId":data.id});
}