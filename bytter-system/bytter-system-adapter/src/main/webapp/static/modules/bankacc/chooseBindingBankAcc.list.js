var defaultSelectedIds;
bt.use(['form','datatable','select','dicts','corp'], function(){
    $ = bt.jquery,form = bt.form();
    var ids = bt.navtab.getCurrentDom().$("#bindingBankAccHid").val();
    $("#chooseId").val(ids);
    defaultSelectedIds = ids.split(",") || [];
    bt.dicts.init();
    bt.corp.init();
    /*
    * Array数组添加remove函数
    * */
    Array.prototype.remove = function(){
        for (var i = 0; i < arguments.length; i++) {
            var ele = arguments[i];
            var index = this.indexOf(ele);
            if(index > -1){
                this.slice(index,1)
            }
        }
    }

  //下拉框
    $.each($("div[type='list']"),function(i,item){
        bt.select.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        jumpPageSelect: true,//开启跨页选择
        checkbox:true,order: [[7,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bankacc/btbankacc/bindingBankAccDatatable"
        },
        buttons: [
            {
                text:'选择',
                action: function (e, dt, node, config) {
                    chooseBatch();
                }
            },
           {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
               {data:"bankAcc",bSortable:true},
               {data:"accName",bSortable:true},
               {data:"corpName",bSortable:true},
               {data:"bankTypeName",bSortable:true},
               {data:"bankName",bSortable:true},
               {data:"curName",bSortable:true},
               {data:"sign",bSortable:true}
        ],
        btDrawCallback:function(setting){
            if(defaultSelectedIds.length){
                bt.datatable.onCheckRow("appTable", {
                    "key": "id",
                    "value": defaultSelectedIds,
                    "checked": true
                });
            }
        },
        btClickRowCallback:function (data) {
            if(data){
                if(data[0].isSelect){
                    if(defaultSelectedIds.indexOf(data[0].id) == -1){
                        defaultSelectedIds.push(data[0].id)
                    }
                }else{
                    if(defaultSelectedIds.indexOf(data[0].id) > -1){
                        defaultSelectedIds.splice(defaultSelectedIds.indexOf(data[0].id),1);
                    }
                }
            }
        }
    });
});
//批量选择
function chooseBatch() {
    if (!defaultSelectedIds.length) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var selectedIds = "";
    for (var i = 0; i < defaultSelectedIds.length; i++) {
        if(selectedIds){
            selectedIds += "," + defaultSelectedIds[i]
        }else{
            selectedIds += defaultSelectedIds[i]
        }
    }
    // 调父页面的回调方法
    bt.navtab.getCurrentDom().setBindingBankAcc(selectedIds)
}